import java.awt.*;
import javax.swing.*;

public class PanelLegend extends JPanel{

    //attributs
    FrameData frame;
    boolean[] choose;
    Color[] color;

    public PanelLegend(Color[] _color,FrameData _frame)
    {
        frame=_frame;
        choose=_frame.getChoose();
        color=_color;
        this.setPreferredSize(new Dimension(270,90));
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 =(Graphics2D) g;
        Font f = new Font ("Arial", Font.PLAIN, 10);
        g2.setFont (f);

        //demand function
        g2.setColor(Color.ORANGE);
        g2.drawLine(5,10,20,10);
        g2.drawString("Demand function",25,12);
        //demand minimal function
        g2.setColor(Color.YELLOW);
        g2.drawLine(5,30,20,30);
        g2.drawString("Minimal function",25,32);

        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                new float[] { 3, 1 }, 0);
        g2.setStroke(stroke);
        //demand (production) constraint
        g2.setColor(Color.RED);
        g2.drawLine(5,50,20,50);
        g2.drawString("Demand constraint",25,52);
        //minimal price
        g2.setColor(Color.PINK);
        g2.drawLine(5,70,20,70);
        g2.drawString("Production cost",25,72);

        String[] name=frame.getPbName();
        int count=0;
        for (int i = 0; i < choose.length; i++)
        {
            if (choose[i])
            {
                g2.setColor(color[i]);
                g2.drawLine(125,10+20*count,140,10+20*count);
                g2.drawString(name[i],145,12+20*count);
                count++;
            }
        }
    }

    /*public static void main(String[] args)
    {
        boolean[] choose ={true,true,true,true};
        JFrame frame=new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PanelLegend pnl = new PanelLegend(choose);
        frame.setContentPane(pnl);
        frame.pack();
        frame.setVisible(true);
    }*/
}
