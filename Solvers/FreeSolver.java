package Solvers;

public class FreeSolver {
    //attributs : parameters
    private float[] alpha;
    private float[] beta;
    private float[] constraint;
    private float[] prod_cost;
    private float[] stor_cost;
    private int time;
    //attributs : variables
    private float[][] production;
    private float[][] cost;
    private float[] demand;
    private float[] lambda;
    private boolean[] link;

    //constructor
    public FreeSolver(float[] _alpha,float[] _beta,float[] _constraint,
        float[] _prod_cost,float[] _stor_cost,float[] _demand, int _time) {
        //allocate time
        time = _time;

        //define all tabulars
        alpha = new float[time];
        beta = new float[time];
        constraint = new float[time];
        prod_cost = new float[time];
        stor_cost = new float[time];
        production = new float[time][time];
        cost = new float[time][time];
        demand = new float[time];
        lambda = new float[time];
        link = new boolean[time];

        //allocate all paramters
        alpha = _alpha;
        beta = _beta;
        constraint = _constraint;
        prod_cost = _prod_cost;
        stor_cost = _stor_cost;

        //allocate all variables
        demand =_demand;
        for (int i = 0; i < time; i++) {
            lambda[i] = 0;
            link[i] = false;
            for (int j = 0; j < time; j++) {
                cost[i][j] = 0;
                production[i][j]=0;
                if (j<=i) {
                    cost[i][j] = prod_cost[j]+(i-j)*stor_cost[j];
                }
            }
            production[i][i] = demand[i];
        }
    }

    //all routines necessary for solve function
    //update routine
    public float Update(int T,float delta) {
        float delta_lambda;

        //choose of the lambda increase
        if ( (T>0) && (cost[T][T-1]-cost[T][T] < delta)  ) {
            delta_lambda = Math.abs(cost[T][T]-cost[T][T-1]);
            link[T-1] =true;
        }
        else {
            delta_lambda = delta;
        }
        System.out.println("Rise lambda["+T+"]: "+delta_lambda);

        //increase of cost, lambda, demand
        for (int i = T; i < this.time; i++) {
            cost[i][T] += delta_lambda;
        }
        lambda[T] += delta_lambda;
        demand[T] = (alpha[T]-cost[T][T])/(2*beta[T]);

        return delta_lambda;
    }

    //function to manage active constraint
    private void ExecessCapacity(int T) {
        float delta_lambda;
        //float sum_col=0;

        delta_lambda = alpha[T]-2*beta[T]*constraint[T]-cost[T][T];
        if ( T>0 ) {
            this.Update(T,delta_lambda);
            //manage production and storage
            System.out.println("Modify Production from "+T+" to "+ T);
            production[T][T-1]=Math.max(constraint[T-1]-production[T-1][T-1],(float)0);
            System.out.println("Modify Production["+T+"]["+(T-1)+"]: "+production[T][T-1]);
        }
    }

    //function to count periods who are linked together
    private int Counter(int T) {
        int k = T-1;
        int counter = 1;

        if (T!=0) {
            while (link[k]) {
                counter ++;
                if (k>0){
                    k--;
                }
                else{
                    break;
                }
            }
        }
        //System.out.println("counter: "+counter);
        return counter;
    }

    //function to calcul lambda's increment fo linked periods
    private float RiseLambda(int T) {
        float rise = 0;
        float sum = 0;

        for (int i = T-this.Counter(T); i < T; i++) {
            rise += beta[T]/(float)2;
        }

        for (int i = 0; i < T; i++) {
            sum+= production[T][i];
        }
        sum+=constraint[T];
        //System.out.println(demand[T]+"-"+sum+"/"+rise);
        return Math.max(demand[T]-sum,(float)0)/rise;
    }

    //function who increses lambda for all linked periods
    private void RiseCost(int T) {
        float delta_lambda;
        //float sum;
        //int k;

        delta_lambda = this.RiseLambda(T);
        for(int i = T+1-this.Counter(T); i <= T; i++) {
            this.Update(i, delta_lambda);
        }
            //manage production and storage
        System.out.println("Modify Production from "+T+" to "+(T+1-this.Counter(T)));
        for(int i = T; i >= T+1-this.Counter(T); i--) {
            if (i<T && i>0) {
                production[i][i]=constraint[i]-production[i+1][i];
            }
            else {
                production[i][i]=Math.min(constraint[i],demand[i]);
            }
            System.out.println("Modify Production["+i+"]["+i+"]: "+production[i][i]);
            if (i>0 && link[i-1]){
                production[i][i-1]=demand[i]-production[i][i];//Math.max(constraint[i-1]-production[i-1][i-1],demand[i]-production[i][i]);
                System.out.println("Modify Production["+i+"]["+(i-1)+"]: "+production[i][i-1]);
            }
            else{
               // break;
            }
        }
    }

    //function who display all values
    private void Display() {
        System.out.println("Demand:"+LineDisplay(demand));
        System.out.println("Constraint:"+LineDisplay(constraint));
        System.out.println("Lambda:"+LineDisplay(lambda));
        System.out.println("Link:"+LineDisplay(link));
        System.out.println("Cost:");
        for (int i = 0; i < time; i++) {
            System.out.println(i+":"+LineDisplay(cost[i]));
        }
        System.out.println("Production:");
        for (int i = 0; i < time; i++) {
            System.out.println(i+":"+LineDisplay(production[i]));
        }
    }

    //sub function who only one line. need by Display
    static private String LineDisplay(float[] _vector) {
        String line = new String();
        for (int i=0; i< _vector.length; i++) {
            line += _vector[i]+" ";
        }
        return line;
    }

    //sub function who only one line. need by Display for boolean vector
    static private String LineDisplay(boolean[] _vector) {
        String line = new String();
        for (int i=0; i< _vector.length; i++) {
            line += _vector[i]+" ";
        }
        return line;
    }

    //function who return the optimum
    private float Optimum() {
        float sum = 0;
        float stor = 0;
        float prod;

        for (int i = 0; i < time; i++) {
            if  (i<time-1) {
                prod = production[i][i]+production[i+1][i];
            }
            else {
                prod = production[i][i];
            }
            if (i>1) {
                stor =production[i][i-1];
            }
            sum+=demand[i]*cost[i][i]-stor_cost[i]*stor-prod_cost[i]*prod;
        }

        /*for (int i = 0; i < time; i++) {
            for (int j = 0; j < i; j++) {
                _sum += production[i][j];
                __sum += (cost[i][j]-lambda[j])*production[i][j];
            }
            sum += (alpha[i]-2*beta[i]*_sum)*_sum-__sum;
        }*/
        return sum;
    }

    //function who execute one iteration
    private void Iterate(int i) {
        float sum = 0;
        if (demand[i]>constraint[i]){
            System.out.println("***** excesscap "+i+" *****");
            this.ExecessCapacity(i);
            this.Display();
        }
        for (int j = 0; j < i; j++) {
            sum += production[i][j];
        }
        if ((sum < demand[i])&& (demand[i]>constraint[i])) {
            System.out.println("***** RiseCost "+i+" *****");
            this.RiseCost(i);
            this.Display();
        }
        else {
            System.out.println("***** Unactive "+i+" *****");
            this.Update(i, (float)0);
            this.link[i]=false;
            this.Display();
        }
    }


    //function who solves optimization program
    public float [] FreeSolve() {
        float[] answer = new float[4*time+1];

        for(int i =0; i < time; i++) {
            this.Iterate(i);
        }

        answer[0]=this.Optimum();
        for (int i = 0; i < time; i++) {
            answer[1+i]=this.demand[i];
            answer[time+1+i]=(this.alpha[i]-this.demand[i])/this.beta[i];
            answer[2*time+1+i]=this.production[i][i]+answer[time+1+i];
            if (i<time-1) {
                answer[3*time+1+i]=this.production[i+1][i];
            }
            else {
                answer[3*time+1+i]=0;
            }
        }
        System.out.println("Answer: "+LineDisplay(answer));
        return answer;
    }

 /* public static void main(String[] args) {
       //float[] _alpha = {100, 100, 100};
       //float[] _beta = {1, 1, 1};
       //float[] _constraint = {50, 10, 21} ;
       //float[] _demand = {40, 40, 40};
       //float[] _prod_cost ={20, 20, 20};
       //float[] _stor_cost = {2, 2, 2} ;
       //int _time = 3;

       float[] _alpha = {100, 100,100,100};
       float[] _beta ={1,1,1,1};
       float[] _constraint = {18,42,26,14};
       float[] _demand = {40, 40, 40,40};
       float[] _prod_cost ={20, 20, 20,20};
       float[] _stor_cost = {2, 2, 2,2} ;
       int _time =4;

       FreeSolver solve = new FreeSolver(_alpha,_beta, _constraint,_prod_cost,_stor_cost,_demand,_time);
       System.out.println("******Initialisation******");
       solve.Display();
       float[] value = solve.FreeSolve();
       System.out.println("****** End ******");
       System.out.println("Optimum:"+LineDisplay(value));
       solve.Display();
    }*/
}
