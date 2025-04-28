package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogIn(
	val data: Token? = null,
	val status: Boolean? = null,
	val statusCode: Int? = null
)

@Serializable
data class Token(
	val accessToken: String? = null,
	val refreshToken: String? = null,
	val securityKey: String? = null,
	val sseId: String? = null
)

