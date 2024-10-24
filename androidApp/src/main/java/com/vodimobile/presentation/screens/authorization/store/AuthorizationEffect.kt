package com.vodimobile.presentation.screens.authorization.store

sealed class AuthorizationEffect {
    data object OpenUserAgreement : AuthorizationEffect()
    data object SmsVerification : AuthorizationEffect()
    data object ReturnBack : AuthorizationEffect()
    data object AskPermission : AuthorizationEffect()
    data object RememberPassword: AuthorizationEffect()
    data object AuthError : AuthorizationEffect()
    data object ShowLoadingDialog : AuthorizationEffect()
    data object DismissLoadingDialog : AuthorizationEffect()
}
