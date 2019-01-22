package distance.cc

import java.util.ArrayList

class ConnectedComponentAnalyser {

    val processed = -1
    data class Point(val x: Int, val y:Int)
    val neighbours = arrayListOf<Point>(
            Point(-1,0),
            Point(1, 0),
            Point(0,-1),
            Point(0, 1)
            )

    fun analyseComponents(level: Array<IntArray>): ArrayList<ComponentSummary>? {
        val components = ArrayList<ComponentSummary>()
        for (i in 0 until level.size) {
            for (j in 0 until level[i].size) {
                if (level[i][j] != processed) {
                    val code = level[i][j]
                    val cs = ComponentSummary(code)
                    components.add(cs)
                    analyse(cs, level, i, j)
                }
            }
        }
        return components
    }

    fun analyse(cs: ComponentSummary, level: Array<IntArray>, x: Int, y: Int ) {

        // quit if off grid
        if (!(x in 0 until level.size) || !(y in 0 until level[0].size)) return

        // quit if already processed
        if (level[x][y] == processed) return

        // quit if it is not of this type
        if (level[x][y] != cs.code) return

        // ok, new point to process
        // add this one, then process the others
        cs.add(x,y)
        level[x][y] = processed

        // iterate over the set of neighbours
        for (p in neighbours) {
            analyse(cs, level,x+p.x, y+p.y)
        }

    }
}
