package ua.varandas.coordinator.ui

import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.custom.customView
import org.jetbrains.anko.verticalLayout
import ua.varandas.coordinator.fragments.PreviewFragment

class PreviewUI : AnkoComponent<PreviewFragment> {
    lateinit var canvasView: ua.varandas.coordinator.customviews.CanvasView
    override fun createView(ui: AnkoContext<PreviewFragment>) = ui.apply {
        verticalLayout {
            canvasView = customView {
                id = ua.varandas.coordinator.IDs.CANVAS_VIEW
            }
        }
    }.view
}