package distance.test;

import distance.convolution.ConvNTuple;
import distance.kl.JSD;
import distance.kl.KLDiv;
import distance.pattern.PatternCount;
import distance.util.MarioReader;
import distance.view.LevelView;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static distance.util.MarioReader.getAndShowLevel;
import static distance.util.MarioReader.tileColors;


/**
 * Aim is simple: read in and compare two training files.
 * <p>
 * Could even be rthe same file for comparison
 */

public class KLDivTest {

    // no concern about where they come from for now
    // the principle is the same
    static String inputFile1 = "data/mario/levels/mario-1-1.txt";
    static String inputFile2 = "data/mario/levels/mario-8-1.txt";

    static String inputFile3 = "data/mario/levels/mario-5-1.txt";

    static int filterWidth = 4;
    static int filterHeight = 4;
    static int stride = 1;


    public static void main(String[] args) throws Exception {
        // any more and we'd make a loop
        int[][] level1 = getAndShowLevel(true, inputFile1);
        int[][] level2 = getAndShowLevel(true, inputFile2);
        int[][] level3 = getAndShowLevel(true, inputFile3);

        ConvNTuple c1 = getConvNTuple(level1, filterWidth, filterHeight, stride);
        ConvNTuple c2 = getConvNTuple(level2, filterWidth, filterHeight, stride);

        ConvNTuple c3 = getConvNTuple(level3, filterWidth, filterHeight, stride);

        double klDiv12 = KLDiv.klDiv(c1.sampleDis, c2.sampleDis);
        double klDiv21 = KLDiv.klDiv(c2.sampleDis, c1.sampleDis);
        double klDiv = KLDiv.klDivSymmetric(c1.sampleDis, c2.sampleDis);

        System.out.println("klDiv 1-2: " + klDiv12);
        System.out.println("klDiv 2-1: " + klDiv21);
        System.out.println("klDiv Sym: " + klDiv);

        System.out.println("JSD 1-2: " + new JSD().div(c1.sampleDis, c2.sampleDis, 0.5));
        System.out.println("JSD 2-1: " + new JSD().div(c2.sampleDis, c1.sampleDis, 0.5));

        System.out.println("Adding a distribution to itself");
        c1.sampleDis.add(c1.sampleDis);
        System.out.println("Sanity check, should be same as before: KLDiv Sym 1-2: " + KLDiv.klDivSymmetric(c1.sampleDis, c2.sampleDis));

        System.out.println("Adding c3 to c1");
        c1.sampleDis.add(c3.sampleDis);
        System.out.println("Sanity check, should be different KLDiv Sym 1-2: " + KLDiv.klDivSymmetric(c1.sampleDis, c2.sampleDis));

        System.out.println();
        System.out.println("Sanity check, should be zero: KLDiv Sym 1-1: " + KLDiv.klDiv(c1.sampleDis, c1.sampleDis));

        // printDis(c1);

        // showTilePatterns(c1);

    }

    public static void printDis(ConvNTuple convNTuple) {
        System.out.println();
        List<PatternCount> patterns = convNTuple.sampleDis.getFrequencyList();
        for (PatternCount pc : patterns) {
            System.out.println(pc);
        }
        System.out.println();
    }

    public static void showTilePatterns(ConvNTuple convNTuple) {
        System.out.println();
        List<PatternCount> patterns = convNTuple.sampleDis.getFrequencyList();

        JComponent jc = new JPanel();
        jc.setLayout(new FlowLayout());

        for (PatternCount pc : patterns) {
            // System.out.println(pc);
            int[][] rect = LevelView.toRect(pc.pattern.v, filterWidth, filterHeight);
            rect = MarioReader.flip(rect);
            LevelView lv = new LevelView(rect).setColorMap(tileColors).setCellSize(10);
            jc.add(lv);
        }

        new JEasyFrame(jc, "Tile Patterns");
        System.out.println("n distinct patterms = " + patterns.size());

    }

    // convenience method
    public static ConvNTuple getConvNTuple(int[][] level, int filterWidth, int filterHeight, int stride) {
        int w = level.length;
        int h = level[0].length;

        System.out.println(w + " : " + h);
        int nDims = w * h;
        System.out.println("N Dims = " + nDims);
        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        // convNTuple.setMValues(mValues).setStride(stride);
        convNTuple.setStride(stride);
        convNTuple.makeIndices();
        convNTuple.reset();

        convNTuple.addPoint(level, 1);

        System.out.println(convNTuple.sampleDis.statMap.size());

        return convNTuple;
    }
}
