package evomaze;

import graph.Graph;
import graph.GraphBuilder;
import graph.ShortestPath;
import graph.Vertex;
import utilities.JEasyFrame;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

import static evomaze.Constants.from;
import static evomaze.Constants.to;

/**
 * Created by simonmarklucas on 04/08/2016.
 */
public class MazeView extends JComponent {

    public MazeView(int[] bits) {
        this.bits = bits;
        squareSize = (int) Math.sqrt(bits.length);
        System.out.println("Made a MazeView: " + getPreferredSize() + " : " + squareSize);
    }

    public static void showMaze(int[] bits, String title) {
        MazeModel mazeModel = new MazeModel(bits);
        MazeView mazeView = new MazeView(bits);
        mazeView.path = mazeModel.findShortestPath(from, to);
        // mazeView.setPath(path);
        new JEasyFrame(mazeView, title);
    }

    private void setPath(List<Vertex> path) {
    }

    public static void main(String[] args) {
        int n = 400;
        int[] bits = new int[n];
        Random random = new Random();
        for (int i=0; i<n; i++) {
            bits[i] = random.nextInt(2);
        }
        MazeView mazeView = new MazeView(bits);
        new JEasyFrame(mazeView, "Maze Test");

        MazeModel mazeModel = new MazeModel(bits);

        Graph graph = new GraphBuilder().buildGraph(mazeModel);

        System.out.println(graph.getArcs());

        System.out.println("Finding shortest path:");

        Vertex from = new Vertex(0, 0);
        Vertex to = new Vertex(2, 2);

        System.out.println(new ShortestPath().runShortestPath(graph, from, to));

    }

    int cellSize = 20;  // cell size in pixels

    int[] bits;

    int squareSize;

    List<Vertex> path;

    public void paintComponent(Graphics go) {
        // System.out.println("Painting: " + getPreferredSize());
        Graphics2D g = (Graphics2D) go;

//        g.setColor(Color.red);
//        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i=0; i<bits.length; i++) {
            Color col = bits[i] == 1 ? Color.black : Color.white;
            g.setColor(col);
            g.fillRect((i % squareSize) * cellSize, (i / squareSize) * cellSize, cellSize, cellSize);
        }

        drawPath(g, path);

    }

    private void drawPath(Graphics2D g, List<Vertex> path) {
        if (path!=null) {
            for (Vertex v : path) {
            }
            g.setColor(Color.blue);
            g.setStroke(new BasicStroke(cellSize / 4)); //  BasicStroke.JOIN_ROUND));
            for (int i=1; i < path.size(); i++) {
                Vertex a = path.get(i-1);
                Vertex b = path.get(i);
                int mid = cellSize / 2;
                g.drawLine(a.x * cellSize + mid, a.y * cellSize+ mid, b.x * cellSize + mid, b.y * cellSize + mid);
            }

            g.setColor(Color.green);
            drawDisc(g, path.get(0));
            drawDisc(g, path.get(path.size()-1));
        }
    }

    private void drawDisc(Graphics2D g, Vertex v) {
        g.fillOval(v.x*cellSize, v.y*cellSize,cellSize,cellSize);
    }

    public Dimension getPreferredSize() {
        return new Dimension(squareSize*cellSize, squareSize*cellSize);
    }



}
