package com.example.core.common.extension

import android.app.Activity
import android.content.Intent
import com.example.core.R

fun Activity.slideInLeftTransition() {
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.slideInRightTransition() {
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}

fun Activity.startActivityLeftTransition(intent: Intent) {
    startActivity(intent)
    slideInLeftTransition()
}