package spinbattle.tune

import ggi.agents.SimpleEvoAgent
import spinbattle.actuator.SourceTargetActuator
import spinbattle.core.SpinGameState
import spinbattle.params.Constants
import spinbattle.params.SpinBattleParams
import spinbattle.players.TunablePriorityLauncher
import spinbattle.view.SpinBattleView
import utilities.JEasyFrame
import java.awt.Point
import java.util.*


fun main(args: Array<String>) {
    // to always get the same initial game
    val seed = Random().nextLong()
    // seed = -6330548296303013003L;
    println("Setting seed to: $seed")
    SpinBattleParams.random = Random(seed)
    // SpinBattleParams.random = new Random();

    val space = SpinBattleFitnessSpace()

    // previous evolved example
    val solution = intArrayOf(2, 2, 1, 1, 1)
    val params = space.getParams(solution)
    params.height *= 3
    params.width *= 3
    // params.nPlanets /=2
    params.maxGrowth /= 2
    params.minGrowth /= 2
    val gameState = SpinGameState().setParams(params).setPlanets()


    // set up the actuator
    gameState.actuators[0] = SourceTargetActuator().setPlayerId(0)

    // gameState.actuators[0] = HeuristicController().setPlayerId(0)

    // gameState.actuators[1] = new SourceTargetActuator().setPlayerId(1);
    val evoAgent = SimpleEvoAgent()

    // SimplePlayerInterface player2 = getEvoAgent();
    val randomPlayer = agents.dummy.RandomAgent()
    // evoAgent = randomPlayer;

    // but now we also need to establish a player

    val view = SpinBattleView().setParams(params).setGameState(gameState)
    // HeuristicLauncher launcher = new HeuristicLauncher();
    val launcher = TunablePriorityLauncher()
    val title = "Spin Battle Game"
    val frame = JEasyFrame(view, "$title: Waiting for Graphics")
    frame.location = Point(800, 0)
    //        MouseSlingController mouseSlingController = new MouseSlingController();
    //        mouseSlingController.setGameState(gameState).setPlayerId(Constants.playerOne);
    //        CaveView.addMouseListener(mouseSlingController);
    val launchPeriod = 5 // params.releasePeriod;
    waitUntilReady(view)
    val actions = IntArray(2)

    val frameDelay = 40

    val falseParams = params.copy()

    // may want to stop before the end of the game for demo purposes
    val nTicks = 5000
    var i = 0

    while (i < nTicks && !gameState.isTerminal) {
        // SpinGameState copy = ((SpinGameState) gameState.copy()).setParams(altParams);
        actions[0] = evoAgent.getAction(gameState.copy(), 0)
        // actions[0] = falsePlayer.getAction(gameState.copy(), 0);
        // actions[1] = player2.getAction(gameState.copy(), 1);
        // actions[0] = randomPlayer.getAction(gameState.copy(), 0);
        // actions[1] = randomPlayer.getAction(gameState.copy(), 1);
        // System.out.println(i + "\t " + actions[0]);
        gameState.next(actions)
        // mouseSlingController.update();
        // launcher.makeTransits(gameState, Constants.playerOne);
        if (i % launchPeriod == 0)
            launcher.makeTransits(gameState, Constants.playerTwo)
        val viewCopy = gameState.copy() as SpinGameState
        viewCopy.logger = gameState.logger
        view.setGameState(viewCopy)
        view.repaint()
        frame.title = "$title : $i" //  + " : " + CaveView.getTitle());
        println(gameState.actuators[0])
        Thread.sleep(frameDelay.toLong())
        i++
    }
    println(gameState.isTerminal)
    val trajTitle = String.format("g = %.3f, spd = %.3f", params.gravitationalFieldConstant, params.transitSpeed)
    // logger.showTrajectories(params.width, params.height, trajTitle);
    // System.out.println("nTraj: " + logger.getTrajectoryLogger().trajectories.size());
}

@Throws(Exception::class)
internal fun waitUntilReady(view: SpinBattleView) {
    val i = 0
    while (view.nPaints == 0) {
        // System.out.println(i++ + " : " + CaveView.nPaints);
        Thread.sleep(50)
    }
}

