package spinbattle.actuator

import spinbattle.core.Planet
import spinbattle.core.SpinGameState
import spinbattle.params.Constants
import utilities.StatSummary

class HeuristicActuator : Actuator {

    internal var planetSelected: Int? = null
    internal var playerId: Int = 0

    // we're going to sort actions into a priority list order,
    // and use the evo player to select the nth in the order
    internal var maxActions = 10

    val stats = StatSummary("average")

    internal var doNothing = false

    fun setDoNothing(): HeuristicActuator {
        doNothing = true
        return this
    }

    fun reset(): HeuristicActuator {
        planetSelected = null
        return this
    }

    override fun copy(): HeuristicActuator {
        val copy = HeuristicActuator()
        copy.playerId = playerId
        copy.planetSelected = planetSelected
        return copy
    }

    fun setPlayerId(playerId: Int): HeuristicActuator {
        this.playerId = playerId
        return this
    }

    fun nActions(spinGameState: SpinGameState): Int {
        return maxActions
    }

    val verbose = true

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

        if (verbose) {
            // then print out the actions
            for (pp in actions) {
                println(pp)
            }
        }

        stats.add(actions.size)

        if (action < actions.size) {
            // take the action
            with (actions.get(action)) {
                // get the transit if available, otherwise return the game state without taking any action
                val transit = source.getTransporter() ?: return gameState
                // shift 50%
                // println("Took action: " + this)
                try {
                    transit.setPayload(source, source.shipCount * ratio)
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

data class PlanetPair(var source: Planet, var target: Planet, var ratio: Double = 0.5) {
    var score: Double = 0.0
    var shipsToSend = 0
}

class Heuristic (
        var distanceWeight: Double = 1.0,
        var invadeNeutral: Double = 1.0,
        var invadeEnemy: Double = 2.0,
        var planetSizeFactor: Double = 1.0,
        var pairDistanceFactor: Double = 0.01
){

    fun score(pp: PlanetPair) : Double {
        var score = 0.0
        // rate the quality of each action
        //
        if (pp.target.ownedBy == Constants.neutralPlayer) {
            // rate the strength of the action
            score += invadeEnemy
        } else if (pp.target.ownedBy != pp.source.ownedBy) {
            // this is an enemy planet: how much do we want to invade enemies
            score += invadeEnemy
        }

        // knock off the distance factor - time would be wasted in transit
        score -= pairDistanceFactor * pp.source.position.dist(pp.target.position)
        return score
    }
}
