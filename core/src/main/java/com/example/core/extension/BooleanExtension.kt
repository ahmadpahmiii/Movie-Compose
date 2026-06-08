package com.example.core.extension

/**
 * Created by Ahmad Pahmi on June 2026
 */

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

fun Boolean?.orTrue(): Boolean {
    return this ?: true
}