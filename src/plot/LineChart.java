
package plot;

import utilities.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class LineChart extends JComponent {

    Dimension d;
    Color fg = Color.black;
    public Color bg = Color.gray; // new Color( 200, 200, 255 );
    public Color plotBG = Color.white;
    public Color tickColor = new Color(200, 200, 200, 200);
    public Color envelopeColor = new Color(128, 128, 128, 200);

    ArrayList<LinePlot> lines;
    ArrayList<LineGroup> lineGroups;
    public String title;


    // set these up in ratios
    // right and top do not need as much space
    // as left and bottom due to the labelling
    // on the latter ones

    double leftMargin = 0.10;
    double rightMargin = 0.03;

    double bottomMargin = 0.1;
    double topMargin = bottomMargin / 1.5;

    public LineChartAxis xAxis, yAxis;

    // todo: add ticks for each axis


    // todo

    public boolean gridLines = true;

    public static void main(String[] args) {
//        LineChart lineChart = new LineChart();
//        double[] y = new double[101];
//        double[] yy = new double[101];
//        for (int i = 0; i < y.length; i++) {
//            y[i] = Math.abs(50 - i);
//            yy[i] = i;
//        }
//        lineChart.addLine(LinePlot.randomLine());
//        lineChart.addLine(LinePlot.randomLine());
//        lineChart.addLine(new LinePlot().setData(y).setColor(Color.blue));
//        lineChart.addLine(new LinePlot().setData(yy).setColor(Color.red));
//        new JEasyFrame(lineChart, "Line Chart GVGAISimpleTest");
//        lineChart.xAxis = new LineChartAxis(new double[]{0, 20, 40, 60, 80, 100});
//        lineChart.yAxis = new LineChartAxis(new double[]{-2, -1, 0, 1, 2, 3});


        LineChart lineChart = new LineChart().addLine(LinePlot.randomLine());
        // lineChart.yAxis = new LineChartAxis(new double[]{-2, 0, 2});
        lineChart.xAxis = new LineChartAxis(new double[]{0, 20, 40, 60, 80, 100});

        new JEasyFrame(lineChart, "Single Line GVGAISimpleTest");

    }


    public LineChart() {
        this(new Dimension(800, 600));
    }

    public LineChart(Dimension d) {

        this.d = d;
        lines = new ArrayList<>();
        lineGroups = new ArrayList<>();
        // System.out.println(d);

    }
    public LineChart setBG(Color bg) {
        this.bg = bg;
        return this;
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

    public static LineChart easyPlot(ArrayList<Double> data) {
        LinePlot linePlot = new LinePlot().setData(data).setColor(Color.blue);
        LineChart lineChart = new LineChart().addLine(linePlot);
        // lineChart.yAxis = new LineChartAxis(linePlot.sy.min(), linePlot.sy.max());

        lineChart.xAxis = new LineChartAxis(new double[]{0, data.size()/2, data.size()-1});
        return lineChart;
    }


    public LineChart addLine(LinePlot line) {
        lines.add(line);
        return this;
    }

    public LineChart setLines(ArrayList<LinePlot> lines) {
        this.lines = lines;
        return this;
    }

    public LineChart addLineGroup(LineGroup lineGroup) {
        lineGroups.add(lineGroup);
        return this;
    }

    public LineChart setTitle(String title) {
        this.title = title;
        return this;
    }

    String xLabel;
    public LineChart setXLabel(String xLabel) {
        this.xLabel = xLabel;
        return this;
    }

    String yLabel;
    public LineChart setYLabel(String yLabel) {
        this.yLabel = yLabel;
        return this;
    }


    RangeMapper xMap, yMap;
    double plotLeft, plotRight, plotTop, plotBottom;

    public void autoScale() {
        yAxis.range = new StatSummary();
        for (LinePlot lp : lines) {
            yAxis.range.add(lp.sy);
        }
        for (LineGroup lg : lineGroups) {
            for (StatSummary ss : lg.stats)
            yAxis.range.add(ss);
        }
    }

    public void paintComponent(Graphics graphics) {
        paintComponent((Graphics2D) graphics, getSize());
    }

    // the second version is to enable various sizes to be passed
    // when not used as part of a window component
    public void paintComponent(Graphics2D g, Dimension size) {
        // System.out.println(d);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.translate(0, size.getHeight());
        g.scale(1, -1);
        g.fillRect(0, 0, size.width, size.height);
        StatSummary sx = new StatSummary(), sy = new StatSummary();

        // first of all find the limits of the data

        if (xAxis != null) {
            sx = xAxis.range;


        } else {
            sx.add(0);
            for (LinePlot line : lines) {
                // for sx we are only interested in the number of points on each line
                sx.add(line.getData().size());
            }
            for (LineGroup lineGroup : lineGroups) {
                // for sx we are only interested in the number of points on each line
                sx.add(lineGroup.stats.size());
            }
        }

        if (yAxis != null) {
            sy = yAxis.range;
        } else {
            for (LinePlot line : lines) {
                sy.add(line.sy);
            }
            for (LineGroup lineGroup : lineGroups) {
                // for sx we are only interested in the number of points on each line
                for (StatSummary ss : lineGroup.stats) {
                    sy.add(ss);
                }
            }
        }

//        System.out.println("Sx: " + sx);
//        System.out.println("Sy: " + sy);

        // now plot each of the lines, as a new Path2D
        plotLeft = size.getWidth() * leftMargin;
        plotRight = size.getWidth() * (1 - rightMargin);
        plotTop = size.getHeight() * (1 - topMargin);
        plotBottom = size.getHeight() * bottomMargin;

//        RangeMapper xMap = new RangeMapper(0, sx.max(), 0, getWidth());
//        RangeMapper yMap = new RangeMapper(sy.min(), sy.max(), getHeight(), 0);

//        double rw = getWidth() * (1 - (leftMargin + rightMargin));
//        double rh = getHeight() * (1 - (topMargin + bottomMargin));

        // System.out.println("SX: " + sx);

        xMap = new RangeMapper(sx.min(), sx.max(), plotLeft, plotRight);
        yMap = new RangeMapper(sy.min(), sy.max(), plotBottom, plotTop);


        double rw = plotRight - plotLeft;
        double rh = plotTop - plotBottom;

        Rectangle2D.Double plotRegion = new Rectangle2D.Double(plotLeft, plotBottom, rw, rh);
        g.setColor(plotBG);
        g.fill(plotRegion);
        g.setStroke(new BasicStroke(2));
        g.setColor(fg);
        g.draw(plotRegion);
        // System.out.println(plotRegion);


        drawLines(g);
        drawLineGroups(g);
        drawScatterData(g);

        // they will only be drawn in ticks have been set
        if (gridLines)
            drawGridLines(g);


        drawTicks(g, size);

        // ues the same fonts for all the labelling / titling
        g.setFont(new Font("Monospaced", Font.BOLD, getFontSize(size)));
        drawTitle(g, size);
        drawXLabel(g, size);
        drawYLabel(g, size);
        drawLegend(g, size);
    }

    private void drawLegend(Graphics2D g, Dimension size) {

        if (lineGroups == null) return;

        // get the string stat size stats: we need this to get the
        // size of the bounding box to be correct

        StatSummary stringLengths = new StatSummary();
        for (LineGroup lineGroup : lineGroups) {
            if (lineGroup.name != null) {
                Rectangle2D stringRect = g.getFontMetrics().getStringBounds(lineGroup.name, g);
                stringLengths.add(stringRect.getWidth());
            }
        }

        double plotWidth = plotRight - plotLeft;

        double lineLength = plotWidth / 10;
        g.setFont(new Font("Monospaced", Font.BOLD, getFontSize(size)));

        // first draw in the lines

        double gap = getFontSize(size) * 1.5;
        double top = plotBottom + gap * (lineGroups.size() + 1);

        double boxWidth = stringLengths.max() + gap + lineLength;
        double boxHeight = gap * (lineGroups.size() + 1);
        Rectangle2D.Double rect = new Rectangle2D.Double(plotRight - gap/2 - boxWidth, plotBottom + gap/2, boxWidth, boxHeight);
        g.setColor(new Color(255, 255, 255, 230));
        g.fill(rect);
        g.setColor(Color.black);
        g.draw(rect);

        top -= gap/2;
        for (LineGroup lineGroup : lineGroups) {
            Path2D path = new Path2D.Double();
            double xStart = plotRight - gap - lineLength;
            path.moveTo( xStart, top);
            path.lineTo(plotRight - gap, top);

            top -= gap;
            g.setColor(lineGroup.color);
            // change this to better styles later
            g.setStroke(new BasicStroke(2));
            g.draw(path);

            if (lineGroup.name != null) {
                Rectangle2D stringRect = g.getFontMetrics().getStringBounds(lineGroup.name, g);

                g.setColor(fg);
                drawInvertedString(g, lineGroup.name, xStart - stringRect.getWidth() - gap/5,  top + gap/2 - stringRect.getCenterY());            }
        }
        // Rectangle2D rect = new Rectangle2D.Double()
    }

    // these label functions were done in a hurry and
    // could be greatly simplified with some generic
    // string placement and rotation methods

    private void drawXLabel(Graphics2D g, Dimension size) {
        if (xLabel == null) return;
        Rectangle2D rect = g.getFontMetrics().getStringBounds(xLabel, g);
        int sx = (int) ( xMap.map(leftMargin) + (plotRight - plotLeft - rect.getWidth()) / 2);
        int sy = (int) (plotBottom - labelGap * size.getHeight()  + rect.getCenterY() * 2 );
        g.setColor(labelColor);
        drawInvertedString(g, xLabel, sx, sy);
    }

    private void drawYLabel(Graphics2D g, Dimension size) {
        if (yLabel == null) return;
        int sx = (int) (plotLeft * 0.2); // (xMap.map(leftMargin*8));
        int sy = (int) (size.getHeight()/2 );
        // System.out.println("Y Label: " + sx + " : " + sy);
        g.setColor(labelColor);
        drawRotatedString(g, yLabel, sx, sy);
    }

    private void drawTitle(Graphics2D g, Dimension size) {
        if (title == null) return;
        Rectangle2D rect = g.getFontMetrics().getStringBounds(title, g);
        // use a drawString method for now
        int sx = (int) ( xMap.map(leftMargin) + (plotRight - plotLeft - rect.getWidth()) / 2);
//        int sx = (int) ((size.getWidth() - rect.getWidth()) / 2);
        int sy = (int) (plotTop + labelGap * size.getHeight()  + rect.getCenterY() * 2 );
        g.setColor(labelColor);
        drawInvertedString(g, title, sx, sy);
    }

    private void drawLines(Graphics2D g) {
        for (LinePlot line : lines) {

            // for each line create a Path2D, then draw it

            ArrayList<Double> data = line.getData();
            Path2D.Double path = new Path2D.Double();
            path.moveTo(xMap.map(0), yMap.map(data.get(0)));

            // now draw lines to the other points
            for (int i = 1; i < data.size(); i++) {
                path.lineTo(xMap.map(i), yMap.map(data.get(i)));
            }

            // now render the path on the component
            // by setting the color and then drawing it
            g.setColor(line.color);
            g.setStroke(new BasicStroke(3));
            g.draw(path);
        }
    }

    // select size of ennvelope

    public double stdErrs = 2.0;

    int rad = 5;
    int pointFontSize = 10;
    private void drawScatterData(Graphics2D g) {
        if (scatterPlot == null) return;

        // otherwise go ahead ...
        g.setFont(new Font("Monospaced", Font.BOLD, pointFontSize));

        // draw in two stages with solid font and transparent points
        for (DataPoint point : scatterPlot.points) {
            int x = (int) xMap.map(point.x);
            int y = (int) yMap.map(point.y);
            if (point.name != null) {
                g.setColor(Color.white);
                this.drawInvertedString(g, point.name, x + rad, y);
            }
        }
        for (DataPoint point : scatterPlot.points) {
            int x = (int) xMap.map(point.x);
            int y = (int) yMap.map(point.y);
            g.setColor(new Color(255, 255, 255, 200));
            if (point.color != null) {
                g.setColor(point.color);
            }
            g.fillOval(x-rad, y-rad, 2*rad, 2*rad);
        }
    }

    private void drawLineGroups(Graphics2D g) {
        for (LineGroup lineGroup : lineGroups) {

            // for each line create a Path2D, then draw it
            // extract the data
            ArrayList<StatSummary> data = lineGroup.getLineStats();

            Path2D.Double env = new Path2D.Double();

            env.moveTo(xMap.map(0), yMap.map(data.get(0).mean()));

            // create the top of the envelope
            for (int i = 0; i < data.size(); i++) {
                env.lineTo(xMap.map(i), yMap.map(data.get(i).mean() + data.get(i).stdErr() * stdErrs));
            }

            // now work back along rhe bottom

            for (int i = data.size() - 1; i >= 0; i--) {
                env.lineTo(xMap.map(i), yMap.map(data.get(i).mean() - data.get(i).stdErr() * stdErrs));
            }

            env.closePath();
            g.setColor(envelopeColor);
            g.fill(env);


            Path2D.Double path = new Path2D.Double();

            path.moveTo(xMap.map(0), yMap.map(data.get(0).mean()));

            // now draw lines to the other points
            for (int i = 1; i < data.size(); i++) {
                path.lineTo(xMap.map(i), yMap.map(data.get(i).mean()));
            }

            // now render the path on the component
            // by setting the color and then drawing it
            g.setColor(lineGroup.color);
            g.setStroke(new BasicStroke(3));
            g.draw(path);
        }
    }


    private void drawGridLines(Graphics2D g) {
        if (xAxis != null) {
            // draw the xLines in
            Path2D.Double path = new Path2D.Double();
            for (double x : xAxis.ticks) {
                // System.out.println(x);
                g.setColor(new Color(100, 100, 100, 100));
                path.moveTo(xMap.map(x), plotBottom);
                path.lineTo(xMap.map(x), plotTop);

            }
            g.draw(path);

        }
        if (yAxis != null) {
            // draw the yLines in
            Path2D.Double path = new Path2D.Double();
            for (double y : yAxis.ticks) {
                g.setColor(new Color(100, 100, 100, 100));
                path.moveTo(plotLeft, yMap.map(y));
                path.lineTo(plotRight, yMap.map(y));

            }
            g.draw(path);

        }
    }

    double tickHeight = 0.015;
    double labelGap = 0.05;
    Color labelColor = fg;

    private int getFontSize(Dimension size) {
        return (int) (1.8 * size.getHeight() * tickHeight);
    }

    private void drawTicks(Graphics2D g, Dimension size) {
        int fontSize = getFontSize(size);
        // System.out.println("Font size: " + fontSize);
        // fontSize = 20;
        if (xAxis != null) {
            // draw the x ticks in
            Path2D.Double path = new Path2D.Double();

            // if (xAxis.scaleTicks != null)

            for (int i=0; i<xAxis.ticks.length; i++) {
                g.setColor(tickColor);

                double x = xAxis.ticks[i];
                // draw the tick
                path.moveTo(xMap.map(x), plotBottom);
                path.lineTo(xMap.map(x), plotBottom + size.getHeight() * tickHeight);

                // label it

                String xLabel = xAxis.tickLabel(i); // String.format("%.0f", x);

//                if (xAxis.scaleTicks != null) {
//                    // overwrite the tick value with something different
//                    xLabel = String.format
//                }

                g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                Rectangle2D rect = g.getFontMetrics().getStringBounds(xLabel, g);
                // use a drawString method for now
                int sx = (int) (xMap.map(x) - rect.getWidth() / 2);

                // but we need to check that the right of the string does not go off the edge of the screen

                int margin = 10;
                int overshoot = (int) ((rect.getWidth() + sx) - size.getWidth());
                if (overshoot > - margin)
                    sx -= (overshoot + margin);

                int sy = (int) (plotBottom - rect.getCenterY() - size.getHeight() * labelGap);
                g.setColor(labelColor);
                drawInvertedString(g, xLabel, sx, sy);

//                g.scale(1, -1);
//                g.drawString(xLabel, sx, -sy);
//                g.scale(1, -1);

            }
            g.draw(path);
        }

        if (yAxis != null) {
            // draw the yLines in
            Path2D.Double path = new Path2D.Double();
            for (double y : yAxis.ticks) {
                g.setColor(tickColor);
                path.moveTo(plotLeft, yMap.map(y));
                // make the length of the tick always proportional to the height of
                // graph
                path.lineTo(plotLeft + tickHeight * size.getHeight(), yMap.map(y));

                // label it

                // need a better way to handle the ticks:
                // may be better to specify as a string ...
                // String yLabel = String.format("%.0f", y);
                String yLabel = String.format("%.1f", y);
                g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                Rectangle2D rect = g.getFontMetrics().getStringBounds(yLabel, g);
                // use a drawString method for now
                int sx = (int) (plotLeft - rect.getWidth() - size.getHeight() * labelGap / 4);
                int sy = (int) (yMap.map(y) + rect.getCenterY());
                g.setColor(labelColor);
                drawInvertedString(g, yLabel, sx, sy);


            }
            g.draw(path);

        }
    }

    private void drawInvertedString(Graphics2D g, String s, double x, double y) {
        g.scale(1, -1);
        g.drawString(s, (int) x, (int) -y);
        g.scale(1, -1);
    }

    private void drawRotatedString(Graphics2D g, String s, double x, double y) {
        Rectangle2D rect = g.getFontMetrics().getStringBounds(yLabel, g);
        double angle = Math.PI / 2;
        g.translate(x,y);
        g.scale(1, -1);
        g.rotate(-angle);
        g.drawString(s, (int) -rect.getWidth()/2, 0);
        g.rotate(angle);
        g.scale(1, -1);
        g.translate(-x,-y);
    }

    public Dimension getPreferredSize() {
        return d;
    }

    public LineChart addLines(List<LinePlot> linePlots) {

        for (LinePlot linePlot : linePlots) addLine(linePlot);
        return this;

    }

    ScatterPlot scatterPlot;
    public LineChart setScatterPlot(ScatterPlot scatterPlot) {
        this.scatterPlot = scatterPlot;
        return this;
    }

    public LineChart saveImage(String dir, String fileName) throws Exception {
        try {
            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            // if (getWidth() < )
            // BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            BufferedImage bi = new BufferedImage((int) d.getWidth(), (int) d.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bi.createGraphics();
            paintComponent(graphics, getPreferredSize());
            File fDir = new File(dir);
            fDir.mkdirs();
            fileName = dir + fileName;
            File file = new File(fileName);
            ImageIO.write(bi, "PNG", file);
            // file.c
            // Thread.sleep(1000);
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        return this;
    }


}

