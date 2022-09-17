package com.example.article.presentation.detailarticle

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import com.example.article.R
import com.example.article.databinding.ActivityDetailArticleBinding
import com.example.core.common.extension.gone
import com.example.core.common.extension.viewBinding
import com.example.core.common.extension.visible
import com.example.core.presentation.base.BaseActivity

class DetailArticleActivity : BaseActivity() {

    private val url: String
        get() = intent?.extras?.getString("EXTRA_URL", "").toString()
    private val binding by viewBinding(ActivityDetailArticleBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        bindData()
    }

    private fun initView() {
        title = getString(R.string.article_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun bindData() {
        with(binding) {
            wvArticle.loadUrl(url)
            wvArticle.requestFocus(View.FOCUS_DOWN)
            wvArticle.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            wvArticle.settings.javaScriptEnabled = true

            wvArticle.webViewClient = MyWebViewClient()
            wvArticle.webChromeClient = MyWebChromeClient()
            wvArticle.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> if (!v.hasFocus()) {
                        v.requestFocus()
                    }
                }
                false
            }

            wvArticle.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    if (progress > 50) {
                        binding.viewErrorHandling.llErrorHandling.gone()
                    }
                }
            }

            viewErrorHandling.btnRetry.setOnClickListener {
                wvArticle.loadUrl(url)
            }
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {}
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.viewLoading.clLoading.visible()
            binding.viewErrorHandling.llErrorHandling.gone()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.viewLoading.clLoading.gone()
        }

        @SuppressWarnings("deprecation")
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            binding.viewLoading.clLoading.gone()
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_TIMEOUT || errorCode == ERROR_CONNECT) {
                binding.viewErrorHandling.llErrorHandling.visible()
                binding.viewErrorHandling.ivError.setImageResource(com.example.core.R.drawable.ic_no_wifi)
                binding.viewErrorHandling.tvErrorMessage.text = getString(com.example.core.R.string.core_connection_error)

            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView,
            req: WebResourceRequest,
            rerr: WebResourceError
        ) {
            onReceivedError(view, rerr.errorCode, rerr.description.toString(), req.url.toString())
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.wvArticle.canGoBack()) {
            binding.wvArticle.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}