package caveswing.view;

import caveswing.core.Anchor;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import math.Vector2d;
import spinbattle.util.DrawUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CaveView extends JComponent {
    CaveSwingParams params;
    CaveGameState gameState;
    Color bg = Color.black;
    int nStars = 200;
    int rad = 10;
    Color anchorColor = Color.getHSBColor(0.17f, 1, 1);
    Color avatarColor = Color.getHSBColor(0.50f, 1, 1);
    int scoreFontSize = 16;
    int planetFontSize = 14;
    DrawUtil scoreDraw = new DrawUtil().setColor(Color.white).setFontSize(scoreFontSize);

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
        return new Dimension(params.width, params.height);
    }

    public int nPaints = 0;

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(0, 0, getWidth(), getHeight());
        paintStars(g);

        paintAnchors(g);
        paintAvatar(g);
        paintScore(g);
        nPaints++;
    }

    private void paintAnchors(Graphics2D g) {
        g.setColor(Color.white);
        int index = 0;
        for (Anchor a : gameState.map.anchors) {
            if (index == gameState.nextAnchorIndex) {
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

    ArrayList<Star> stars = new ArrayList();

    private void paintStars(Graphics2D g) {
        for (Star star : stars) star.draw(g);
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
