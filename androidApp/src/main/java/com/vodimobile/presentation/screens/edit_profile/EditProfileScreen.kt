package com.vodimobile.presentation.screens.edit_profile

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vodimobile.App
import com.vodimobile.android.R
import com.vodimobile.presentation.DialogIdentifiers
import com.vodimobile.presentation.screens.edit_profile.components.ProfileField
import com.vodimobile.presentation.screens.edit_profile.components.VodimobileCenterTopAppBar
import com.vodimobile.presentation.screens.edit_profile.store.EditProfileEffect
import com.vodimobile.presentation.screens.edit_profile.store.EditProfileIntent
import com.vodimobile.presentation.screens.edit_profile.store.EditProfileState
import com.vodimobile.presentation.theme.ExtendedTheme
import com.vodimobile.presentation.theme.VodimobileTheme
import kotlinx.coroutines.flow.MutableSharedFlow

@SuppressLint("ComposeMutableParameters")
@Composable
fun EditProfileScreen(
    onEditProfileIntent: (EditProfileIntent) -> Unit,
    editProfileState: State<EditProfileState>,
    editProfileEffect: MutableSharedFlow<EditProfileEffect>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val textModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 32.dp, vertical = 12.dp)

    LaunchedEffect(key1 = Unit) {
        editProfileEffect.collect { effect ->
            when (effect) {
                EditProfileEffect.ClickBack -> {
                    navHostController.navigateUp()
                }

                EditProfileEffect.ClickEditPassword -> {

                }

                is EditProfileEffect.SaveData -> {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = App.INSTANCE.resources.getString(effect.msgResId),
                            actionLabel = if (effect.actionResId > 0) App.INSTANCE.resources.getString(
                                effect.actionResId
                            ) else null,
                            duration = SnackbarDuration.Indefinite
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            onEditProfileIntent(EditProfileIntent.SaveData)
                        }

                        SnackbarResult.Dismissed -> {}
                    }
                }

                EditProfileEffect.ProgressDialog -> {
                    navHostController.navigate(route = DialogIdentifiers.LOADING_DIALOG)
                }

                EditProfileEffect.RemoveProgressDialog -> {
                    navHostController.navigateUp()
                }
            }
        }
    }

    ExtendedTheme {
        Scaffold(
            topBar = {
                VodimobileCenterTopAppBar(
                    onNavBackClick = { onEditProfileIntent(EditProfileIntent.ClickBack) },
                    onActionClick = { onEditProfileIntent(EditProfileIntent.SaveData) })
            },
            containerColor = ExtendedTheme.colorScheme.containerBack,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            modifier = modifier
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 16.dp, vertical = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(id = R.string.your_data),
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                            modifier = textModifier
                        )

                        ProfileField(
                            text = editProfileState.value.fullName,
                            modifier = textModifier,
                            onValueChange = {
                                onEditProfileIntent(EditProfileIntent.EditFullName(fullName = it))
                            },
                            label = stringResource(id = R.string.full_name),
                            enabled = true
                        )

                        ProfileField(
                            modifier = textModifier,
                            onValueChange = {
                                onEditProfileIntent(EditProfileIntent.EditFullName(fullName = it))
                            },
                            label = stringResource(id = R.string.change_password),
                            trailingIcon = {
                                IconButton(
                                    onClick = { onEditProfileIntent(EditProfileIntent.EditPasswordClick) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowRight,
                                        contentDescription = stringResource(id = R.string.change_password)
                                    )
                                }
                            },
                            enabled = false,
                        )

                        ProfileField(
                            text = editProfileState.value.phone,
                            modifier = textModifier,
                            onValueChange = {
                                onEditProfileIntent(EditProfileIntent.EditFullName(fullName = it))
                            },
                            label = stringResource(id = R.string.registration_label_phoneNumber),
                            enabled = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EditProfileScreenDarkPreview() {
    VodimobileTheme(dynamicColor = false) {
        val editProfileViewModel = EditProfileViewModel()
        EditProfileScreen(
            onEditProfileIntent = editProfileViewModel::onIntent,
            editProfileState = editProfileViewModel.editProfileState.collectAsState(),
            editProfileEffect = editProfileViewModel.editProfileEffect,
            navHostController = rememberNavController()
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun EditProfileScreenLightPreview() {
    VodimobileTheme(dynamicColor = false) {
        val editProfileViewModel = EditProfileViewModel()
        EditProfileScreen(
            onEditProfileIntent = editProfileViewModel::onIntent,
            editProfileState = editProfileViewModel.editProfileState.collectAsState(),
            editProfileEffect = editProfileViewModel.editProfileEffect,
            navHostController = rememberNavController()
        )
    }
}