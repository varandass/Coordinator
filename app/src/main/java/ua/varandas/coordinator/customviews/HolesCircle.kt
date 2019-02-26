package ua.varandas.coordinator.customviews

import android.graphics.Paint
import ua.varandas.coordinator.models.CircleModel

class HolesCircle(x: Float, y: Float, radius: Float, paint: Paint) : ua.varandas.coordinator.customviews.SimpleCircle(x, y, radius, paint) {
    fun changeRadius() {
        radius = CircleModel.holesD / 2
        radius *= 1.5f
    }

    fun initRadius() {
        radius = CircleModel.holesD / 2
    }
}