package ntuple;

import evodef.SolutionEvaluator;
import math.BanditEquations;
import utilities.StatSummary;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by sml on 22/05/2017.
 */

public class NTupleView2D extends JComponent {

    static int cellSize = 100;
    double fill = 0.9;

    NTupleBanditEA banditEA;

    SolutionEvaluator solutionEvaluator;

    public NTupleView2D(NTupleBanditEA banditEA, int m) {
        this.banditEA = banditEA;
        this.m = m;
    }

    NTuple xn, yn, xyn;
    int m;


//    public NTupleView2D(NTuple xn, NTuple yn, NTuple xyn, int m) {
//        this.xn = xn;
//        this.yn = yn;
//        this.xyn = xyn;
//        this.m = m;
//    }

    public Dimension getPreferredSize() {
        return new Dimension(cellSize * (m + 1), cellSize * (m + 1));
    }

    static Color bg = Color.black;

    public void paintComponent(Graphics gx) {
        setupNTuples();
        Graphics2D g = (Graphics2D) gx;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                int[] p = new int[]{i, j};
                StatSummary ss = xyn.getStats(p);
                String cellString = String.format("[%d,%d]", i, j);

                String trueVal = null;
                if (solutionEvaluator != null) {
                    trueVal = String.format("(%.3f)", solutionEvaluator.evaluate(p));
                }

                drawCell(g, ss, i, j, cellString, trueVal);

            }
        }

        for (int i=0; i<m; i++) {
            // show the 1-tuple ones
            // note that each of these only samples the
            // non-zero dimension: we could set the other value to anything
            int[] px = new int[]{i, 0};
            int[] py = new int[]{0, i};

            String cellStringX = String.format("[%d,*]", i);
            String cellStringY = String.format("[*,%d]", i);

            // calculate the trueval for each one
            StatSummary xTrue = new StatSummary();
            StatSummary yTrue = new StatSummary();
            for (int j=0; j<m; j++) {
                xTrue.add(solutionEvaluator.evaluate(new int[]{i, j}));
                yTrue.add(solutionEvaluator.evaluate(new int[]{j, i}));
            }
            String xs = null, ys = null;
            if (solutionEvaluator != null) {
                xs = String.format("(%.3f)", xTrue.mean());
                ys = String.format("(%.3f)", yTrue.mean());
            }

            drawCell(g, xn.getStats(px), i, m, cellStringX, xs);
            drawCell(g, yn.getStats(py), m, i, cellStringY, ys);

            // draw the borders:

            g.setColor(Color.red);
            int linwWidth = 6;
            g.fillRect(0, m * cellSize - linwWidth/2, getWidth(), linwWidth);
            g.fillRect(m * cellSize - linwWidth/2, 0, linwWidth, getHeight());

        }
    }

    private void setupNTuples() {
        System.out.println(banditEA.banditLandscapeModel);
        try {
            NTupleSystem nts = (NTupleSystem) banditEA.banditLandscapeModel;
            xn = nts.tuples.get(0);
            yn = nts.tuples.get(1);
            xyn = nts.tuples.get(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void drawCell(Graphics2D g, StatSummary ss, int i, int j, String cellString, String trueVal) {
        AffineTransform affineTransform = g.getTransform();
        g.translate(i * cellSize, j * cellSize);
        double offset = cellSize * (1 - fill) / 2;
        g.translate(offset, offset);
        g.scale(fill, fill);
        g.setColor(Color.YELLOW);
        Path2D.Double path = new Path2D.Double();
        path.append(new Rectangle2D.Double(0, 0, cellSize, cellSize), false);
        g.fill(path);
        if (ss != null) {
            float v = (float) BanditEquations.sigmoid(ss.mean()); //  Math.max(0, Math.min((float) ss.mean(), 1));
            g.setColor(Color.getHSBColor(v, 1, 0.5f));
            g.fill(path);
            String statString = String.format("%d: %.2f", ss.n(), ss.mean());
            centreString(g, cellSize / 2, cellSize / 2, statString, 15);
            String ucbString = ucbString(ss);
            centreString(g, cellSize/2, cellSize * 0.8f, ucbString, 14);
        } else {
            System.out.println("Null " + ss);
            g.setColor(Color.gray);
            g.fill(path);
        }
        if (trueVal != null) {
            centreString(g, cellSize/2, cellSize / 4, trueVal, 12);
        }
        centreString(g, cellSize / 2, cellSize / 10, cellString, 12);
        g.setTransform(affineTransform);
    }

    private void centreString(Graphics2D g, float x, float y, String str, int fontSize) {
        // Font font = Font.getFont(Font.MONOSPACED);

        Font font = new Font("Lucida Console", Font.PLAIN, fontSize);
        FontMetrics fm = g.getFontMetrics(font);

        int textWidth = fm.stringWidth(str);
        int textHeigth = fm.getHeight();
        int yOff = fm.getDescent();

        g.setFont(font);
        g.setColor(Color.white);
        // g.scale(3,3);
        g.drawString(str, x - textWidth / 2, y + yOff);
    }

    private String ucbString(StatSummary ss) {
        return String.format("ucb: %.2f", BanditEquations.UCB(ss.mean(), ss.n(), banditEA.evaluator.nEvals()));
    }

}
