package distance.test;

import distance.convolution.ConvNTuple;
import distance.kl.KLDiv;
import distance.view.LevelView;
import utilities.JEasyFrame;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static distance.util.MarioReader.flip;
import static distance.util.MarioReader.readLevel;
import static distance.util.MarioReader.tileColors;

public class KLDivWaveFunctionTest {

    public static void main(String[] args) throws Exception {

        String inputDirectory = "data/mario/wfc2levels/";
        int fileLimit = 20;

        int filterWidth = 2, filterHeight = 2, stride = 1;

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

        ArrayList<RatedLevel> ratedLevels = new ArrayList<>();
        for (int[][] level : levels) {
            ConvNTuple cnt = getConvNTuple(w, h, filterWidth, filterHeight, stride).addPoint(level, 1);

            // change version of KLDiv here
            // double score = KLDiv.klDivSymmetric(convNTuple.sampleDis, cnt.sampleDis);

            // double score = KLDiv.klDiv(convNTuple.sampleDis, cnt.sampleDis);

            // todo: compare also with a sample level
            // this may give best results in terms of ensuring coverage
            double score = KLDiv.klDiv(cnt.sampleDis, convNTuple.sampleDis);

            RatedLevel ratedLevel = new RatedLevel(level, score);
            ratedLevels.add(ratedLevel);
        }

        Collections.sort(ratedLevels);

        int index = 0;
        for (RatedLevel ratedLevel : ratedLevels) {
            String title = String.format("%d\t %.6f", index++, ratedLevel.score);
            LevelView levelView = new LevelView(flip(ratedLevel.level)).setColorMap(tileColors).setCellSize(10);
            new JEasyFrame(levelView, title);

        }


    }

    static class RatedLevel implements Comparable<RatedLevel> {

        int[][] level;
        Double score;

        public RatedLevel(int[][] level, Double score) {
            this.level = level;
            this.score = score;
        }

        @Override
        public int compareTo(RatedLevel o) {
            return score.compareTo(o.score);
        }
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
                new JEasyFrame(levelView, inputFile);
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

