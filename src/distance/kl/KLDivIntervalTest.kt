package distance.kl

import distance.pattern.Pattern
import distance.pattern.PatternDistribution

fun main(args: Array<String>) {

    val p = PatternDistribution()
    val q = PatternDistribution()

    val p1 = Pattern().setPattern(intArrayOf(0))
    val p2 = Pattern().setPattern(intArrayOf(1))
    val p3 = Pattern().setPattern(intArrayOf(2))
    val p4 = Pattern().setPattern(intArrayOf(3))
    p.add(p1)
    p.add(p2)
    p.add(p3)
    // p.add(p4)

    q.add(p2)
    q.add(p3)
    q.add(p4)

    println(KLDiv.klDiv(p, p))
    println(KLDiv.klDiv(p, q))
    println()
    println(KLDiv.klDiv(q, p))
    println(KLDiv.klDiv(q, q))

    println()
    println(KLDiv.klDivSymmetric(p, q))
    println(KLDiv.klDivSymmetric(q, p))


}