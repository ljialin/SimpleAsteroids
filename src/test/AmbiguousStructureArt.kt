package test

import utilities.JEasyFrame
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Path2D
import java.awt.geom.Rectangle2D
import javax.swing.JComponent

fun main(args: Array<String>) {
    val frame = JEasyFrame(AmbiguousStructureArt(12), "Art Test")
}

class AmbiguousStructureArt(val n: Int = 10) : JComponent() {
    val w = 50
    val h = 50
    val cx = w/2
    val cy = h/2
    override fun getPreferredSize() = Dimension(n * w, n * h)

    override fun paintComponent(go: Graphics?) {
        super.paintComponent(go)
        val g = go as Graphics2D

        for (i in 0 until n) {
            for (j in 0 until n) {
                val t = g.transform
                g.translate(w*i + cx, h * j + cy)
//                val sx = if (i < n/2) 1.0 else - 1.0
//                val sy = if (j < n/2) 1.0 else - 1.0
                val sx = if (i < j) 1.0 else - 1.0
                val sy = if ((n-i) > j) 1.0 else - 1.0


                val rad = n.toDouble()/2
                val dx = (i + 0.5f- rad) / (rad * Math.sqrt(2.0))
                val dy = (j + 0.5f- rad) / (rad * Math.sqrt(2.0))
                val b = 1.0f - 0.5f * Math.sqrt(dx*dx + dy*dy).toFloat()


                g.scale(sx, sy)
                draw(g,b)
                g.transform = t
            }
        }
    }

    // fun brightness()

    val col2 = Color.getHSBColor(0.6f, 1.0f, 1.0f)
    val col3 = Color.getHSBColor(0.9f, 1.0f, 1.0f)

    fun draw(g: Graphics2D, b:Float) {
        val col1 = Color.getHSBColor(0.52f, 1.0f, b)
        g.translate(-cx, -cy)
        val r1 = Rectangle2D.Double(0.0, cy.toDouble(), cx.toDouble(), cy.toDouble())
        g.color = col1
        g.fill(r1)
        val p1 = Path2D.Double()
        p1.moveTo(0.0, 0.0)
        p1.lineTo(w.toDouble(), 0.0)
        p1.lineTo(cx.toDouble(),cy.toDouble())
        p1.lineTo(0.0, cy.toDouble())
        p1.closePath()
        g.color = col2
        g.fill(p1)

        val p2 = Path2D.Double()
        p2.moveTo(w.toDouble(), h.toDouble())
        p2.lineTo(w.toDouble(), 0.0)
        p2.lineTo(cx.toDouble(),cy.toDouble())
        p2.lineTo(cx.toDouble(), h.toDouble() )
        p2.closePath()
        g.color = col3
        g.fill(p2)


    }
}

