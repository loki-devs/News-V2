package com.example.core.presentation.viewstate


data class ViewError(
    val errorCode: String,
    val message: String? = null,
    val data: Any? = null
)