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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yusuf.bankmandiri.newsapps.R
import com.yusuf.bankmandiri.newsapps.component.inputs.SearchInput
import com.yusuf.bankmandiri.newsapps.component.lists.LoadMessage
import com.yusuf.bankmandiri.newsapps.feature.news.NewsViewModel
import com.yusuf.bankmandiri.newsapps.utils.CommonConstant
import com.yusuf.bankmandiri.newsapps.utils.toLocalDate
import com.yusuf.bankmandiri.newsapps.views.categories.CategoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isSearch = remember { mutableStateOf(false) }
            var search by remember { mutableStateOf<String?>(null) }
            var source by remember { mutableStateOf(CommonConstant.DEFAULT_SOURCE) }
            val viewModel = viewModel<NewsViewModel>()
            val state by viewModel.state.collectAsState()
            val pageItems = viewModel.findALl(search, source, 4).collectAsLazyPagingItems()
            val scaffoldState = rememberScaffoldState()
            val swipeState = rememberSwipeRefreshState(isRefreshing = false)
            val filterLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { result ->
                    if (result.resultCode == 200) {
                        source = result.data?.getStringExtra("SOURCE").orEmpty()
                    }
                }
            )
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    if (isSearch.value) {
                        SearchInput(isSearch = isSearch) { search = it }
                    } else {
                        TopAppBar(
                            title = { Text(text = "News Apps") },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White,
                            contentColor = Color.Black,
                            actions = {
                                IconButton(onClick = {
                                    isSearch.value = true
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_round_search_24),
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        )
                    }
                },
                scaffoldState = scaffoldState
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    SwipeRefresh(
                        modifier = Modifier.fillMaxSize(),
                        state = swipeState,
                        onRefresh = { pageItems.refresh() }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val loadState = pageItems.loadState
                            swipeState.isRefreshing =
                                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
                            if (loadState.source.refresh is LoadState.Error) {
                                val error = (loadState.source.refresh as LoadState.Error).error
                                viewModel.showError(error)
                            }
                            items(items = pageItems) { news ->
                                if (news != null) {
                                    TextButton(
                                        onClick = {
                                            startActivity(
                                                Intent(
                                                    this@NewsActivity,
                                                    NewsDetailActivity::class.java
                                                ).putExtra(
                                                    "URL",
                                                    news.url
                                                ).putExtra(
                                                    "NAME",
                                                    news.source?.name
                                                )
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
                                                    .data(news.urlToImage)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = news.title.orEmpty(),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(180.dp),
                                                contentScale = ContentScale.FillWidth
                                            )
                                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                            Text(
                                                text = news.title.orEmpty(),
                                                modifier = Modifier.fillMaxWidth(),
                                            )
                                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = news.source?.name.orEmpty(),
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = news.publishedAt.toLocalDate(),
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    LoadMessage(message = "No Data Found !\n\nYou can swipe down to refresh")
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
                            painter = painterResource(id = R.drawable.ic_round_filter_alt_24),
                            contentDescription = source,
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Text(text = source)
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
        }
    }

}