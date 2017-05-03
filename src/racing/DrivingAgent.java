package racing;

import math.Vector2d;

import java.util.List;

/**
 * Created by sml on 12/09/2016.
 */



public interface DrivingAgent {

    public Action getAction(Track track, Car you, List<Car> opponents);

    interface Track {
        // values show whether pixels is traversable or not
        // other representations also possible
        int[][] getPixmap();
    }

    interface Car {
        // deviation from velocity vector
        double getHeadingDeviationAngle();
        Vector2d getVelocity();
        Vector2d getPosition();
    }

    interface Action {
        double getAccel();
        double getSteer();
    }

}
