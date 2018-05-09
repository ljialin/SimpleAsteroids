package logger.view;

import logger.core.Trajectory;
import math.Vector2d;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

public class TrajectoryView extends JComponent {

    public static void main(String[] args) {
        int n = 1000;

        int len = 1000;
        int w = 600, h = 350;
        double initScale = h * 2;
        double scale = 5;


        ArrayList<Trajectory> trajectories = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            Trajectory t = new Trajectory();
            Vector2d s = new Vector2d(initScale * random.nextGaussian(), initScale * random.nextGaussian());

            for (int j = 0; j < len; j++) {
                s.add(scale * random.nextGaussian(), scale * random.nextGaussian());
                t.addPoint(s.copy());
            }
            trajectories.add(t);
        }
        TrajectoryView tv = new TrajectoryView();
        tv.setTrajectories(trajectories).setDimension(new Dimension(2 * w, 2 * h));
        new JEasyFrame(tv, "Trajectory Views");

    }

    Dimension dimension = new Dimension(100, 100);
    ArrayList<Trajectory> trajectories = new ArrayList<>();
    boolean commonStartPoint = true;

    public TrajectoryView setDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    public TrajectoryView setTrajectories(ArrayList<Trajectory> trajectories) {
        this.trajectories = trajectories;
        return this;
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.translate(getWidth() / 2, getHeight() / 2);
        if (trajectories != null) {
            for (Trajectory t : trajectories) {
                drawTrajectory(g, t);
            }
        }

    }

    private void drawTrajectory(Graphics2D g, Trajectory trajectory) {
        Path2D.Double path = new Path2D.Double();
        Vector2d start = trajectory.points.get(0);
        path.moveTo(start.x, start.y);
        if (commonStartPoint) {
            g.translate(-start.x, -start.y);
        }
        for (Vector2d v : trajectory.points) {
            path.lineTo(v.x, v.y);
        }

        // now render the path on the component
        // by setting the color and then drawing it
        g.setColor(trajectory.fg);
        g.setStroke(new BasicStroke((float) trajectory.lineWidth));
        g.draw(path);
        if (commonStartPoint) {
            g.translate(start.x, start.y);
        }
    }

    public Dimension getPreferredSize() {
        return dimension;
    }
}
