package ntuple;

import math.Vector2d;
import ropegame.RopeGameState;
import utilities.StatSummary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by sml on 22/05/2017.
 */

public class NTupleView2D extends JComponent {


    static int cellSize = 100;

    NTuple xn, yn, xyn;
    int m;

    public NTupleView2D(NTuple xn, NTuple yn, NTuple xyn, int m) {
        this.xn = xn;
        this.yn = yn;
        this.xyn = xyn;
        this.m = m;
    }

    public Dimension getPreferredSize() {
        return new Dimension(cellSize * (m + 1), cellSize * (m + 1));
    }

    static Color bg = Color.black;

    public void paintComponent(Graphics gx) {
        Graphics2D g = (Graphics2D) gx;
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i=0; i<m; i++) {
            for (int j=0; j<m; j++) {
                int[] p = new int[]{i, j};

            }
        }

    }

    private void drawCell(Graphics2D g, StatSummary ss) {
        
    }

}
