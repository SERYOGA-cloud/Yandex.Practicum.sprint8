package com.example.playlistmaker.search.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.blue

@Composable
fun CustomProgressIndicator() {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.progress_bar_padding_top)),
            color = blue,
            strokeWidth = 4.dp
        )
    }
}

@Preview
@Composable
fun CustomProgressIndicatorPreview() {
    CustomProgressIndicator()
}