package com.example.game2048

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var gameManager: GameManager? = null
    private val paint = Paint()

    private fun getTileColor(value: Int): Int {
        return when (value) {
            0 -> Color.parseColor("#FCF2D4")
            2 -> Color.parseColor("#FADE9D")
            4 -> Color.parseColor("#F3CCCD")
            8 -> Color.parseColor("#E9AEA0")
            16 -> Color.parseColor("#D1C4D5")
            32 -> Color.parseColor("#BFD8DC")
            64 -> Color.parseColor("#99B6A0")
            128 -> Color.parseColor("#9DA075")
            256 -> Color.parseColor("#FDBC68")
            512 -> Color.parseColor("#D57A5D")
            1024 -> Color.parseColor("#B47A77")
            2048 -> Color.parseColor("#9FB6AE")
            else -> Color.parseColor("#B69DBA")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val gm = gameManager ?: return

        val cellSize = width / gm.size
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = cellSize / 3f

        for (i in 0 until gm.size) {
            for (j in 0 until gm.size) {
                val value = gm.board[i][j]

                paint.color = getTileColor(value)

                canvas.drawRect(
                    (j * cellSize).toFloat(),
                    (i * cellSize).toFloat(),
                    ((j + 1) * cellSize).toFloat(),
                    ((i + 1) * cellSize).toFloat(),
                    paint
                )

                if (value != 0) {
                    paint.color = Color.BLACK
                    canvas.drawText(
                        value.toString(),
                        (j * cellSize + cellSize / 2).toFloat(),
                        (i * cellSize + cellSize / 1.5).toFloat(),
                        paint
                    )
                }
            }
        }
    }
}
