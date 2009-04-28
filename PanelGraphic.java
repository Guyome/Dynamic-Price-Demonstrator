import java.awt.geom.AffineTransform ;
import java.awt.*;
import javax.swing.*;
import java.util.*;
public class PanelGraphic extends JPanel
{

    protected float alpha;
    protected float beta;
    protected int count = 0;
    protected int constraint;
    protected int cost;
    protected int flag=-1;
    protected float[] price;
    protected boolean[] choose;
    protected boolean hide =false;
    protected boolean zoom;
    protected Color[] color;
    protected FrameData mother;

    public PanelGraphic(FrameData _mother,int t,float[] _price,Color[] _color)
    {
        mother=_mother;
        alpha=mother.getAlphaP()[t];
        beta=mother.getBetaP()[t];
        constraint =(int)mother.getConstraint()[t];
        cost =(int)mother.getProdCost()[t];
        price = _price;
        choose=mother.getChoose();
        color=_color;
        zoom=false;
        this.setPreferredSize(new Dimension(160,180));
    }

    public PanelGraphic(PanelGraphic graphic)
    {
        mother=graphic.mother;
        alpha=graphic.alpha;
        beta=graphic.beta;
        constraint =graphic.constraint;
        cost =graphic.cost;
        price=graphic.price;
        choose=graphic.choose;
        color=graphic.color;
        zoom=true;
        this.setPreferredSize(new Dimension(320,360));
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 =(Graphics2D) g;
        Font f = new Font ("Arial", Font.PLAIN, 10);
        g2.setFont (f);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        if(zoom) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            System.out.println("Zooming...");
            g2.scale(2,2);
        }

        //demand function
        g2.setColor(Color.ORANGE);
        g2.drawLine(5,150-(int)alpha,5+(int)Math.floor(alpha/beta),150);
        //demand minimal function
        g2.setColor(Color.YELLOW);
        g2.drawLine(5,150-(int)alpha,5+(int)Math.floor(alpha/(2*beta)),150);

        //All axes
        g2.setColor(Color.BLACK);
        //y axe
        g2.drawLine(5,5,5,150);
        g2.drawLine(5,5,2,8);
        g2.drawLine(5,5,8,8);
        //x axe
        g2.drawLine(5,150,150,150);
        g2.drawLine(150,150,147,153);
        g2.drawLine(150,150,147,147);
        //axes label
        g2.drawString("Demand",100,165);
        g2.drawString("0",5,165);
        g2.drawString("Price",10,15);


        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                new float[] { 3, 1 }, 0);
        g2.setStroke(stroke);
        //demand (production) constraint
        g2.setColor(Color.RED);
        g2.drawLine(5+constraint,20,5+constraint,150);
        if((int)constraint+8<110)
        {
            g2.drawString(Float.toString(constraint),(int)constraint+8,40);
        }
        else
        {
            if(constraint<160)
            {
                g2.drawString(Float.toString(constraint),(int)constraint-27,40);
            }
        }

        //minimal price
        if(cost!=0)
        {
            g2.setColor(Color.PINK);
            g2.drawLine(5,150-cost,135,150-cost);
            if (Math.abs(100-(int)constraint)>25)
            {
                g2.drawString(Float.toString(cost),100,148-(int)cost);
            }
            else
            {
                g2.drawString(Float.toString(cost),60,148-(int)cost);
            }
        }

        if(!hide)
        {
            for (int i = 0; i < price.length; i++)
             {
                if (choose[i])
                {
                    if (greenTest(i))
                    {
                        g2.setColor(Color.GREEN);
                        System.out.println("set to green");
                    }
                    else {
                        g2.setColor(color[i]);
                    }
                    g2.drawLine(5,150-(int)price[i],135,150-(int)price[i]);
                    g2.drawString(Double.toString(Math.floor(price[i]*10)/10),xpos(i),148-(int)price[i]);
                }
            }
            count=0;
        }
    }

    private int xpos(int t)
    {
        ArrayList<Float> al = new ArrayList<Float> ();
        for (int i =0;i<price.length;i++)
        {
            al.add (price[i]);
        }
        HashSet hs = new HashSet (al);
        Iterator it = hs.iterator();
        for (int i =0;it.hasNext();i++)
        {
            if((Float)it.next()==price[t])
                return 100-25*i;
        }
        return 100-25*t;
    }

    public void hideshow()
    {
        hide=!hide;
        this.repaint();
    }

    public void setZoom()
    {
        zoom=true;
    }

    public void ShowUp(int nb)
    {
        flag=nb;
        this.repaint();
    }

    public Color[] getColorArray()
    {
        return color;
    }

    private boolean greenTest(int i)
    {
        if ((flag>=0) && (flag<=3))
        {
            return ((flag==i)||(price[i]==price[flag]));
        }
        return false;
    }

    /*public static void main(String[] args){
        float[] price = {90,65,24,87};
        boolean[] choose ={true,false,false,true};
        JFrame frame=new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PanelGraphic pnl = new PanelGraphic(100,1,18,20,price,choose);
        frame.setBackground(Color.white);
        frame.setContentPane(pnl);
        frame.pack();
        frame.setVisible(true);
        System.out.println("color:"+Color.BLUE.toString());
    }*/
}



