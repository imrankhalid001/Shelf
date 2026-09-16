package com.shelf.core.result

import com.shelf.core.error.AppError

sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
    data object Loading : AppResult<Nothing>

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading
}

fun <T> AppResult<T>.getOrNull(): T? = (this as? AppResult.Success)?.data

fun <T> AppResult<T>.getOrElse(default: T): T {
    return when (this) {
        is AppResult.Success -> data
        is AppResult.Error, is AppResult.Loading -> default
    }
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Error -> this
        is AppResult.Loading -> AppResult.Loading
    }
}

inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> {
    return when (this) {
        is AppResult.Success -> transform(data)
        is AppResult.Error -> this
        is AppResult.Loading -> AppResult.Loading
    }
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(error)
    return this
}
