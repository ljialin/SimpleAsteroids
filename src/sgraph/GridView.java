package sgraph;

import spinbattle.util.DrawUtil;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;

public class GridView extends JComponent {

    public static void main(String[] args) {
        new JEasyFrame(new GridView(), "Hue Range");
    }

    int cellSize = 50;
    float inc = 0.01f;

    Color[] colors = new Color[]{
            Color.getHSBColor(0.05f, 1, 1),
            Color.getHSBColor(0.25f, 1, 1),
            Color.getHSBColor(0.45f, 1, 1),
            Color.getHSBColor(0.65f, 1, 1),
            Color.getHSBColor(0.85f, 1, 1),
    };

    GraphParams params;
    Graph graph;

    public GridView setGraph(Graph graph) {
        this.graph = graph;
        this.params = graph.params;
        return this;
    }

    public GridView setParams(GraphParams params) {
        this.params = params;
        return this;
    }

    public void paintComponent(Graphics g) {
        int n = params.width * params.height;
        float h = 0;
        for (int i=0; i<n; i++) {
            int label = graph.labels[i];
            int x = cellSize * (i % params.width), y = cellSize * (i / params.width);
            g.setColor(colors[label]);
            g.fillRect(x, y, cellSize, cellSize);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(cellSize * params.width, cellSize * params.height);
    }

    public void processClick(int x, int y) {
        // map the x y to a node value
        int i = (x / cellSize) + params.width * (y / cellSize);
        // now advance this node
        graph.nextLabel(i);
        repaint();
    }
}
