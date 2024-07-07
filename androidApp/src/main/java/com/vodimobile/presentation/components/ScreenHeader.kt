package com.vodimobile.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vodimobile.presentation.theme.VodimobileTheme
import com.vodimobile.resources.Res
import com.vodimobile.resources.header_back_content_desc
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateBack: () -> Unit,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(resource = Res.string.header_back_content_desc),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1.5f))
    }
}

@Preview
@Composable
private fun PageHeaderPreview() {

    VodimobileTheme {

        Surface(
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            ScreenHeader(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(resource = Res.string.header_back_content_desc),
                onNavigateBack = {}
            )
        }
    }
}
