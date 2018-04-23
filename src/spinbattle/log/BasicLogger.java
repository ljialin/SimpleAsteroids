package spinbattle.log;


import sound.SoundManager;
import spinbattle.event.LaunchEvent;
import spinbattle.event.SelectPlanetEvent;

/**
 *  The aim of this logging class is to provide a single reference point for
 *  all the logging done in the SpinBattle game.
 *
 *   The Basic Logger handles all communication with the main logging framework
 */
public class BasicLogger {

    static {
        SoundManager.silent = false;
    }

    public static void main(String[] args) throws Exception {
        BasicLogger basicLogger = new BasicLogger();
        for (int i=0; i<10; i++) {
            // basicLogger.logEvent(new LaunchEvent());
            basicLogger.logEvent(new SelectPlanetEvent());
            Thread.sleep(500);
            basicLogger.logEvent(new LaunchEvent());
        }
    }


    static SoundManager soundManager = new SoundManager();

    public BasicLogger logEvent(Object event) {
        // test whether the

        playSound(event);

        return this;
    }

    public BasicLogger playSound(Object event) {
        if (event instanceof LaunchEvent) {
            soundManager.fire();
            System.out.println("Fire!");
        }

        if (event instanceof SelectPlanetEvent) {
            soundManager.playSafe(soundManager.bangLarge);
            System.out.println("SelectPlanet!");
        }
        return this;
    }

}
