package com.example.widgets.model

sealed class ApiState {
    class Success(val data: String) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading:ApiState()
    object Empty: ApiState()
}