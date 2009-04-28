import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;


public class FullPanelGraphic extends JPanel implements ActionListener, MouseListener{

    //attributs
    int time;
    FrameData mother;
    boolean[] choose;
    PanelGraphic[] skeches;
    PanelLegend legend;
    float[][] price;
    Color[] color;
    JButton hide;
    JButton save;
    JPanel skechespnl;

    public FullPanelGraphic (FrameData _mother,float[][] _price,Color[] _color)
    {
        //initiate attributs
        mother=_mother;
        choose=mother.getChoose();
        price=_price;
        time = mother.getTime();
        skeches = new PanelGraphic[time];
        this.setBackground(Color.WHITE);
        for(int i=0;i<time;i++)
        {
             skeches[i] = new PanelGraphic(mother,i,getLine(price,i),_color);
             skeches[i].addMouseListener( this ) ;
        }
        legend = new PanelLegend(_color,mother);
        legend.setOpaque(false);
        //local attributs
        //Buttons
        save = new JButton("Save graphic");
        hide = new JButton("Hide prices");
        //Panel
        //Button Panel
        JPanel buttonpnl = new JPanel();
        buttonpnl.setOpaque(false);
        buttonpnl.setLayout(new GridLayout(2,1));
        buttonpnl.add(save);
        buttonpnl.add(hide);
        //Skeches Panel
        skechespnl = new JPanel();
        skechespnl.setBackground(Color.WHITE);
        if(time>4)
        {
            skechespnl.setLayout(new GridLayout(2,5));
        }
        else
        {
            skechespnl.setLayout(new GridLayout(1,5));
        }
        for (int i = 0; i < time; i++)
        {
            skechespnl.add(skeches[i]);
        }
        //Bottom Panel
        JPanel bottompnl =new JPanel();
        bottompnl.setOpaque(false);
        bottompnl.add(legend);
        bottompnl.add(buttonpnl);
        //Main Panel
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(skechespnl);
        this.add(bottompnl);

        //actionlistener
        hide.addActionListener(this);
        save.addActionListener(this);
    }

    private float[] getLine(float[][]vector, int i){
        float[]result= new float[vector.length];
        for (int j = 0; j < vector.length; j++)
        {
            result[j]=vector[j][i];
        }
        return result;
    }

    public static void toPng( JPanel jp ) {
        BufferedImage bufferedImage;
        bufferedImage = new BufferedImage( jp.getWidth(), jp.getHeight(), BufferedImage.TYPE_INT_RGB );

        Graphics g = bufferedImage.createGraphics();
        jp.paint(g);

        //Select file
        JFileChooser ImageFile= new JFileChooser();
        int returnVal = ImageFile.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = ImageFile.getSelectedFile();
            if (!(file.getName().endsWith(".png") || file.getName().endsWith(".PNG"))) {
                file= new File(file.getParentFile(), file.getName()+ ".png");
            }
            try
            {
            ImageIO.write( bufferedImage, "png", file );
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,
                "Failled!",
                "Dpd error",
                JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void ShowUp(int nb)
    {
        for (int i = 0; i < time; i++)
        {
            skeches[i]. ShowUp(nb);
        }
        System.out.println("Repaint graphics(selected:"+nb+")");
        this.repaint();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource()==hide)
        {
            boolean[] empty = {false,false,false,false};
            for(int i=0;i<time;i++)
            {
                skeches[i].hideshow();
            }
            if(hide.getText()=="Hide prices")
            {
                hide.setText("Show prices ");
            }
            else
            {
                hide.setText("Hide prices");
            }
            this.repaint();
        }
        if (e.getSource()==save)
        {
            this.toPng(skechespnl);
        }
    }

    public Color[] getColorArray()
    {
        return skeches[0].getColorArray();
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e)
    {
        for (int i = 0; i < time; i++)
        {
            if (e.getSource()==skeches[i])
            {
                System.out.println("Show Up !!");
                PanelGraphic zoom = new PanelGraphic(skeches[i]);
                JFrame zoomframe = new JFrame("DPD: Zoom on "+(i+1)+" time period");
                zoomframe.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("dpd.png")));
                zoomframe.setBackground(Color.WHITE);
                zoomframe.add(zoom);
                zoomframe.pack();
                zoomframe.setVisible(true);
            }
        }


    }

}
