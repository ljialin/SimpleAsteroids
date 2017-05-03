package ga;

import javax.swing.*;
import java.awt.*;

import static asteroids.Constants.size;

/**
 * Created by sml on 17/05/2016.
 */
public class BitView extends JComponent {
    int[] v;
    int w;

    int size;
    int grid = 50;

    public BitView(int[] v) {
        this.v = v;
        size = (int) (Math.sqrt(v.length));

    }

    public void paintComponent(Graphics g) {
        for (int i=0; i<v.length; i++) {
            if(v[i] == 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.YELLOW);
            }
            int x = (i % size) * grid;
            int y = (i / size) * grid;
            g.fillRect(x, y, grid, grid );
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(size*grid, size*grid);
    }


}
