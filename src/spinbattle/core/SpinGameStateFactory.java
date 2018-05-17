package spinbattle.core;

import ggi.core.AbstractGameFactory;
import ggi.core.AbstractGameState;
import spinbattle.actuator.Actuator;
import spinbattle.actuator.SourceTargetActuator;
import spinbattle.params.SpinBattleParams;

public class SpinGameStateFactory implements AbstractGameFactory {

    public SpinBattleParams params = new SpinBattleParams();


    public Actuator[] actuators;

    public SpinGameStateFactory() {
        actuators = new Actuator[2];
    }

    @Override
    public AbstractGameState newGame() {
        // params.transitSpeed = 0;
        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();
        actuators[0] = new SourceTargetActuator().setPlayerId(0);
        actuators[1] = new SourceTargetActuator().setPlayerId(1);
        gameState.actuators = actuators;
        // actuators = new Actuator[2];
        // System.out.println("Actuators: " + actuators);
        return gameState;
    }
}
