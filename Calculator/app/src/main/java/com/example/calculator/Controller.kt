package com.example.calculator

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList
import net.objecthunter.exp4j.ExpressionBuilder

class Controller(_activity: Activity) : Observer {
    private val mActivity: Activity = _activity
    private val model: Model = Model()

    //TextViews
    private val mainTextView: TextView = mActivity.findViewById(R.id.main_text) //equation
    private val mResultView: TextView = mActivity.findViewById(R.id.sub_text) //final result

    //Buttons
    private val mButtons: ArrayList<Button> = (mActivity.findViewById<View>(R.id.parent_layout))
        .touchables as ArrayList<Button>

    init {
        model.addObserver(this)

        for (button in mButtons) {
            button.setOnClickListener {
                val equation = model.getEquation()

                if (!button.text.contains("=")) model.setEquation(equation + button.text)
                else {
                    model.setEquation(equation)
                    //calculation of equation
                    calculate(equation)
                }
            }
        }

        mActivity.findViewById<Button>(R.id.clear_btn).setOnClickListener { reset() }

        mActivity.findViewById<Button>(R.id.del_btn).setOnClickListener {
            val intEqution = model.getEquation()

            if (intEqution.isNotEmpty()) model.setEquation(
                intEqution.substring(
                    0,
                    intEqution.lastIndex
                )
            )
        }
    }

    override fun update(p0: Observable?, p1: Any?) {
        mainTextView.text = model.getEquation()
        mResultView.text = model.getFinalResult()
        mainTextView.announceForAccessibility(mainTextView.text)
    }

    private fun calculate(equation: String) {
        try {
            val e = ExpressionBuilder(equation).build()
            val result = e.evaluate()


            model.setFinalResult(result.toString())
            mResultView.contentDescription = "Result: 0.0" + mResultView.text
            mResultView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
        } catch (e: IllegalArgumentException) {
            handleError(equation) // bad user input
        } catch (e: EmptyStackException) {
            handleError(equation)
        }
    }

    private fun handleError(reason: String) {
        Log.i("DEBUG", "Invalid input: $reason")
        mainTextView.announceForAccessibility("Error, couldn't parse equation")
        reset()
    }

    private fun reset() {
        mResultView.announceForAccessibility("Equation is cleared")
        model.reset()
    }
}