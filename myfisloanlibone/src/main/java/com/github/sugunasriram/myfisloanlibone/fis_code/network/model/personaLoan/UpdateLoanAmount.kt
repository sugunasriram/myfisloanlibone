package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan

import kotlinx.serialization.Serializable


@Serializable
data class UpdateLoanAmountBody(
    val requestAmount: String? = null,
    val requestTerm: String? = null,
    val id: String? = null,
    val offerId: String? = null,
    val loanType: String? = null
)

@Serializable
data class UpdateResponse(
    val data: UpdateResponseData? = null,
    val status: Boolean? = null,
    val statusCode: Int? = null
)

@Serializable
data class UpdateResponseData(
    val offerResponse: OfferResponseItem? = null,
    val id: String? = null
)


