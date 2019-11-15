package spinbattle.tune

import agents.dummy.RandomAgent
import evodef.AnnotatedFitnessSpace
import evodef.EvolutionLogger
import evodef.SearchSpace
import evodef.SearchSpaceUtil
import ggi.agents.SimpleEvoAgent
import ggi.core.AbstractGameState
import ggi.core.SimplePlayerInterface
import gvglink.SpinBattleLinkState
import ntuple.params.DoubleParam
import ntuple.params.IntegerParam
import ntuple.params.Param
import ntuple.params.Report
import spinbattle.actuator.SourceTargetActuator
import spinbattle.core.SpinGameState
import spinbattle.core.SpinGameStateFactory
import spinbattle.params.SpinBattleParams
import spinbattle.players.GVGAIWrapper
import tools.ElapsedCpuTimer
import utilities.ElapsedTimer

class SpinBattleFitnessSpace : AnnotatedFitnessSpace {

    internal var verbose = false

    val gravity = doubleArrayOf(0.0, 1.0, 2.0)
    val nPlanets = intArrayOf(6, 10, 20)
    val height = intArrayOf(400, 600, 800)
    val width = intArrayOf(300, 600, 900)
    val transitSpeed = doubleArrayOf(0.5, 1.0, 2.0)



    internal var nValues = intArrayOf(gravity.size, nPlanets.size, height.size, width.size, transitSpeed.size)
    internal var nDims = nValues.size

    internal var nGames = 1
    internal var maxSteps = 2000

    var logger: EvolutionLogger

    override fun getParams(): Array<Param> {
        return arrayOf(
                DoubleParam().setArray(gravity).setName("Gravity"),
                IntegerParam().setArray(nPlanets).setName("Number of Planets"),
                IntegerParam().setArray(height).setName("Height in pixels"),
                IntegerParam().setArray(width).setName("Width in pixels"),
                DoubleParam().setArray(transitSpeed).setName("Transit Speed")
        )
    }

    init {
        this.logger = EvolutionLogger()
    }

    override fun nDims(): Int {
        return nDims
    }

    override fun nValues(i: Int): Int {
        return nValues[i]
    }

    override fun reset() {
        // nEvals = 0;
        logger.reset()
    }

    fun getParams(x: IntArray): SpinBattleParams {
        // bundle extract the selected params from the solution vector
        // and inject in to the game design params

        // set up the params
        val params = SpinBattleParams()

        params.gravitationalFieldConstant *= gravity[x[gravityIndex]]
        params.nPlanets = nPlanets[x[nPlanetsIndex]]
        params.height = height[x[heightIndex]]
        params.width = width[x[widthIndex]]
        params.transitSpeed *= transitSpeed[x[transitSpeedIndex]]

        params.maxTicks = maxSteps
        return params
    }

    override fun evaluate(x: IntArray): Double {

        val params = getParams(x)
        // using a Game Factory enables the tester to start with a fresh copy of the
        // game each time

        val gameState = SpinGameState().setParams(params).setPlanets()

        gameState.actuators[0] = SourceTargetActuator().setPlayerId(0)
        gameState.actuators[1] = SourceTargetActuator().setPlayerId(1)

        val player1 = SimpleEvoAgent()
        var player2: SimplePlayerInterface = getMCTSAgent(gameState, 1)
        // player2 = RandomAgent()

        val nTicks = 5000
        var i = 0

        val actions = IntArray(2)
        while (i < nTicks && !gameState.isTerminal()) {
            // SpinGameState copy = ((SpinGameState) gameState.copy()).setParams(altParams);
            actions[0] = player1.getAction(gameState.copy(), 0)
            actions[1] = player2.getAction(gameState.copy(), 1);
            gameState.next(actions)
//            println(Arrays.toString(actions))
//            println(i.toString() + "\t "+ gameState.score)
            i++
        }

        // modify this depending on which player we're trying to make win...
        val value = Math.signum(gameState.score)
        logger.log(value, x, false)
//        println("i=${logger.nEvals()}, \t Score = $value")
        return value
    }

    override fun optimalFound(): Boolean {
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

    override fun optimalIfKnown(): Double? {
        return null
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val timer = ElapsedTimer()
            val fitnessSpace = SpinBattleFitnessSpace()
            fitnessSpace.verbose = true
            var point = SearchSpaceUtil.randomPoint(fitnessSpace)

            // point = intArrayOf(1, 0, 1, 0)

            println(Report.report(fitnessSpace, point))
            println()
            println("Size: " + SearchSpaceUtil.size(fitnessSpace))
            println("Value: " + fitnessSpace.evaluate(point))
            println(timer)
        }

        internal var gravityIndex = 0
        internal var nPlanetsIndex = 1
        internal var heightIndex = 2
        internal var widthIndex = 3
        internal var transitSpeedIndex = 4
    }

    fun getMCTSAgent(gameState: AbstractGameState, playerId: Int): GVGAIWrapper {
        val timer = ElapsedCpuTimer()
        val linkState = SpinBattleLinkState(gameState)
        val agent = controllers.multiPlayer.discountOLMCTS.Agent(linkState, timer, playerId)
        return GVGAIWrapper().setAgent(agent)
    }

}
