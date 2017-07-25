package plot;

import utilities.JEasyFrame;
import utilities.Stats;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by simonmarklucas on 18/07/2017.
 */
public class ScatterDataTest {
    static String file = "../data/modules.txt";

    static int alpha = 150;
    static Color[] colors = new Color[]{
            new Color(255, 0, 0, alpha),
            new Color(0, 255, 0, alpha),
            new Color(0, 0, 255, alpha),
            new Color(128, 128, 128, alpha),
    };

    static int enrolmentLimit = 0;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new File(file));

        ScatterPlot scatterPlot = new ScatterPlot().setTitle("Modules");
        while (scanner.hasNext()) {

            String line = null;
            try {
                line = scanner.nextLine();
                // System.out.println("Line: " + line);

                Scanner data = new Scanner(line);
                data.next();
                String module = data.next();
                double x = data.nextDouble();
                double y = data.nextDouble();
                char ch = module.charAt(3);
                Color col = colors[ch - '4'];


                System.out.println(module + " : " + ch + " : " + col);
                // System.out.println(module + " " + x + " " + y);
                if (x < enrolmentLimit) {
                    System.out.println("Skipped due to small enrolment");
                } else {
                    scatterPlot.addPoint(new DataPoint(module, x, y).setColor(col));
                    System.out.println("Added");
                }
                System.out.println();
            }
            catch (Exception e) {
                System.out.println("Skipping: " + line);
            }

        }

        // now get the points and create arrays for the correlation stats

        ArrayList<DataPoint> points = scatterPlot.points;
        double[] x = new double[points.size()];
        double[] y = new double[points.size()];
        for (int i=0; i<x.length; i++) {
            x[i] = points.get(i).x;
            y[i] = points.get(i).y;
        }
        double correlation = Stats.correlation(x, y);
        double rSquared = correlation * correlation;


        LineChart chart = new LineChart(new Dimension(800, 800));
        chart.setScatterPlot(scatterPlot);
        chart.xAxis = new LineChartAxis(new double[]{0, 100, 200, 300, 400, 500});
        chart.yAxis = new LineChartAxis(new double[]{2, 3, 4, 5});
        chart.setXLabel("Enrolment").setYLabel("Module Rating");

        chart.setTitle(String.format("Correlation: %.2f, r squared: %.3f", correlation, rSquared));
        chart.saveImage("../data/", "EECS-ScatterPlot.png");
        new JEasyFrame(chart, "Module evaluation versus enrolment");
    }

}
