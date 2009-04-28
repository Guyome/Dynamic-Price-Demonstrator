// Copyright (C) 2005, 2006 International Business Machines and others.
// All Rights Reserved.
// This code is published under the Common Public License.
//
// $Id: hs071_nlp.cpp 1327 2008-09-18 19:01:17Z andreasw $
//
// Authors:  Carl Laird, Andreas Waechter     IBM    2005-08-16

#include "SingleSolver.hpp"

using namespace Ipopt;

// constructor
SingleSolver::SingleSolver(float* _a,float* _b,float* _v,float* _h,float* _r,int _T){
    T= _T;
    a= new float[T];
    b= new float[T];
    v= new float[T];
    h= new float[T];
    r= new float[T];
    var = new float[2*T+1];
    a=_a;
    b=_b;
    v=_v;
    h=_h;
    r=_r;
}

//destructor
SingleSolver::~SingleSolver(){}

// returns the size of the problem
bool SingleSolver::get_nlp_info(Index& n, Index& m, Index& nnz_jac_g,
                            Index& nnz_h_lag, IndexStyleEnum& index_style) {
    n = 2*T+1; //number of variable
    m = T; //number constraint
    nnz_jac_g = 4*T-1;//number of non zero value in constraint jacobian
    nnz_h_lag = 1;//number of non zero value in lagrangian hessian
    index_style = TNLP::C_STYLE;// use the C style indexing (0-based)
    return true;
}

// returns the variable bounds
bool SingleSolver::get_bounds_info(Index n, Number* x_l, Number* x_u,
                            Index m, Number* g_l, Number* g_u) {
    // here, the n and m we gave IPOPT in get_nlp_info are passed back to us.
    // If desired, we could assert to make sure they are what we think they are.
    assert(n == 2*T+1);
    assert(m == T);
    // the variables are positives
    for (Index i=0; i<n; i++) {
        x_l[i] = 0.0;
    }
    // production have upper bounds
    for (Index i=0; i<n; i++) {
        if (i<T) {
            x_u[i] = r[i];
        }
        else {
            x_u[i] = 2e19;
        }
    }
    //all constraint are equality constraint
    for (Index i=0; i<m; i++) {
        g_l[i] = g_u[i] = a[i];
    }
    return true;
}

// returns the initial point for the problem
bool SingleSolver::get_starting_point(Index n, bool init_x, Number* x,
                            bool init_z, Number* z_L, Number* z_U,
                            Index m, bool init_lambda,
                            Number* lambda) {
    // Here, we assume we only have starting values for x, if you code
    // your own NLP, you can provide starting values for the dual variables
    // if you wish
    assert(init_x == true);
    assert(init_z == false);
    assert(init_lambda == false);
    // initialize to the given starting point
    for (Index i=0; i<n; i++) {
        x[i] = 0.;
    }
    x[T]=a[0]/b[0];
return true;
}

// returns the value of the objective function
bool SingleSolver::eval_f(Index n, const Number* x, bool new_x, Number& obj_value) {
    assert(n == 2*T+1);
    obj_value = 0.0;
    for(Index i=0; i<T;i++) {
        obj_value -= (a[i]-b[i]*x[T])*x[T]-v[i]*x[i]-h[i]*x[T+1+i];
    }
    return true;
}

// return the gradient of the objective function grad_{x} f(x)
bool SingleSolver::eval_grad_f(Index n, const Number* x, bool new_x, Number* grad_f) {
    assert(n == 2*T+1);
    float sum =0.;
    for(Index i=0; i<T;i++) {
        grad_f[i] = v[i];
        sum += 2*b[i]*x[T]-a[i];
        grad_f[T+1+i] = h[i];
    }
    grad_f[T] = sum;
    return true;
}

// return the value of the constraints: g(x)
bool SingleSolver::eval_g(Index n, const Number* x, bool new_x, Index m, Number* g) {
    assert(n == 2*T+1);
    assert(m == T);
    for(Index i=0; i<T;i++) {
        if(i>0){
            g[i] = x[i] + x[T+i] - x[T+1+i] + b[i]*x[T];
        }
        else {
            g[i] = x[i] - x[T+1+i] + b[i]*x[T]; //no storage form time period 0
        }
    }
    return true;
}

// return the structure or values of the jacobian
bool SingleSolver::eval_jac_g(Index n, const Number* x, bool new_x,
                            Index m, Index nele_jac, Index* iRow, Index *jCol,
                            Number* values) {
    if (values == NULL) {
    // return the structure of the jacobian
        //element on 3 diagonals
        Index idx=0;
        for(Index i=0; i<T;i++) {
            iRow[idx]=i;
            jCol[idx]=i;
            idx++;
        }
        for (int i = 0; i < T; i++){
            iRow[idx]=i;
            jCol[idx]=T;
            idx++;
        }
        for (int i = 0; i < T; i++)        {
            iRow[idx]=i;
            jCol[idx]=i+T+1;
            idx++;
        }
        // elements on the sub-diagonale (i_{t-1})
        for(Index i=1;i<T;i++) {
            iRow[idx]=i;
            jCol[idx]=i+T;
            idx++;
        }
        assert(idx == nele_jac);
    }
    else {
    // return the values of the jacobian of the constraints
        for(Index i=0;i<T;i++){
            values[i]=1;
            values[i+T]=b[i];
            values[i+2*T]=-1;
            if (i<T-1){
                values[i+3*T]=1;
            }
        }
    }
    return true;
}

//return the structure or values of the hessian
bool SingleSolver::eval_h(Index n, const Number* x, bool new_x,
                   Number obj_factor, Index m, const Number* lambda,
                   bool new_lambda, Index nele_hess, Index* iRow,
                   Index* jCol, Number* values) {
    Index idx =0;
    if (values == NULL) {
        // the hessian for this problem is actually dense
        iRow[idx] = T;
        jCol[idx] = T;
        idx++;
        assert( idx == nele_hess);
    }
    else {
        float sum = 0;
        for (int i = 0; i < T; i++)
        {
            sum+=2*b[i];
        }
        values[0] = sum*obj_factor;
    }
    return true;
}

void SingleSolver::finalize_solution(SolverReturn status,
                              Index n, const Number* x, const Number* z_L, const Number* z_U,
                              Index m, const Number* g, const Number* lambda,
                              Number obj_value,
              const IpoptData* ip_data,
              IpoptCalculatedQuantities* ip_cq) {
    for (int i = 0; i < n; i++)
    {
        var[i]=x[i];
    }
}

float* SingleSolver::getVariable(){
    return var;
}
