package ua.varandas.coordinator.ui

import android.view.Gravity
import android.widget.ListView
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.listView
import org.jetbrains.anko.verticalLayout
import ua.varandas.coordinator.fragments.OutputFragment
import ua.varandas.coordinator.models.CircleModel

class OutputUI : AnkoComponent<OutputFragment> {
    private lateinit var listView: ListView

    override fun createView(ui: AnkoContext<OutputFragment>) = ui.apply {

        verticalLayout {

            listView = listView {}.lparams { gravity = Gravity.CENTER_HORIZONTAL }
            listView.adapter = ua.varandas.coordinator.adapters.MyListAdapter(CircleModel.arrayX, CircleModel.arrayY, CircleModel.numberOfHoles.toInt())
        }

    }.view

}