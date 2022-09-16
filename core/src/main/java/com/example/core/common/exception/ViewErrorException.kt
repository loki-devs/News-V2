package com.example.core.common.exception

import com.example.core.presentation.viewstate.ViewError
import java.lang.Exception

data class ViewErrorException(val viewError: ViewError) : Exception()