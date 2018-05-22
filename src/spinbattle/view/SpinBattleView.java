package spinbattle.view;

import math.Vector2d;
import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;
import spinbattle.core.VectorField;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.util.DrawUtil;
import utilities.SideStack;

import static spinbattle.params.Constants.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SpinBattleView extends JComponent {
    static int nParticles = 10000;
    SpinBattleParams params;
    SpinGameState gameState;
    Color bg = Color.black;
    int nStars = 200;
    Color[] playerColors = {
            Color.getHSBColor(0.17f, 1, 1),
            Color.getHSBColor(0.50f, 1, 1),
            Color.lightGray
    };
    Color vectorColor = new Color(1f, 1f, 1f, 0.3f);
    int scoreFontSize = 16;
    int planetFontSize = 14;
    DrawUtil planetDraw = new DrawUtil().setColor(Color.black).setFontSize(planetFontSize);
    DrawUtil scoreDraw = new DrawUtil().setColor(Color.white).setFontSize(scoreFontSize);

    SideStack<Particle> particles = new SideStack<>(nParticles);

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
        paintVectorField(g);
        paintStars(g);
        paintPlanets(g);
        paintTransits(g);
        paintScore(g);
        updateParticles();
        paintParticles(g);
        nPaints++;
    }

    private void updateParticles() {
        // pop all the effects
        // System.out.println("Popping: " + gameState.logger);
        // System.out.println("nParticles = " + particles.size());
        if (gameState.logger != null) {
            //
            SideStack<ParticleEffect> effects = gameState.logger.effects;
            while (!effects.isEmpty()) {
                ParticleEffect effect = effects.pop();
                effect.trigger(particles, gameState.params);
            }
        }
        // now update all the particles
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update();
            if (particle.isDead()) iterator.remove();
        }
    }

    private void paintParticles(Graphics2D g) {
        int r = 2;
        for (Particle particle : particles) {
            // for now just draw a white blob
            g.setColor(Color.white);
            g.fillOval((int) particle.mo.s.x-r, (int) particle.mo.s.y-r, 2*r, 2*r);
        }
    }

    private void paintScore(Graphics2D g) {
        g.setColor(Color.white);
        int score = (int) gameState.getScore();
        String leader = score > 0 ? "P1" : "P2";
        score = Math.abs(score);
        String message = String.format("%d (%s)", score, leader);
        scoreDraw.centreString(g, message, getWidth()/2, scoreFontSize);

        String p1Message = String.format("P1: %d", (int) gameState.getPlayerShips(Constants.playerOne));
        String p2Message = String.format("P2: %d", (int) gameState.getPlayerShips(Constants.playerTwo));

        scoreDraw.centreString(g, p1Message, 50, scoreFontSize, playerColors[0]);

        scoreDraw.centreString(g, p2Message, getWidth()-50, scoreFontSize, playerColors[1]);


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

            String label = "" + (int) p.shipCount;
            planetDraw.centreString(g, label, p.position.x, p.position.y);
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

    private void paintVectorField(Graphics2D g) {
        // System.out.println(gameState.vectorField);
        if (gameState.vectorField == null) return;
        VectorField vf = gameState.vectorField;
        for (int i=0; i<vf.w; i++) {
            for (int j=0; j<vf.h; j++) {
                double cx = (i+0.5) * vf.cellSize, cy = (j+0.5) * vf.cellSize;
                g.setColor(vectorColor);
                Vector2d f = vf.vf[i][j];
                g.drawLine((int) cx, (int) cy, (int) (f.x + cx), (int) (f.y + cy));
            }
        }
        // System.out.println("Painted FV");
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
