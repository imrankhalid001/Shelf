package com.shelf.core.error

sealed interface AppError {
    sealed interface Network : AppError {
        data object NoInternet : Network
        data object Timeout : Network
        data class ServerError(val code: Int, val message: String? = null) : Network
        data class Serialization(val message: String) : Network
    }

    sealed interface Database : AppError {
        data class ReadFailed(val cause: Throwable) : Database
        data class WriteFailed(val cause: Throwable) : Database
        data object NotFound : Database
    }

    sealed interface Validation : AppError {
        data class InvalidPageNumber(val maxPages: Int) : Validation
        data object EmptyQuery : Validation
        data object DuplicateCollectionName : Validation
        data class Custom(val message: String) : Validation
    }

    data class Unknown(val cause: Throwable? = null) : AppError
}
