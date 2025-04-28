package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPassword(
    val data: ForgotData? = null,
    val status: Boolean? = null,
    val statusCode: Int? = null
)
@Serializable
data class ForgotData(
	val orderId: String? = null,
	val message: String? = null
)

