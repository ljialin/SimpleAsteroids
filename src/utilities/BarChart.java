
package utilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BarChart extends JComponent {

    double[] x;
    Dimension d;
    Color fg = new Color( 20, 20, 150 );
    Color bg = new Color( 200, 200, 255 );
    Color highlight = Color.red;
    // static int gap = 2;

    public static void main(String[] args) {
        double[] x = { 0, 0.2, 0.5, -0.3, 0.2, 0.5, 0.0, 0.8, 0.3 };
        BarChart bc = new BarChart();
        new JEasyFrame( bc , "Bar Chart GVGAISimpleTest");
        bc.update( x );
    }

    public BarChart() {
        this(new Dimension(400, 180));
    }

    public BarChart(Dimension d) {
        this.d = d;
    }

    public BarChart(Dimension d, double[] x) {
        this.d = d;
        this.x = x;
    }

    public static void display(ArrayList<Double> arrayList, String title) {
        BarChart bc = new BarChart();
        bc.x = new double[arrayList.size()];
        for (int i=0; i<bc.x.length; i++)
            bc.x[i] = arrayList.get(i);
        new JEasyFrame(bc, title);
    }

    public static void display(double[] x, String title) {
        BarChart bc = new BarChart();
        bc.update(x);
        new JEasyFrame(bc, title);
    }

    public void update(double[] x) {
        this.x = x;
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        g2.setColor( bg );
        g2.fillRect( 0, 0, size.width, size.height );

        if (x != null) {
            g2.translate(0 , size.height);
            g2.scale( 1 , -1 );
            int n = x.length;
            // assume all in range zero to one
            // double max = VecMat.max( x );
            double max = max( x );
            int barWidth = size.width / n;
            int gap = barWidth / 10;
            for (int i = 0; i < x.length; i++) {
                int h = (int) (size.height * (x[i] / max) );
                Color c = x[i] == max ? highlight : fg;
                g2.setColor( c );
                g2.fill3DRect( gap + i * barWidth, 0, barWidth-gap, h, false );
            }
        }
    }

    public Dimension getPreferredSize() {
        return d;
    }

    public static double max(double[] vec) {
        if (vec.length == 0) return 0;
        double m = vec[0];
        for (double x : vec) {
            m = Math.max(m, x);
        }
        return m;
    }
}

