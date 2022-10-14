package com.yusuf.bankmandiri.newsapps.component.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yusuf.bankmandiri.newsapps.R
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchInput(
    isSearch: MutableState<Boolean>,
    textSearch: MutableState<String?>,
    delay: Long = 2000,
    onClose: () -> Unit = {},
    onChange: (String) -> Unit
) {
    var timer by remember { mutableStateOf<Timer?>(null) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                isSearch.value = false
                onClose()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_search_off_24),
                contentDescription = "Close"
            )
        }
        OutlinedTextField(
            value = textSearch.value.orEmpty(),
            onValueChange = {
                textSearch.value = it
                timer?.cancel()
                timer = Timer()
                timer?.schedule(
                    object : TimerTask() {
                        override fun run() {
                            onChange(it)
                        }
                    },
                    delay
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .weight(1f)
                .padding(end = 16.dp)
                .focusRequester(focusRequester = focusRequester),
            placeholder = {
                Text(text = "Search . . .")
            },
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                IconButton(onClick = { textSearch.value = "" }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_clear_24),
                        contentDescription = "Clear"
                    )
                }
            }
        )
    }
    LaunchedEffect(key1 = focusRequester) {
        delay(300)
        keyboardController?.hide()
        focusRequester.requestFocus()
    }
}