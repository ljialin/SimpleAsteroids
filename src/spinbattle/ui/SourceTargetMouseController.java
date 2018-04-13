package spinbattle.ui;

import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SourceTargetMouseController extends MouseAdapter {

    Integer planetSelected;
    SpinGameState gameState;
    int playerId;


    public SourceTargetMouseController setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    public SourceTargetMouseController setGameState(SpinGameState gameState) {
        this.gameState = gameState;
        return this;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        System.out.println(e);
        // find which one was clicked

        Integer clicked = gameState.proximityMap.getPlanetIndex(e.getX(), e.getY());
        // what happens next depends on ...

        System.out.println("Clicked: " + clicked);
        if (planetSelected != null) {
            if (clicked != null && !planetSelected.equals(clicked)) {
                // this is action -
                // assigning the variable just for naming clarity
                Planet source = gameState.planets.get(planetSelected);
                Planet target = gameState.planets.get(clicked);
                Transporter transit = source.getTransporter();
                // shift 50%
                transit.incPayload(source, source.shipCount / 2);
                transit.launch(source.position, target.position, playerId);
                transit.setTarget(clicked);
            }
            // now reset the selection irrespective of whether an action was launched
            planetSelected = null;
        } else {
            // set the selectewd planet to this one, if it does not already have a ship in transit
            if (clicked != null) {
                Planet source = gameState.planets.get(clicked);
                if (source.transitReady() && source.ownedBy == playerId) {
                    planetSelected = clicked;
                }
            }
        }
        System.out.println("Planet selected: " + planetSelected);
    }

}
