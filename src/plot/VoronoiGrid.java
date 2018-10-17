package plot;

import math.Vector2d;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.Picker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class VoronoiGrid extends JComponent {

    public static void main(String[] args) {
        // System.out.println(new VoronoiGrid().s );

        VoronoiGrid vg = new VoronoiGrid().setRandomPoints(20);
        new JEasyFrame(vg, "Voronoi GVGAISimpleTest");
    }

    int width = 800, height = 500;
    int cellSize = 1;
    Dimension dimension = new Dimension(width, height);

    int nPoints;

    static Random random = new Random();

    ArrayList<Vector2d> points = new ArrayList<>();
    ArrayList<Color> colors;

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;


        Dimension d = getSize();

        ElapsedTimer timer = new ElapsedTimer();
        int nCells = 0;
        for (int y=0; y<d.getHeight(); y+=cellSize) {
            for (int x=0; x<d.getWidth(); x+=cellSize) {
                nCells++;
                int closestIndex = getClosestPointIndex(new Vector2d(x, y));
                g.setColor(colors.get(closestIndex));
                g.fillRect(x, y, cellSize, cellSize);
            }
            // System.out.println(y);
        }
        System.out.println("Painted n cells: " + nCells);
        System.out.println(timer);

        // if (!dimension.equals(d) || )

    }

    public VoronoiGrid setRandomPoints(int n) {
        this.nPoints = n;
        colors = new ArrayList<>();
        for (int i=0; i<n; i++) {
            Vector2d p1 = randomPoint();
            points.add(p1);
            points.add(reflectionXY(p1));
            Color color = randColor();
            colors.add(color);
            colors.add(color);
        }
        return this;
    }

    public Vector2d randomPoint() {
        return new Vector2d(width * random.nextDouble(), height * random.nextDouble());
    }

    public Vector2d reflectionXY(Vector2d p) {

        return new Vector2d(width - p.x, height - p.y);
    }

    public int getClosestPointIndex(Vector2d probe) {
        Picker<Integer> picker = new Picker<>(Picker.MIN_FIRST);
        for (int i=0; i<nPoints; i++) {
            picker.add(probe.dist(points.get(i)), i);
        }
        return picker.getBest();
    }

    public static Color randColor() {
        // return Color.getHSBColor(random.nextFloat(), 0.5f + random.nextFloat()/2, 1);
        // return Color.getHSBColor(random.nextFloat(), random.nextFloat(), 1);
        return Color.getHSBColor(random.nextFloat(), 1, 1);
        // return Color.getHSBColor(random.nextFloat(), random.nextFloat(), 0.7f * random.nextFloat() + 0.3f);
    }

    public Dimension getPreferredSize() {
        return dimension;
    }




}
