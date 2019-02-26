package ua.varandas.coordinator.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.textColor
import ua.varandas.coordinator.R
import ua.varandas.coordinator.ext.asFloat
import ua.varandas.coordinator.models.CircleModel
import ua.varandas.coordinator.ui.DefineUI

class DefineFragment : Fragment() {
    companion object {
        private const val ARC_DEFAULT = "360"
        private const val TAG = "DefineFragment"
    }

    private lateinit var defineUI: DefineUI
    private lateinit var parseInputToModel: TextWatcher
    private lateinit var arcSpanWatcher: TextWatcher
    private lateinit var numOfHolesWatcher: TextWatcher
    private lateinit var incrementWatcher: TextWatcher

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        Log.d(TAG, "onCreateView")
        defineUI = DefineUI()

        return defineUI.createView(AnkoContext.create(ctx, this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCircleModelText() //Устанавливаем данные с модели в поля
        setupWatchers() //Прослушка EditText
    }

    private fun setCircleModelText() {
        defineUI.apply {
            arcSpan.setText(CircleModel.arcSpan.toString())
            if (CircleModel.arcSpan > 0 && CircleModel.arcSpan < 360) {
                incrementAngle.setText(CircleModel.incrementAngle.toString())
            } else incrementAngle.setText(CircleModel.setIncrementAngle().toString())
            centerX.setText(CircleModel.centerX.toString())
            centerY.setText(CircleModel.centerY.toString())
            boltCircleD.setText(CircleModel.boltCircleD.toString())
            holesD.setText(CircleModel.holesD.toString())
            numberOfHoles.setText(CircleModel.numberOfHoles.toInt().toString())
            startAngle.setText(CircleModel.startAngle.toString())
        }
    }

    private fun setupWatchers() {
        parseInputToModel = object : TextWatcher {           //Стандартная прослушка с обновлением модели
            override fun afterTextChanged(s: Editable?) {
                parseInputToModel()                         //Обновление модели
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        arcSpanWatcher = object : TextWatcher {              //Прослушка для основного угла
            override fun afterTextChanged(s: Editable?) {
                onChangedArcSpan()                             // Перещитываем угол
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        incrementWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (defineUI.arcSpan.hasFocus() || defineUI.numberOfHoles.hasFocus()) {
                    onChangedIncrementAngle(false)
                } else {
                    onChangedIncrementAngle(true)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        }
        numOfHolesWatcher = object : TextWatcher {           //Прослушка изменения количества отверстий
            override fun afterTextChanged(s: Editable?) {

                onChangeNumOfHoles()                        //Перещитываем промежуточный угол

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        defineUI.apply {
            arcSpan.addTextChangedListener(arcSpanWatcher)
            incrementAngle.addTextChangedListener(incrementWatcher)
            centerX.addTextChangedListener(parseInputToModel)
            centerY.addTextChangedListener(parseInputToModel)
            boltCircleD.addTextChangedListener(parseInputToModel)
            holesD.addTextChangedListener(parseInputToModel)
            numberOfHoles.addTextChangedListener(numOfHolesWatcher)
            startAngle.addTextChangedListener(parseInputToModel)
        }
    }

    private fun calculateAndUpdate() {
        parseInputToModel()
        defineUI.incrementAngle.setText("${CircleModel.setIncrementAngle()}")
        parseInputToModel()                                                 //Обновляем модель
    }

    private fun parseInputToModel() {
        CircleModel.arcSpan = defineUI.arcSpan.asFloat()
        CircleModel.incrementAngle = defineUI.incrementAngle.asFloat()
        CircleModel.centerX = defineUI.centerX.asFloat()
        CircleModel.centerY = defineUI.centerY.asFloat()
        CircleModel.boltCircleD = defineUI.boltCircleD.asFloat()
        CircleModel.holesD = defineUI.holesD.asFloat()
        CircleModel.numberOfHoles = defineUI.numberOfHoles.asFloat()
        CircleModel.startAngle = defineUI.startAngle.asFloat()

        doAsync {
            CircleModel.pointXY()
        }
    }

    private fun onChangedArcSpan() {
        defineUI.apply {
            if (arcSpan.asFloat() > 0 && arcSpan.asFloat() < 360) {      //Если основной угол = 0..360
                incAngleVisible(true)
                calculateAndUpdate()                                     //Обновляем модель
            } else {
                removeWatchers()                                        //Иначе удаляем прослушку
                arcSpan.setText(ARC_DEFAULT)                                  //Основной угол ставим 360
                setupWatchers()                                         //Устанавливаем прослушку
                parseInputToModel()                                     //Обновляем модель
                incAngleVisible(false)
                calculateAndUpdate()                                    //Перещитываем промежуточный угол и обновляем модель

            }
        }
    }

    private fun onChangedIncrementAngle(run: Boolean) {
        if (run) {
            removeWatchers()
            parseInputToModel()
            defineUI.arcSpan.setText("${(CircleModel.incrementAngle * CircleModel.numberOfHoles) - CircleModel.incrementAngle}")
            if (defineUI.arcSpan.asFloat() > 360) {
                defineUI.arcSpan.setText(ARC_DEFAULT)
                incAngleVisible(false)
                calculateAndUpdate()
            }
            parseInputToModel()
            setupWatchers()
        }
    }

    private fun onChangeNumOfHoles() {
        if (defineUI.incrementAngle.isEnabled) {
            parseInputToModel()
            removeWatchers()
            defineUI.arcSpan.setText("${CircleModel.incrementAngle * CircleModel.numberOfHoles - CircleModel.incrementAngle}")
            if (defineUI.arcSpan.asFloat() > 360) {
                defineUI.arcSpan.setText(ARC_DEFAULT)
                incAngleVisible(false)
                calculateAndUpdate()
            }
            parseInputToModel()
            setupWatchers()
        } else calculateAndUpdate()
    }

    @Suppress("DEPRECATION")
    private fun incAngleVisible(bool: Boolean) {
        defineUI.incrementAngle.isEnabled = bool
        if (bool) defineUI.incrementAngle.textColor = resources.getColor(R.color.colorText)
        else defineUI.incrementAngle.textColor = resources.getColor(R.color.colorTabText)
    }

    private fun removeWatchers() {
        defineUI.apply {
            arcSpan.removeTextChangedListener(arcSpanWatcher)
            incrementAngle.removeTextChangedListener(incrementWatcher)
            centerX.removeTextChangedListener(parseInputToModel)
            centerY.removeTextChangedListener(parseInputToModel)
            boltCircleD.removeTextChangedListener(parseInputToModel)
            holesD.removeTextChangedListener(parseInputToModel)
            numberOfHoles.removeTextChangedListener(numOfHolesWatcher)
            startAngle.removeTextChangedListener(parseInputToModel)
        }
    }
}

