package com.ulagos.myapplication.tmb

data class ApiResponse(
    val message: String,
    val data: UserData,
    val tyc: TermListData
)

data class UserData(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String
)

data class TermListData(
    val message: String,
    val data: List<TermData>
)

data class TermData(
    val title: String,
    val description: String
)
