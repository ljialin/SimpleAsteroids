package spinbattle.actuator

import spinbattle.core.Planet
import spinbattle.core.SpinGameState
import spinbattle.params.Constants
import utilities.StatSummary

class HeuristicController : Actuator {

    internal var planetSelected: Int? = null
    internal var playerId: Int = 0

    // we're going to sort actions into a priority list order,
    // and use the evo player to select the nth in the order
    internal var maxActions = 10

    val stats = StatSummary("average")

    internal var doNothing = false

    fun setDoNothing(): HeuristicController {
        doNothing = true
        return this
    }

    fun reset(): HeuristicController {
        planetSelected = null
        return this
    }

    override fun copy(): HeuristicController {
        val copy = HeuristicController()
        copy.playerId = playerId
        copy.planetSelected = planetSelected
        return copy
    }

    fun setPlayerId(playerId: Int): HeuristicController {
        this.playerId = playerId
        return this
    }

    fun nActions(spinGameState: SpinGameState): Int {
        return maxActions
    }


    override fun actuate(action: Int, gameState: SpinGameState): SpinGameState? {
        if (doNothing) {
            println("Doing nothing in actuate")
            return gameState
        }

        // not sure what the purpose of this one is - it disables this type of actuator
        if (gameState.params.transitSpeed == 0.0) {
            return gameState
        }

        // here is the idea: we're going to consider all possible actions

        val actions = ArrayList<PlanetPair>()
        val h = Heuristic()
        for (source in gameState.planets) {
            // only consider if this player owns it, and it is ready for transit
            if (source.ownedBy == playerId && source.transitReady()) {
                for (target in gameState.planets) {
                    val pp = PlanetPair(source, target)
                    actions.add(pp)
                    pp.score = h.score(pp)
                }
            }
        }

        // now sort them in to order
        actions.sortByDescending { t -> t.score }
        stats.add(actions.size)

        if (action < actions.size) {
            // take the action
            with (actions.get(action)) {
                val transit = source.getTransporter() ?: return gameState
                // shift 50%
                // println("Took action: " + this)
                try {
                    transit.setPayload(source, source.shipCount / 2)
                    transit.launch(source.position, target.position, playerId, gameState)
                    transit.setTarget(action)
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Transit = $transit")
                    println("Source  = $source")
                    println("Target  = $target")
                }
            }
        } else {
            // do nothing
        }
        return gameState
    }

    override fun toString(): String {
        return "Ave actions = ${stats.mean()}"
    }
}

data class PlanetPair(var source: Planet, var target: Planet) {
    var score: Double = 0.0
    var shipsToSend = 0
}

class Heuristic (
        var distanceWeight: Double = 1.0,
        var invadeNeutral: Double = 1.0,
        var invadeEnemy: Double = 1.0

){

    fun score(pp: PlanetPair) : Double {
        var score = 0.0
        // rate the quality of each action
        //
        if (pp.target.ownedBy == Constants.neutralPlayer) {
            // rate the strength of the action
        }

        return score
    }
}
