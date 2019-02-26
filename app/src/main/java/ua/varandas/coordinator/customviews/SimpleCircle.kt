package ua.varandas.coordinator.customviews

import android.graphics.Paint

open class SimpleCircle(var x: Float, var y: Float, var radius: Float, var paint: Paint){
    private val xMax = x + radius
    private val xMin = x - radius
    private val yMax = y + radius
    private val yMin = y - radius

    fun contains(x: Float, y: Float): Boolean = x in xMin..xMax && y in yMin..yMax
}