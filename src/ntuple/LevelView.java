package ntuple;

import levelgen.MarioReader;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by simonmarklucas on 04/08/2016.
 */

public class LevelView extends JComponent {

    public static void main(String[] args) {
        int[][] randRect = randomRect(40, 25);
        showMaze(randRect, "Random Test of LevelView");
    }

    int width, height;
    int cellSize = 20;
    int[][] tiles;

    static Color[] colors = {
            Color.white, Color.black, Color.red,
    };

    HashMap<Integer,Color> colorMap;

    public LevelView setColorMap(HashMap<Integer, Color> colorMap) {
        this.colorMap = colorMap;
        return this;
    }

    public LevelView setCellSize(int cellSize) {
        this.cellSize = cellSize;
        return this;
    }

    static Random random = new Random();

    static int[][] randomRect(int w, int h) {
        int[][] tiles = new int[w][h];
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                tiles[i][j] = random.nextInt(colors.length);
            }
        }
        return tiles;
    }

    public static int[][] toRect(int[] x, int w, int h) {
        int[][] tiles = new int[w][h];
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                tiles[i][j] = x[i + j * w];
            }
        }
        return tiles;
    }

    public LevelView(int[][] tiles) {
        this.tiles = tiles;
        width = tiles.length;
        height = tiles[0].length;
    }

    public static void showMaze(int[][] tiles, String title) {
        LevelView levelView = new LevelView(tiles).setColorMap(MarioReader.tileColors);
        new JEasyFrame(levelView, title);
    }

    public static void showMaze(int[] tiles, int w, int h, String title) {
        LevelView levelView = new LevelView(toRect(tiles, w, h));
        new JEasyFrame(levelView, title);
    }

    public static void showMaze(int[] tiles, int w, int h, String title, HashMap<Integer,Color> tileMap) {
        LevelView levelView = new LevelView(toRect(tiles, w, h)).setColorMap(tileMap);
        new JEasyFrame(levelView, title);
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (colorMap != null) {
                    try {
                        g.setColor(colorMap.get(tiles[x][y]));
                    } catch (Exception e) {
                        g.setColor(Color.white);
                    }
                } else {
                    g.setColor(colors[tiles[x][y]]);
                }
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width * cellSize, height * cellSize);
    }

}
