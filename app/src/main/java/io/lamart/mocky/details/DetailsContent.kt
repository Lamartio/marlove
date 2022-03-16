package io.lamart.mocky.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.lamart.mocky.R

@Composable
fun DetailsContent(viewModel: DetailsViewModel) {
    val value = viewModel.details.value

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            Surface {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Header(
                        model = value?.image,
                        width = this@BoxWithConstraints.maxWidth
                    )
                    Title(value?.text ?: "")
                    Property(
                        label = stringResource(R.string.confidence),
                        value = value?.confidence?.toString() ?: ""
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(
    model: String?,
    width: Dp,
    ratio: Float = 9f / 16f,
) =
    AsyncImage(
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .height(width * ratio)
            .fillMaxWidth()
    )

@Composable
private fun Title(text: String) =
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }

@Composable
fun Property(label: String, value: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Divider(modifier = Modifier.padding(bottom = 4.dp))
        Text(
            text = label,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.caption,
        )
        Text(
            text = value,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.body1
        )
    }
}