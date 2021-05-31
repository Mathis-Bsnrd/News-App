package com.example.fr.news_app.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.fr.news_app.R
import com.example.fr.news_app.base.BaseActivity
import com.example.fr.news_app.databinding.ActivityDetailBinding
import com.example.fr.news_app.util.VideoEnabledWebChromeClient
import com.example.fr.news_app.util.VideoEnabledWebView

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private var webView: VideoEnabledWebView? = null
    private var webChromeClient: VideoEnabledWebChromeClient? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_detail

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideProgress()
        showData()
        setToolbarText("Détails des actualités")
        val url: String = intent.getStringExtra("url")

        initWebView(url)

        binding.webDetail.settings.javaScriptEnabled = true
        binding.webDetail.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        binding.webDetail.loadUrl(url)

    }

    @SuppressLint("InflateParams", "ObsoleteSdkInt")
    private fun initWebView(url: String) {
        val nonVideoLayout: View =
            findViewById(R.id.nonVideoLayout)
        val videoLayout =
            findViewById<ViewGroup>(R.id.videoLayout)
        val loadingView = layoutInflater.inflate(
            R.layout.view_loading_video,
            null
        )

        binding.progressbar.visibility = View.VISIBLE
        binding.webDetail.visibility = View.GONE

        webChromeClient = object : VideoEnabledWebChromeClient(
            nonVideoLayout, videoLayout, loadingView, webView
        ) {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    binding.progressbar.visibility = View.GONE
                    binding.webDetail.visibility = View.VISIBLE
                }
            }
        }
        (webChromeClient as VideoEnabledWebChromeClient).setOnToggledFullscreen { fullscreen ->
            if (fullscreen) {
                val attrs = window.attributes
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                window.attributes = attrs
                if (Build.VERSION.SDK_INT >= 14) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
                }
            } else {
                val attrs = window.attributes
                attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                attrs.flags =
                    attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
                window.attributes = attrs
                if (Build.VERSION.SDK_INT >= 14) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
        }
        binding.webDetail.webChromeClient = webChromeClient
        binding.webDetail.webViewClient = InsideWebViewClient()
        binding.webDetail.loadUrl(url)
    }

    private class InsideWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}