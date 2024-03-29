package com.hnu.heshequ.network.entity

class UnifyResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
)