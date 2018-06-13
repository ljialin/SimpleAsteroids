package sgraph;

import ggi.core.AbstractGameState;

public class Graph implements AbstractGameState {

    // the arcs define the connectivity and never change
    // once the graph is set up
    int[][] arcs;

    // the labels change and must be deep copied
    int[] labels;

    int nSteps = 0;
    GraphParams params;

    public int[] getNeighbours(int i) {
        return arcs[i];
    }

    public Graph setParams(GraphParams params) {
        this.params = params;
        return this;
    }

    public Graph setRandomLabels() {
        labels = new int[params.nNodes()];
        for (int i=0; i<labels.length; i++) {
            labels[i] = params.random.nextInt(params.nColors);
        }
        return this;
    }


    @Override
    public AbstractGameState copy() {
        Graph copy = new Graph();
        copy.arcs = arcs;
        copy.labels = new int[labels.length];
        this.nSteps = nSteps;
        return this;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        return null;
    }

    @Override
    public int nActions() {
        return 0;
    }

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    public void nextLabel(int i) {
        labels[i] = (labels[i] + 1) % params.nColors;
    }
}
