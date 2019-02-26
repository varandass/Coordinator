package ua.varandas.coordinator.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import ua.varandas.coordinator.ui.OutputUI

class OutputFragment : Fragment() {
    companion object {
        private const val TAG = "OutputFragment"
    }

    private lateinit var outputUI: OutputUI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, "onCreateView")
        outputUI = OutputUI()

        return outputUI.createView(AnkoContext.create(ctx, this))
        }
    }



