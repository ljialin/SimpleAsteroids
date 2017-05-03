package trackgen;

import javax.swing.*;
import java.awt.*;

public class LissajousTest extends JComponent {
    int size = 400;

    public static void main(String[] args) {
        // I usually use a helper class called JEasyFrame
        // but have omitted it to keep it to one class
        JFrame frame = new JFrame("Lissajous Curve");
        frame.add(new LissajousTest());
        frame.pack();
        frame.setVisible(true);
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.green);
        g.fillRect(0, 0, size, size);
        g.translate(size/2,size/2);
        g.setStroke(new BasicStroke(16, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.red);
        drawTrack(g);
        g.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.black);
        drawTrack(g);
    }

    static double a=40, b=5, d = Math.PI / 2;
    static double scale = 0.95;
    static double inc = 0.05;
    static int nReps = 10;

    public void drawTrack(Graphics2D g) {
        int s = size/2;
        for (double t=0; t<Math.PI*2*nReps; t+= inc) {
            double ax = fx(t, a, d);
            double ay = fy(t, b);
            double bx = fx(t+inc, a, d);
            double by = fy(t+inc, b);
            g.drawLine((int) (s * ax), (int) (s * ay),
                    (int) (s * bx), (int) (s * by));
        }
    }

    double fx(double t, double a, double d) {
        return scale * Math.sin(a*t+d);
    }

    double fy(double t, double b) {
        return scale * Math.sin(b*t);
    }

    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }
}

