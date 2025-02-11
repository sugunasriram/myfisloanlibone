package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordOtpVerify(
	val data: String? = null,
	val status: Boolean? = null,
	val statusCode: Int? = null
)

