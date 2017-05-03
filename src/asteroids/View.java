package asteroids;

import math.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import static asteroids.Constants.bg;
import static java.awt.Color.black;
import static asteroids.Constants.*;


public class View extends JComponent {
    public static boolean trace = false;
    static int offset = 0;
    int scale;
    // static int carSize = 5;
    static Color bg = black;
    GameState game;
    // Font font;

    Ship ship;

    static double viewScale = 1.0;


    public View(GameState game) {
        this.game = game;
        scale = size.width - 2 * offset;
        ship = new Ship(game, new Vector2d(), new Vector2d(), new Vector2d());
    }

    public void paintComponent(Graphics gx) {
        Graphics2D g = (Graphics2D) gx;
        AffineTransform at = g.getTransform();
        g.translate((1 - viewScale) * width / 2, (1-viewScale)*height / 2);


        // this was an experiment to turn it into a side-scroller
        // but it produces a weird moving screen effect
        // needs more logic in the drawing process
        // to wrap the asteroids that have been projected off the screen
        // g.translate(-(game.ship.s.x - width/2), 0);

        g.scale(viewScale, viewScale);

        game.draw(g);
        g.setTransform(at);
        paintState(g);

//        g.setFont(font);
//        g.drawString("Hello", 100, 100);
    }


    public void paintState(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(font);
        String str = "" + game.score + " : " + game.list.nShips() + " : " + game.state
                + " : " + game.list.isSafe(game.ship) + " : " + game.nLives;
        // FontMetrics fm = font.

        g.drawString(str, 10, 20);


        // now show the lives as ships ...
        ship.s.set(10, 40);
        ship.d.set(0, -1);
        for (int i = 0; i < game.nLives; i++) {
            ship.draw(g);
            ship.s.add(20, 0);
        }
    }

    public static void messageScreen(Graphics2D g, String message) {
        g.setColor(bg);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.yellow);
        g.setFont(font);
        Rectangle2D rect = font.getStringBounds(message, g.getFontRenderContext());
        g.drawString(message, width/2 - (int) rect.getWidth()/2,
                height/2 - (int)rect.getHeight()/2);
    }

    public Dimension getPreferredSize() {
        return size;
    }


}
