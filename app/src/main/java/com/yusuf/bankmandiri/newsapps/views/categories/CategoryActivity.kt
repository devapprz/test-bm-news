package com.yusuf.bankmandiri.newsapps.views.categories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yusuf.bankmandiri.newsapps.R
import com.yusuf.bankmandiri.newsapps.feature.categories.CategoryViewModel
import com.yusuf.bankmandiri.newsapps.views.sources.SourceActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import timber.log.Timber

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private var job: Job? = null

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val activityResult = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { result ->
                    val code = result.resultCode
                    if (code == 200) {
                        setResult(code, result.data)
                        finish()
                    }
                }
            )
            val viewModel = viewModel<CategoryViewModel>()
            val state by viewModel.state.collectAsState()
            val scaffoldState = rememberScaffoldState()
            val swipeState = rememberSwipeRefreshState(isRefreshing = false)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Choose Categories")
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
                        contentColor = Color.Black
                    )
                },
                scaffoldState = scaffoldState
            ) {
                SwipeRefresh(
                    state = swipeState,
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = {
                        job = viewModel.findAll()
                    }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        if (state.categories.isNullOrEmpty()) {
                            val message = if (state.isLoading) {
                                "Please wait for the category to finish loading"
                            } else {
                                "No Data Found !\n\nYou can swipe down to refresh"
                            }
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 70.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = message,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        } else {
                            items(items = state.categories.orEmpty()) {
                                TextButton(
                                    onClick = {
                                        activityResult.launch(
                                            Intent(
                                                this@CategoryActivity,
                                                SourceActivity::class.java
                                            ).putExtra("CATEGORY", it.name)
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_round_arrow_right_24),
                                        contentDescription = "back"
                                    )
                                    Text(
                                        text = it.name,
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier
                                            .weight(weight = 1f)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            LaunchedEffect(key1 = state.messages) {
                val message = state.messages
                if (!message.isNullOrEmpty()) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message,
                        "Close",
                        SnackbarDuration.Long
                    )
                }
            }
            LaunchedEffect(key1 = state.isLoading, block = {
                swipeState.isRefreshing = state.isLoading
            })
            LaunchedEffect(key1 = true, block = { job = viewModel.findAll() })
        }
    }

    override fun onDestroy() {
        job?.runCatching {
            if (isActive) cancel()
        }?.onFailure {
            Timber.tag("CATEGORY").d(it.localizedMessage)
        }
        super.onDestroy()
    }

}