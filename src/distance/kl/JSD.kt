package distance.kl

import distance.pattern.PatternDistribution

fun main(args: Array<String>) {

}

class JSD {
    fun div(p: PatternDistribution, q: PatternDistribution, w: Double = 0.5) : Double {
        val m = PatternDistribution()
        m.add(p)
        m.add(q)

        return 0.5 * KLDiv.klDiv(p, m) + 0.5 * KLDiv.klDiv(q,m)
        // return 0.5 * KLDiv.klDiv(m,  p) + 0.5 * KLDiv.klDiv(m, q)

    }
}