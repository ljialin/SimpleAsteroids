package spinbattle.view;

import spinbattle.util.DrawUtil;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ColorTriangleDisplay extends JComponent {

    public static void main(String[] args) {
        ColorTriangleDisplay triangleDisplay = new ColorTriangleDisplay().setTriangles();
        new JEasyFrame(triangleDisplay, "Hue Range");
    }

    int cellSize = 100;
    float inc = 0.01f;
    int width = 10;
    int height = 10;

    ArrayList<Triangle> triangles = new ArrayList<>();

    public ColorTriangleDisplay setTriangles() {
        float hue = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                double x = cellSize * i * Math.sin(Math.PI/3);
                double y = cellSize * j;
                Triangle triangle = new Triangle(x, y, cellSize, (i + j * width) % 2 == 0);
                triangle.color = Color.getHSBColor(hue, 1, 1);
                triangle.label = i + ":" + j;
                triangles.add(triangle);
                hue += inc;
            }
        }
        return this;
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        for (Triangle t : triangles) {
            t.paint(g);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension((int) (cellSize * width * Math.sin(Math.PI / 3)), cellSize * height);
    }

    class Triangle {

        double cx, cy;
        double size;
        boolean even;
        Path2D path = new Path2D.Double();

        public Triangle(double cx, double cy, double size, boolean even) {
            this.cx = cx;
            this.cy = cy;
            this.size = size;
            this.even = even;

            setPath();

        }

        void setPath() {
            double yd = size/2;
            if (even) {
                double xm = size/4;
                double xp = size * (Math.sin(Math.PI / 3) - 0.25);
                path.moveTo(cx - xm, cy - yd);
                path.lineTo(cx + xp, cy);
                path.lineTo(cx - xm, cy + yd);
                path.closePath();
            } else {
                double xp = size/4;
                double xm = size * (Math.sin(Math.PI / 3) - 0.25);
                path.moveTo(cx + xm, cy - yd);
                path.lineTo(cx - xp, cy);
                path.lineTo(cx + xm, cy + yd);
                path.closePath();
            }
        }

        String label;
        Color color;


        public Triangle paint(Graphics2D g) {
            g.setColor(color);
            g.fill(path);
            DrawUtil draw = new DrawUtil().setColor(Color.black).setFontSize(cellSize / 5);
            draw.centreString(g, label, cx, cy);
            return this;
        }


    }


}
