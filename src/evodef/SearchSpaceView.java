package evodef;

import gvglink.BattleGameSearchSpace;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import static asteroids.Constants.size;

/**
 * Created by Simon Lucas on 01/05/2017.
 *
 * Enables an easy way to view a search space
 * Actually draws an EvoVectorSet because that has more information in it
 * i.e. the name of each parameter and its actual values
 *
 */
public class SearchSpaceView extends JComponent {

    public static void main(String[] args) {
        EvoVectorSet searchSpace = BattleGameSearchSpace.getSearchSpace();
        SearchSpaceView view = new SearchSpaceView(searchSpace);
        new JEasyFrame(view, searchSpace.toString());
    }

    int width = 640;
    int height = 480;
    Dimension size = new Dimension(width, height);

    EvoVectorSet searchSpace;

    public SearchSpaceView(EvoVectorSet searchSpace) {
        this.searchSpace = searchSpace;
    }

    public Dimension getPreferredSize() {
        return size;
    }


    static Color bg = Color.BLACK;
    public void paintComponent(Graphics gx) {

        int[] randPoint = SearchSpaceUtil.randomPoint(searchSpace);
        Graphics2D g = (Graphics2D) gx;
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);

        // now need to work out the various sizes needed for these ...

        int n = searchSpace.nDims();

        int inset = 5;

        double yStep = (double) size.getHeight() / n;
        float colStep = 1.0f / n;
        float col = 0.1f;
        double y = 0;
        for (int i=0; i<n; i++) {
            Path2D path = new Path2D.Double();
            path.moveTo(0, y);
            g.setColor(Color.getHSBColor(col, 1, 1));
            col += colStep;
            Shape shape = new Rectangle2D.Double(0, y, size.getWidth(), yStep);
            path.append(shape, false);
            g.fill(shape);
            g.setColor(Color.cyan);
            g.setStroke(new BasicStroke(5));
            g.draw(path);

            g.setColor(Color.black);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) yStep / 4 ));

            //
            // Draw Hello World String
            //
            String paramName = searchSpace.params.get(i).name;
            int yString = (int) (y + yStep/2 + inset * 2);
            g.drawString(paramName, (int)inset * 2, yString);

            // now iterate over all the values there

            FontMetrics fontMetrics = g.getFontMetrics();

            Rectangle2D rect = fontMetrics.getStringBounds(paramName, g);

            int x = (int) inset * 2;
            x += rect.getWidth() + inset * 2;

            double[] vec = searchSpace.params.get(i).values;
            for ( int j = 0; j< vec.length; j++ ) {
                double val = vec[j];
                String sVal = String.format("%.1f", val);
                rect = fontMetrics.getStringBounds(sVal, g);

                if (j == randPoint[i]) {
                    g.setColor(Color.white);

                    // need to move the rect to the correct place
                    g.fill(rect);
                    System.out.println("Filled: " + rect);
                }
                g.setColor(Color.black);
                g.drawString(sVal, x, yString);
                x += rect.getWidth() + inset*2;
            }


            y += yStep;

            System.out.println(y);
        }
    }
}
