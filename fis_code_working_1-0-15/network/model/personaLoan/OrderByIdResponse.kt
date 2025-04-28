package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderByIdResponse(

	@SerialName("data")
	val data: OfferResponseItem? = null,

	@SerialName("status")
	val status: Boolean? = null,

	@SerialName("statusCode")
	val statusCode: Int? = null
)
