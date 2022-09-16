package com.example.news

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.category.presentation.CategoryActivity
import com.example.core.common.extension.startActivityLeftTransition
import com.example.core.presentation.base.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        navigateToCatgory()
    }

    private fun navigateToCatgory() {
        lifecycleScope.launch {
            val intent = Intent(this@SplashActivity, CategoryActivity::class.java)
            delay(500)
            startActivityLeftTransition(intent)
            finish()
        }
    }
}