package distance.test

import com.google.gson.Gson
import distance.convolution.ConvNTuple
import distance.kl.JSD
import distance.kl.KLDiv
import distance.test.KLDivWaveFunctionTest.PairListComponent
import distance.test.KLDivWaveFunctionTest.RatedLevelView
import utilities.JEasyFrame
import utilities.StatSummary
import utilities.Stats
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

val useReducedTileset = false
val filterWidth = 3
val filterHeight = 3

// val div = JSD()::div





fun main(args: Array<String>) {

    val gson = Gson()
    // val file = "data/zelda/singleroom/ZeldaDungeonFixedAll.json"
    val file = "data/zelda/singleroom/ZeldaAllDungeonsNoDoors3TileTypes.json"



    println(ArrayList<Integer>().javaClass)

    val arrayLists = gson.fromJson(FileReader(file), ArrayList<ArrayList<ArrayList<Int>>>().javaClass)

    println(arrayLists.size)


    val a1 = intArrayOf(1, 2)
    val a2 = intArrayOf(1, 2)
    val aa1 = Array<IntArray>(1) { a1 }
    val aa2 = Array(1) { a2 }

    val flatSet = HashSet<IntArray>()
    // flatSet.has
    flatSet.add(a1)
    flatSet.add(a2)
    println("Flatset size: ${flatSet.size}")

    println(Arrays.hashCode(aa1))
    println(Arrays.hashCode(aa2))

    val levels = ArrayList<Array<IntArray>>()
    for (raw in arrayLists)
        levels.add(toLevelArray(raw))

    if (useReducedTileset)
        for (aa in levels) useReducedTileset(aa, zeldaHash)


    // val levelSet = HashSet<Array<IntArray>>()
    val codeSet = HashSet<Int>()
    val levelSet = HashSet<Level>()
    val levelObjects = ArrayList<Level>()
    for (t in levels) {
        levelSet.add(Level(t))
        codeSet.add(Level(t).hashCode())
        levelObjects.add(Level(t))
    }
    println("Level object hashset size = ${levelSet.size}")

//    println(codeSet.size)
//
//    println(zeldaHash.get(0))

    calcDistances(levelObjects)

    calcDivergences(levelObjects)


    // val sortedList = list.sortedWith(compareBy(Person::age, Person::name))
    Collections.sort(levelObjects)

    val levelObjectsSorted = levelObjects.sortedBy{t -> t.div}


    for (level in levelObjectsSorted) {
       //  LevelView.showMaze(level.aa, level.toString())
    }



    var ix = 0
    for (lo in levelObjectsSorted) {
         println("${ix++} \t ${lo}")
    }

    val div = DoubleArray(levelObjects.size)
    val dis = DoubleArray(levelObjects.size)
    for (i in 0 until div.size) {
        div[i] = levelObjects[i].div
        dis[i] = levelObjects[i].ss.mean()
    }
    println("Correlation = ${Stats.correlation(dis,div)}")


    var index = 0
    val plc = PairListComponent()
    for (ratedLevel in levelObjectsSorted) {
        val title = String.format("%d\t %.2f", ++index, ratedLevel.div)
        val rlv = RatedLevelView(ratedLevel.aa, title)
        plc.add(rlv)
    }
    JEasyFrame(plc, "Rate Zelda Levels")


}

fun calcDivergences(levels: ArrayList<Level>) {
    val convNTuple = ConvNTuple()
    convNTuple.imageWidth=levels[0].aa.size
    convNTuple.imageHeight=levels[0].aa[0].size
    convNTuple.setFilterDimensions(filterWidth, filterHeight)

    println("Making indices")

    convNTuple.makeIndices()
    println("Made indices")
    convNTuple.reset()
    println("Reset conv nTuple")

    // now add them all
    var ix = 0
    for (level in levels) {
        println("Adding level ${++ix}")
        convNTuple.addPoint(level.aa, 1.0)
    }

    for (level in levels) {
        level.div = convNTuple.getJSD(level.aa)
        // level.div = convNTuple.getKLDivergence(level.aa)
    }

    convNTuple.sampleDis.printReport()

}

// not used any more - input levels have already have reduced tile set
val zeldaHash = hashMapOf<Int,Int>(
        0 to 0,
        1 to 1, 2 to 1, 3 to 1, 4 to 1, 5 to 1,
        6 to 2, 7 to 2, 8 to 2
)

fun calcDistances(levels: ArrayList<Level>) {
    val range = 0 until levels.size
    for (i in range) {
        for (j in range) {
            if (i!=j) levels[i].ss.add(levelDist(levels[i], levels[j]))
        }
    }
}

fun toLevelArray(raw: ArrayList<ArrayList<Int>>): Array<IntArray> {

    val w = raw.size
    val h = raw.get(0).size
    val array = Array(w) { IntArray(h) }
    for (i in 0 until w) {
        for (j in 0 until h) {
            array[i][j] = raw[i][j]
        }
    }
    return array
}

fun levelDist(a : Level, b:Level) : Int {
    var tot = 0
    // return sum
    for (i in 0 until a.a.size) {
        tot += if (a.a[i] == b.a[i]) 0 else 1
    }
    return tot
}

fun useReducedTileset(aa: Array<IntArray>, map: HashMap<Int,Int>) : Array<IntArray> {
    for (i in 0 until aa.size) {
        for (j in 0 until aa[0].size) {
            val x:Int? = map.get(aa[i][j])
            aa[i][j] =
                    if (x == null) {
                        println("Null x: $x")
                        println(aa[i][j])
                        0
                    } else x
        }
    }
    return aa
}

class Level(val aa: Array<IntArray>) : Comparable<Level> {

    val ss = StatSummary("Distances")

    val a: IntArray
    var div = 0.0

    init {
        a = ConvNTuple.forceFlatten(aa)
    }

    override fun hashCode() = Arrays.hashCode(a)

    override fun equals(other: Any?): Boolean {
        if (!(other is Level)) return false
        val level = other
        for (i in 0 until a.size) {
            if (a[i] != level.a[i]) return false
        }
        return true
    }

    override fun compareTo(other: Level): Int {
        return ss.mean().compareTo(other.ss.mean())
    }

    override fun toString() : String {

        return "${ss.n()} \t ${ss.min().toInt()}\t ${"%.1f".format(ss.mean())}\t ${"%.2f".format(div)}"
    }
}

