package trackgen;

import math.Vector2d;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;

public class TrackTest extends JComponent {
    static double inc = 0.05;
    int size = 400;
    static Paint paint;


    public static void main(String[] args) {
        new JEasyFrame(new TrackTest(), "Trig Track");
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

    public void drawTrack(Graphics2D g) {
        int s = size/2;
        for (double t=0; t<20; t+= inc) {
            Vector2d a = f(t);
            Vector2d b = f(t+inc);
            g.drawLine((int) (s * a.x), (int) (s * a.y),
                    (int) (s * b.x), (int) (s * b.y));
        }
    }

    Vector2d f(double t) {
        double x = Math.sin(t+Math.PI/6) +
                Math.sin(2.0 * t + Math.PI) + 0.0* Math.sin(4*t);
        double y = 1.0*Math.sin(1.0 * t) + Math.sin(Math.PI/5 + 2*t) +
                0.5 * Math.sin(3*t + Math.PI/8);
        x/= 2.5;
        y /= 3;
        return new Vector2d(x,y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }
}

