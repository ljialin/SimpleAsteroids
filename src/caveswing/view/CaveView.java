package caveswing.view;

import asteroids.Ship;
import caveswing.core.Anchor;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import math.Vector2d;
import spinbattle.util.DrawUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class CaveView extends JComponent {
    CaveSwingParams params;
    CaveGameState gameState;
    Color bg = Color.black;
    Color deadZone = Color.getHSBColor(0.9f, 1, 1);
    Color goalZone = Color.getHSBColor(0.3f, 1, 1);
    Color finishZone = Color.getHSBColor(0.7f, 1, 1);
    int nStars = 200;
    int rad = 10;
    Color anchorColor = Color.getHSBColor(0.17f, 1, 1);
    Color avatarColor = Color.getHSBColor(0.50f, 1, 1);
    int scoreFontSize = 16;
    int planetFontSize = 14;
    DrawUtil scoreDraw = new DrawUtil().setColor(Color.white).setFontSize(scoreFontSize);

    public boolean scrollView = true;
    public int scrollWidth = 600;

    public ArrayList<int[]> playouts;

    static Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);


    public CaveView setGameState(CaveGameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public CaveView setParams(CaveSwingParams params) {
        this.params = params;
        setStars();
        return this;
    }

    private void setStars() {
        for (int i=0; i<nStars; i++) {
            stars.add(new Star());
        }
    }

    public String getTitle() {
        return gameState.nTicks + " : " + nPaints;
    }

    public Dimension getPreferredSize() {
        if (scrollView) {
            return new Dimension(scrollWidth, params.height);
        }
        else {
            return new Dimension(params.width, params.height);
        }
    }

    public int nPaints = 0;

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(0, 0, getWidth(), getHeight());

        double xScroll = -gameState.avatar.s.x + scrollWidth/2;
        if (scrollView) {
            g.translate(xScroll, 0);
        }
        paintStars(g);
        paintZones(g);


        paintAnchors(g);
        paintAvatar(g);
        drawPlayouts(g);

        if (scrollView) {
            g.translate(-xScroll, 0);
        }
        // have to paint the score last so that it is not obscured by any game objects
        paintScore(g);
        nPaints++;
    }

    private void paintAnchors(Graphics2D g) {
        g.setColor(Color.white);
        int index = 0;
        Anchor currentAnchor = gameState.map.getClosestAnchor(gameState.avatar.s);
        for (Anchor a : gameState.map.anchors) {
            if (a.equals(currentAnchor)) {
                g.setColor(anchorColor);
            } else {
                g.setColor(Color.lightGray);
            }
            g.fillOval((int) a.s.x - rad, (int) a.s.y-rad, 2*rad, 2*rad);
            index++;
        }
    }

    static Color ropeColor = new Color(178,34,34);


    private void paintAvatar(Graphics2D g) {
        g.setColor(avatarColor);
        Vector2d s = gameState.avatar.s;
        g.fillRect((int) s.x - rad, (int) s.y-rad, 2*rad, 2*rad);
        if (gameState.currentAnchor != null) {
            // todo: draw a rope to the selected one
            g.setColor(ropeColor);
            g.setStroke(new BasicStroke(rad/2));
            Vector2d p = gameState.currentAnchor.s;
            g.drawLine((int) s.x, (int) s.y, (int) p.x, (int) p.y);
        }
    }

    private void paintScore(Graphics2D g) {
        g.setColor(Color.white);
        int score = (int) gameState.getScore();
        String message = String.format("%d", score);
        scoreDraw.centreString(g, message, getWidth()/2, scoreFontSize);
    }

    private void drawPlayouts(Graphics2D g) {
        try {
            g.setColor(new Color(255, 0, 128, 100));
            if (playouts != null) {
                for (int[] seq : playouts) {
                    drawShipPlayout(g, (CaveGameState) gameState.copy(), seq);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawShipPlayout(Graphics2D g, CaveGameState gameState, int[] seq) {

        g.setStroke(stroke);
        Path2D path = new Path2D.Double();
        Vector2d pos = gameState.avatar.s;
        path.moveTo(pos.x, pos.y);
        for (int a : seq) {
            gameState.next(new int[]{a});
            pos = gameState.avatar.s;
            path.lineTo(pos.x, pos.y);
        }
        g.draw(path);

    }

    ArrayList<Star> stars = new ArrayList();

    private void paintStars(Graphics2D g) {
        for (Star star : stars) star.draw(g);
    }

    static int zoneWidth = 200;
    static int goalRatio = 10;
    static int borderRatio = 20;

    private void paintZones(Graphics2D g) {

        g.setColor(deadZone);
        g.fillRect(-zoneWidth, 0, zoneWidth, getHeight());
        g.fillRect(0, 0, gameState.params.width, getHeight()/borderRatio);
        g.fillRect(0, getHeight() - getHeight() / borderRatio, gameState.params.width, getHeight()/borderRatio);

        g.setColor(finishZone);
        g.fillRect(gameState.params.width, 0, zoneWidth, getHeight());

        g.setColor(goalZone);
        g.fillRect(gameState.params.width, 0, zoneWidth/goalRatio, getHeight()/ goalRatio);


    }

    class Star {
        int x,y;
        double inc;
        double shine = params.getRandom().nextDouble();
        Star() {
            x = params.getRandom().nextInt(params.width);
            y = params.getRandom().nextInt(params.height);
            inc = 0.1 * (params.getRandom().nextDouble() + 1);
        }
        void draw(Graphics2D g) {
            shine += inc;
            float bright = (float) (1 + Math.sin(shine)) / 2;
            Color grey = new Color(bright, 1-bright, bright);
            g.setColor(grey);
            g.fillRect(x, y, 2, 2);
        }
    }


}
