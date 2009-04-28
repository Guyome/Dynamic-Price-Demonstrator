import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class PanelResult extends JPanel implements MouseListener
{

    FrameResult mother;
    SI solver;
    float[][] data;
    JLabel[] pb;
    boolean[] choose;

    public PanelResult(FrameResult _mother,Color[] color)
    {
        mother = _mother;
        solver = new SI(mother.getData());

        //need data
        choose = mother.getData().getChoose();
        String[] name = mother.getData().getPbName();
        int count=0;
        System.out.println("***Choose"+choose[0]+" "+choose[1]+" "+choose[2]+" "+choose[3]);

        if(choose[0])
        {
            solver.Free();
            count++;
        }
        if(choose[1])
        {
            solver.Multi();
            count++;
        }
        if(choose[2])
        {
            solver.Single();
            count++;
        }
        if(choose[3])
        {
            solver.Fixed();
            count++;
        }
        data=solver.getResults();

        //change layout
        this.setLayout(new FlowLayout());
        pb = new JLabel[4];
        for (int i = 0; i < choose.length; i++)
        {
            if(choose[i])
            {
                String rgb = Integer.toHexString(color[i].getRGB());
                rgb = rgb.substring(2, rgb.length());
                pb[i]= makeLabel(data[i],name[i],rgb);
                pb[i].addMouseListener( this ) ;
                this.add(pb[i]);
                this.add(Box.createHorizontalStrut(5));
                JSeparator sep=new JSeparator(JSeparator.VERTICAL);
                sep.setPreferredSize(sep.getPreferredSize());
                this.add(sep);
                this.add(Box.createHorizontalStrut(5));
            }
            else
            {
                pb[i]=new JLabel("");
            }
        }
    }

    public float[][] getPrice(){
        float[][] price = new float[4][mother.getData().getTime()];
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < mother.getData().getTime() ; j++)
            {
                price[i][j]=data[i][j+1+mother.getData().getTime()];
                System.out.println("***Price["+i+"]["+j+"]: "+String.valueOf(price[i][j]));
            }
        }
        return price;
    }

    private int sumBool(boolean[] vector)
    {
        int count = 0;
        for (int i = 0; i < vector.length; i++)
        {
            if(vector[i])
            {
                count++;
            }
        }
        return count;
    }

    private JLabel makeLabel(float[] data, String name,String color)
    {
        String[] dataname={"Demand: ","Price: ","Prod.: ","Stor.: "};
        String output= new String("<html><font color=\""+color+"\">"+name
                +":</font> ");
        if(data[0]>0)
        {
            output +=String.valueOf(data[0])+"<small>";
            for (int i = 0; i < 4; i++)
            {
                output += "<br>"+dataname[i];
                for (int j = 1; j < mother.getData().getTime()+1; j++)
                {
                    output+=String.valueOf((float)((int)(100*data[i*mother.getData().getTime()+j]))/100)+" / ";
                }
            }
            output +="</small>";
        }
        else
        {
            output += "<font color=\"RED\">UNSOLVED</font> ";
        }
        JLabel lab=new JLabel(output+"</html>");
        //lab.setFont(new Font("Serif", Font.PLAIN, 14));
        return lab;
    }

    private int IndexPB(int ind)
    {
        int index=0;
        for (int i = 0; i < 4; i++)
        {
            if(choose[i]) {ind--;}
            if((ind==0) && choose[i]) {index=i;}
        }
        return index;

    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
        System.out.println("mouse entered");
        int count=0;
        for (int i = 0; i < 4; i++)
        {
            if(e.getSource()==pb[i])
            {
                System.out.println("Jlabel source: "+i);
                count=i;
            }
        }
        System.out.println("PB "+count+" selected");
        mother.getGraphic().ShowUp(count);
    }

    public void mouseExited(MouseEvent e) {
        mother.getGraphic().ShowUp(-1);
    }

    public void mouseClicked(MouseEvent e) {}
}
