import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.lang.String;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class CsvTools {

    protected File file;
    protected FrameData parent;

    public CsvTools(FrameData _parent){
        file=null;
        parent=_parent;
    }

    private void importData() {
        try {
            float[] alpha = new float[parent.getTime()];
            float[] beta = new float[parent.getTime()];
            float[] demand = new float[parent.getTime()];
            float[] constraint = new float[parent.getTime()];
            float[] prod_cost = new float[parent.getTime()];
            float[] stor_cost = new float[parent.getTime()];
            BufferedReader source_file = new BufferedReader(new FileReader(file));
            String line;
            int i =-1;
            while(((line= source_file.readLine())!= null)&&(i<parent.getTime())){
                if (i>=0){
                    String[] data = line.split(";");
                    alpha[i] = (float)Float.parseFloat(data[1]);
                    beta[i] = (float)Float.parseFloat(data[2]);
                    demand[i] = (float)Float.parseFloat(data[3]);
                    constraint[i] = (float)Float.parseFloat(data[4]);
                    prod_cost[i] = (float)Float.parseFloat(data[5]);
                    stor_cost[i] = (float)Float.parseFloat(data[6]);
                }
                i++;
            }
            source_file.close();
            for(i=0;i<parent.getTime();i++){
                parent.setData(alpha,beta,demand,constraint,prod_cost,stor_cost);
            }
        }
        catch (IOException e) {
            System.out.println("File not fund!");
            JOptionPane.showMessageDialog(parent,
                "File not fund!",
                "Dpd error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportData() {
        try{
            BufferedWriter source_file=new BufferedWriter(new FileWriter(file));
            String line;
            float[] alpha= parent.getAlphaP();
            float[] beta= parent.getBetaP();
            float[] demand= parent.getDemand();
            float[] constraint= parent.getConstraint();
            float[] production= parent.getProdCost();
            float[] storage= parent.getStorCost();
            for(int i=-1;i<parent.getTime();i++){
                if (i>=0) {
                        line=Integer.toString(i)+';'+Float.toString(alpha[i])+';'+
                            Float.toString(beta[i])+';'+Float.toString(demand[i])+';'+
                            Float.toString(constraint[i])+';'+Float.toString(production[i])
                            +';'+Float.toString(storage[i])+'\n';
                }
                else {
                    line="T;Alpha;Beta;Demand;Constraint;Prod.Cost;Stor.Cost\n";
                }
                source_file.write(line,0,line.length());
            }
            source_file.close();
        }
        catch(IOException e) {
            System.out.println("File not fund!");
            JOptionPane.showMessageDialog(parent,
                "File not fund!",
                "Dpd error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void importfile(boolean save) {
        JFileChooser choose;
        int returnVal;
        choose = new JFileChooser();
        if (!save) {
            //add filter
            DpdFilter filter = new DpdFilter();
            choose.setFileFilter(filter);
            returnVal = choose.showOpenDialog(null);
        }
        else {
            returnVal = choose.showSaveDialog(null);
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = choose.getSelectedFile();
            if (save) {
                if (!(file.getName().endsWith(".csv") || file.getName().endsWith(".CSV"))) {
                    file= new File(file.getParentFile(), file.getName()+ ".csv");
                }
                exportData();
            }
            else {
                importData();
            }
        }
    }

}
