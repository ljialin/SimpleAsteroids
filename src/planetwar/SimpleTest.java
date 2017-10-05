package planetwar;

import utilities.JEasyFrame;

import java.util.Random;

public class SimpleTest {

    public static void main(String[] args) throws Exception {
        GameState game = new GameState().setNPlanets(10).setRandomOwnerships().setRandomGrowthRates();

        PlanetWarView view = new PlanetWarView(game);
        JEasyFrame frame = new JEasyFrame(view, "Test View");
        KeyController controller = new KeyController();
        frame.addKeyListener(controller);

        // now p;ay
        Random random = new Random();
        for (int i=0; i<1000; i++) {
            int p1 = random.nextInt(GameState.nActions);

            p1 = controller.action(game);

            int p2 = random.nextInt(GameState.nActions);
            int[] a = new int[]{p1, p2};
            game.next(a);
            // game.update();
            view.update(game);
            Thread.sleep(100);
        }
    }
}
