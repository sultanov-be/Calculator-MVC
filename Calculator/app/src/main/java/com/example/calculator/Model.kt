package com.example.calculator

import java.util.*

class Model : Observable() {
    private var intermediateEquation: String = ""
    private var finalResult: String = ""

    private fun notifyChange() {
        setChanged()
        notifyObservers()
    }

    fun getEquation(): String {
        return intermediateEquation
    }

    fun setEquation(nIntermediateResult: String) {
        intermediateEquation = nIntermediateResult
        notifyChange()
    }

    fun getFinalResult(): String {
        return finalResult
    }

    fun setFinalResult(nFinalResult: String) {
        finalResult = nFinalResult
        notifyChange()
    }

    fun reset() {
        intermediateEquation = ""
        finalResult = ""
        notifyChange()
    }
}