package hyperopt;

import planetwar.EvoAgentSearchSpace;
import planetwar.GameState;
import utilities.ElapsedTimer;

public class TestSolutionPlanetWars {
    public static void main(String[] args) {

        // use this code to re-rest a particular point in the search space
        GameState.includeBuffersInScore = true;

        EvoAgentSearchSpace.tickBudget = 2000;

        int[] solution = {2, 1, 0, 0, 2};
        // int[] solution = {1, 0, 0, 0, 4};
        solution = new int[]{3, 1, 1, 0, 3};
        solution = new int[]{3, 1, 1, 0, 2};
        System.out.println(new EvoAgentSearchSpace().report(solution));
        int nChecks = 100;
        ElapsedTimer timer = new ElapsedTimer();
        new HyperParamTuneRunner().runChecks(new EvoAgentSearchSpace(), solution, nChecks);
        System.out.println(timer);
    }
}
