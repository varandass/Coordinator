package ua.varandas.coordinator.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import org.jetbrains.anko.*

class MyListAdapter(
        private val listX: List<Float>,
        private val listY: List<Float>,
        private val mCount: Int): BaseAdapter() {
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        return with(parent!!.context){

            relativeLayout {

                val textX = textView {
                    textSize = 18f
                }.lparams {
                    alignParentTop()
                    alignParentLeft()
                    horizontalPadding = 30
                }

                val textY = textView {
                    textSize = 18f
                }.lparams {
                    alignParentTop()
                    alignParentRight()
                    width = parent.width - 300
                    horizontalPadding = 30
                }

                textX.text = String.format("X: %1$.4f", listX[position])
                textY.text = String.format("Y: %1$.4f", listY[position])

            }

        }
    }

    override fun getItem(position: Int): Float {
        return listX[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mCount
    }
}