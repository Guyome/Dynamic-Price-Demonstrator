package Solvers;

public class MultiSolver{
    protected int time;
    protected float[] price_poss;
    protected float[] alpha;
    protected float[] beta;
    protected float[] price;
    protected float[] demand;
    protected float[] constraint;
    protected float[] storage;
    protected float[] production;
    protected float[] stor_cost;
    protected float[] prod_cost;
    protected final int MAX_PRICE=3;

    public MultiSolver(int _time, float[]_constraint,float[] _alpha,float[] _beta,
        float[] _prod_cost,float[] _stor_cost,float[] _price_poss){
        demand = new float [_time];
        alpha = new float [_time];
        beta = new float [_time];
        production = new float [_time];
        storage = new float [_time];
        price = new float [_time];

        time=_time;
        price_poss=_price_poss;
        constraint=_constraint;
        prod_cost=_prod_cost;
        stor_cost=_stor_cost;
        alpha=_alpha;
        beta=_beta;
        for(int i=0; i< time; i++){
            demand[i] = (float)0;
            production[i]= (float)0;
            storage[i]= (float)0;
            price[i]= (float)0;
        }
    }


    protected float[] SimplexRun(int p, int t) {
        float demandt =alpha[t]-beta[t]*price_poss[p];
        float[] nodes_prod = new float [4];
        float[] nodes_stor = new float [4];
        float[] variable = new float [4];
        int k ;
        if (t!=0) {
            float[] aux1 = {(float)0,(float)0, constraint[t],Math.min(constraint[t],demandt-storage[t])};
            float[] aux2 = {(float)0,storage[t-1]-demandt,constraint[t]+storage[t-1]-demandt,(float)0};
            nodes_prod = aux1;
            nodes_stor = aux2;
        }
        else{
            float[] aux1 = {(float)0,(float)0, constraint[t],Math.min(constraint[t],demandt)};
            float[] aux2 = {(float)0,(float)0,constraint[t]-demandt,(float)0};
            nodes_prod = aux1;
            nodes_stor = aux2;
        }

        if (t < (MAX_PRICE-1)){
            float max_demand=alpha[t+1]-beta[t+1]*Math.min(price_poss[0],Math.min(price_poss[1],price_poss[2]));
            if (max_demand< constraint[t+1]) {
                if ((t!=0) && (demandt< storage[t-1])) {
                    k=0;
                }
                else{
                    k=3;
                }
            }
            else {
                if((t!=0) && (demandt< storage[t-1])){
                    k=1;
                }
                else{
                    k=2;
                }
            }
        }
        else {
            if(demandt< storage[t-1]) {
                    k=1;
                }
            else{
                    k=2;
                }
        }
        System.out.println("Nodes Choosed: "+k);
        variable[0]=nodes_prod[k];
        variable[1]=nodes_stor[k];
        variable[2]=price_poss[p];
        variable[3]=demandt*price_poss[p]-prod_cost[t]*nodes_prod[k]-stor_cost[t]*nodes_stor[k];
        System.out.println("Profit: "+variable[3]+"/ Production: "+variable[0]+"/ Storage:"+variable[1]+"/ Price: "+variable[2]
        +"/Demand: "+demandt);
        return variable;
    }

    protected void Simplex(int t) {
        //find maximun for time fixed
        float[] variable=SimplexRun(0,t);
        float[] aux;
        float value=variable[3];
        price[t]=price_poss[0];

        for (int k =1; k < MAX_PRICE;k++){
            aux=SimplexRun(k,t);
            if (t==0) {
                if ((aux[0]>=0)&&(aux[1]>=0)&&(constraint[t]>=alpha[t]-beta[t]*price_poss[k])&&(value< aux[3])){
                    value=aux[3];
                    variable=aux;
                    price[t]=price_poss[k];
                    System.out.println("Price updated: "+price[t]);
                }
            }
            else {
                if ((aux[0]>=0)&&(aux[1]>=0)&&(value< aux[3])){
                    value=aux[3];
                    variable=aux;
                    price[t]=price_poss[k];
                    System.out.println("Price updated: "+price[t]);
                }
            }
        }
        production[t]=variable[0];
        storage[t]=variable[1];
        demand[t]=alpha[t]-beta[t]*price[t];
        System.out.println("Production["+t+"]: "+variable[0]);
        System.out.println("Storage["+t+"]: "+variable[1]);


    }


    public float Critere() {
        float objective=0;
        for(int i=0; i< time; i++) {
            objective += price[i]*demand[i]-stor_cost[i]*storage[i]-prod_cost[i]*production[i];
        }
        return objective;
    }


    public float[] MultiPriceSolve() {
        float[] answer = new float[4*time+1];

        for(int t=0; t<time;t++) {
            System.out.println("*********t= "+t+"*******");
            Simplex(t);
        }


        answer[0]=this.Critere();
        for (int i = 0; i < time; i++) {
            answer[1+i]=this.demand[i];
            answer[time+1+i]=this.price[i];
            answer[2*time+1+1]=this.production[i];
            answer[3*time+1+i]=Math.max(this.storage[i],(float)0);
        }
        return answer;
    }

    //Next two functions are only needed for testing and display
    static private String LineDisplay(float[] _vector) {
        String line = new String();
        for (int i=0; i< _vector.length; i++) {
            line += _vector[i]+" ";
        }
        return line;
    }


    /*public static void main(String[] args) {
        float[] constraint = { 18, 42, 26, 14};
        float[] alpha = {100, 100, 100, 100};
        float[] beta = {1, 1, 1, 1};
        float[] prodcost = {20,20,20,20};
        float[] storcost = {2,2,2,2};
        float[] pricepossibility = {100, 90,80};
        int time = 4;
        //float[] alpha = {100, 100,100,100};
        //float[] beta ={1,1,1,1};
        //float[] constraint = {18,42,26,14};
        //float[] demand = {40, 40, 40,40};
        //float[] prodcost ={20, 20, 20,20};
        //float[] storcost = {2, 2, 2,2};
        //float[] pricepossibility = {82,82,82};
        //int time =4;
        MultiSolver_bis solve = new MultiSolver(time,constraint,alpha,beta,prodcost,storcost,pricepossibility);

        System.out.println("*********Init*******");
        System.out.println("Initial time: "+solve.time);
        System.out.println("Initial alpha: "+LineDisplay(solve.alpha));
        System.out.println("Initial beta: "+LineDisplay(solve.beta));
        System.out.println("Initial constraint: "+LineDisplay(solve.constraint));
        System.out.println("Initial prodcost: "+LineDisplay(solve.prod_cost));
        System.out.println("Initial storcost: "+LineDisplay(solve.stor_cost));
        System.out.println("Initial price possibility: "+LineDisplay(solve.price_poss));

        System.out.println("*********Solve*******");
        System.out.println("Optimum: "+LineDisplay(solve.MultiPriceSolve()));
        System.out.println("Optimum production: "+LineDisplay(solve.production));
        System.out.println("Optimum storage: "+LineDisplay(solve.storage));
        System.out.println("Optimum price: "+LineDisplay(solve.price));
    }*/

}
