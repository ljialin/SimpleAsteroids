package plot;

import utilities.JEasyFrame;

import java.awt.*;
import java.io.File;
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
                scatterPlot.addPoint(new DataPoint(module, x, y).setColor(col));
            }
            catch (Exception e) {
                System.out.println("Skipping: " + line);
            }

        }
        LineChart chart = new LineChart(new Dimension(800, 800));
        chart.setScatterPlot(scatterPlot);
        chart.xAxis = new LineChartAxis(new double[]{0, 100, 200, 300, 400, 500});
        chart.yAxis = new LineChartAxis(new double[]{2, 3, 4, 5});
        chart.setXLabel("Enrolment").setYLabel("Module Rating");

        chart.saveImage("../data/", "scatter.png");
        new JEasyFrame(chart, "Module evaluation versus enrolment");
    }

}
