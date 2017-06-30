package paired;

import evodef.EvalMaxM;
import evodef.NoisySolutionEvaluator;
import utilities.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonmarklucas on 29/06/2017.
 *
 */

public class PowerOfDifferencePairsTest {

    // okay this is interesting: the paired idea does not work well when the
    // vectors are far apart

    // this might have been expected from the way that having the
    // sliding history window too big causes deterioration in performance

    public static void main(String[] args) {


        // create the random vectors, score them
        // and put them in a list

        // now run an experiment each way to determine the arg max
        // and then evaluate the quality of that

        ScoredVectorLearner meanLearner = new MeanLearner();
        ScoredVectorLearner diffLearner = new PairedDifferenceLearner();
        ScoredVectorLearner[] learners = new ScoredVectorLearner[]{meanLearner, diffLearner};

        List<ProblemInstance> problems = new ArrayList<>();
        int nTrials = 10;
        int n=100, m = 2;
        double noise = 1.0;
        NoisySolutionEvaluator evaluator = new EvalMaxM(n,m, noise);

        int k = 30;
        LineChart lineChart = new LineChart();
        for (int i=0; i<nTrials; i++) {
            // ProblemInstance problem = new ProblemInstance(n, m, k, evaluator).useRandomVecs();
            ProblemInstance problem = new ProblemInstance(n, m, k, evaluator).useVecsAroundRandomPoint();

            int ix = 0;
            for (ScoredVectorLearner learner : learners) {

                System.out.println("Testing: " + learner.getClass().getSimpleName());
                int[] p = learner.learn(problem.scoredVecs, problem.evaluator);
                // System.out.println(Arrays.toString(p));
                System.out.println("True fitness is: " + evaluator.trueFitness(p));

                System.out.println();
                // now show evolution of fitness
//                for (double x : learner.getFitness()) {
//                    System.out.println(x);
//                }
                Color color = ix++ % 2 == 0 ? Color.red : Color.blue;
                LinePlot linePlot = new LinePlot().setData(learner.getFitness()).setColor(color);
                lineChart.addLine(linePlot);
            }
        }
        new JEasyFrame(lineChart,"Fitness v. vectors processes");
    }
}
