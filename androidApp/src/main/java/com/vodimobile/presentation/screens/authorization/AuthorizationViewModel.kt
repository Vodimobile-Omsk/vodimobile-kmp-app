package com.vodimobile.presentation.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vodimobile.domain.model.User
import com.vodimobile.domain.repository.hash.HashRepository
import com.vodimobile.domain.storage.data_store.UserDataStoreStorage
import com.vodimobile.domain.storage.supabase.SupabaseStorage
import com.vodimobile.presentation.screens.authorization.store.AuthorizationEffect
import com.vodimobile.presentation.screens.authorization.store.AuthorizationIntent
import com.vodimobile.presentation.screens.authorization.store.AuthorizationState
import com.vodimobile.presentation.utils.validator.PasswordValidator
import com.vodimobile.presentation.utils.validator.PhoneNumberValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthorizationViewModel(
    private val phoneNumberValidator: PhoneNumberValidator,
    private val passwordValidator: PasswordValidator,
    private val dataStoreStorage: UserDataStoreStorage,
    private val supabaseStorage: SupabaseStorage,
    private val hashRepository: HashRepository
) : ViewModel() {

    val authorizationState = MutableStateFlow(AuthorizationState())
    val authorizationEffect = MutableSharedFlow<AuthorizationEffect>()
    private val supervisorIOCoroutineContext = Dispatchers.IO + SupervisorJob()
    private var getFromJob: Job = Job()

    fun onIntent(intent: AuthorizationIntent) {
        when (intent) {
            AuthorizationIntent.OpenUserAgreement -> {
                viewModelScope.launch {
                    authorizationEffect.emit(AuthorizationEffect.OpenUserAgreement)
                }
            }

            AuthorizationIntent.SmsVerification -> {
                viewModelScope.launch {
                    authorizationEffect.emit(AuthorizationEffect.SmsVerification)
                }
            }

            AuthorizationIntent.ReturnBack -> {
                viewModelScope.launch {
                    authorizationEffect.emit(AuthorizationEffect.ReturnBack)
                }
            }

            is AuthorizationIntent.PhoneNumberChange -> {
                viewModelScope.launch {
                    val isValidPhoneNumber = validatePhoneNumber(intent.value)
                    authorizationState.update {
                        it.copy(
                            phoneNumber = intent.value,
                            phoneNumberError = !isValidPhoneNumber
                        )
                    }
                }
            }

            is AuthorizationIntent.PasswordChange -> {
                viewModelScope.launch {
                    val isValidPassword = validatePassword(intent.value)
                    authorizationState.update {
                        it.copy(
                            password = intent.value,
                            passwordError = !isValidPassword
                        )
                    }
                }
            }

            AuthorizationIntent.AskPermission -> {
                getFromJob = viewModelScope.launch(supervisorIOCoroutineContext) {
                    withContext(context = viewModelScope.coroutineContext) {
                        authorizationEffect.emit(AuthorizationEffect.ShowLoadingDialog)
                    }
                    with(authorizationState.value) {
                        dataStoreStorage.editPassword(password = password)
                    }
                    getFrom()
                }
            }

            AuthorizationIntent.RememberPassword -> {
                viewModelScope.launch {
                    authorizationEffect.emit(AuthorizationEffect.RememberPassword)
                }
            }

            AuthorizationIntent.DismissAllCoroutines -> {
                getFromJob.cancel()
                viewModelScope.cancel()
            }
        }
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return phoneNumberValidator.isValidPhoneNumber(phoneNumber)
    }

    private fun validatePassword(password: String): Boolean {
        return passwordValidator.isValidPassword(password)
    }

    private suspend fun getFrom() {

        val hashPassword = hashRepository.hash(text = authorizationState.value.password)

        val user: User = supabaseStorage.getUser(
            password = hashPassword.decodeToString(),
            phone = authorizationState.value.phoneNumber
        )

        if (user != User.empty()) {
            withContext(context = viewModelScope.coroutineContext) {
                authorizationEffect.emit(AuthorizationEffect.DismissLoadingDialog)
                authorizationEffect.emit(AuthorizationEffect.AskPermission)
            }
            dataStoreStorage.edit(user = user.copy(password = authorizationState.value.password))
        } else {
            withContext(context = viewModelScope.coroutineContext) {
                authorizationEffect.emit(AuthorizationEffect.DismissLoadingDialog)
                authorizationEffect.emit(AuthorizationEffect.AuthError)
            }
        }
    }
}
