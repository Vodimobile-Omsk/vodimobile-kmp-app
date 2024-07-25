import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vodimobile.android.R
import com.vodimobile.domain.model.Car
import com.vodimobile.presentation.components.PrimaryButton
import com.vodimobile.presentation.components.cars_card.CarSpecif
import com.vodimobile.presentation.components.cars_card.CarsTitle
import com.vodimobile.presentation.theme.VodimobileTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CarInfo(
    onClick: () -> Unit,
    carItem: Car
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(height = 123.dp, width = 341.dp),
            painter = painterResource(id = R.drawable.ca1),
            contentDescription = null
        )
        CarsTitle(
            title = carItem.model,
            subtitle = stringResource(
                R.string.tariff,
                carItem.tariffs.minBy { it.cost }.cost.toInt()
            )
        )

        Text(
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onBackground),
            text = stringResource(R.string.spec_tile)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row {
                CarSpecif(
                    title = stringResource(R.string.transmis),
                    subtitle = carItem.transmission,
                    icon = {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.spec1),
                            contentDescription = null
                        )
                    })
                CarSpecif(
                    title = stringResource(R.string.wheel_drive),
                    subtitle = carItem.wheelDrive,
                    icon = {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.privod),
                            contentDescription = null
                        )
                    })
            }
            Row {
                CarSpecif(title = stringResource(R.string.year_of_release),
                    subtitle = SimpleDateFormat("yyyy", Locale.getDefault()).format(
                        Date(carItem.year ?: 0)
                    ),
                    icon = {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.year),
                            contentDescription = null
                        )
                    })
                CarSpecif(
                    title = stringResource(R.string.tank_value),
                    subtitle = (stringResource(id = R.string.car, carItem.tankValue ?: 0f)),
                    icon = {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.bak),
                            contentDescription = null
                        )}
                )
            }
        }

        PrimaryButton(
            text = stringResource(R.string.to_book),
            enabled = true,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CarInfoPreview() {
    VodimobileTheme(darkTheme = false, dynamicColor = false) {
        CarInfo({}, carItem = Car.empty())
    }
}