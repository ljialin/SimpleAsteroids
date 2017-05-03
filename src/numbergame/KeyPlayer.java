package numbergame;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

import javax.swing.*;

/**
 * Created by simonmarklucas on 30/03/2017.
 */
public class KeyPlayer extends AbstractMultiPlayer{

    public JComponent component;

    KeyController controller;

    public KeyPlayer(JComponent component) {
        this.component = component;
        controller = new KeyController();
        component.addKeyListener(controller);
    }

    public Types.ACTIONS act(StateObservationMulti stateObservationMulti, ElapsedCpuTimer elapsedCpuTimer) {
        return controller.action(stateObservationMulti);
    }
}
