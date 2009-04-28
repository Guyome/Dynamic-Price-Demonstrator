package Solvers;


public class IpOptSolvers {
    static {
        System.loadLibrary("solvers");
    }
    public static native float[] CplSimplex(float[] alpha,float[] beta,float[] constraint,
        float[] prod_cost,float[] stor_cost, float[] price, int time);

    public static native float[] IpOptFreeSolver(float[] alpha,float[] beta,float[] constraint,
        float[] prod_cost,float[] stor_cost, int time);

    public static native float[] IpOptSingleSolver(float[] alpha,float[] beta,float[] prod_cost,
        float[] stor_cost,float[] constraint, int time);

    public static float[] IpOptMultiSolver(float[] alpha,float[] beta,float[] constraint,
        float[] prod_cost,float[] stor_cost,float[] price, int time)
    {
        float[] solve=new float[4*time+1];
        //full ennumeration time period limited at 4
        float[] results = new float[2*time+1];
        float[][] priceposs= new float[(int)java.lang.Math.pow(price.length,time)][time];
        int count=0;
        System.out.println("Number of possibilities:"+price.length+"**"+time+"="+java.lang.Math.pow(price.length,time));
        for (int i = 0; i < price.length; i++)
        {
            for (int j = 0; j < price.length; j++)
            {
                for (int k = 0; k < price.length; k++)
                {
                    for (int l = 0; l < price.length; l++)
                    {
                        priceposs[count][0]=price[i];
                        if(time>1)
                        {
                            priceposs[count][1]=price[j];
                            if(time>2)
                            {
                                priceposs[count][2]=price[k];
                                if(time>3)
                                {
                                    priceposs[count][3]=price[l];
                                }
                            }
                        }
                        if (time==4) {count++;}
                    }
                    if (time==3) {count++;}
                }
                if (time==2) {count++;}
            }
            if (time==1) {count++;}
        }
        for (int i = 0; i <java.lang.Math.pow(price.length,time) ; i++)
        {
            System.out.println("Price["+i+"]: "+priceposs[i][0]+" "+priceposs[i][1]+" "+priceposs[i][2]+" "+priceposs[i][3]);
        }

        for (int i= 0; i <java.lang.Math.pow(price.length,time);i++)
        {

            results=IpOptSolvers.CplSimplex(alpha,beta,constraint,prod_cost,stor_cost, priceposs[i], time);
            if (results[0]>solve[0])
            {
                solve[0]=results[0];
                System.out.println("Result["+i+"]:"+results[0]+"\nPoss["+i+"]:");
                for (int j = 0; j < time; j++)
                {
                    solve[j+1]=alpha[j]-beta[j]*priceposs[i][j];
                    solve[j+1+time]=priceposs[i][j];
                    solve[j+1+2*time]=results[j+1];
                    solve[j+1+3*time]=results[j+1+time];
                    System.out.println("\t"+priceposs[i][j]);
                }
            }
        }
        return solve;
    }

    /*public static void main(String[] args){
        float[] _alpha = {100, 100,100,100};
        float[] _beta ={1,1,1,1};
        float[] _constraint = {18,42,25,14};
        float[] _prod_cost ={20, 20, 20,20};
        float[] _stor_cost = {2, 2, 2,2} ;
        float[] _priceposs ={90,95,100};
        float[] _price ={72,85,91,87};

        int _time=4;

        float result = Solvers.IpOptSolvers.IpOptFreeSolver(_alpha, _beta, _constraint,_prod_cost, _stor_cost,_time);
        System.out.println("\nResults Free: "+result);

        result = Solvers.IpOptSolvers.IpOptSingleSolver(_alpha, _beta, _constraint,_prod_cost, _stor_cost,_time);
        System.out.println("\nResults Single:"+result);

        result = Solvers.IpOptSolvers.CplSimplex(_alpha, _beta, _constraint,_prod_cost, _stor_cost,_price,_time);
        System.out.println("\nResults Cimplex:"+result);

        float result = Solvers.IpOptSolvers.IpOptMultiSolver(_alpha, _beta, _constraint,_prod_cost, _stor_cost,_priceposs,_time);
        System.out.println("\nResults Multi:"+result);
    }*/
}
