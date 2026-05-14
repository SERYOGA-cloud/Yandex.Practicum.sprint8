package com.example.playlistmaker.search.ui.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme


@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.custom_button_corner_radius)),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Text(
            text = text,
            style = LocalTypography.current.buttonText,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun CustomButtonPreview() {
    PlaylistMakerTheme {
        CustomButton("test button") {}
    }
}