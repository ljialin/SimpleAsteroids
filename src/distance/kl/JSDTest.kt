package distance.kl

import java.util.*

fun main(args: Array<String>) {
    val p = doubleArrayOf(0.0, 0.5, 0.5)
    val q = doubleArrayOf(0.5, 0.5, 0.0)
    println(jsDiv(p,q))
}
// assumes that p and q are already normalised
fun jsDiv (p: DoubleArray, q: DoubleArray) : Double {
    val m = DoubleArray(p.size)
    for (i in 0 until p.size)
        m[i] = (p[i] + q[i]) / 2
    println(Arrays.toString(m))
    return 0.5 * (klDiv(p, m) + klDiv(q,m) )
}
val eps = 1e-10
fun klDiv(p: DoubleArray, q:DoubleArray) : Double {
    var tot = 0.0
    for (i in 0 until p.size) {
        tot += p[i] * Math.log((p[i] + eps) / (q[i] + eps))
    }
    return tot / Math.log(2.0)
}

