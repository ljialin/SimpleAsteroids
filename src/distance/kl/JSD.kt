package distance.kl

import distance.pattern.PatternDistribution

fun main(args: Array<String>) {

}

class JSD {
    fun div(p: PatternDistribution, q: PatternDistribution, w: Double = 0.5) : Double {
        val m = PatternDistribution()
        m.mixIn(p, w)
        m.mixIn(q, w)
        // m.printReport()
        return w * KLDiv.klDiv(p, m) + (1-w) * KLDiv.klDiv(q,m)
        // return 0.5 * KLDiv.klDiv(m,  p) + 0.5 * KLDiv.klDiv(m, q)

    }
}
