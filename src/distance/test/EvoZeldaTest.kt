package distance.test

import distance.convolution.ConvMutator
import distance.convolution.ConvNTuple
import distance.convolution.EvalConvNTuple
import distance.util.MarioReader
import evodef.DefaultMutator
import ga.SimplestRMHC
import ntuple.LevelView
import ntuple.tests.EvolveMarioLevelTest
import plot.LineChart
import plot.LineChartAxis
import utilities.ElapsedTimer
import utilities.JEasyFrame
import utilities.StatSummary
import java.awt.Color
import java.util.*
import kotlin.test.assertTrue

fun main(args: Array<String>) {

    val file = "data/zelda/singleroom/ZeldaAllDungeonsNoDoors3TileTypes.json"
    // val file = "data/zelda/singleroom/ZeldaDungeonFixedAll.json"

    val levelObjects = readLevels(file)

    val sample = levelObjects[2]

    val useOneLevel = false

    if (useOneLevel) {
        levelObjects.clear()
        levelObjects.add(sample)
    }

    calcDistances(levelObjects)
    val sampleLevel = levelObjects[0].aa
    val imageWidth = sampleLevel.size
    val imageHeight = sampleLevel[0].size
    val mValues = 3
    val filterWidth = 4
    val filterHeight = 4

    val convNTuple = getConvNTuple(levelObjects, filterWidth, filterHeight)
    convNTuple.jsdWeight = 0.5

    val mutator = DefaultMutator(null)
    mutator.flipAtLeastOneValue = true
    mutator.pointProb = 2.0
    // mutator.totalRandomChaosMutation = true;

    // mutator.totalRandomChaosMutation = true;
    mutator.setSwap(false)

    val evoAlg = SimplestRMHC().setMutator(mutator)

    val convMutator = ConvMutator().setConvNTuple(convNTuple).setForceBorder(true)

    evoAlg.setMutator(convMutator)

    var evaluator: EvalConvNTuple = EvalConvNTuple(imageWidth * imageHeight, mValues)
    evaluator.setConvNTuple(convNTuple)

    val t = ElapsedTimer()
    val solution = evoAlg.runTrial(evaluator, 10000)

    println(t)
    plotData(evoAlg.logger.fa)


    evaluator = EvalConvNTuple(imageWidth*imageHeight, mValues).setConvNTuple(getConvNTuple(levelObjects, 3, 3))
    val fitness = evaluator.evaluate(solution)

    val sampleFit = evaluator.evaluate(levelObjects[0].a)
    LevelView.showMaze(levelObjects[0].a, imageWidth, imageHeight, "Sample: %.2f".format(sampleFit), MarioReader.tileColors)
    LevelView.showMaze(solution, imageWidth, imageHeight, "Fitness = %.2f".format(fitness), MarioReader.tileColors)



}

fun plotData(data: ArrayList<Double>) {
    val lineChart = LineChart.easyPlot(data)
    val mid = (data.size - 1) / 2
    val end = data.size - 2
    println("Endpoint: $end")
    lineChart.xAxis = LineChartAxis(doubleArrayOf(0.0, mid.toDouble(), end.toDouble()))
    lineChart.plotBG = Color.getHSBColor(0.45f, 1.0f, 1.0f)
    val ss = StatSummary().add(data)
    lineChart.yAxis = LineChartAxis(doubleArrayOf(ss.min(), ss.max()))
    lineChart.title = "Evolution of Fitness"
    lineChart.setXLabel("Iterations").setYLabel("Fitness")
    JEasyFrame(lineChart, "JSD Tile Pattern PCG")
}

fun getConvNTuple(levels: ArrayList<Level>, filterWidth: Int, filterHeight: Int): ConvNTuple {
    val convNTuple = ConvNTuple()
    convNTuple.imageWidth = levels[0].aa.size
    convNTuple.imageHeight = levels[0].aa[0].size
    convNTuple.setFilterDimensions(filterWidth, filterHeight)

    convNTuple.makeIndices()
    convNTuple.reset()
    var ix = 0
    for (level in levels) {
        convNTuple.addPoint(level.aa, 1.0)
    }
    convNTuple.sampleDis.printReport()
    return convNTuple
}



