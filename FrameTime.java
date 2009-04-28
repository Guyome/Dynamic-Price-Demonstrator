import java.awt.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.border.*;
import javax.swing.BorderFactory;

public class FrameTime extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L;
    // attributs : time period
    private int time_period;
    private JComboBox timeList;
    // attributs : button
    private JButton validate;

    public FrameTime() {
        //locals variables
        JLabel intro = new JLabel ("<html>This software aims to be<br>"+
                                    "a pedagogic tool to show<br >"+
                                    "the interest of dynamic <br>"+
                                    "pricing.<br>"+
                                    "It runs with CoinOr solvers<br>"+
                                    "or heuristics</html>");
        JPanel intro_pnl =new JPanel();
        JPanel label_pnl =new JPanel();
        JPanel time_pnl = new JPanel();
        JPanel button_pnl = new JPanel(new FlowLayout());
        String[] time_poss = { "1", "2", "3", "4","5","6","7","8","9","10"};
        TitledBorder intro_ttl;
        intro_ttl = BorderFactory.createTitledBorder("Intro");
        // initialiasation of attributs
        // attributs : time period & heuristic boolean
        timeList = new JComboBox(time_poss);
        timeList.setSelectedIndex(3);
        time_period =timeList.getSelectedIndex()+1;
        // attributs : button
        validate = new JButton("Ok");

        //textfield for time period value
        time_pnl.add(new JLabel("Number of time period"));
        //time_pnl.add(time_field);
        time_pnl.add(timeList);
        //button panel
        button_pnl.add(validate);
        //intro panel
        intro_pnl.setLayout(new BoxLayout(intro_pnl,BoxLayout.Y_AXIS));
        intro_pnl.setBorder(intro_ttl);
        label_pnl.add(intro);
        intro_pnl.add(label_pnl);

        //Construct Frame
        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.add(intro_pnl);
        this.add(time_pnl);
        this.add(button_pnl);

        //Frame spectifications
        validate.addActionListener(this);
        this.setTitle("DPD: Intro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("dpd.png")));
    }


    public int getTime() {
        return time_period=this.timeList.getSelectedIndex()+1;
    }

    public void actionPerformed(ActionEvent item) {
        FrameData next = new FrameData(this.getTime());
        next.pack();
        next.setVisible(true);
        this.dispose();
    }
}
