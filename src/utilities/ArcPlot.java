package utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by simonmarklucas on 28/01/2017.
 */
public class ArcPlot extends JComponent {

    double[] x;
    Dimension d;
    Color fg = new Color( 20, 20, 150 );
    Color bg = new Color( 200, 200, 255 );
    Color highlight = Color.red;

    public static void main(String[] args) {
        double[] x = { 0, 0.2, 0.5, 0.3, 0.2, 0.5, 0.0, 0.8, 0.3 };
        ArcPlot ap = new ArcPlot(new Dimension(400, 400), x);
        new JEasyFrame( ap , "Arc Plot GVGAISimpleTest");
        ap.update( x );
    }

    public ArcPlot(Dimension d, double[] x) {
        this.d = d;
        this.x = x;
    }

    public void update(double[] x) {
        this.x = x;
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRect(0, 0, size.width, size.height);

        if (x == null) return;

        // ok, ready to go now
        // so what next?

        // step one, draw an arc
        // how thick?

        g2.translate(size.getWidth() / 2, size.getHeight() / 2);

        g2.setColor(fg);
        g2.setStroke(new BasicStroke(30));
        Path2D path = new Path2D.Double();
        Shape testArc = new Arc2D.Double(0, 0, 100, 100, 90, 180, Arc2D.OPEN);
        path.append(testArc, false);

        g2.draw(testArc);
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
