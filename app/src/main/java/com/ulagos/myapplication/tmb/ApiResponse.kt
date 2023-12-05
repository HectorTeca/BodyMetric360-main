package com.ulagos.myapplication.tmb

data class ApiResponse(
    val message: String,
    val data: UserData
)

data class UserData(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String
)
