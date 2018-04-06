package spinbattle.view;

import spinbattle.core.SpinGameState;
import spinbattle.params.SpinBattleParams;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SpinBattleView extends JComponent {
    SpinBattleParams params;
    SpinGameState gameState;
    Color bg = Color.black;
    int nStars = 100;

    public SpinBattleView setParams(SpinGameState gameState) {
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
        g.fillRect(0, 0, getWidth(), getHeight());
        paintStars(g);


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
