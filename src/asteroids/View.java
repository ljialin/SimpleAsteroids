package asteroids;

import battle.SimpleBattleState;
import evodef.GameActionSpaceAdapterMulti;
import math.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static asteroids.Asteroid.stroke;
import static java.awt.Color.black;
import static asteroids.Constants.*;


public class View extends JComponent {
    public static boolean trace = false;
    static int offset = 0;
    int scale;
    // static int carSize = 5;
    static Color bg = black;
    GameState gameState;
    // Font font;

    Ship ship;

    static double viewScale = 1.0;

    ArrayList<int[]> playouts;


    public View(GameState gameState) {
        this.gameState = gameState;
        scale = size.width - 2 * offset;
        ship = new Ship(gameState, new Vector2d(), new Vector2d(), new Vector2d());
    }



    public void paintComponent(Graphics gx) {
        Graphics2D g = (Graphics2D) gx;
        AffineTransform at = g.getTransform();
        g.translate((1 - viewScale) * width / 2, (1-viewScale)*height / 2);


        // this was an experiment to turn it into a side-scroller
        // but it produces a weird moving screen effect
        // needs more logic in the drawing process
        // to wrap the asteroids that have been projected off the screen
        // g.translate(-(gameState.ship.s.x - width/2), 0);

        g.scale(viewScale, viewScale);

        gameState.draw(g);
        drawPlayouts(g);

        g.setTransform(at);

        paintState(g);

//        g.setFont(font);
//        g.drawString("Hello", 100, 100);
    }

    private void drawPlayouts(Graphics2D g) {
        try {
            //EvoAgentAdapter adapter = (EvoAgentAdapter) gameState.

            g.setColor(new Color(255, 0, 128, 100));
            if (playouts != null) {
                for (int[] seq : playouts) {
                    drawShipPlayout(g, gameState.forwardModel.ship.copy(), seq);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawShipPlayout(Graphics2D g, Ship ship, int[] seq) {

        g.setStroke(stroke);
        Path2D path = new Path2D.Double();
        path.moveTo(ship.s.x, ship.s.y);

        for (int a : seq) {
//            for (int i = 0; i< GameActionSpaceAdapterMulti.actionRepeat; i++) {
//                ship.update(SimpleBattleState.actions[a]);
//            }
            ship.update(actionAdapter.getAction(a));

            // the ship should be wrapped, but the problem is with how to draw the path
            // it would need to be restarted ...
            // gameState.forwardModel.wrap(ship);
            path.lineTo(ship.s.x, ship.s.y);
        }
        g.draw(path);

    }

    ActionAdapter actionAdapter = new ActionAdapter();



    public void paintState(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(font);
        String str = "" + gameState.getScore() + " : " + gameState.forwardModel.nLives + " : " + gameState.state
                + " : " + gameState.forwardModel.isShipSafe();
        // FontMetrics fm = font.

        g.drawString(str, 10, 20);


        // now show the lives as ships ...
        ship.s.set(10, 40);
        ship.d.set(0, -1);
        for (int i = 0; i < gameState.forwardModel.nLives; i++) {
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
