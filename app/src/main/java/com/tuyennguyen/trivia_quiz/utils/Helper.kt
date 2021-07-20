package com.tuyennguyen.trivia_quiz.utils

import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_SHORT, @ColorInt backgroundColor: Int) {
    val snack = Snackbar.make(this, message, length)
    snack.setBackgroundTint(backgroundColor)
    snack.show()
}


