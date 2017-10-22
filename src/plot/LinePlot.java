package plot;

import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 21/06/2017.
 */


public class LinePlot {

    // for each line we need to know the
    // setup

    public static void main(String[] args) {
        LinePlot lp = randomLine();
        System.out.println(lp);
    }

    static Random random = new Random();


    Color color;
    StatSummary sy;

    private ArrayList<Double> line;

    public LinePlot() {
        line = new ArrayList<>();
        sy = new StatSummary();
    }

    public LinePlot setColor(Color color) {
        this.color = color;
        return this;
    }

    public LinePlot setRandomColor() {
        this.color = randColor();
        return this;
    }

    public LinePlot add(double x) {
        line.add(x);
        sy.add(x);
        return this;
    }

    public ArrayList<Double> getData() {
        return line;
    }

    public LinePlot setData(double[] x) {
        line = new ArrayList<>();
        sy = new StatSummary();
        for (double p : x) {
            line.add(p);
            sy.add(p);
        }
        return this;
    }

    public LinePlot setData(ArrayList<Double> x) {
        line = new ArrayList<>();
        sy = new StatSummary();
        for (double p : x) {
            line.add(p);
            sy.add(p);
        }
        return this;
    }

    static LinePlot randomLine() {
        int n = 100;
        LinePlot lp = new LinePlot();
        for (int i=0; i<n; i++) {
            lp.add(random.nextGaussian() + i * 1.0 / n);
        }
        lp.color = randColor();
        return lp;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(color + "\t ");
        for (double x : line)
            sb.append(String.format("%.2f\t ", x));
        return sb.toString();
    }

    public static Color randColor() {
        return Color.getHSBColor(random.nextFloat(), 0.5f + random.nextFloat()/2, 1);
    }


}
