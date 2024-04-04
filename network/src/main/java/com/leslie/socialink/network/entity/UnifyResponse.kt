package com.leslie.socialink.network.entity

class UnifyResponse<T>(
    val code: Int,
    val msg: String?,
    val data: T?
)