package com.github.sugunasriram.myfisloanlibone.fis_code.utils

import kotlinx.serialization.Serializable

@Serializable
data class KycUrlData (
    val id : String,
    val url: String,
    val transactionId : String
)