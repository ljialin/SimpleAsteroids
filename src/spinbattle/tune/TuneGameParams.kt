package spinbattle.tune

import caveswing.design.CaveSwingFitnessSpace
import hyperopt.HyperParamTuneRunner
import ntbea.NTupleBanditEA
import ntbea.NTupleSystem
import utilities.ElapsedTimer


object TuneCaveSwingParams {
    @JvmStatic
    fun main(args: Array<String>) {
        val nEvals = 200
        println("Optimization budget: $nEvals")
        val ntbea = NTupleBanditEA().setKExplore(500.0)

        ntbea.logBestYet = true
        val model = NTupleSystem()
        // set up a non-standard tuple pattern
        model.use1Tuple = true
        model.use2Tuple = true
        model.useNTuple = true

        ntbea.model = model

        val nChecks = 20
        val nTrials = 1

        val timer = ElapsedTimer()

        val runner = HyperParamTuneRunner()
        runner.verbose = true
        //            runner.setLineChart(lineChart);
        runner.nChecks = nChecks
        runner.nTrials = nTrials
        runner.nEvals = nEvals

        // this allows plotting of the independently assessed fitness of
        // the algorithm's best guesses during each run
        // set to zero for fastest performance, set to 5 or 10 to learn
        // more about the convergence of the algorithm
        runner.plotChecks = 0

        val spinBattleSpace = SpinBattleFitnessSpace()
        // uncomment to run the skilful one
        // caveSwingSpace = new CaveSwingGameSkillSpace();
        println("Testing: $ntbea")
        runner.runTrials( ntbea, spinBattleSpace )
        println("Finished testing: $ntbea")
        println("Time for all experiments: $timer")
    }
}

