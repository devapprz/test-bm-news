package com.yusuf.bankmandiri.newsapps.views.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yusuf.bankmandiri.newsapps.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent?.getStringExtra("URL").orEmpty()
        val name = intent?.getStringExtra("NAME").orEmpty()
        var mWebView: WebView? = null
        setContent {
            var isLoading by remember { mutableStateOf(true) }
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar(
                    title = {
                        Text(text = name)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Image(
                                painter = painterResource(R.drawable.ic_round_arrow_back_ios_24),
                                contentDescription = "back"
                            )
                        }
                    },
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    actions = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.Black,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 4.dp)
                            )
                        } else {
                            IconButton(onClick = {
                                mWebView?.reload()
                                isLoading = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_round_refresh_24),
                                    contentDescription = "Refresh"
                                )
                            }
                        }
                    }
                )
            }) {
                AndroidView(factory = {
                    val webView = WebView(it).apply {
                        settings.javaScriptEnabled = true
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                if (view?.progress == 100) {
                                    isLoading = false
                                }
                            }
                        }
                        loadUrl(url)
                    }
                    mWebView = webView
                    webView
                }, update = {
                    it.loadUrl(url)
                })
            }
        }
    }

}