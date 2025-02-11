package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class Signup(
    val status: Boolean? = false,
    val data: Data? = null,
    val statusCode: Int? = 0
)

@Serializable
data class Data(
    val orderId: String? = null,
    val otp:String? = null
)
