package asteroids;

public interface Controller {
    public Action action(GameState game);
    public void setVehicle(Ship ship);
}

