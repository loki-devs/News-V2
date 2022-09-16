package com.example.core.common.extension


import com.example.core.ErrorCode
import com.example.core.common.exception.ViewErrorException
import com.example.core.presentation.viewstate.ViewError
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException


fun Exception.asErrorObject(message: String? = null): ViewError {
    return when {
        this is HttpException -> {
            asErrorObject()
        }
        this is IOException -> {
            ViewError(ErrorCode.GLOBAL_INTERNET_ERROR, message)
        }
        this is ViewErrorException -> {
            viewError
        }
        else -> {
            ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR, message)
        }
    }
}

fun HttpException.asErrorObject(message: String? = null): ViewError {
    val errorBody = response()?.errorBody()?.string()
    return errorBody?.let {
        try {
            val jsonObj = JSONObject(it)
            ViewError(
                jsonObj.getString("error_code"),
                jsonObj.getString("message")
            )
        } catch (e: JSONException) {
            ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR, message)
        }
    } ?: ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR, message)
}