package distance.cc

import distance.util.MarioReader
import utilities.StatSummary

data class ComponentFeatures (val ch: Char?, val width:Int, val height: Int)

data class ComponentSummary(
        val code: Int,
        val ssx: StatSummary = StatSummary("x stats"),
        val ssy: StatSummary = StatSummary("y stats")
) {
    val tileMap = HashMap<Int,Char>()

    init {
        for ((k,v) in MarioReader.tiles)
            tileMap.put(v,k)
    }
    fun add(x: Int, y: Int): ComponentSummary {
        ssx.add(x)
        ssy.add(y)
        return this
    }

    fun getFeatures() = ComponentFeatures(tileMap.get(code), width(), height())

    fun width(): Int {
        return (ssx.max() - ssx.min()).toInt() + 1
    }

    fun height(): Int {
        return (ssy.max() - ssy.min()).toInt() + 1
    }

    fun n() = ssx.n()

    fun area() = width() * height()

    fun density() = (area().toDouble() / ssx.n())

    override fun toString() : String
    {
        // return "cs[$code, ${area()}, ${density()}]"
        return "cs[${tileMap.get(code)}, ${width()}, ${height()}, ${ssx.n()}]"
    }


}

fun main() {
    val cs = ComponentSummary(10)
    for (i in 1 until 10)
        cs.add(i, i)

    println(cs)
}

