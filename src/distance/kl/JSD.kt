package distance.kl

import distance.pattern.PatternDistribution

class JSD {
    fun div(p: PatternDistribution, q: PatternDistribution, w: Double = 0.5) : Double {
        val m = PatternDistribution()
        m.add(p)
        m.add(q)

        return 0.5 * KLDiv.klDiv(p, m) + 0.5 * KLDiv.klDiv(q,m)

    }
}