package spinbattle.view;

import spinbattle.util.DrawUtil;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;

public class ColorSwatchGrid extends JComponent {

    public static void main(String[] args) {
        new JEasyFrame(new ColorSwatchGrid(), "Hue Range");
    }

    int cellSize = 50;
    float inc = 0.01f;
    int width = 10;
    int height = 10;

    public void paintComponent(Graphics g) {
        int n = width * height;
        float h = 0;
        for (int i=0; i<n; i++) {
            g.setColor(Color.getHSBColor(h, 1, 1));
            int x = cellSize * (i % width), y = cellSize * (i / width);
            g.fillRect(x, y, cellSize, cellSize);
            String label = String.format("%.2f", h);
            DrawUtil draw = new DrawUtil().setColor(Color.black).setFontSize(cellSize/5);
            draw.centreString((Graphics2D) g, label, x + cellSize/2, y+ cellSize/2);
            h += inc;
        }
    }

    public Dimension getPreferredSize() {

        return new Dimension(cellSize * width, cellSize * height);
    }


}
