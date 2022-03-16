package io.lamart.mocky.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun MainContent(viewModel: MainViewModel, showDetails: (item: Item) -> Unit = {}) =
    with(viewModel) {
        MdcTheme {
            SwipeRefresh(
                state = SwipeRefreshState(isLoading.value),
                onRefresh = refresh,
            ) {
                LazyColumn {
                    val values = items.value

                    items(values.size, { values[it].id }) { index ->
                        when (val value = values[index]) {
                            is Item.Default -> DefaultItem(value, showDetails)
                            is Item.More -> MoreButton(viewModel.loadMore)
                        }
                    }
                }
            }
        }
    }

@Composable
fun MoreButton(loadMore: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { loadMore() }) {
            Text(text = "Load more...")
        }
    }
}

@Composable
fun Loader(isLoading: Boolean) =
    AnimatedVisibility(visible = isLoading) {
        Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.Center) {
            val modifier = Modifier
                .wrapContentWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(Int.MAX_VALUE.dp)
                )

            Card(modifier) {
                CircularProgressIndicator(modifier = Modifier
                    .size(36.dp)
                    .padding(6.dp))
            }
        }
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DefaultItem(item: Item.Default, showDetails: (item: Item.Default) -> Unit) =
    with(item) {
        Column(Modifier.clickable { showDetails(item) }) {
            ListItem(
                text = {
                    Text(
                        text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
            Divider()
        }
    }