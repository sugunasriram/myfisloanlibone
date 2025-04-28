package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan

import kotlinx.serialization.Serializable

@Serializable
data class SearchModel(
    val data: SearchResponseModel? = null,
    val status: Boolean? = null,
    val statusCode: Int? = null
)
@Serializable
data class SearchBodyModel(
    val loanType: String? = null,
    val endUse: String? = null,
    val bureauConsent: String? = null,
)

@Serializable
data class SearchResponseModel(
    val id: String? = null,
    val url: String? = null,
    val transactionId: String? = null
)