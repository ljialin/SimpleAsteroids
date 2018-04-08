package spinbattle.core;

public class Collider {

    // provide a variety of methods to see whether
    // a transit has reached its destination or
    // has collided with another transit

//    public boolean hasArrived(Transporter t) {
//        if (t.target != null) {
//            return t.mo.s.dist(t.target.position) < t.target.getRadius();
//        }
//        return false;
//    }


    public Planet getPlanetInRange(SpinGameState gameState, Transporter t) {
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
