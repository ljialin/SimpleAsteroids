package utilities;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * Created by simonmarklucas on 28/01/2017.
 */
public class ShapeComponent extends JComponent {

    Dimension d;
    Color fg = new Color( 20, 20, 150 );
    Color bg = new Color( 200, 200, 255 );

    List<Shape> shapes;

    public static void main(String[] args) {
        double[] x = { 0, 0.2, 0.5, 0.3, 0.2, 0.5, 0.0, 0.8, 0.3 };
        List<Shape> shapes = new ArrayList<>();
        Shape testArc = new Arc2D.Double(-50, -50, 100, 100, 90, 360, Arc2D.OPEN);
        shapes.add(testArc);
        shapes.add(new Arc2D.Double(-100, -100, 200, 200, 60, 300, Arc2D.OPEN));

        ShapeComponent ap = new ShapeComponent(new Dimension(400, 400), shapes);
        new JEasyFrame( ap , "Shapes GVGAISimpleTest");
    }

    public ShapeComponent(Dimension d, List<Shape> shapes) {
        this.d = d;
        this.shapes = shapes;
    }

    public void update(List<Shape> shapes) {
        this.shapes = shapes;
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRect(0, 0, size.width, size.height);

        if (shapes == null) return;

        // ok, ready to go now
        // so what next?

        // step one, draw an arc
        // how thick?

        g2.translate(size.getWidth() / 2, size.getHeight() / 2);

        g2.setColor(fg);

        g2.setStroke(new BasicStroke(30, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

        for (Shape shape : shapes) {
            g2.draw(shape);
            //
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
