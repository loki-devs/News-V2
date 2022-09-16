package com.example.core.common.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.example.core.R
import com.google.android.material.snackbar.Snackbar


fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun ViewGroup?.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this?.context).inflate(layoutRes, this, attachToRoot)
}

@Suppress("DEPRECATION")
fun Context.showErrorSnackBar(
    view: View,
    message: Int,
    action: ((view: View) -> Unit)? = null,
    actionMessage: String? = null,
    duration: Int = Snackbar.LENGTH_LONG
): Snackbar {
    return Snackbar.make(view, message, duration).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getView().setBackgroundColor(resources.getColor(R.color.grapefruit, theme))
        } else {
            getView().setBackgroundColor(resources.getColor(R.color.grapefruit))
        }
        val tvMessage = getView().findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val tvAction = getView().findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        tvMessage.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Caption)
        tvMessage.setTextColor(Color.WHITE)
        tvMessage.maxLines = 5
        tvAction.setTextColor(Color.WHITE)
        action?.let {
            setAction(actionMessage ?: getString(R.string.core_retry), action)
        }
        show()
    }
}

fun View.setSingleClickListener(onSafeClick: (View) -> Unit) {
    val defaultInterval = 2000
    var lastTimeClicked: Long = 0
    val safeClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                return
            }
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeClick(v)
        }
    }
    setOnClickListener(safeClickListener)
}

fun View.asToolbarOrNull(): Toolbar? {
    return try {
        (this as Toolbar)
    } catch (e: Exception) {
        null
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged.invoke(p0.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    })
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}