package com.yusuf.bankmandiri.newsapps.views.sources

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yusuf.bankmandiri.newsapps.R
import com.yusuf.bankmandiri.newsapps.component.lists.LoadMessage
import com.yusuf.bankmandiri.newsapps.feature.sources.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SourceActivity : AppCompatActivity() {

    private var mSourceJob: Job? = null

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent?.getStringExtra("CATEGORY")
        setContent {
            val sourceViewModel = viewModel<SourceViewModel>()
            val sourceState by sourceViewModel.state.collectAsState()
            val scaffoldState = rememberScaffoldState()
            val swipeState = rememberSwipeRefreshState(isRefreshing = false)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Choose Source")
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
                        mSourceJob = sourceViewModel.findAll(category = category)
                    }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        if (sourceState.isLoading) {
                            item {
                                LoadMessage(message = "Please wait for the sources to finish loading")
                            }
                        } else {
                            if (sourceState.sources.isNullOrEmpty()) {
                                item {
                                    LoadMessage(message = "No Data Found !\n\nYou can swipe down to refresh")
                                }
                            } else {
                                items(items = sourceState.sources.orEmpty()) {
                                    TextButton(
                                        onClick = {
                                            setResult(
                                                200,
                                                Intent()
                                                    .putExtra("CATEGORY", category)
                                                    .putExtra("SOURCE", it.id.orEmpty())
                                            )
                                            finish()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_round_arrow_right_24),
                                            contentDescription = "back"
                                        )
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = it.name.orEmpty(),
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(text = it.category.orEmpty())
                                                Text(text = "Country : ${it.country.orEmpty()}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            LaunchedEffect(key1 = sourceState.messages) {
                val message = sourceState.messages
                if (!message.isNullOrEmpty()) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message,
                        "Error",
                        SnackbarDuration.Long
                    )
                }
            }
            LaunchedEffect(key1 = sourceState.isLoading, block = {
                swipeState.isRefreshing = sourceState.isLoading
            })
            LaunchedEffect(
                key1 = true,
                block = { mSourceJob = sourceViewModel.findAll(category = category) })

        }
    }

}