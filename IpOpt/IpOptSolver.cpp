#include <jni.h>
#include <iostream>
#include "ClpSimplex.hpp"
#include "Solvers_IpOptSolvers.h"
#include "IpIpoptApplication.hpp"
#include "IpSolveStatistics.hpp"
#include "FreeSolver.hpp"
#include "SingleSolver.hpp"

JNIEXPORT jfloatArray JNICALL Java_Solvers_IpOptSolvers_IpOptFreeSolver
                        (JNIEnv * env, jclass, jfloatArray _a, jfloatArray _b,
                        jfloatArray _r, jfloatArray _v, jfloatArray _h, jint _T) {
    //converte Java objects to C++ objects
    int T = (int)_T;
    jfloat* a= env->GetFloatArrayElements(_a,false);//coeff demand function
    jfloat* b= env->GetFloatArrayElements(_b,false);//coeff demand function
    jfloat* v= env->GetFloatArrayElements(_v,false);//production cost
    jfloat* h= env->GetFloatArrayElements(_h,false);//storage cost
    jfloat* r= env->GetFloatArrayElements(_r,false);//production constraint
    jfloatArray jresult = env -> NewFloatArray(3*T+1);
    jfloat* result = env -> GetFloatArrayElements(jresult, false);

    printf("***Data\n");
    printf("constraint:\t%f\t%f\t%f\t%f\n",r[0],r[1],r[2],r[3]);
    printf("alpha     :\t%f\t%f\t%f\t%f\n",a[0],a[1],a[2],a[3]);
    printf("beta      :\t%f\t%f\t%f\t%f\n",b[0],b[1],b[2],b[3]);
    printf("prodcost  :\t%f\t%f\t%f\t%f\n",v[0],v[1],v[2],v[3]);
    printf("storcost  :\t%f\t%f\t%f\t%f\n",h[0],h[1],h[2],h[3]);

    // Create an instance of your nlp...
    FreeSolver* free = new FreeSolver(a,b,v,h,r,T);
    SmartPtr<TNLP> mynlp = free;

    // Create an instance of the IpoptApplication
    SmartPtr<IpoptApplication> app = new IpoptApplication();

    // Initialize the IpoptApplication and process the options
    ApplicationReturnStatus status;
    status = app->Initialize();
    if (status != Solve_Succeeded) {
        printf("\n\n*** Error during initialization!\n");
        result[0]= (jfloat) -1;
    }

    try{
        status = app->OptimizeTNLP(mynlp);
        if (status != Solve_Succeeded) {
            throw  -1;
        }
    }
    catch(int e){
        result[0]= (jfloat) e;
    }

    Number final_obj = app->Statistics()->FinalObjective();
    if (-final_obj<0){
        result[0]= (jfloat) -1;
    }
    else {
        result[0]= (jfloat) -final_obj;
    }
    for (int i = 0; i < 3*T; i++)
    {
        result[i+1]=free->getVariable()[i] ;
    }
    //release java array
    env->ReleaseFloatArrayElements(_a,a,0);
    env->ReleaseFloatArrayElements(_b,b,0);
    env->ReleaseFloatArrayElements(_v,v,0);
    env->ReleaseFloatArrayElements(_h,h,0);
    env->ReleaseFloatArrayElements(_r,r,0);
    env->ReleaseFloatArrayElements(jresult,result,0);
    return jresult;
}

JNIEXPORT jfloatArray JNICALL Java_Solvers_IpOptSolvers_IpOptSingleSolver
  (JNIEnv * env, jclass, jfloatArray _a , jfloatArray _b, jfloatArray _v, jfloatArray _h, jfloatArray _r, jint){
    //converte Java objects to C++ objects
    int T = (int)env->GetArrayLength(_a);
    jfloat* a= env->GetFloatArrayElements(_a,false);//coeff demand function
    jfloat* b= env->GetFloatArrayElements(_b,false);//coeff demand function
    jfloat* v= env->GetFloatArrayElements(_v,false);//production cost
    jfloat* h= env->GetFloatArrayElements(_h,false);//storage cost
    jfloat* r= env->GetFloatArrayElements(_r,false);//production constraint
    jfloatArray jresult = env -> NewFloatArray(2*T+2);
    jfloat* result = env -> GetFloatArrayElements(jresult, false);

    printf("***Data\n");
    printf("constraint:\t%f\t%f\t%f\t%f\n",r[0],r[1],r[2],r[3]);
    printf("alpha     :\t%f\t%f\t%f\t%f\n",a[0],a[1],a[2],a[3]);
    printf("beta      :\t%f\t%f\t%f\t%f\n",b[0],b[1],b[2],b[3]);
    printf("prodcost  :\t%f\t%f\t%f\t%f\n",v[0],v[1],v[2],v[3]);
    printf("storcost  :\t%f\t%f\t%f\t%f\n",h[0],h[1],h[2],h[3]);

    // Create an instance of your nlp...
    SingleSolver* single = new SingleSolver(a,b,v,h,r,T);
    SmartPtr<TNLP> mynlp = single;

    // Create an instance of the IpoptApplication
    SmartPtr<IpoptApplication> app = new IpoptApplication();

    // Initialize the IpoptApplication and process the options
    ApplicationReturnStatus status;
    status = app->Initialize();
    if (status != Solve_Succeeded) {
        printf("\n\n*** Error during initialization!\n");
       result[0]=(jfloat) -1;
    }

    try {
        status = app->OptimizeTNLP(mynlp);
        if (status != Solve_Succeeded) {
            throw  -1;
        }
    }
    catch(int e){
        result[0]=(jfloat) e;
    }

    Number final_obj = app->Statistics()->FinalObjective();
    if (-final_obj<0){
        result[0]= (jfloat)  -1;
    }
    else {
        result[0]= (jfloat) -final_obj;
    }

    for (int i = 0; i < 2*T+1; i++)
    {
        result[i+1]=(jfloat) single->getVariable()[i] ;
    }
    //release java array
    env->ReleaseFloatArrayElements(_a,a,0);
    env->ReleaseFloatArrayElements(_b,b,0);
    env->ReleaseFloatArrayElements(_v,v,0);
    env->ReleaseFloatArrayElements(_h,h,0);
    env->ReleaseFloatArrayElements(_r,r,0);
    env->ReleaseFloatArrayElements(jresult,result,0);
    return jresult;
}

JNIEXPORT jfloatArray JNICALL Java_Solvers_IpOptSolvers_CplSimplex
  (JNIEnv *env, jclass, jfloatArray _a, jfloatArray _b, jfloatArray _v, jfloatArray _h, jfloatArray _r, jfloatArray _p, jint _T){
    //converte Java objects to C++ objects
    int T = (int)_T;
    jfloat* a= env->GetFloatArrayElements(_a,false);//coeff demand function
    jfloat* b= env->GetFloatArrayElements(_b,false);//coeff demand function
    jfloat* v= env->GetFloatArrayElements(_v,false);//production cost
    jfloat* h= env->GetFloatArrayElements(_h,false);//storage cost
    jfloat* r= env->GetFloatArrayElements(_r,false);//production constraint
    jfloat* p= env->GetFloatArrayElements(_p,false);//price for each time period


    //initiate data
    double* columnLB = new double[2*T];
    double* columnUB = new double[2*T];
    double* rowLB = new double[T];
    double* rowUB = new double[T];
    double* objCoef = new double[2*T];
    int* rowIndex = new int[3*T-1];
    int* colIndex = new int[3*T-1];
    double* elemt = new double[3*T-1];
    CoinBigIndex nbelemt=3*T-1;
    ClpSimplex  model; //LP
    ClpSimplex  model2; //LP
    //optimum
    jfloatArray jresult = env -> NewFloatArray(2*T+1);
    jfloat* opti = env -> GetFloatArrayElements(jresult, false);

    for(int i=0;i<T;i++) {
        rowLB[i]=rowUB[i]=a[i]-p[i]*b[i]; //equality constraint
        columnUB[i]=r[i]; //production constraint
        columnUB[i+T]=1e99; //storage has no upper bound
        columnLB[i]=0; // production is positive
        columnLB[i+T]=0; //storage too

        //objective
        objCoef[i]=v[i];
        objCoef[i+T]=h[i];
        //define equality constraint matrix
        rowIndex[i]=rowIndex[i+T]=i;
        colIndex[i]=i;
        colIndex[i+T]=i+T;
        elemt[i]= 1;
        elemt[i+T]=-1;
        if(i<T-1) {
            rowIndex[i+2*T]=i+1;
            colIndex[i+2*T]=i+T;
            elemt[i+2*T]=1;
        }
    }

    printf("***Data\n");
    printf("demand    :\t%f\t%f\t%f\t%f\n",a[0]-p[0]*b[0],a[1]-p[1]*b[1],a[2]-p[2]*b[2],a[3]-p[3]*b[3]);
    printf("constraint:\t%f\t%f\t%f\t%f\n",r[0],r[1],r[2],r[3]);
    printf("price     :\t%f\t%f\t%f\t%f\n",p[0],p[1],p[2],p[3]);
    printf("alpha     :\t%f\t%f\t%f\t%f\n",a[0],a[1],a[2],a[3]);
    printf("beta      :\t%f\t%f\t%f\t%f\n",b[0],b[1],b[2],b[3]);
    printf("prodcost  :\t%f\t%f\t%f\t%f\n",v[0],v[1],v[2],v[3]);
    printf("storcost  :\t%f\t%f\t%f\t%f\n",h[0],h[1],h[2],h[3]);

    //construct and load this program
    CoinPackedMatrix matrix(false,rowIndex,colIndex,elemt,nbelemt);
    model.loadProblem(matrix,columnLB,columnUB,objCoef,rowLB,rowUB);
    model2.loadProblem(matrix,columnLB,columnUB,objCoef,rowLB,rowUB);
    printf("***Primal\n");
    model.primal();
    if(model.status()==0) {
        opti[0]= (jfloat) -model.objectiveValue();
        for (int i = 0; i < 2*T; i++){
            opti[i+1] = (jfloat) model.primalColumnSolution()[i];
        }

    }
    else {
        printf("***Dual\n");
        model2.dual();
        if(model2.status()==0) {
            opti[0]=(jfloat)-model2.objectiveValue();
            for (int i = 0; i < 2*T; i++){
                opti[i+1] = (jfloat) model2.dualColumnSolution()[i];
            }
        }
    }

    printf("Primal infeasibilities(%d,%d): %f\n",model.numberPrimalInfeasibilities(),model.numberDualInfeasibilities(),model.objectiveValue());
    printf("Dual infeasibilities(%d,%d): %f\n",model2.numberPrimalInfeasibilities(),model2.numberDualInfeasibilities(),model2.objectiveValue());
    if(model.isProvenOptimal()| model2.isProvenOptimal()){
        for(int i=0;i<T;i++) {
            opti[0]+=(jfloat)(a[i]-b[i]*p[i])*p[i];
        }
        printf("Objective:%e\n",opti[0]);
    }
    else {
        printf("Objective:UNSOLVED\n");
    }

    printf("***Opti\n");
    printf("production:\t%f\t%f\t%f\t%f\n",opti[1],opti[2],opti[3],opti[4]);
    printf("storage   :\t%f\t%f\t%f\t%f\n",opti[5],opti[6],opti[7],opti[8]);

    //release java array
    env->ReleaseFloatArrayElements(_a,a,0);
    env->ReleaseFloatArrayElements(_b,b,0);
    env->ReleaseFloatArrayElements(_v,v,0);
    env->ReleaseFloatArrayElements(_h,h,0);
    env->ReleaseFloatArrayElements(_r,r,0);
    env->ReleaseFloatArrayElements(_p,p,0);
    env->ReleaseFloatArrayElements(jresult,opti,0);
    return jresult;
}
