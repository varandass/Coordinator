package ua.varandas.coordinator.ui

import android.content.res.Configuration
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.*
import ua.varandas.coordinator.R
import ua.varandas.coordinator.fragments.DefineFragment
import ua.varandas.coordinator.models.CircleModel

class DefineUI : AnkoComponent<DefineFragment> {

    lateinit var arcSpan: EditText
    lateinit var incrementAngle: EditText
    lateinit var centerX: EditText
    lateinit var centerY: EditText
    lateinit var boltCircleD: EditText
    lateinit var holesD: EditText
    lateinit var numberOfHoles: EditText
    lateinit var startAngle: EditText


            @Suppress("DEPRECATION")
    override fun createView(ui: AnkoContext<DefineFragment>) = ui.apply {

                val screenSize = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

                        relativeLayout {
            scrollView {
                linearLayout {
                    descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
                    isFocusableInTouchMode = true
                    orientation = LinearLayout.VERTICAL

                    tableLayout {
                        orientation = TableLayout.VERTICAL

                        tableRow {
                            imageView(R.drawable.ic_arcspan) {}
                            textView(R.string.arc_span)
                            arcSpan = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.grad)
                        }

                        tableRow {
                            imageView(R.drawable.ic_incrementangle)
                            textView(R.string.increment_angle)
                            incrementAngle = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                maxLines = 1
                                ems = 3
                                if (CircleModel.arcSpan > 0 && CircleModel.arcSpan < 360) {
                                    isEnabled = true
                                    textColor = resources.getColor(R.color.colorText)

                                } else {
                                    isEnabled = false
                                    textColor = resources.getColor(R.color.colorTabText)
                                }
                                setSelectAllOnFocus(true)

                            }
                            textView(R.string.grad)
                        }

                        tableRow {
                            imageView(R.drawable.ic_centerx)
                            textView(R.string.center_X)
                            centerX = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.mm)
                        }

                        tableRow {
                            imageView(R.drawable.ic_centerx)
                            textView(R.string.center_Y)
                            centerY = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.mm)
                        }

                        tableRow {
                            imageView(R.drawable.ic_boltcircled)
                            textView(R.string.bolt_circle_D)
                            boltCircleD = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.mm)
                        }

                        tableRow {
                            imageView(R.drawable.ic_holesd)
                            textView(R.string.holes_D)
                            holesD = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.mm)
                        }

                        tableRow {
                            imageView(R.drawable.ic_numbersofholes)
                            textView(R.string.number_of_holes)
                            numberOfHoles = editText {
                                inputType = InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.pcs)
                        }

                        tableRow {
                            imageView(R.drawable.ic_startangle)
                            textView(R.string.start_angle)
                            startAngle = editText {
                                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                                textColor = resources.getColor(R.color.colorText)
                                maxLines = 1
                                ems = 3
                                setSelectAllOnFocus(true)
                            }
                            textView(R.string.grad)
                        }
                    }.lparams()
                }
            }
        }.applyRecursively {
            when (it) {
                is TableLayout -> {
                    it.layoutParams = LinearLayout.LayoutParams(wrapContent, matchParent)
                    it.padding = 20
                    it.setColumnStretchable(2, true)
                }
                is ImageView -> {
                    it.layoutParams = TableRow.LayoutParams(dip(60), dip(60)).apply {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                }
                is TextView -> {
                    it.layoutParams = TableRow.LayoutParams().apply {
                        gravity = Gravity.CENTER_VERTICAL
                    }


                    when (screenSize) {
                        Configuration.SCREENLAYOUT_SIZE_SMALL -> it.textSize = 12f
                        Configuration.SCREENLAYOUT_SIZE_NORMAL -> it.textSize = 14f
                        Configuration.SCREENLAYOUT_SIZE_LARGE -> it.textSize = 16f
                        Configuration.SCREENLAYOUT_SIZE_XLARGE -> it.textSize = 16f
                    }
                }
            }
        }

    }.view
}