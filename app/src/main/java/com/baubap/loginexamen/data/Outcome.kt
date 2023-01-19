package com.baubap.loginexamen.data

sealed interface Outcome<T>
data class Loading<T>(val data: T? = null): Outcome<T>
data class Success<T>(val data: T? = null): Outcome<T>
data class Failure<T>(val error: Throwable? = null, val data: T? = null): Outcome<T>