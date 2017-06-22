
package utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class LineChart extends JComponent {

    Dimension d;
    Color fg = new Color( 20, 20, 150 );
    Color bg = Color.black; // new Color( 200, 200, 255 );
    ArrayList<LinePlot> lines;

    public static void main(String[] args) {
        LineChart lineChart = new LineChart();
        lineChart.addLine(LinePlot.randomLine());
        lineChart.addLine(LinePlot.randomLine());
        lineChart.addLine(LinePlot.randomLine());
        new JEasyFrame( lineChart , "Line Chart Test");
    }

    public LineChart() {
        this(new Dimension(600, 350));
    }

    public LineChart(Dimension d) {

        this.d = d;
        lines = new ArrayList<>();

    }

//    public static void display(ArrayList<Double> arrayList, String title) {
//        LineChart lineChart = new LineChart();
//        lineChart.addLine(new LinePlot(arrayList));
//        new JEasyFrame(lineChart, title);
//    }
//
//    public static void display(double[] x, String title) {
//        LineChart lineChart = new LineChart();
//        lineChart.addLine(new LinePlot(x));
//        new JEasyFrame(lineChart, title);
//    }
//
//    public void addLine(double[] x) {
//        addLine(new LinePlot(x));
//    }

    public void addLine(LinePlot line) {
        lines.add(line);
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        Dimension size = getSize();
        g.setColor( bg );
        g.fillRect( 0, 0, size.width, size.height );
        StatSummary sx = new StatSummary(), sy = new StatSummary();

        // first of all find the limits of the data
        for (LinePlot line : lines) {
            // for sx we are only interested in the number of points on each line
            sx.add(line.getData().size());
            sy.add(line.sy);
        }

        // now plot each of the lines, as a new Path2D
        RangeMapper xMap = new RangeMapper(0, sx.max(), 0, getWidth());
        RangeMapper yMap = new RangeMapper(sy.min(), sy.max(), getHeight(), 0);

        for (LinePlot line : lines) {

            // for each line create a Path2D, then draw it

            ArrayList<Double> data = line.getData();
            Path2D.Double path = new Path2D.Double();
            path.moveTo(xMap.map(0), yMap.map(data.get(0)));

            // now draw lines to the other points
            for (int i=1; i<data.size(); i++) {
                path.lineTo(xMap.map(i), yMap.map(data.get(i)));
            }

            // now render the path on the component
            // by setting the color and then drawing it
            g.setColor(line.color);
            g.draw(path);


        }

    }

    public Dimension getPreferredSize() {
        return d;
    }

}

