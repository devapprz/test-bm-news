package com.yusuf.bankmandiri.newsapps.views.news

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yusuf.bankmandiri.newsapps.R
import com.yusuf.bankmandiri.newsapps.component.lists.LoadMessage
import com.yusuf.bankmandiri.newsapps.feature.news.NewsViewModel
import com.yusuf.bankmandiri.newsapps.utils.CommonConstant
import com.yusuf.bankmandiri.newsapps.utils.toLocalDate
import com.yusuf.bankmandiri.newsapps.views.categories.CategoryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import timber.log.Timber

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private var mNewsJob: Job? = null

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var textFilter by remember {
                mutableStateOf(CommonConstant.DEFAULT_SOURCE)
            }
            val newsViewModel = viewModel<NewsViewModel>()
            val newsState by newsViewModel.state.collectAsState()
            val scaffoldState = rememberScaffoldState()
            val swipeState = rememberSwipeRefreshState(isRefreshing = false)
            val filterLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { result ->
                    if (result.resultCode == 200) {
                        textFilter = result.data?.getStringExtra("SOURCE").orEmpty()
                        mNewsJob = newsViewModel.find(textFilter)
                    }
                }
            )
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "News Apps")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                },
                scaffoldState = scaffoldState
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    SwipeRefresh(
                        modifier = Modifier.fillMaxSize(),
                        state = swipeState,
                        onRefresh = { mNewsJob = newsViewModel.find(textFilter) }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (newsState.isLoading) {
                                item {
                                    LoadMessage(message = "Please wait for the sources to finish loading")
                                }
                            } else {
                                if (newsState.news.isNullOrEmpty()) {
                                    item {
                                        LoadMessage(message = "No Data Found !\n\nYou can swipe down to refresh")
                                    }
                                } else {
                                    items(items = newsState.news.orEmpty()) {
                                        TextButton(
                                            onClick = {
                                                startActivity(
                                                    Intent(
                                                        this@NewsActivity,
                                                        NewsDetailActivity::class.java
                                                    ).putExtra("URL", it.url)
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 16.dp)
                                            ) {
                                                AsyncImage(
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data(it.urlToImage)
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = it.title.orEmpty(),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(180.dp),
                                                    contentScale = ContentScale.FillWidth
                                                )
                                                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                                Text(
                                                    text = it.title.orEmpty(),
                                                    modifier = Modifier.fillMaxWidth(),
                                                )
                                                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = it.source?.name.orEmpty(),
                                                        fontSize = 12.sp,
                                                        color = Color.Gray,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = it.publishedAt.toLocalDate(),
                                                        fontSize = 12.sp,
                                                        color = Color.Gray,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            filterLauncher.launch(
                                Intent(
                                    this@NewsActivity,
                                    CategoryActivity::class.java
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            backgroundColor = Color.Black
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .padding(bottom = 24.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_round_search_24),
                            contentDescription = textFilter,
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Text(text = textFilter)
                    }
                }
            }
            LaunchedEffect(key1 = newsState.messages) {
                val message = newsState.messages
                if (!message.isNullOrEmpty()) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message,
                        "Close",
                        SnackbarDuration.Long
                    )
                }
            }
            LaunchedEffect(key1 = newsState.isLoading, block = {
                swipeState.isRefreshing = newsState.isLoading
            })
            LaunchedEffect(
                key1 = true,
                block = { mNewsJob = newsViewModel.find(textFilter) })
        }
    }

    override fun onDestroy() {
        mNewsJob?.runCatching {
            if (isActive) cancel()
        }?.onFailure {
            Timber.tag("NEWS").d(it.localizedMessage)
        }
        super.onDestroy()
    }

}