package spinbattle.params;

public interface Constants {

    int playerOne = 0;
    int playerTwo = 1;
    int neutralPlayer = 2;

    // might move this to params
    // since radius has an effect on game play
    int growthRateToRadius = 250;

    // moved radSep in to params: it is an interesting thing to evolve
    // double radSep = 1.5;

    int minTransitRadius = 7;
}
