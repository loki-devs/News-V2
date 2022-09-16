package com.example.core.presentation.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }

    protected open fun setupToolbar(
        toolbar: Toolbar,
        title: String? = null,
        isDisplayHomeAsUpEnabled: Boolean = false
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.apply {
                setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled)
                setDisplayShowTitleEnabled(title != null)
                this.title = title
            }
        }
    }

}