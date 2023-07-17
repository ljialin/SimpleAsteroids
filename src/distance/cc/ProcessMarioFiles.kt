package distance.cc

import distance.util.MarioReader
import distance.util.deepCopy
import java.io.File

data class LevelDescriptor (
        val path: String,
        val level: Array<IntArray>,
        val featureMap: Map<ComponentFeatures,Int>
) {
    fun printOut() {
        println(path)
        data class Pair(val n:Int, val cf: ComponentFeatures)
        var sorted: ArrayList<Pair> = ArrayList()
        featureMap.forEach { t, u ->  sorted.add(Pair(u, t))}
        sorted.sortBy { it.n }
        sorted.forEach{ println(it) }
    }
}

fun main() {
    val path = "data/mario/levels/"
    val file = File(path)
    val levelList = ArrayList<LevelDescriptor>()
    file.walk().forEach {
        println(it)
        if (it.path.endsWith(".txt")) {
            val level = MarioReader.getAndShowLevel(true, it.path)
            // connected component analysis destroys the level passed to it so operate on a copy
            val levelCopy = deepCopy(level)
            val components = ConnectedComponentAnalyser().analyseComponents(levelCopy)
            val featureMap = components!!.groupingBy { it.getFeatures() }.eachCount()
            levelList.add(LevelDescriptor(it.path, level, featureMap))
        }
    }
    levelList.forEach {
        it.printOut()
        println()
    }
}
