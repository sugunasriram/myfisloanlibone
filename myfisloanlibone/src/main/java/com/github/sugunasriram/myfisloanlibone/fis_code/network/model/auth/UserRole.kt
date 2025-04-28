package com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserRole(
    val data: List<RoleData>? = null,
    val status: Boolean? = null
)


@Serializable
data class RoleData(
    val _id: String,
    val role: String
)
