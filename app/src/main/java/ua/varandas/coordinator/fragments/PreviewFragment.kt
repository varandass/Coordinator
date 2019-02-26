package ua.varandas.coordinator.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import ua.varandas.coordinator.ui.PreviewUI


class PreviewFragment : Fragment() {
    companion object {
        private const val TAG = "PreviewFragment"
    }

    private lateinit var previewUI: PreviewUI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        previewUI = PreviewUI()
        return previewUI.createView(AnkoContext.create(ctx, this))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, "Видимость: $isVisibleToUser")
        if (isVisibleToUser){
            val activity = activity
            activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commitAllowingStateLoss ()
        }
    }
}
