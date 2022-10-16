package com.yusuf.bankmandiri.newsapps.views.sources

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yusuf.bankmandiri.newsapps.R
import com.yusuf.bankmandiri.newsapps.component.inputs.SearchInput
import com.yusuf.bankmandiri.newsapps.feature.sources.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent?.getStringExtra("CATEGORY")
        setContent {
            val isSearch = remember { mutableStateOf(false) }
            var search by remember { mutableStateOf<String?>(null) }
            val viewModel = viewModel<SourceViewModel>()
            val state by viewModel.state.collectAsState()
            val pageItems = viewModel.findAll(category, search).collectAsLazyPagingItems()
            val scaffoldState = rememberScaffoldState()
            val swipeState = rememberSwipeRefreshState(isRefreshing = false)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    if (isSearch.value) {
                        SearchInput(isSearch = isSearch) { search = it }
                    } else {
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
                SwipeRefresh(
                    state = swipeState,
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = { pageItems.refresh() }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        val loadState = pageItems.loadState
                        swipeState.isRefreshing =
                            loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
                        if (loadState.source.refresh is LoadState.Error) {
                            val error = (loadState.source.refresh as LoadState.Error).error
                            viewModel.showError(error)
                        }
                        items(items = pageItems) {
                            it?.also { data ->
                                TextButton(
                                    onClick = {
                                        setResult(
                                            200,
                                            Intent().putExtra("SOURCE", data.name)
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
                                            text = data.name.orEmpty(),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = data.category.orEmpty())
                                            Text(text = "Country : ${data.country.orEmpty()}")
                                        }
                                    }
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
        }
    }

}