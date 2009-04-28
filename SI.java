import Solvers.*;
import java.util.Arrays;


public class SI{

    //data
    FrameData frame;
    float[][] all;

    public SI(FrameData _frame)
    {
        frame=_frame;
        all= new float[4][4*frame.getTime()+1];
        for (int i = 0; i < 4; i++)
        {
            Arrays.fill(all[i],0);
        }
    }

    public float[] Single()
    {
        System.out.println("COIN-OR Single Solver");
        float[] result = new float[2*frame.getTime()+1];//solvers return optimum and price
        result = IpOptSolvers.IpOptSingleSolver(frame.getAlpha() ,frame.getBeta(),frame.getProdCost(),
                    frame.getStorCost(), frame.getConstraint(), frame.getTime());
        all[2][0]=result[0];
        for (int i = 0; i < frame.getTime(); i++)
        {
            all[2][i+1]= frame.getAlpha()[i]-frame.getBeta()[i]*result[frame.getTime()+1];
            all[2][i+1+frame.getTime()]=result[frame.getTime()+1];
            all[2][i+1+2*frame.getTime()]=result[i+1];
            all[2][i+1+3*frame.getTime()]=result[i+2+frame.getTime()];
        }
        return all[2];
    }

    public float[] Multi()
    {
        if (frame.getHeuristic())
        {
            MultiSolver multi = new MultiSolver(frame.getTime(), frame.getConstraint(), frame.getAlpha(), frame.getBeta(),
                    frame.getProdCost(), frame.getStorCost(), frame.getMultiPrice());
            all[1] = multi.MultiPriceSolve();
        }
        else
        {
            System.out.println("COIN-OR Multi Solver");
            all[1] = IpOptSolvers.IpOptMultiSolver(frame.getAlpha() ,frame.getBeta(), frame.getProdCost(),
                    frame.getStorCost(),frame.getConstraint(),frame.getMultiPrice(), frame.getTime());
        }
        return all[1];
    }

    public float[] Fixed()
    {
        float[] result = new float[2*frame.getTime()+1];
        if (frame.getHeuristic())
        {
            FixedSolver fixed = new FixedSolver(frame.getConstraint(),frame.getAlpha(),frame.getBeta(),frame.getProdCost(),
                    frame.getStorCost(),frame.getFixedPrice());
            all[3]=fixed.solvefixed();
        }
        else
        {
            System.out.println("COIN-OR Fixed Solver");
            result= IpOptSolvers.CplSimplex(frame.getAlpha() ,frame.getBeta(), frame.getProdCost(),
                    frame.getStorCost(),frame.getConstraint(),toArray(frame.getFixedPrice()), frame.getTime());
            all[3][0]=result[0];
            for (int i = 0; i < frame.getTime(); i++)
            {
                all[3][i+1]=frame.getAlpha()[i]-frame.getBeta()[i]*frame.getFixedPrice();
                all[3][i+1+frame.getTime()]=frame.getFixedPrice();
                all[3][i+1+2*frame.getTime()]=result[1+i];
                all[3][i+1+3*frame.getTime()]=result[1+i+frame.getTime()];
            }
        }
        return all[2];
    }

    public float[] Free()
    {
        float[] result;
        if (frame.getHeuristic())
        {
            FreeSolver free =  new FreeSolver (frame.getAlpha() ,frame.getBeta(), frame.getConstraint(),
                    frame.getStorCost(),frame.getProdCost(), frame.getDemand(), frame.getTime());
            all[0]=free.FreeSolve();
        }
        else {
            System.out.println("COIN-OR Free Solver");
            result = IpOptSolvers.IpOptFreeSolver(frame.getAlpha() ,frame.getBeta(), frame.getConstraint(),
                    frame.getProdCost(),frame.getStorCost(),frame.getTime());
            System.out.println("Result length: "+result.length);
            all[0][0]=result[0];
            for (int i = 0; i < frame.getTime(); i++)
            {
                all[0][i+1]=frame.getAlpha()[i]-frame.getBeta()[i]*result[1+i+frame.getTime()];
                all[0][i+1+frame.getTime()]=result[1+i+frame.getTime()];
                all[0][i+1+2*frame.getTime()]=result[1+i];
                all[0][i+1+3*frame.getTime()]=result[1+i+2*frame.getTime()];
            }
        }
        return all[3];
    }

    public float[][] getResults(){
        return all;
    }

    private float[] toArray(float value)
    {
        float[] array = new float [frame.getTime()];
        for (int i = 0; i < frame.getTime(); i++)
        {
            array[i]=value;
        }
        return array;
    }
}
