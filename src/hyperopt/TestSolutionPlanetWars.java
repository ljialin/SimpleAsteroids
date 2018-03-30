package hyperopt;

import planetwar.EvoAgentSearchSpacePlanetWars;
import planetwar.GameState;
import utilities.ElapsedTimer;

public class TestSolutionPlanetWars {
    public static void main(String[] args) {

        // use this code to re-rest a particular point in the search space
        GameState.includeBuffersInScore = true;

        EvoAgentSearchSpacePlanetWars.tickBudget = 2000;

        int[] solution = {2, 1, 0, 0, 2};
        // int[] solution = {1, 0, 0, 0, 4};
        solution = new int[]{3, 1, 1, 0, 3};
        solution = new int[]{3, 1, 1, 0, 2};
        System.out.println(new EvoAgentSearchSpacePlanetWars().report(solution));
        int nChecks = 100;
        ElapsedTimer timer = new ElapsedTimer();
        new HyperParamTuneRunner().runChecks(new EvoAgentSearchSpacePlanetWars(), solution, nChecks);
        System.out.println(timer);
    }
}
