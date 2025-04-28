package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import kotlinx.serialization.Serializable
@Serializable
data class PincodeModel(
	val pincode: String? = null,
	val city: String? = null,
	val state: String? = null
)