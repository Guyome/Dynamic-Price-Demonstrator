import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.*;
import java.awt.*;

public class FrameData extends JFrame implements ActionListener,ItemListener,DocumentListener{

    private static final long serialVersionUID = 1L;

    // attributs : PanelData
    private JTextField [] alpha;
    private JTextField [] beta;
    private JTextField [] constraint;
    private JTextField [] production;
    private JTextField [] storage;
    //attributs : checkbox
    private JCheckBox alpha_cst;
    private JCheckBox beta_cst;
    private JCheckBox constraint_cst;
    private JCheckBox production_cst;
    private JCheckBox storage_cst;
    //attributs : Warning
    private JLabel warning;
    // attributs : time period, cost, price
    private int time_period;
    private boolean heuristic=false;
    private boolean[] choose;
    private float[] stor_cost;
    private float[] prod_cost;
    private float price_fixed;
    private float[] price_multi;
    // attributs : checkbox
    public JCheckBox free_price;
    public JCheckBox multi_price;
    public JCheckBox fixed_price;
    public JCheckBox single_price;
    // attributs : button
    private JButton clear;
    private JButton validate;
    private JButton save;
    private JButton input;
    // attributs: Jcombobox
    private JComboBox solverChoose;

    public FrameData(int time) {
        // Locals variables
        // Locals Panels
        JPanel button =new JPanel ();
        JPanel checkbox_pnl = new JPanel ();
        JPanel constant_pnl = new JPanel ();
        JPanel problem_pnl = new JPanel ();
        JPanel data = new JPanel();
        JPanel main = new JPanel();
        JPanel sub = new JPanel();
        JPanel datafield_alpha = new JPanel ();
        JPanel datafield_beta = new JPanel ();
        JPanel datafield_constraint = new JPanel ();
        JPanel datafield_prod = new JPanel ();
        JPanel datafield_stor = new JPanel ();
        JPanel datafield_alpha2 = new JPanel ();
        JPanel datafield_beta2 = new JPanel ();
        JPanel datafield_constraint2 = new JPanel ();
        JPanel datafield_prod2 = new JPanel ();
        JPanel datafield_stor2 = new JPanel ();
        JPanel datafield_alpha_m = new JPanel ();
        JPanel datafield_beta_m = new JPanel ();
        JPanel datafield_constraint_m = new JPanel ();
        JPanel datafield_prod_m = new JPanel ();
        JPanel datafield_stor_m = new JPanel ();
        JPanel warning_pnl =new JPanel (new FlowLayout());
        // Locals borders
        TitledBorder constant_ttl;
        TitledBorder price_ttl;
        price_ttl = BorderFactory.createTitledBorder("Kind of price");
        constant_ttl = BorderFactory.createTitledBorder("Copy variable");
        // Locals Labels
        JLabel alpha_lbl = new JLabel("Alpha:");
        JLabel beta_lbl = new JLabel("Beta:");
        JLabel constraint_lbl = new JLabel("Constraint:");
        JLabel constant_lbl = new JLabel("Constant variable:");
        JLabel prod_lbl = new JLabel("Prod. Cost:");
        JLabel stor_lbl = new JLabel("Stor. Cost:");
        // initialiasation of attributs
        // attributs : time period & cost
        time_period = time;
        heuristic = (time_period>5);
        choose = new boolean[4];

        // initialiasation of attributs
        // attributs : data
        alpha = new JTextField [time_period];
        beta = new JTextField [time_period];
        constraint = new JTextField [time_period];
        production = new JTextField [time_period];
        storage = new JTextField [time_period];
        //attributs : Warning
        warning = new JLabel("\n");
        //attributs : checkbox
        alpha_cst= new JCheckBox("Alpha");
        beta_cst= new JCheckBox("Beta");
        constraint_cst= new JCheckBox("Constraint");
        production_cst=new JCheckBox("Prod. Cost");
        storage_cst= new JCheckBox("Stor. Cost");
        free_price = new JCheckBox("Variable multiple prices");
        multi_price = new JCheckBox("Discrete prices");
        fixed_price = new JCheckBox("Constant price");
        single_price = new JCheckBox("Variable single price");
        if(heuristic) {
            single_price.setEnabled(false);
        }
        //Button
        clear = new JButton("Clear");
        validate = new JButton("Run");
        save = new JButton("Save");
        input = new JButton("Import");
        //JcomboBox
        String[] choose = {"COIN-OR","Heuristics"};
        solverChoose= new JComboBox(choose);
        if(time_period>4){
            solverChoose.removeItem("COIN-OR");
        }
        //Data field
        datafield_alpha.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_beta.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_constraint.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_prod.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_stor.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_alpha2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_beta2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_constraint2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_prod2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_stor2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datafield_alpha_m.setLayout(new BoxLayout(datafield_alpha_m,BoxLayout.Y_AXIS));
        datafield_beta_m.setLayout(new BoxLayout(datafield_beta_m,BoxLayout.Y_AXIS));
        datafield_constraint_m.setLayout(new BoxLayout(datafield_constraint_m,BoxLayout.Y_AXIS));
        datafield_prod_m.setLayout(new BoxLayout(datafield_prod_m,BoxLayout.Y_AXIS));
        datafield_stor_m.setLayout(new BoxLayout(datafield_stor_m,BoxLayout.Y_AXIS));
        datafield_alpha.add(alpha_lbl);
        datafield_beta.add(beta_lbl);
        datafield_constraint.add(constraint_lbl);
        datafield_prod.add(prod_lbl);
        datafield_stor.add(stor_lbl);
        for (int i = 0; i < time_period; i++) {
            alpha[i] = new JTextField (10);
            alpha[i].setText("");
            beta[i] = new JTextField (10);
            beta[i].setText("");
            constraint[i] = new JTextField (10);
            constraint[i].setText("");
            production[i] = new JTextField (10);
            production[i].setText("");
            storage[i] = new JTextField (10);
            storage[i].setText("");
            if (i<5){
                datafield_alpha.add(alpha[i]);
                datafield_beta.add(beta[i]);
                datafield_constraint.add(constraint[i]);
                datafield_prod.add(production[i]);
                datafield_stor.add(storage[i]);
            }
            else {
                datafield_alpha2.add(alpha[i]);
                datafield_beta2.add(beta[i]);
                datafield_constraint2.add(constraint[i]);
                datafield_prod2.add(production[i]);
                datafield_stor2.add(storage[i]);
            }
            //Define all actionlistener
            alpha[i].getDocument().addDocumentListener(this);
            beta[i].getDocument().addDocumentListener(this);
            constraint[i].getDocument().addDocumentListener(this);
            production[i].getDocument().addDocumentListener(this);
            storage[i].getDocument().addDocumentListener(this);
        }
        //warning panel
        warning_pnl.add(warning);

        //checkbox panel
        //constant panel

        constant_pnl.setLayout(new GridLayout(3,2));
        constant_pnl.setBorder(constant_ttl);
        constant_pnl.add(alpha_cst);
        constant_pnl.add(beta_cst);
        constant_pnl.add(constraint_cst);
        constant_pnl.add(production_cst);
        constant_pnl.add(storage_cst);
        //problem panel
        problem_pnl.setLayout(new BoxLayout(problem_pnl,BoxLayout.Y_AXIS));
        problem_pnl.setBorder(price_ttl);
        problem_pnl.add(free_price);
        problem_pnl.add(multi_price);
        problem_pnl.add(fixed_price);
        problem_pnl.add(single_price);

        //global checkbox panel
        checkbox_pnl.setLayout(new GridLayout(2,1));
        checkbox_pnl.add(constant_pnl);
        checkbox_pnl.add(problem_pnl);

        // Button Panel
        button.setLayout(new FlowLayout(FlowLayout.RIGHT));
        button.add(clear);
        button.add(save);
        button.add(input);
        button.add(new JSeparator());
        button.add(validate);
        button.add(new JLabel("Solvers:"));
        button.add(solverChoose);

         // Data panel
        data.setLayout(new GridLayout(5,1));
        if (time_period<5){
            data.add(datafield_alpha);
            data.add(datafield_beta);
            data.add(datafield_constraint);
            data.add(datafield_prod);
            data.add(datafield_stor);
        }
        else {
            datafield_alpha_m.add(datafield_alpha);
            datafield_alpha_m.add(datafield_alpha2);
            datafield_beta_m.add(datafield_beta);
            datafield_beta_m.add(datafield_beta2);
            datafield_constraint_m.add(datafield_constraint);
            datafield_constraint_m.add(datafield_constraint2);
            datafield_prod_m.add(datafield_prod);
            datafield_prod_m.add(datafield_prod2);
            datafield_stor_m.add(datafield_stor);
            datafield_stor_m.add(datafield_stor2);
            data.add(datafield_alpha_m);
            data.add(datafield_beta_m);
            data.add(datafield_constraint_m);
            data.add(datafield_prod_m);
            data.add(datafield_stor_m);
        }
        //Main Panel
        sub.setLayout(new BoxLayout(sub,BoxLayout.Y_AXIS));
        sub.add(data);
        sub.add(warning_pnl);
        sub.add(button);
        main.setLayout(new FlowLayout());
        main.add(sub);
        main.add(checkbox_pnl);

        // creation of this frame
        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("dpd.png")));
        this.add(main);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("DPD: Problem definition.");
        this.setVisible(true);

        // definition of addActionListener
        validate.addActionListener(this);
        clear.addActionListener(this);
        save.addActionListener(this);
        input.addActionListener(this);
        //definition of addItemListener
        solverChoose.addItemListener(this);
        alpha_cst.addItemListener(this);
        beta_cst.addItemListener(this);
        constraint_cst.addItemListener(this);
        production_cst.addItemListener(this);
        storage_cst.addItemListener(this);
        multi_price.addItemListener(this);
        fixed_price.addItemListener(this);
        single_price.addItemListener(this);
    }

    private boolean check() {
        if(!heuristic){
            System.out.println("COIN-OR Solver !!");
            if (free_price.isSelected() | multi_price.isSelected() | fixed_price.isSelected() |single_price.isSelected() ){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (free_price.isSelected() | multi_price.isSelected() | fixed_price.isSelected()){
                return true;
            }
            else {
                return false;
            }
        }
    }

    public boolean datacheck() {
        boolean answer = true;
        for (int i = 0; i < alpha.length; i++) {
            if ( alpha[i].getText().contentEquals("") | beta[i].getText().contentEquals("") |
                constraint[i].getText().contentEquals("") ){
                answer = false;
            }
        }
        return answer;
    }

    public void setWarning(boolean on) {
        if (on) {
            warning.setText("! Need more value or type of problem non choosed !");
            warning.setForeground(Color.red);
        }
        else {
            warning.setText("\n");
        }
    }

    public float[] getAlpha() {
        float[] value = new float[alpha.length];
        for (int i = 0; i < value.length; i++) {
            if(!alpha[i].getText().contentEquals("")) {
                value[i]=Float.parseFloat(alpha[i].getText())/Float.parseFloat(beta[i].getText());
            }
            else {
                value[i]=0;
            }
        }
        return value;
    }

    public float[] getBeta() {
        float[] value = new float[beta.length];
        for (int i = 0; i < value.length; i++) {
            if(!beta[i].getText().contentEquals("")) {
                value[i]=1/Float.parseFloat(beta[i].getText());
            }
            else {
                value[i]=0;
            }
        }
        return value;
    }

//this is an very stupid think where alpha
//for plotting is equal to alpha/beta and beta for plotting 1/beta
    public float[] getAlphaP() {
        float[] value = new float[alpha.length];
        for (int i = 0; i < value.length; i++) {
            if(!alpha[i].getText().contentEquals("")) {
                value[i]=Float.parseFloat(alpha[i].getText());
            }
            else {
                value[i]=0;
            }
        }
        return value;
    }

    public float[] getBetaP() {
        float[] value = new float[beta.length];
        for (int i = 0; i < value.length; i++) {
            if(!beta[i].getText().contentEquals("")) {
                value[i]=Float.parseFloat(beta[i].getText());
            }
            else {
                value[i]=0;
            }
        }
        return value;
    }

    public float[] getConstraint() {
        float[] value = new float[constraint.length];
        for (int i = 0; i < value.length; i++) {
            if(!constraint[i].getText().contentEquals("")) {
                value[i]=Float.parseFloat(constraint[i].getText());
            }
            else {
                value[i]=0;
            }
        }
        return value;
    }

    public float[] getDemand() {
        float[] value = new float[alpha.length];
        for (int i = 0; i < value.length; i++) {
            value[i]=40;//!! Ajouter une prevision de la demande
        }

        return value;
    }

    public float[] getProdCost() {
        float[] value = new float[alpha.length];
        for (int i = 0; i < value.length; i++) {
            value[i]=Float.parseFloat(production[i].getText());
        }
        return value;
    }

    public float[] getStorCost() {
        float[] value = new float[alpha.length];
        for (int i = 0; i < value.length; i++) {
            value[i]=Float.parseFloat(storage[i].getText());
        }
        return value;
    }

    public void setData(float[] _alpha,float[] _beta,
        float[]_demand,float[] _constraint,float[] _prod_cost,float[] _stor_cost) {
        for (int i = 0; i < alpha.length; i++) {
            alpha[i].setText(String.valueOf(_alpha[i]));
            beta[i].setText(String.valueOf(_beta[i]));
            constraint[i].setText(String.valueOf(_constraint[i]));
            production[i].setText(String.valueOf(_prod_cost[i]));
            storage[i].setText(String.valueOf(_stor_cost[i]));
        }
    }

    public int getTime() {
        return time_period;
    }

    public float[] getMultiPrice() {
        return price_multi;
    }

    public float getFixedPrice() {
        return price_fixed;
    }

    public boolean getHeuristic() {
        return heuristic;
    }

    public void setPrice(float[] value) {
        if (value.length == 1) {
            price_fixed = value[0];
        }
        else {
            price_multi = new float[value.length];
            for (int i = 0; i < value.length; i++) {
                price_multi[i] = value[i];
            }
        }

    }

    private String getValue(JTextField [] object) {
        String value="";
        int i=0;
        while(i<object.length && value.contentEquals("")) {
            if (!object[i].getText().contentEquals("")) {
                value=object[i].getText();
            }
            i++;
        }
        return value;
    }

    private void setline(JTextField [] object) {
        for (int i = 0; i < object.length; i++) {
            object[i].setText(getValue(object) );
        }
    }

    private void setline(JTextField [] object,String value) {
        for (int i = 0; i < object.length; i++) {
            object[i].setText(value);
        }
    }

    public void reset() {
        for (int i = 0; i < alpha.length; i++) {
            alpha[i].setText("");
            beta[i].setText("");
            constraint[i].setText("");
            production[i].setText("");
            storage[i].setText("");
        }
        alpha_cst.setSelected(false);
        beta_cst.setSelected(false);
        constraint_cst.setSelected(false);
        production_cst.setSelected(false);
        storage_cst.setSelected(false);
    }

    public boolean[] getChoose(){
        boolean[] aux = {free_price.isSelected(),multi_price.isSelected(),single_price.isSelected(),fixed_price.isSelected()};
        return aux;
    }

    public String[] getPbName(){
        String[] name ={"Variable multiple prices","Discrete prices","Variable single price","Constant price"};
        return name;
    }

    public void setDocumentListener(boolean add,JTextField[] vector){
        if (add) {
            for (int i = 0; i < vector.length; i++){
                vector[i].getDocument().addDocumentListener(this);
            }
        }
        else {
            for (int i = 0; i < vector.length; i++) {
                vector[i].getDocument().removeDocumentListener(this);
            }
        }
    }


    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==input){
            CsvTools csv = new CsvTools(this);
            csv.importfile(false);
        }
        if (datacheck()){
            setWarning(false);
            if(e.getSource() == validate) {
                if (this.check()) {
                    FrameResult result = new FrameResult(this);
                    result.pack();
                    result.setVisible(true);
                }
                else {setWarning(true);};
            }
            if (e.getSource()==save){
                CsvTools csv = new CsvTools(this);
                csv.importfile(true);
            }
        }
        else {
            this.setWarning(true);
        }
        if(e.getSource() == clear){
            this.setWarning(false);
            this.reset();
            free_price.setSelected(false);
            multi_price.setSelected(false);
            fixed_price.setSelected(false);
            single_price.setSelected(false);
        }
    }

    public void itemStateChanged(ItemEvent item) {
        float[] aux;
        aux = new float[time_period];
        int status = item.getStateChange();
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == fixed_price)) {
            FramePrice price_fm = new FramePrice(this,1);
            price_fm.pack();
            price_fm.setVisible(true);
        }
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == multi_price)) {
            FramePrice price_fm = new FramePrice(this,3);
            price_fm.pack();
            price_fm.setVisible(true);
        }
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == alpha_cst)) {
            this.setline(alpha);
        }
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == beta_cst)) {
            this.setline(beta);
        }
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == constraint_cst)) {
            this.setline(constraint);
        }
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == production_cst)) {
            this.setline(production);
        }
        if ( (status == ItemEvent.SELECTED) && (item.getSource() == storage_cst)) {
            this.setline(storage);
        }
        if(solverChoose.getSelectedIndex()>0){
            heuristic=true;
            single_price.setSelected(false);
            single_price.setEnabled(false);
        }
        else {
            heuristic=false;
            single_price.setEnabled(true);
        }
    }

    public void updateJTextField(DocumentEvent e) {
        for(int i = 0; i < alpha.length; i++) {
            if(e.getDocument()==alpha[i].getDocument() && alpha_cst.isSelected()) {
                final int selected = i;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setDocumentListener(false,alpha);
                        setline(alpha,alpha[selected].getText());
                        setDocumentListener(true,alpha);
                    }
                });
            }
            if(e.getDocument() ==beta[i].getDocument() && beta_cst.isSelected()) {
                final int selected = i;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setDocumentListener(false,beta);
                        setline(beta,beta[selected].getText());
                        setDocumentListener(true,beta);
                    }
                });
            }
            if(e.getDocument() ==constraint[i].getDocument() && constraint_cst.isSelected()) {
                final int selected = i;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setDocumentListener(false,constraint);
                        setline(constraint,constraint[selected].getText());
                        setDocumentListener(true,constraint);
                    }
                });
            }
            if(e.getDocument() ==production[i].getDocument() && production_cst.isSelected()) {
                final int selected = i;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setDocumentListener(false,production);
                        setline(production,production[selected].getText());
                        setDocumentListener(true,production);
                    }
                });
            }
            if(e.getDocument() ==storage[i].getDocument() && storage_cst.isSelected()) {
                final int selected = i;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setDocumentListener(false,storage);
                        setline(storage,storage[selected].getText());
                        setDocumentListener(true,storage);
                    }
                });
            }
        }
        if (fixed_price.isSelected()|| multi_price.isSelected())  {
            for (int i = 0; i < time_period; i++){
                if ((e.getDocument()  == alpha[i].getDocument()) || (e.getDocument()  == beta[i].getDocument()) ||
                        (e.getDocument()  == constraint[i].getDocument())) {
                    fixed_price.setSelected(false);
                    multi_price.setSelected(false);
                }
            }
        }
    }

    public void insertUpdate(DocumentEvent e) {
        updateJTextField(e);
    }

    public void removeUpdate(DocumentEvent e) {
        updateJTextField(e);
    }

    public void changedUpdate(DocumentEvent e) {
      //Plain text components don't fire these events.
    }

}
