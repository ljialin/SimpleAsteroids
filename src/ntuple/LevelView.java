package ntuple;

import evomaze.MazeModel;
import graph.Graph;
import graph.GraphBuilder;
import graph.ShortestPath;
import graph.Vertex;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

import static evomaze.Constants.from;
import static evomaze.Constants.to;

/**
 * Created by simonmarklucas on 04/08/2016.
 */
public class LevelView extends JComponent {

    int width, height;
    int cellSize = 20;
    int[][] tiles;

    static Color[] colors = {
            Color.red, Color.black, Color.white,
    };

    public LevelView(int[][] tiles) {
        this.tiles = tiles;
        width = tiles.length;
        height = tiles[0].length;
    }

    public static void showMaze(int[][] tiles, String title) {
        LevelView levelView = new LevelView(tiles);
        new JEasyFrame(levelView, title);
    }

    public void paintComponent(Graphics go) {
        // System.out.println("Painting: " + getPreferredSize());
        Graphics2D g = (Graphics2D) go;

//        g.setColor(Color.red);
//        g.fillRect(0, 0, getWidth(), getHeight());

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                g.setColor(colors[tiles[x][y]]);
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width * cellSize, height * cellSize);
    }

}
