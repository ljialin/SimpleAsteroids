package asteroids;

public interface Controller {
    public Action action(AsteroidsGameState game);
    public void setVehicle(Ship ship);
}

