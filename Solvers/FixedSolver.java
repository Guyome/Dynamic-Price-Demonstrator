package Solvers;

public class FixedSolver {
    //attributs
    protected float[] production;
    protected float[] demand;
    protected static float[] constraint;
    protected float[][] storage;
    protected int time;
    protected float[] stor;
    protected float[] prod;
    protected float price;

    //constructor
    public FixedSolver(float[]cst,float[] alpha,float[] beta,float[] prod_cost,float[] stor_cost,float _price) {
        time = cst.length;
        demand = new float[time];
        constraint = new float[time];
        production = new float[time];
        prod = new float[time];
        stor = new float[time];
        constraint = cst;
        price=_price;
        prod = prod_cost;
        stor = stor_cost;
        storage = new float[time][time];
        for (int i = 0; i <time; i++) {
            demand[i]=(alpha[i]-price)/beta[i];
            //test data
            production[i]=demand[i];
            for (int j = 0; j < time; j++) {
                storage[i][j] = 0;
            }
        }
    }

    //functions who return variables
    //storage

    public float[][] getStorage(){
        return storage;
    }

    //Production

    public float[] getProduction(){
        return production;
    }

    //Constraint

    public float[] getConstraint(){
        return constraint;
    }

    //Time

    public int getTime() {
        return time;
    }

    static private String LineDisplay(float[] _vector) {
        String line = new String();
        for (int i=0; i< _vector.length; i++) {
            line += _vector[i]+" ";
        }
        return line;
    }

    //Function who return True if it's not the optimum
    protected boolean NotOptimum() {
        boolean awr = false;
        for (int i = 0; i < time; i++) {
            if (production[i] > constraint[i])  {
                System.out.println("Pb constraint T:"+i);
                awr = true;
            }
        }
        return awr;
    }

    //Function who verify if all demand are satisfy

    protected boolean NotSolve() {
        boolean awr = false;
        for (int i = 0; i < time; i++) {
            if (demand[i] > production[i]+sumrow(storage)[i])  {
                System.out.println("Unsatisfy demand: "+i);
                awr = true;
            }
        }
        return awr;
    }

    //Test if this constraint is active

    protected boolean ActiveConstraint(int T) {
        if (demand[T] > constraint[T] ) {
            System.out.println("Active");
            return true;
        }
        else {
            System.out.println("Unactive");
            return false;
        }
    }

    //summations functions
    //classic summations

    protected float sum(float[] value) {
        float summation = 0;
        for (int i = 0; i < value.length; i++) {
            summation+=value[i];
        }
        return summation;
    }

    //matrix summations
    protected float[]  sum(float[][] value) {
        float[] summation = new float[value.length];
        for (int i = 0; i < summation.length; i++) {
            summation[i]=sum(value[i]);
        }
        return summation;
    }

    protected float[]  sumrow(float[][] value) {
        float[][] transpose = new float[value.length][value.length];
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value.length; j++) {
                transpose[i][j]=value[j][i];
            }
        }
        return sum(transpose);
    }
    //function who calculs storge at T             answer[3*time+1+i]=this.price[i];

    protected float stor_value (float limit,int T) {
        System.out.println("value: "+Math.max((float)0,constraint[T]-demand[T]-sum(storage[T])) );
        return Math.min(limit,Math.max((float)0,constraint[T]-demand[T]-sum(storage[T])) );
    }
    //function whor calculs objective value

    protected float objective() {
        float summation =0;
        for (int i = 0; i < time; i++) {
            summation += price*demand[i]-prod[i]*(production[i]+sum(storage[i]))-stor[i]*sum(storage[i]);
        }
        return summation;
    }

    protected  void violation(int T) {
        float limit= demand[T]-constraint[T];
        System.out.println("difference: "+limit);
        production[T]=constraint[T];
        for (int i = 0; i < T+1; i++) {
                storage[T-i][T]=stor_value(limit,T-i);
                limit-=storage[T-i][T];
                System.out.println("storage["+(T-i)+","+T+"]:"+storage[T-i][T]);
        }
    }

    // function who display one vector
    public void display(String label,float [] vector ) {
        String output = new String();
        for (int i = 0; i < vector.length; i++) {
            output += vector[i]+"/";
        }
        System.out.println(label+": "+output);
    }


    // function who solve this optimisation pb
    public float[] solvefixed () {
        float[] answer =new float[4*time+1];
        int t =0;
        this.display("Storage:", this.sum(this.getStorage()));
        this.display("Production:",this.getProduction());
        this.display("constrait:", this.getConstraint());
        this.display("Demand:",this.demand);
        System.out.println("Price: "+price);
        System.out.println("Storage cost:"+LineDisplay(stor));
        System.out.println("Production Cost:"+LineDisplay(prod));
        while (this.NotOptimum() & t < this.time) {
            System.out.println("Demand: "+this.demand[t]+" Constraint: "+this.constraint[t]);
            if(this.ActiveConstraint(t)) {
                this.violation(t);
            }
            System.out.println("End Iteration "+t);
            System.out.println("");
            t++;
        }
        this.display("Storage:", this.sum(this.getStorage()));
        this.display("Production:",this.getProduction());
        this.display("constrait:", this.getConstraint());
        this.display("Demand:",this.demand);

        if(this.NotOptimum() & this.NotSolve()) {
            System.out.println("! Unsolved !");
        }
        else {
            System.out.println("! Solved !");
            System.out.println("Objective value:"+this.objective());
        }
        //create vector with all informations to display them
        answer[0]=this.objective();
        for (int i = 0; i < time; i++) {
            answer[1+i]=this.demand[i];
            answer[time+1+i]=this.price;
            answer[2*time+1+i]=production[i]+answer[time+1+i];
            if (i <time-1) {
                answer[3*time+1+i]=sumrow(storage)[i+1];
            }
            else {
                answer[3*time+1+i]=0;
            }
        }
        return answer;
    }


  /* public static void main(String[] args) {
        float[] _alpha = {100, 100,100,100};
        float[] _beta ={1,1,1,1};
        float[] _constraint = {18,42,26,14};
        float[] _prod_cost ={20, 20, 20,20};
        float[] _stor_cost = {2, 2, 2,2} ;
        float _price = 82;
        FixedSolver solver = new FixedSolver(_constraint,_alpha,_beta,_prod_cost,_stor_cost,_price);
        solver.solvefixed();
    }*/
}
