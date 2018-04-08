package spinbattle.core;

public class Collider {

    // provide a variety of methods to see whether
    // a transit has reached its destination or
    // has collided with another transit

    public boolean hasArrived(Transporter t) {
        if (t.target != null) {
            return t.mo.s.dist(t.target.position) < t.target.getRadius();
        }
        return false;
    }

    public Planet getPlanetInRange(Transporter t) {
        if (t.target != null) {
            double dist = t.mo.s.dist(t.target.position);
            // System.out.println(dist);
            if (dist < t.target.getRadius()) {
                return t.target;
            }
        }
        return null;
    }
}
