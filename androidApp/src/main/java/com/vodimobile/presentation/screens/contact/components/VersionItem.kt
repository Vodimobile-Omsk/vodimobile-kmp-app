package com.vodimobile.presentation.screens.contact.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vodimobile.android.BuildConfig
import com.vodimobile.android.R
import com.vodimobile.presentation.BottomAppBarAlpha
import com.vodimobile.presentation.theme.ExtendedTheme
import com.vodimobile.presentation.theme.VodimobileTheme
import java.time.LocalDate.now


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VersionItem() {
    val versionName = BuildConfig.VERSION_NAME
    val startYear = stringResource(id = R.string.version_year_str)
    val currentYear = now().year
    val versionYear = "$startYear-$currentYear"
    ExtendedTheme {
        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = ExtendedTheme.colorScheme.headerBack
            ),
            shape = RectangleShape

        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .size(48.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .size(48.dp),
                        painter = painterResource(id = R.drawable.logo3),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = stringResource(R.string.version_str, versionName),
                        color = ExtendedTheme.colorScheme.hintText
                    )

                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = versionYear + " " + stringResource(id = R.string.version1_str),
                        color = ExtendedTheme.colorScheme.hintText
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun VersionItemPreview() {
    VodimobileTheme {
        VersionItem()
    }

}