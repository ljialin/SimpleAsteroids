package distance.cc

import distance.util.MarioReader.getAndShowLevel

fun main(args: Array<String>) {
    var inputFile1 = "data/mario/levels/mario-1-1.txt"
    var inputFile2 = "data/mario/levels/mario-8-1.txt"

    val level1 = getAndShowLevel(true, inputFile1)
    val level2 = getAndShowLevel(true, inputFile2)


    val c1 = ConnectedComponentAnalyser().analyseComponents(level1)
    val c2 = ConnectedComponentAnalyser().analyseComponents(level2)

    printSummary(c1!!)
    println()
    printSummary(c2!!)

    val words = "one two three four five six seven eight nine ten".split(' ')
    val frequenciesByFirstChar = words.groupingBy { it.first() }.eachCount()
    println("Counting first letters:")
    println(frequenciesByFirstChar) // {o=1, t=3, f=2, s=2, e=1, n=1}



    var features = c1!!.groupingBy { it.getFeatures() }.eachCount()

    data class Pair(val n:Int, val cf: ComponentFeatures)
    var sorted: ArrayList<Pair> = ArrayList()
    features.forEach { t, u ->  sorted.add(Pair(u, t))}

    sorted.sortBy { it.n }

    sorted.forEach{ println(it) }

    // val sorted = listOf<>( features.forEach((k,v)))




}

fun printSummary(csl: ArrayList<ComponentSummary>) {
    val sorted = csl.sortedBy { it.n() }
    for (cs in sorted) {
        println(cs)
    }


}

