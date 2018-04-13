package spinbattle.core;

public class Collider {

    // provide a variety of methods to see whether
    // a transit has reached its destination or
    // has collided with another transit


    public Planet getPlanetInRange(SpinGameState gameState, Transporter t) {
        if (gameState.proximityMap != null) {
            return getClosestInRange(gameState, t);
        } else {
            return getTargetInRange(gameState, t);
        }
    }

    public Planet getClosestInRange(SpinGameState gameState, Transporter t) {
        // this one does not depend on having a target, it just gets the closest one
        if (!t.inTransit()) return null;
        Planet closest = gameState.proximityMap.getPlanet(gameState, t.mo.s);
        // if the closest one is the transporter's parent then return null
        if (closest == null) return null;

        if (t.parent == closest.index) return null;
        else return closest;
    }

    public Planet getTargetInRange(SpinGameState gameState, Transporter t) {
        if (t.target != null) {
            Planet target = gameState.planets.get(t.target);
            double dist = t.mo.s.dist(target.position);
            // System.out.println(dist);
            if (dist < target.getRadius()) {
                return target;
            }
        }
        return null;
    }
}
