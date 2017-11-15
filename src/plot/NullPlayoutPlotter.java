package plot;

public class NullPlayoutPlotter implements PlayoutPlotterInterface {
    @Override
    public PlayoutPlotterInterface startPlot(int sequenceLength) {
        return this;
    }

    @Override
    public PlayoutPlotterInterface startPlayout(double currentScore) {
        return this;
    }

    @Override
    public PlayoutPlotterInterface addScore(double score) {
        return this;
    }

    @Override
    public PlayoutPlotterInterface plotPlayout() {
        return this;
    }

    @Override
    public PlayoutPlotterInterface reset() {
        return this;
    }
}
