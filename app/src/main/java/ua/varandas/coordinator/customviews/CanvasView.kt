@file:Suppress("DEPRECATION")

package ua.varandas.coordinator.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.View
import ua.varandas.coordinator.models.CircleModel

class CanvasView(context: Context) : View(context) {
    companion object {
        private const val TAG = "CanvasView"
    }

    private lateinit var canvas: Canvas
    private var widthX: Int = 0
    private var heightY: Int = 0
    private val max = CircleModel.boltCircleD + CircleModel.holesD + 15
    private val customViewManager = ua.varandas.coordinator.customviews.CustomViewManager(this)

    init {
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        this.canvas = canvas
        widthX = canvas.width
        heightY = canvas.height
        canvas.save()
        canvas.drawColor(Color.parseColor("#0171d9"))
        canvasTranslate(canvas)
        customViewManager.onDraw()

    }

    fun reDraw() {
        Log.d(TAG, "reDraw")
        invalidate()
    }

    fun drawCircle(circle: ua.varandas.coordinator.customviews.SimpleCircle) {
        Log.d(TAG, "drawCircle")
        canvas.drawCircle(circle.x, circle.y, circle.radius, circle.paint)
    }

    fun drawAxialLine(axialLine: ua.varandas.coordinator.customviews.AxialLine) {
        Log.d(TAG, "drawAxialLine, X1: ${axialLine.x1}, Y1: ${axialLine.y1}, X2: ${axialLine.x2}, Y2: ${axialLine.y2}")
        canvas.drawLine(axialLine.x1, axialLine.y1, axialLine.x2, axialLine.y2, axialLine.paint)
    }

    fun drawText(myText: ua.varandas.coordinator.customviews.MyText) {
        Log.d(TAG, "drawText, X: ${myText.x}, Y: ${myText.y}")
        canvas.drawText(myText.text, myText.x, myText.y, myText.paint)
    }

    fun restore() {
        canvas.restore()
    }

    private fun canvasTranslate(canvas: Canvas) {
        Log.d(TAG, "canvasTranslate")
        if (widthX < heightY) {
            canvas.translate(widthX / 2f, heightY / 2f)
            canvas.scale(widthX / max, -widthX / max)
            canvas.translate(-CircleModel.centerX, -CircleModel.centerY)
        } else {
            canvas.translate(widthX / 2f, heightY / 2f)
            canvas.scale(heightY / max, -heightY / max)
            canvas.translate(-CircleModel.centerX, -CircleModel.centerY)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val dx: Float
        val dy: Float
        if (widthX < heightY) {
            dx = (x - widthX / 2f) / (widthX / max) + CircleModel.centerX
            dy = -(y - heightY / 2f) / (widthX / max) + CircleModel.centerY
        } else {
            dx = (x - widthX / 2f) / (heightY / max) + CircleModel.centerX
            dy = -(y - heightY / 2f) / (heightY / max) + CircleModel.centerY
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d("onTouchEvent", "X: $x, Y: $y")
            customViewManager.onTouchEvent(dx, dy)
        }
        return true
    }

}
