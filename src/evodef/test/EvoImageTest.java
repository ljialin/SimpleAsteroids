package evodef.test;

import distance.pattern.Pattern;
import distance.pattern.PatternCount;
import distance.pattern.PatternDistribution;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EvoImageTest extends JComponent {

    static String filepath = "data/logos/QMLogoBinary.png";
    // delay per frame in ms
    int cycleDelay = 2;
    int scale = 2;
    int flipsPerCycle = 20;
    int initialDelay = 100;


    public static void main(String[] args) throws Exception {

        EvoImageTest eit = new EvoImageTest();
        eit.setImage(filepath);

        eit.computeDistribution();

        int nSteps = 10000;
        // set to zero to just show initial image
        // nSteps = 0;
        eit.setRandomImage();
        eit.evolve(nSteps);
    }

    public void randomWalkAway(int nSteps) throws Exception {
        JEasyFrame frame = new JEasyFrame(this, "Random test");
        ElapsedTimer elapsedTimer = new ElapsedTimer();
        Thread.sleep(initialDelay);
        int n = 0;
        for (int i = 0; i < nSteps; i++) {
            for (int j = 0; j < flipsPerCycle; j++) {
                setRandomDot();
                n++;
            }
            repaint();
            Thread.sleep(cycleDelay);
            frame.setTitle("Random walk away: " + n + " : " + nErrs);
        }
        System.out.println(elapsedTimer);
    }

    public void evolve(int nSteps) throws Exception {
        JEasyFrame frame = new JEasyFrame(this, "Random test");
        ElapsedTimer elapsedTimer = new ElapsedTimer();
        Thread.sleep(initialDelay);
        int n = 0;

        for (int i = 0; i < nSteps; i++) {
            for (int j = 0; j < flipsPerCycle; j++) {
                randEvoStep();
                n++;
            }
            frame.setTitle("Evolving: " + n + " : " + nErrs);
            repaint();
            Thread.sleep(cycleDelay);
        }
        System.out.println(elapsedTimer);
    }

    public EvoImageTest computeDistribution() {
        patternDistribution = new PatternDistribution();
        int w = bufferedImage.getWidth(), h = bufferedImage.getHeight();
        System.out.println(w + " : " + h);
        im = new int[w * h];
        System.out.println(im.length);
        int ix = 0;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int pixel = binarise(bufferedImage.getRGB(i, j));
                bufferedImage.setRGB(i, j, pixel);
                im[ix++] = pixel;
            }
        }
        for (int i : im) {
            patternDistribution.add(new Pattern().setPattern(new int[]{i}));
        }
        return this;
    }

    public void randEvoStep() {
        int ix = rand.nextInt(w * h);
        // we'll only accept the mutation if it brings us closer
        // to the true image, so instead of flipping and checking fitness
        // change, simply flip if it's not already equal to
        int i = ix % w, j = ix / w;
        int cur = bufferedImage.getRGB(i, j);
        int target = im[ix];
        if (cur != target) {
            bufferedImage.setRGB(i, j, target);
            // just made a correction, so reduce number of errs
            nErrs--;
        }
    }

    public EvoImageTest setRandomDot() {
        int ix = rand.nextInt(w * h);
        int col = rand.nextInt(2) == 0 ? black : white;
        int i = ix % w, j = ix / w;
        int cur = bufferedImage.getRGB(i, j);
        int target = im[ix];
        if (col != cur) {
            // change for good or bad?
            if (col == target) {
                nErrs--;
            } else {
                nErrs++;
            }
        }
        bufferedImage.setRGB(i, j, col);
        return this;
    }

    public int binarise(int x) {
        return x == white ? white : black;
    }

    static int black = 0xFF000000;
    static int white = 0xFFFFFFFF;
    static Random rand = new Random();
    // int size;
    BufferedImage bufferedImage;
    int[] im;
    PatternDistribution patternDistribution;

    // bit of sloppy programming just to keep track of the number of errors
    int nErrs = 0;

    public EvoImageTest setRandomImage() {

        nErrs = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int col = rand.nextInt(2) == 0 ? black : white;
                if (col != bufferedImage.getRGB(i, j))
                    nErrs++;
                bufferedImage.setRGB(i, j, col);
            }
        }
        return this;
    }

    int w, h;

    EvoImageTest setImage(String imageFile) {
        try {
            bufferedImage = ImageIO.read(new File(imageFile));
            w = bufferedImage.getWidth();
            h = bufferedImage.getHeight();
            // bufferedImage.getRGB()
        } catch (IOException e) {

            e.printStackTrace();
        }
        nErrs = 0;
        return this;
    }

    public EvoImageTest() {
//        this.size = size;
//        bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    }

    public void paintComponent(Graphics go) {
        // super.paintComponent(go);
        Graphics2D g = (Graphics2D) go;
        // Clears the previously drawn image.
        // g.setColor(Color.white);
        // g.fillRect(0, 0, getWidth(), getHeight());

        // System.out.println(bufferedImage);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(scale, scale);
        g.drawImage(bufferedImage, affineTransform, null);
        // g.drawImage(bufferedImage, null, 0, 0);
    }

    public Dimension getPreferredSize() {

        return new Dimension(scale * bufferedImage.getWidth(), scale * bufferedImage.getHeight());
    }

}

