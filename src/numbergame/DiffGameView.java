package numbergame;

import battle.SimpleBattleState;
import battle.SimpleShip;
import math.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static asteroids.Constants.*;
import static asteroids.Constants.size;

/**
 * Created by simonmarklucas on 30/03/2017.
 */
public class DiffGameView extends JComponent {


    DiffGame game;
    public DiffGameView(DiffGame game) {
        this.game = game;
    }

    static Color bg = Color.BLACK;
    public void paintComponent(Graphics gx) {
        Graphics2D g = (Graphics2D) gx;
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);
        drawGame(g);
        paintState(g);

//        g.setFont(font);
//        g.drawString("Hello", 100, 100);
    }

    static Vector2d tmp = new Vector2d();

    private void drawGame(Graphics2D g) {

        double sectorAngle = Math.PI * 2 / DiffGame.nValues;

        drawPlayer(g, Color.GREEN, sectorAngle * game.i1);
        drawPlayer(g, Color.RED, sectorAngle * game.i2);

        System.out.println(game.i1 + " : " + game.i2 + "\t: " + game.score);
    }

    private void drawPlayer(Graphics2D g, Color color, double angle) {
        g.setColor(color);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int scale = Math.min(cx, cy);
        int discSize = scale / 5;
        int x = cx + (int) (scale * Math.cos(angle));
        int y = cy + (int) (scale * Math.sin(angle));
        g.fillOval(x - discSize/2, y - discSize/2, discSize, discSize);
    }


        public void update(DiffGame game) {
        this.game = game;
        repaint();
    }


    public void paintState(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(font);
        String str = "" + game.score  + " : " + game.getGameTick();
        // FontMetrics fm = font.
        g.drawString(str, 10, 20);

    }

    public Dimension getPreferredSize() {
        return size;
    }


}


