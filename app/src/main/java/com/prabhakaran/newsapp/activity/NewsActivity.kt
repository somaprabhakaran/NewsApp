package com.prabhakaran.newsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.databinding.ActivityListBinding
import com.prabhakaran.newsapp.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityNewsBinding>(this,R.layout.activity_news)
        val url :String = intent.getStringExtra("Url").toString()

        binding.webview.webViewClient =  WebViewClient()
        binding.webview.loadUrl(url)
        binding.webview.settings.javaScriptEnabled = true
    }
}