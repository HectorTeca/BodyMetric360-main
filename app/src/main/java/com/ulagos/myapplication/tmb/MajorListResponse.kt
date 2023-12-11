package com.ulagos.myapplication.tmb

data class MajorListResponse (
    val message: String,
    val data: List<MajorData>
)

data class Major(
    val id: String?, // O el tipo de datos correcto según tus necesidades
    val descripcion: String
)


data class MajorData(
    val message: String,
    val data: String
)

