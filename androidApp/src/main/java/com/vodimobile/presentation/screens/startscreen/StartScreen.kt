package com.vodimobile.presentation.screens.startscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vodimobile.presentation.components.PrimaryButton
import com.vodimobile.presentation.components.SecondaryButton
import com.vodimobile.presentation.theme.VodimobileTheme
import com.vodimobile.resources.Res
import com.vodimobile.resources.close_button_content_description
import com.vodimobile.resources.login_str
import com.vodimobile.resources.logoapp
import com.vodimobile.resources.requister_str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StartScreen(startScreenViewModel: StartScreenViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 20.dp, alignment = Alignment.Top)
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1.0f))
            IconButton(
                onClick = {
                    startScreenViewModel.onIntent(Intent.CloseClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(resource = Res.string.close_button_content_description)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(20.dp),
        )
        Image(
            painter = painterResource(resource = Res.drawable.logoapp),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 58.dp)
                .size(width = 250.dp, height = 133.33.dp)
        )
        PrimaryButton(
            text = stringResource(resource = Res.string.requister_str),
            enabled = true,
            onClick = {
                startScreenViewModel.onIntent(Intent.ClickRegistration)
            })
        SecondaryButton(
            text = stringResource(resource = Res.string.login_str),
            enabled = true,
            onClick = {
                startScreenViewModel.onIntent(Intent.ClickLogin)
            })
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    VodimobileTheme(dynamicColor = false) {
        Scaffold {
            StartScreen(startScreenViewModel = StartScreenViewModel())
        }
    }
}