package plot;

public interface PlayoutPlotterInterface {
    /*
        just call this to initialise it at the start of a plot routine
         */
    PlayoutPlotterInterface startPlot(int sequenceLength);

    PlayoutPlotterInterface startPlayout(double currentScore);

    PlayoutPlotterInterface addScore(double score);

    PlayoutPlotterInterface plotPlayout();

    PlayoutPlotterInterface reset();
}
