package com.leslie.socialink.login.model

sealed class LoginAction {
    data class RequestLogin(
        val phone: String,
        val password: String
    ) : LoginAction()
}