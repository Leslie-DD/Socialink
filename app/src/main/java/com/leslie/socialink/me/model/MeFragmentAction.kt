package com.leslie.socialink.me.model

sealed class MeFragmentAction {
    data object HeaderClick : MeFragmentAction()
    data object LoginOrOut : MeFragmentAction()
    data object Logout : MeFragmentAction()
}