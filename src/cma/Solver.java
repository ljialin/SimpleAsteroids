package cma;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public interface Solver {
    void setDim(int n);
    void setObjective(IObjectiveFunction fitFun);
    void setMaxEvals(int n);
    double[] run();
}
