package plot;

import math.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class VoronoiGrid extends JComponent {

    public static void main(String[] args) {
        // System.out.println(new VoronoiGrid().s );

    }

    int width = 800, height = 500;
    Dimension dimension = new Dimension(width, height);


    static Random random = new Random();

    ArrayList<Vector2d> points = new ArrayList<>();

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;



    }

    public VoronoiGrid setRandomPoints(int n) {
        for (int i=0; i<n; i++) {
            points.add(new Vector2d(width * random.nextDouble(), height * random.nextDouble()));
        }
        return this;
    }


}
