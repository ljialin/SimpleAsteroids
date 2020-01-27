package distance.test;

import distance.convolution.ConvNTuple;
import distance.kl.KLDiv;
import distance.pattern.PatternDistribution;
import distance.view.LevelView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static distance.util.MarioReader.flip;
import static distance.util.MarioReader.readLevel;
import static distance.util.MarioReader.tileColors;

public class KLDivWaveFunctionTest {

    static int filterWidth = 4, filterHeight = 4, stride = 1;

    static double weight = 0.5;

    public static void main(String[] args) throws Exception {

        // String inputDirectory = "data/mario/wfc2levels/";
        String inputDirectory = "data/mario/kldiv-2x2/";
        // inputDirectory = "data/zelda/singleroom/";
        String marioSample = "data/mario/levels/mario-1-1.txt";
        ConvNTuple sampleCNT = getSampleDistribution(marioSample);

        // todo: read in a Mario level for the training distribution

        int fileLimit = 20;

        ArrayList<int[][]> levels = getLevels(inputDirectory, fileLimit);
        int[][] sampleLevel = levels.get(0);
        // assume same dimensions for all levels
        int w = sampleLevel.length;
        int h = sampleLevel[0].length;

        ConvNTuple convNTuple = getConvNTuple(w, h, filterWidth, filterHeight, stride);

        // now add all the levels to the distribution
        for (int[][] level : levels) {
            convNTuple.addPoint(level, 1);
        }

        // now measure how typical each ones is and rate it
        ArrayList<RatedLevel> intraRatings = rateLevels(convNTuple.sampleDis, levels);
        ArrayList<RatedLevel> sampleRatings = rateLevels(sampleCNT.sampleDis, levels);
        showRatedLevls(intraRatings, "Intra class difference, w = " + weight);
        showRatedLevls(sampleRatings, "Training sample differences, w = " + weight);

        // checkPairs(ratedLevels);
    }

    static void showRatedLevls(ArrayList<RatedLevel> ratedLevels, String frameTitle) {
        int index = 0;
        PairListComponent plc = new PairListComponent();
        for (RatedLevel ratedLevel : ratedLevels) {
            String title = String.format("%d\t %.6f", index++, ratedLevel.score);
            RatedLevelView rlv = new RatedLevelView(ratedLevel.level, title);
            plc.add(rlv);
        }
        new JEasyFrame(plc, frameTitle);
    }

    static ArrayList<RatedLevel> rateLevels(PatternDistribution sampleDis, ArrayList<int[][]> levels) {
        ArrayList<RatedLevel> ratedLevels = new ArrayList<>();
        for (int[][] level : levels) {
            int w = level.length, h = level[0].length;
            ConvNTuple cnt = getConvNTuple(w, h, filterWidth, filterHeight, stride).addPoint(level, 1);

            // change version of KLDiv here
            // double score = KLDiv.klDivSymmetric(convNTuple.sampleDis, cnt.sampleDis);

            // double score = KLDiv.klDiv(convNTuple.sampleDis, cnt.sampleDis);

            // todo: compare also with a sample level
            // this may give best results in terms of ensuring coverage
            double score = KLDiv.klDivWeighted(sampleDis, cnt.sampleDis, weight);

            RatedLevel ratedLevel = new RatedLevel(level, score, cnt);
            ratedLevels.add(ratedLevel);
        }

        Collections.sort(ratedLevels);
        return ratedLevels;
    }

    static void checkPairs(ArrayList<RatedLevel> ratedLevels) {
        System.out.println();
        String filterSize = String.format("Filter size: %d x %d", filterWidth, filterHeight);
        System.out.println(filterSize);
        PairListComponent plc = new PairListComponent();
        for (RatedLevel i : ratedLevels) {
            for (RatedLevel j : ratedLevels) {
                double kl = KLDiv.klDiv(i.cnt.sampleDis, j.cnt.sampleDis);
                double sym = KLDiv.klDivSymmetric(i.cnt.sampleDis, j.cnt.sampleDis);
                String title = String.format("%.5f\t %.5f", kl, sym);
                System.out.println(title);
                plc.add(new PairView(i.level, j.level, title));
            }
        }
        new JEasyFrame(plc, String.format("All Pairs: %s", filterSize));
        System.out.println();
    }

    static class PairListComponent extends JComponent {
        public PairListComponent() {
            setBackground(Color.getHSBColor(0.7f, 1.0f, 1.0f));
            setLayout(new FlowLayout());
        }



//        public void add(PairView pairView) {
//            add(pairView);
//        }



    }

    static class PairView extends JComponent {
        int[][] l1; int[][] l2; String title;

        public PairView(int[][] l1, int[][] l2, String title) {
            this.l1 = l1;
            this.l2 = l2;
            this.title = title;
            setLayout(new BorderLayout());
            LevelView lv1 = new LevelView(flip(l1)).setColorMap(tileColors).setCellSize(8);
            LevelView lv2 = new LevelView(flip(l2)).setColorMap(tileColors).setCellSize(8);
            add(lv1, BorderLayout.EAST);
            add(lv2, BorderLayout.WEST);
            new JEasyFrame(this, title);
            add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
            // Border border = new Border(Border.EMPTY
            // setBorder();
        }
    }

    static class RatedLevelView extends JComponent {
        int[][] l1; String title;

        public RatedLevelView(int[][] l1, String title) {
            this.l1 = l1;
            this.title = title;
            setLayout(new BorderLayout());
            LevelView lv1 = new LevelView(flip(l1)).setColorMap(tileColors).setCellSize(8);
            add(lv1, BorderLayout.CENTER);
            // new JEasyFrame(this, title);
            add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
        }
    }

    static class RatedLevel implements Comparable<RatedLevel> {

        int[][] level;
        Double score;
        ConvNTuple cnt;

        public RatedLevel(int[][] level, Double score) {
            this.level = level;
            this.score = score;
        }

        public RatedLevel(int[][] level, Double score, ConvNTuple cnt) {
            this.level = level;
            this.score = score;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(RatedLevel o) {
            return score.compareTo(o.score);
        }
    }

    public static ConvNTuple getSampleDistribution(String filePath) throws Exception {
        int[][] level = readLevel(new Scanner(new FileInputStream(filePath)));
        System.out.println(level.length + " : " + level[0].length);
        ConvNTuple cnt = getConvNTuple(level.length, level[0].length, filterWidth, filterHeight, stride);
        cnt.addPoint(level, 1);
        return cnt;

    }

    public static ArrayList<int[][]>  getLevels(String inputDirectory, int fileLimit) throws Exception {

        // need to iterate over all the files in a directory

        File file = new File(inputDirectory);
        String[] fileList = file.list();

        int n = 0;
        ArrayList<int[][]> levels = new ArrayList<>();
        for (String inputFile : fileList) {
            try {
                System.out.println("Reading: " + inputFile);
                int[][] level = readLevel(new Scanner(new FileInputStream(inputDirectory + inputFile)));
                levels.add(level);
                LevelView levelView = new LevelView(flip(level)).setColorMap(tileColors).setCellSize(10);
                // new JEasyFrame(levelView, inputFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (++n >= fileLimit) break;
        }
        return  levels;

    }

    // convenience method
    public static ConvNTuple getConvNTuple(int w, int h, int filterWidth, int filterHeight, int stride) {
        System.out.println(w + " : " + h);
        int nDims = w * h;
        System.out.println("N Dims = " + nDims);
        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        convNTuple.setStride(stride);
        convNTuple.makeIndices();
        convNTuple.reset();
        return convNTuple;
    }
}

