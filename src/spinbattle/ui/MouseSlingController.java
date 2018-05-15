package spinbattle.ui;

import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseSlingController extends MouseAdapter {
    Integer planetSelected;
    SpinGameState gameState;
    int playerId;


    public MouseSlingController setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    public MouseSlingController setGameState(SpinGameState gameState) {
        this.gameState = gameState;
        return this;
    }

    int shipCount = 0;
    // boolean pressed = false;

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        Integer clicked = gameState.proximityMap.getPlanetIndex(e.getX(), e.getY());

        // System.out.println("Clicked: " + clicked);
        if (planetSelected != null) {
            if (clicked != null && !planetSelected.equals(clicked)) {
                // this is action -
                // assigning the variable just for naming clarity
                Planet source = gameState.planets.get(planetSelected);
                Planet target = gameState.planets.get(clicked);
                Transporter transit = source.getTransporter();
                // shift 50%
                transit.setPayload(source, source.shipCount / 2);
                transit.launch(source.position, target.position, playerId, gameState);
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
                    gameState.notifySelection(source);
                }
            }
        }
    }

    int nShipsPerTick = 1;

    public MouseSlingController update() {
        // this long winded code is due to the fact that
        // the update may be operating on a different copy of the Planet
        // and hence potentially a different Transit, so we need to identify it by its int id

        // every time there is a planet selected, we need to increment its transit count
        if (planetSelected != null) {
            Planet source = gameState.planets.get(planetSelected);
            Transporter transit = source.getTransporter();
            // shift nShips per unit
            transit.incPayload(source, nShipsPerTick);
            // System.out.println("Incrementing: " + transit.payload);
        }
        return this;
    }

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        // if we'd selected a planet, we now need to launch the transit
        if (planetSelected != null) {
            Planet source = gameState.planets.get(planetSelected);
            Transporter transit = source.getTransporter();
            transit.directionalLaunch(source.position, source.rotation, playerId);
            gameState.notifyLaunch(transit);

        }
        // after a mouse release this is always set to null
        planetSelected = null;
    }
}
