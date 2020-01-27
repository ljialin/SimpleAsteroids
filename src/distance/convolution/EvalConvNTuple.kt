package distance.convolution

import evodef.EvolutionLogger
import evodef.FitnessSpace
import evodef.SearchSpace
import evodef.SolutionEvaluator
import java.util.*

class EvalConvNTuple(var nDims: Int, var m: Int) : SolutionEvaluator, SearchSpace, FitnessSpace {
    var logger: EvolutionLogger
    override fun reset() {
        logger.reset()
    }

    override fun optimalIfKnown(): Double {
        return 0.0
    }

    var convNTuple: ConvNTuple? = null
    fun setConvNTuple(convNTuple: ConvNTuple?): EvalConvNTuple {
        this.convNTuple = convNTuple
        return this
    }

    override fun evaluate(x: IntArray): Double { // keep track of whether it is truly optimal
        val fitness = -convNTuple!!.getJSD(x)
        val isOptimal = fitness == 0.0
        logger.log(fitness, x, isOptimal)
        return fitness + noiseLevel * random.nextGaussian()
        // return Math.random();
    }

    override fun optimalFound(): Boolean { // return false for the noisy optimisation experiments in order
// to prevent the optimiser from cheating
        return false
    }

    override fun searchSpace(): SearchSpace {
        return this
    }

    override fun nEvals(): Int {
        return logger.nEvals()
    }

    override fun logger(): EvolutionLogger {
        return logger
    }

    override fun nDims(): Int {
        return nDims
    }

    override fun nValues(i: Int): Int {
        return m
    }

    companion object {
        var random = Random()
        // public static double epsilon = 1e-60;
        var noiseLevel = 0.00001
    }

    init {
        logger = EvolutionLogger()
    }
}
