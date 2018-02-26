package evodef;

public interface EvolutionListener {

    void update(EvolutionLogger logger, int[] solution, double fitness);

}
