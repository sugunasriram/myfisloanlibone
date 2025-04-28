package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst

import kotlinx.serialization.Serializable

@Serializable
data class GstSearchBody(
	val bureauConsent: String? = null,
	val loanType: String? = null,
	val tnc: String? = null,
	val id: String? = null
)

