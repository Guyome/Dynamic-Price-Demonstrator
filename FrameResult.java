
import java.awt.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.BorderFactory;

class FrameResult extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    // attributs: button
    private JButton close;
    private JButton run;
    //attributs: Frame and panel
    private FrameData frame;
    private FullPanelGraphic graphic;
    private PanelResult result;
    //Color
    Color[] color;

    public FrameResult (FrameData _frame) {
        //initiate attributs
        frame=_frame;
        color= new Color[4];
        //local
        TitledBorder graphic_ttl;
        graphic_ttl = BorderFactory.createTitledBorder("");

        for (int i = 0; i < 4; i++)
        {
            color[i]=new Color(i*50,i*50,i*50);
        }
        result= new PanelResult(this,color);
        graphic= new FullPanelGraphic(frame,result.getPrice(),color);
        graphic.setBorder(graphic_ttl);
        close= new JButton("Close");
        run= new JButton("Restart");


        this.setTitle("DPD: Results.");
        //this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("dpd.png")));
        this.add(graphic);
        this.add(result);


        //definition of addActionListener
        close.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    public FullPanelGraphic getGraphic() {
        return graphic;
    }

    public FrameData getData() {
        return frame;
    }

    private String DisplayTab(float[] tabular) {
        String output = new String(" ");
        for (int i = 0; i < tabular.length; i++) {
            output+=String.valueOf((float)((int)(100*tabular[i]))/100)+" / ";
        }
        return output;
    }

    private float[] subVector(float[] data,int begin,int end){
            float[] vector=new float[end-begin];
            for (int i = 0; i < vector.length; i++) {
                vector[i]=data[begin+i];
            }
            return vector;
    }

};
