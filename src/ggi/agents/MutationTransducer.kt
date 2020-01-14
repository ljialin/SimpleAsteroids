package ggi.agents


import java.util.*

fun main(args: Array<String>) {
    val range = 3
    val n = 20
    val mt = MutationTransducer(repeatProb = 0.5)

    val nSteps = 20

    var output = mt.repSeq(n, 9)

    for (i in 0 until nSteps) {
        println(i)
        output.forEach { print(it) }
        println()
        output = mt.mutate(output, range)
    }
}

data class MutationTransducer (var mutProb: Double = 0.2, var repeatProb: Double = 0.5){

    val random = Random()

    fun mutate(input: IntArray, range: Int) : IntArray {
        val output = IntArray(input.size)
        // now copy across the input

        for (i in 0 until input.size) {
            val p = random.nextDouble()

            if (p < mutProb) {
                // mutate
                output[i] = random.nextInt(range)

            } else if (p < mutProb + repeatProb && i>0) {
                output[i] = output[i-1]
            }
            else {
                // faithful copy
                output[i] = input[i]

            }

        }

        return output

    }

    fun randSeq(n: Int, range: Int) : IntArray {
        return IntArray(n, {x -> random.nextInt(range)})
    }
    fun repSeq(n: Int, v: Int) : IntArray {
        return IntArray(n, {x -> v})
    }
}

