package planetwar;

import math.Vector2d;
import numbergame.DiffGame;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static asteroids.Constants.font;
import static asteroids.Constants.rand;
import static asteroids.Constants.size;

/**
 * Created by simonmarklucas on 30/03/2017.
 */
public class PlanetWarView extends JComponent {

    GameState game;

    public PlanetWarView(GameState game) {
        this.game = game;
    }

    static Color bg = Color.getHSBColor(0.1f, 1, 1);

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

        double sectorAngle = Math.PI * 2 / game.nPlanets;

        for (int i = 0; i < game.nPlanets; i++) {
            Color color = Color.gray;
            if (game.planets[i] > 0) {
                color = Color.green;
            }
            if (game.planets[i] < 0) {
                color = color.red;
            }
            drawPlanet(g, i , color, sectorAngle * i);
        }

        String score = game.buffers[0] + " : " + (int) game.getScore() + " : " + game.buffers[1];
        centreString(g, score,getWidth() / 2, getHeight() / 2);

    }

    static int planetScale = 3;

    private void drawPlanet(Graphics2D g, int i, Color color, double angle) {
        g.setColor(color);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int scale = (int) (0.9 * Math.min(cx, cy));
        int discSize = (int) (scale * planetScale * game.growthRates[i]);
        int x = cx + (int) (scale * Math.cos(angle));
        int y = cy + (int) (scale * Math.sin(angle));
        g.fillOval(x - discSize / 2, y - discSize / 2, discSize, discSize);

        // now see if this planet is also the focus of either player
        if (i == game.focii[0]) {
            // draw a green rectangle
            g.setColor(Color.green);
            g.setStroke(new BasicStroke(5));
            g.translate(x, y);
            g.rotate(Math.PI/4);
            g.drawRect(-discSize / 2, -discSize / 2, discSize, discSize);
            g.rotate(-Math.PI/4);
            g.translate(-x, -y);
        }

        if (i == game.focii[1]) {
            // draw a red rectangle
            // draw a green rectangle
            g.setColor(Color.red);
            g.setStroke(new BasicStroke(5));
            g.drawRect(x - discSize / 2, y - discSize / 2, discSize, discSize);
        }

        g.setColor(Color.black);

        String str = "" + (int) game.planets[i];

        centreString(g, str, x, y);
    }

    public void centreString(Graphics2D g, String str, int x, int y) {
        int fontSize = 16;
        g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        Rectangle2D rect = g.getFontMetrics().getStringBounds(str, g);
        // use a drawString method for now
        int sx = x - (int) rect.getWidth() / 2;
        int sy = y + (int)  + rect.getHeight() / 2;

        g.drawString(str, sx, sy);
    }

    public void update(GameState game) {
        this.game = game;
        repaint();
    }

    public void paintState(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(font);
        String str = "" + (int) game.getScore() + " : " + game.nTicks;
        // FontMetrics fm = font.
        g.drawString(str, 10, 20);
    }

    public Dimension getPreferredSize() {
        return size;
    }

}


