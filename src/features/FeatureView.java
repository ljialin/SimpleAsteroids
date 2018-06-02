package features;

import asteroids.Action;
import battle.SimpleBattleState;
import battle.SimpleShip;
import math.Vector2d;
import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;

public class FeatureView extends JComponent {

    public static void main(String[] args) throws Exception {
        SimpleBattleState state = new SimpleBattleState();
        FeatureView fv = new FeatureView().setBattleState(state);
        new JEasyFrame(fv, "Feature CaveView");

        while(true) {
            Thread.sleep(50);
            asteroids.Action action = new Action(0, 1, false);
            state.ships[0].update(action);
            AgentCentric ac = new AgentCentric(state.ships[0].s, state.ships[0].d);

            Vector2d tmp = ac.transform(state.ships[1].s);
            int r = (int) tmp.mag();
            int theta = (int) (180 * tmp.theta() / Math.PI);
            System.out.println(r + "\t " + theta);
            fv.repaint();
        }

    }

    int w=700, h=700;

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }
    SimpleBattleState state;

    FeatureView setBattleState(SimpleBattleState state) {

        this.state = state;
        return this;

    }

    public void paintComponent(Graphics og) {
        Graphics2D g = (Graphics2D) og;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        // the point of this class is to have an agent-centric CaveView of the game
        // in order to get a good q-learning agent ...
        if (state == null) return;
        SimpleShip ship1 = state.ships[0];
        SimpleShip ship2 = state.ships[1];
        AgentCentric ac = new AgentCentric(ship1.s, ship1.d);

        Vector2d s1 = ac.transform(ship1.s);

        g.translate(getWidth()/2, getHeight()/2);
        SimpleShip ship = new SimpleShip(s1, new Vector2d(0, 0), new Vector2d(0, -1));
        g.setColor(Color.blue);
        ship.draw(g);

        Vector2d s2 = ac.transform(ship2.s);

        // just put a dot there
        g.setColor(Color.green);
        int r = 10;
        g.fillOval((int) s2.x-r, (int) s2.y-r, 2*r, 2*r);

    }
}
