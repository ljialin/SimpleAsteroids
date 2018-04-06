package spinbattle.core;

import spinbattle.params.SpinBattleParams;
import spinbattle.util.MovableObject;

/**
 *  Interesting class: we model both the playerId of the ships,
 *  and the parent planet of the ship.
 *
 *  This allows the possbility to assign ownership to the invasion
 *  based either on the playerId, or the owner of the planet which
 *  may have changed since launching the Transporter
 */

public class Transporter {
    Planet parent;
    SpinBattleParams params;
    int playerId;
    double payLoad;

    MovableObject mo;

    public Transporter next() {
        mo.update();
        return this;
    }
}
