package asteroids;

/**
 * Created by simonlucas on 30/05/15.
 */
public class RotateAndShoot implements Controller {

    Ship ship;

    Action action;

    public RotateAndShoot() {
        action = new Action();
    }

    public Action action(AsteroidsGameState game) {
        action.thrust = 2.0;
        action.shoot = true;
        action.turn = 1;

        return action;
    }

    public void setVehicle(Ship ship) {
        // just in case the ship is needed ...
        this.ship = ship;
    }
}
