package com.mvpsales.github.ui

data class Rule(
    val predicate: () -> Boolean,
    val action: (Double, Double) -> Double
)
