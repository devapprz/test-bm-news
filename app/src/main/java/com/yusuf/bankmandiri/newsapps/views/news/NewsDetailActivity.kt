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
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.yusuf.bankmandiri.newsapps.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent?.getStringExtra("URL").orEmpty()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar(
                    title = {},
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
                    contentColor = Color.Black
                )
            }) {
                AndroidView(factory = {
                    WebView(it).apply {
                        settings.javaScriptEnabled = true
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        loadUrl(url)
                    }
                }, update = {
                    it.loadUrl(url)
                })
            }
        }
    }

}