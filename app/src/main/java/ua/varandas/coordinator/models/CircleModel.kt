package ua.varandas.coordinator.models

import ua.varandas.coordinator.ext.toRad

object CircleModel {


    var arcSpan: Float = 360f
    var incrementAngle: Float = 60f
    var centerX: Float = 0f
    var centerY: Float = 0f
    var boltCircleD: Float = 100f
    var holesD: Float = 10f
    var numberOfHoles: Float = 6f
    var startAngle: Float = 0f

    var arrayX = arrayListOf<Float>()
    var arrayY = arrayListOf<Float>()
    var arrayXY = arrayListOf<Float>()

    fun setIncrementAngle() = arcSpan / numberOfHoles
    fun pointXY() {
        arrayX.clear()
        arrayY.clear()
        arrayXY.clear()
        var iA = 0F
        var i = 1
        while (i <= numberOfHoles) {
            val x = ((Math.cos(toRad(startAngle + iA)) * (boltCircleD / 2)) + centerX)
            val y = ((Math.sin(toRad(startAngle + iA)) * (boltCircleD / 2)) + centerY)
            arrayX.add(x.toFloat())
            arrayY.add(y.toFloat())
            arrayXY.add(x.toFloat())
            arrayXY.add(y.toFloat())
            iA += incrementAngle
            i++
        }
    }
}