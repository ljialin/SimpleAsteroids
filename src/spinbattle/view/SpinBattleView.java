package spinbattle.view;

import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.util.DrawUtil;

import static spinbattle.params.Constants.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SpinBattleView extends JComponent {
    SpinBattleParams params;
    SpinGameState gameState;
    Color bg = Color.black;
    int nStars = 100;
    Color[] playerColors = {
            Color.getHSBColor(0.25f, 1, 1),
            Color.getHSBColor(0.75f, 1, 1),
            Color.lightGray
    };
    int scoreFontSize = 16;
    int planetFontSize = 14;
    DrawUtil planetDraw = new DrawUtil().setColor(Color.black).setFontSize(planetFontSize);
    DrawUtil scoreDraw = new DrawUtil().setColor(Color.white).setFontSize(scoreFontSize);

    public SpinBattleView setGameState(SpinGameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public SpinBattleView setParams(SpinBattleParams params) {
        this.params = params;
        setStars();
        return this;
    }

    private void setStars() {
        for (int i=0; i<nStars; i++) {
            stars.add(new Star());
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(params.width, params.height);
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(0, 0, getWidth(), getHeight());
        paintStars(g);
        paintPlanets(g);
        paintTransits(g);
        paintScore(g);
    }

    private void paintScore(Graphics2D g) {
        g.setColor(Color.white);
        int score = (int) gameState.getScore();
        String leader = score > 0 ? "P1" : "P2";
        score = Math.abs(score);
        String message = String.format("%s: %d", leader, score);
        scoreDraw.centreString(g, message, getWidth()/2, scoreFontSize);
    }

    private void paintPlanets(Graphics2D g) {
        for (Planet p : gameState.planets) {
            g.setColor(playerColors[p.ownedBy]);
            int rad = (int) (p.growthRate * growthRateToRadius);
            int cx = (int) p.position.x, cy = (int) p.position.y;
            g.fillOval(cx-rad, cy-rad, 2* rad, 2* rad);
            // now show it's rotation if non-neutral

            if (p.ownedBy != Constants.neutralPlayer) {
                int xRot = cx + (int) (rad * Math.sin(p.rotation));
                int yRot = cy + (int) (rad * Math.cos(p.rotation));

                rad /= 4;
                g.fillOval(xRot - rad, yRot - rad, rad * 2, rad * 2);
            }

            planetDraw.centreString(g, "" + (int) p.shipCount, p.position.x, p.position.y);
        }
    }

    private void paintTransits(Graphics2D g) {
        for (Planet p : gameState.planets) {
            Transporter t = p.getTransporter();
            if (t != null && t.inTransit()) {
                g.setColor(playerColors[t.ownedBy]);
                int rad = (int) (Constants.minTransitRadius + Math.sqrt(t.payload));
                g.fillRect((int) t.mo.s.x - rad, (int) t.mo.s.y - rad, 2 * rad, 2 * rad);
                TransporterView tv = new TransporterView().setState(t.mo.s, t.mo.v, rad);
                tv.draw(g);
                planetDraw.centreString(g, "" + (int) t.payload, t.mo.s.x, t.mo.s.y);
            }
        }
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
