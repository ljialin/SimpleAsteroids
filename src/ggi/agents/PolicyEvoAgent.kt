package ggi.agents

import agents.dummy.DoNothingAgent
import ggi.core.AbstractGameState
import ggi.core.SimplePlayerInterface
import java.util.*
import kotlin.collections.ArrayList

data class PolicyEvoAgent(
        var flipAtLeastOneValue: Boolean = true,
        // var expectedMutations: Double = 10.0,
        var probMutation: Double = 0.2,
        var sequenceLength: Int = 200,
        var nEvals: Int = 20,
        var useShiftBuffer: Boolean = true,
        var useMutationTransducer: Boolean = true,
        var repeatProb: Double = 0.5,  // only used with mutation transducer
        var discountFactor: Double = 1.0,
        var opponentModel: SimplePlayerInterface = DoNothingAgent(),
        var initUsingPolicy: Double = 0.8,
        var appendUsingPolicy: Double = 0.8, // only used if using ShiftBuffer, and Policy is not null
        var mutateUsingPolicy: Double = 0.5,
        var policy: SimplePlayerInterface? = null
) : SimplePlayerInterface {

    fun getAgentType(): String {
        return "SimpleEvoPolicyAgent"
    }

    internal var random = Random()

    // these are all the parameters that control the agend
    internal var buffer: IntArray? = null // randomPoint(sequenceLength)

    // SimplePlayerInterface opponentModel = new RandomAgent();
    override fun reset(): SimplePlayerInterface {
        buffer = null
        return this
    }

    val solutions = ArrayList<IntArray>()
    val scores = ArrayList<DoubleArray>()

    var x: Int? = 1

    private fun getActions(gameState: AbstractGameState, playerId: Int): IntArray {
        var solution: IntArray
        // this trickery of aetting a val buffer is to avoid Kotlin's worry that a different
        // thread could modify buffer after the null test
        val buf = buffer
        if (useShiftBuffer) {
            if (buf == null) {
                solution = initialSequence(gameState, playerId)
            }
            else {
                // println("After a shift and append")
                solution = shiftLeftAndAppend(buf, gameState, playerId)
                // println(Arrays.toString(solution))
            }
        } else {
            // System.out.println("New random solution with nActions = " + gameState.nActions())
            solution = initialSequence(gameState, playerId)
        }
        solutions.clear()
        solutions.add(solution)
        scores.clear()
        for (i in 0 until nEvals) {
            // evaluate the current one
            val scoreArrray1 = DoubleArray(solution.size)
            val scoreArrray2 = DoubleArray(solution.size)
            val mut = mutate(solution, probMutation, gameState, playerId)
            val curScore = evalSeq(gameState.copy(), solution, playerId, scoreArrray1)
            val mutScore = evalSeq(gameState.copy(), mut, playerId, scoreArrray2)
            if (mutScore >= curScore) {
                solution = mut
            }
            solutions.add(mut)
            scores.add(scoreArrray1)
            scores.add(scoreArrray2)
        }
        buffer = solution
        return solution
    }

    private fun mutate(v: IntArray, mutProb: Double, gameState: AbstractGameState, playerId: Int): IntArray {
        if (useMutationTransducer) {
            // build it dynamically in case any of the params have changed

            val mt = MutationTransducer(mutProb, repeatProb)
            // save the policy mutation transducer for another day
            return mt.mutate(v, gameState.nActions())
        }
        val n = v.size
        val x = IntArray(n)
        // pointwise probability of additional mutations
        // choose element of vector to mutate
        var ix = random.nextInt(n)
        if (!flipAtLeastOneValue) {
            // setting this to -1 means it will never match the first clause in the if statement in the loop
            // leaving it at the randomly chosen value ensures that at least one bit (or more generally value) is always flipped
            ix = -1
        }
        // copy all the values faithfully apart from the chosen ones
        // advance the game state as we go along
        val gs = gameState.copy()
        for (i in 0 until n) {
            if (i == ix || random.nextDouble() < mutProb) {
                x[i] = mutateValue(v[i], gs, playerId)
            } else {
                x[i] = v[i]
            }
            // advance the game state based on the chosen action
            // gs.next(intArrayOf(x[i]))
            advance(gs, x[i], playerId)

        }
        return x
    }

    private fun mutateValue(cur: Int, gameState: AbstractGameState, playerId: Int): Int {
        // we either go with the policy or mutate to a different action
        val vp = policy
        if (vp != null && random.nextDouble() < mutateUsingPolicy) {
            return vp.getAction(gameState, playerId)
        } else {
            // okay pick a random but different action to the current choice
            // the range is nPossible-1, since we
            // selecting the current value is not allowed
            // therefore we add 1 if the randomly chosen
            // value is greater than or equal to the current value
            val nPossible = gameState.nActions()
            if (nPossible <= 1) return cur
            val rx = random.nextInt(nPossible - 1)
            return if (rx >= cur) rx + 1 else rx
        }
    }

    private fun initialSequence(gameState: AbstractGameState, playerId: Int): IntArray {
        if (random.nextDouble() < initUsingPolicy)
            return policySequence(gameState, playerId)
        else
            return randomSequence(gameState)
    }

    // even this will be initialised from the GameState now
    private fun randomSequence(gameState: AbstractGameState): IntArray {
        val p = IntArray(sequenceLength)
        for (i in p.indices) {
            p[i] = random.nextInt(gameState.nActions())
            // no need to advance the game state in the case of a random sequence
            // gameState.next(intArrayOf(p[i]))
        }
        return p
    }

    private fun policySequence(gameState: AbstractGameState, playerId: Int): IntArray {
        val vp = policy
        // println(policy)
        if (vp == null) return randomSequence(gameState)
        val p = IntArray(sequenceLength)
        val gs = gameState.copy()
        for (i in p.indices) {
            // fix the playerId for now
            p[i] = vp.getAction(gs, 0)
            advance(gs, p[i], playerId)
            // gs.next(intArrayOf(p[i]))
        }
        // println("Policy sequence: " + Arrays.toString(p))
        return p
    }

    private fun shiftLeftAndAppend(v: IntArray, gameState: AbstractGameState, playerId: Int): IntArray {

        val p = IntArray(v.size)
        val gs = gameState.copy()
        for (i in 0 until p.size - 1) {
            p[i] = v[i + 1]
            advance(gs, p[i], playerId)
            // the action has already been taken, so we're processing the new p[i]
        }
        val vp = policy
        if (vp == null || random.nextDouble() >= appendUsingPolicy)
            p[p.size - 1] = random.nextInt(gs.nActions())
        else
            p[p.size - 1] = vp.getAction(gs, 0)
        return p
    }

    private fun advance(gs: AbstractGameState, action: Int, playerId: Int) {
        gs.next(intArrayOf(action, opponentModel.getAction(gs, 1-playerId)) )
    }



    private fun evalSeq(gameState: AbstractGameState, seq: IntArray, playerId: Int, scoreArray: DoubleArray): Double {
        var gameState = gameState
        var currentScore = gameState.score
        var delta = 0.0
        var discount = 1.0
        val actions = IntArray(2)
        var ix = 0
        for (action in seq) {
            actions[playerId] = action
            actions[1 - playerId] = opponentModel.getAction(gameState, 1 - playerId)
            gameState = gameState.next(actions)
            val nextScore = gameState.score
            val tickDelta = nextScore - currentScore
            currentScore = nextScore
            scoreArray[ix++] = currentScore
            delta += tickDelta * discount
            discount *= discountFactor
        }
        return if (playerId == 0)
            delta
        else
            -delta
    }

    override fun toString(): String {
        return "PolicyEvoAgent: $nEvals : $sequenceLength : $opponentModel : $policy"
    }

    override fun getAction(gameState: AbstractGameState, playerId: Int): Int {
        return getActions(gameState, playerId)[0]
    }

    fun getSolutionsCopy(): ArrayList<IntArray> {

        val x = ArrayList<IntArray>()
        x.addAll(solutions)
        return x
    }
}
