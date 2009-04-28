import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;


class FramePrice extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    //attributs: price
    private float[] price;
    private int number;
    private JTextField[] price_field;
    //attributs: alpha, beta, constraint
    private float[] alpha;
    private float[] beta;
    private float[] constraint;
    //attributs: Mother frame
    private FrameData mother;
    //attributs: warning label
    JLabel warning = new JLabel("");
    //attributs: button
    private JButton validate = new JButton("Ok");
    private JButton cancel = new JButton("Cancel");

    public FramePrice(FrameData motherframe, int size) {
        //locals variables
        JLabel intro = new JLabel("Give the price, please.");
        JPanel intro_pnl = new  JPanel (new FlowLayout());
        JPanel warning_pnl = new  JPanel (new FlowLayout());
        JLabel price_lbl = new JLabel("Price:");
        JPanel price_pnl = new  JPanel (new FlowLayout());
        JPanel button_pnl = new  JPanel (new FlowLayout());

        //attributs: alpha, beta, constraint
        mother=motherframe;
        alpha=motherframe.getAlphaP();
        beta=motherframe.getBetaP();
        constraint=motherframe.getConstraint();

        //attributs: price
        number=size;
        price_field = new JTextField [size];
        price = new float[size];
        for (int i = 0; i < size; i++) {
            price_field[i] = new JTextField (5);
            price_field[i].setText("0");
        }
        this.Suggest();
        this.getPrice();

        //price Panel
        price_pnl.add(price_lbl);
        for (int i = 0; i < size; i++) {
            price_pnl.add(price_field[i]);
        }
        //Rest of Panel
        intro_pnl.add(intro);
        button_pnl.add(validate);
        button_pnl.add(cancel);
        warning_pnl.add(warning);

        //Define Layout
        this.setLayout(new GridLayout(4,1));
        this.add(intro_pnl);
        this.add(warning_pnl);
        this.add(price_pnl);
        this.add(button_pnl);

        //Frame spectifications
        validate.addActionListener(this);
        cancel.addActionListener(this);
        this.setTitle("DPD: Price");
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("dpd.png")));
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private int IndexMax() {
        int index =0;
        float value= (alpha[0]-constraint[0])/beta[0];
        for (int i = 1; i < alpha.length; i++) {
            if (beta[i]>0){
                if ((alpha[i]-constraint[i])/beta[i]>value) {
                    index=i;
                    value=(alpha[i]-constraint[i])/beta[i];
                }
            }
        }
        return index;
    }

    private int IndexMin() {
        int index =0;
        float value= (alpha[0]-constraint[0])/beta[0];
        for (int i = 1; i < alpha.length; i++) {
            if (beta[i]>0){
                if ((alpha[i]-constraint[i])/beta[i]<value) {
                    index=i;
                    value=(alpha[i]-constraint[i])/beta[i];
                }
            }
        }
        return index;
    }

    private float maxVect(float[]vector){
        float max = vector[0];
        for (int i = 0; i < vector.length; i++) {
            max=Math.max(max,vector[i]);
        }
        return max;
    }

    private void Suggest() {
        try{
            int indmax =IndexMax();
            int indmin =IndexMin();
            float mincost= maxVect(mother.getProdCost());
            System.out.println("Minimal price:"+mincost);
            float min=Math.max((alpha[indmin]-constraint[indmin])/beta[indmin],mincost);
            float max=Math.max((alpha[indmax]-constraint[indmax])/beta[indmax],mincost);
            float first=(alpha[0]-constraint[0])/beta[0];
            if (beta[0]==0) {
                throw new NullPointerException();
            }
            price_field[0].setText(Float.toString(max));//satisfied all period
            if(price_field.length==3){
                if (first==min || first==max) {
                    first=(min+max)/2;
                }
                price_field[1].setText(Float.toString(Math.max(first,mincost)));//smallest price
                price_field[2].setText(Float.toString(min));//bigest price
            }
        }
        catch (NullPointerException e) {
            System.out.println("No Data to suggest prices");
        }
    }

    private void getPrice() {
        for (int i = 0; i < price_field.length; i++) {
                price[i] = Float.parseFloat(price_field[i].getText());
        }
    }

    private boolean CheckPrice() {
        for (int i = 0; i < price.length; i++) {
            if (price[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public void actionPerformed(ActionEvent item) {
        if (item.getSource()==validate) {
            this.getPrice();
            if (this.CheckPrice()){
            mother.setPrice(price);
            this.dispose();
            }
            else {
                warning.setText("! Need non null numbers !");
                warning.setForeground(Color.red);
            }
        }
        else {
            if (number==1){
                mother.fixed_price.setSelected(false);
            }
            else {
                mother.multi_price.setSelected(false);
            }
            this.dispose();
        }
    }
}
