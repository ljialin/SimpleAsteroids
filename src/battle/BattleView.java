package battle;

import evodef.EvoAlg;
import evodef.GameActionSpaceAdapterMulti;
import math.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

import static asteroids.Constants.*;
import static battle.BattleGameParameters.*;

public class BattleView extends JComponent {
    public static boolean trace = false;
    static int offset = 0;
    int scale;
    // static int carSize = 5;
    public SimpleBattleState game;

    public EvoAlg evoAlg;


    static Color[] shipColors = {Color.blue,
            Color.green};
    // Font font;

    static Random rand = new Random();

    int[][][] rolls;


    static double viewScale = 1.0;

    public static Color randColor() {
        // Color color = Color.getHSBColor(rand.nextFloat(), 1, 1);
        Color color = Color.getHSBColor(0.9f, 1, 1);
        return color;
    }

    public void setRolls(int i, EvoAlg evoAlg) {
        try {
            ArrayList<int[]> solutions = evoAlg.getLogger().solutions;
            rolls[i] = new int[solutions.size()][];
            for (int j = 0; j < solutions.size(); j++) {
                rolls[i][j] = solutions.get(j);
            }
        } catch (Exception e) {

        }
    }

    public BattleView(SimpleBattleState game) {
        this.game = game;
        scale = size.width - 2 * offset;
        rolls = new int[2][][];
    }

    static Color bg = randColor();

    public void paintComponent(Graphics gx) {
        Graphics2D g = (Graphics2D) gx;
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);
        AffineTransform at = g.getTransform();
        g.translate((1 - viewScale) * width / 2, (1-viewScale)*height / 2);


        // this was an experiment to turn it into a side-scroller
        // but it produces a weird moving screen effect
        // needs more logic in the drawing process
        // to wrap the asteroids that have been projected off the screen
        // g.translate(-(game.ship.s.x - width/2), 0);

        g.scale(viewScale, viewScale);

        drawGame(g);
        g.setTransform(at);
        paintState(g);

//        g.setFont(font);
//        g.drawString("Hello", 100, 100);
    }

    static Vector2d tmp = new Vector2d();
    private void drawGame(Graphics2D g) {
        // draw the damage radius before the ship, so that each ship can be seen over the top of it
        for (int i=0; i<game.ships.length; i++) {
            SimpleShip ss = game.ships[i];
            g.setColor(shipColors[i]);
            tmp.set(ss.s);
            tmp.add(ss.d, SimpleBattleState.projection);
            // now do a fill-oval
            int r = (int) damageRadius;
            if (game.damageFunction(game.ships[i], game.ships[(i+1) % game.ships.length]) > 0) {
                g.setColor(Color.red);
            }
            g.fillOval((int) tmp.x-r, (int) tmp.y-r, 2*r, 2*r);
        }

        // just test with a temporary Ship vector for now
        int rolloutLength = 20;
        int nActions = SimpleBattleState.actions.length;
        for (int i=0; i<game.ships.length; i++) {
            SimpleShip ss = game.ships[i];
            g.setColor(shipColors[i]);
            ss.draw(g);
            // int[] roll = SearchSpaceUtil.randomPoint(new TenSpace(rolloutLength, nActions));
            int[][] roll = rolls[i];
            if (roll != null && roll.length > 0) {
                // g.setColor(Color.black);
                g.setColor(new Color(0, 0, 0, 50));
                // roll = evoAlg.getLogger().finalSolution();
                // handle evo controllers specially: give them lots of drawing!
                // System.out.println("Solutions: " + roll.length);
                for (int[] a : roll) {
                    drawShipRollout(g, game.ships[i].copy(), a);
                }

            } else {
                // g.setColor(Color.white);
                // drawShipRollout(g, game.ships[i].copy(), roll);
            }
        }
    }

    public void paintState(Graphics2D g) {
        g.setFont(font);
        String str = "" + game.score[0] + " : " + game.score[1] + " : " + game.nTicks;
        // FontMetrics fm = font.
        g.setColor(Color.white);
        g.drawString(str, 10, 20);
    }

    // break it down - draw a vector for each ship...

    static Stroke stroke = new BasicStroke(5);

    private void drawShipRollout(Graphics2D g, SimpleShip ship, int[] seq) {

        g.setStroke(stroke);
        Path2D path = new Path2D.Double();
        path.moveTo(ship.s.x, ship.s.y);

        for (int a : seq) {
            for (int i=0; i< GameActionSpaceAdapterMulti.actionRepeat; i++) {
                ship.update(SimpleBattleState.actions[a]);
            }
            path.lineTo(ship.s.x, ship.s.y);
        }
        g.draw(path);

    }

    public Dimension getPreferredSize() {
        return size;
    }


}
