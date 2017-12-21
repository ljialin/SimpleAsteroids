package asteroids;

import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import static asteroids.Constants.*;

public class Column extends GameObject implements PolyContains {
    static Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    boolean dead;
    AsteroidsGameState game;

    static double minGap = 0.2;
    static double maxGap = 0.4;

    static double colWidth = width / 20;
    Path2D upper, lower;


    public Column(AsteroidsGameState game, int index) {
        super(new Vector2d(width + index * width/4, 0), new Vector2d(-1, 0));
        this.game = game;
        setRandPaths();
    }

    public boolean wrappable() { return false; }

    public void setRandPaths() {

        double gap = (rand.nextDouble() * (maxGap - minGap) + minGap) * height;
        System.out.println("Gap = " + gap);

        double offset = minGap/2 + rand.nextDouble() * (height - gap - minGap/2);
        double bTop = offset + gap;
        double left = 0;
        double right = left + colWidth;
        upper = new Path2D.Double();
        upper.moveTo(left, 0);
        upper.lineTo(right, 0);
        upper.lineTo(right, offset);
        upper.lineTo(left, offset);
        upper.closePath();

        lower = new Path2D.Double();
        lower.moveTo(left, bTop);
        lower.lineTo(right, bTop);
        lower.lineTo(right, height);
        lower.lineTo(left, height);
        lower.closePath();
    }

    public boolean dead() {
        return dead;
    }

    public void draw(Graphics2D g) {
        // store coordinate system
        AffineTransform at = g.getTransform();
        if (isTarget) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.magenta);
        }
        g.translate(s.x, s.y);
        g.setColor(Color.magenta);
        g.fill(upper);
        g.fill(lower);

        g.setColor(Color.white);
        g.setStroke(stroke);
        g.draw(upper);
        g.draw(lower);

        g.setTransform(at);
    }

    public void update(AsteroidsGameState gameState) {
        s.add(v);
        if (s.x < 0) {
            s.x = width * 5 / 4 ;
            setRandPaths();
        }
        // if System.out.println("Column pos = " + s);
    }

    public String toString() {
        return s.toString();
    }

    public void hit() {
        // may need need to flag up that the thing has been hit ...
//        dead = true;
//        gameState.asteroidDeath(this);
    }

    public boolean contains(Vector2d p) {
        return upper.contains(p.x-s.x, p.y-s.y) || lower.contains(p.x-s.x, p.y-s.y);
    }
}
