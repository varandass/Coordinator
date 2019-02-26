package ua.varandas.coordinator.customviews

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import ua.varandas.coordinator.models.CircleModel

class CustomViewManager(private val canvasView: ua.varandas.coordinator.customviews.CanvasView) {

    private val paintWhite = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private val paintWhiteText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f
        textSize = 35f
        typeface = Typeface.MONOSPACE

    }
    private val paintWhiteTextCenter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f
        textSize = 25f
        typeface = Typeface.MONOSPACE

    }
    private val paintBlack = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        pathEffect = DashPathEffect(floatArrayOf(15f, 10f, 2f, 10f), 0.0f)
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    private val paintGreen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    private val xMax = CircleModel.centerX + CircleModel.boltCircleD / 2 + CircleModel.holesD
    private val yMax = CircleModel.centerY + CircleModel.boltCircleD / 2 + CircleModel.holesD
    private val xMin = CircleModel.centerX - CircleModel.boltCircleD / 2 - CircleModel.holesD
    private val yMin = CircleModel.centerY - CircleModel.boltCircleD / 2 - CircleModel.holesD
    private val axialCircle = ua.varandas.coordinator.customviews.AxialCircle(CircleModel.centerX, CircleModel.centerY, CircleModel.boltCircleD / 2, paintBlack)
    private val axialLineX = ua.varandas.coordinator.customviews.AxialLine(xMax, CircleModel.centerY, xMin, CircleModel.centerY, paintBlack)
    private val axialLineY = ua.varandas.coordinator.customviews.AxialLine(CircleModel.centerX, yMax, CircleModel.centerX, yMin, paintBlack)
    private val myTextX = ua.varandas.coordinator.customviews.MyText("X:", 50f, 50f, paintWhiteText)
    private val myTextY = ua.varandas.coordinator.customviews.MyText("Y:", 50f, 100f, paintWhiteText)

    private var holesCircles = arrayListOf<ua.varandas.coordinator.customviews.HolesCircle>()

    init {
        initHolesCircles(CircleModel.arrayXY)
    }

    fun onDraw() {
        canvasView.drawAxialLine(axialLineX)
        canvasView.drawAxialLine(axialLineY)
        canvasView.drawCircle(axialCircle)
        for (circle in holesCircles) {
            canvasView.drawCircle(circle)
        }
        canvasView.restore()
        canvasView.drawText(myTextX)
        canvasView.drawText(myTextY)
    }

    private fun initHolesCircles(arrayXY: ArrayList<Float>) {
        var x = 0f
        var y: Float
        for ((i, v) in arrayXY.withIndex()) {
            if (i % 2 == 0) {
                x = v
                continue
            } else y = v
            holesCircles.add(ua.varandas.coordinator.customviews.HolesCircle(x, y, CircleModel.holesD / 2, paintWhite))
        }
    }

    fun onTouchEvent(x: Float, y: Float) {
        Log.d("Измененные координаты:", "X: $x, Y: $y")
        for ((i, circle) in holesCircles.withIndex()) {
            if (circle.contains(x, y)) {
                Log.d("Диапазон", "${circle.contains(x, y)}")
                circle.changeRadius()
                circle.paint = paintGreen
                myTextX.text = String.format("X${i + 1}: %1$.4f", circle.x)
                myTextY.text = String.format("Y${i + 1}: %1$.4f", circle.y)
                canvasView.reDraw()
            } else {
                circle.initRadius()
                circle.paint = paintWhite
                Log.d("Диапазон", "${circle.contains(x, y)}")
            }
        }
    }
}