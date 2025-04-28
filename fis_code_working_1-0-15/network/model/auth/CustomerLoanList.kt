package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import kotlinx.serialization.Serializable

@Serializable
data class CustomerLoanList(
    val status: Boolean? = null,
    val data: ArrayList<OfferResponseItem>? = null,
    val statusCode: Long? = null
)

































