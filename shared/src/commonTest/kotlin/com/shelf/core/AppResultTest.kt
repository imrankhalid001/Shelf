package com.shelf.core

import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.core.result.getOrElse
import com.shelf.core.result.getOrNull
import com.shelf.core.result.map
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppResultTest {

    @Test
    fun testSuccessResultState() {
        val result: AppResult<String> = AppResult.Success("Atomic Habits")
        assertTrue(result.isSuccess)
        assertFalse(result.isError)
        assertFalse(result.isLoading)
        assertEquals("Atomic Habits", result.getOrNull())
        assertEquals("Atomic Habits", result.getOrElse("Default"))
    }

    @Test
    fun testErrorResultState() {
        val result: AppResult<String> = AppResult.Error(AppError.Network.NoInternet)
        assertFalse(result.isSuccess)
        assertTrue(result.isError)
        assertFalse(result.isLoading)
        assertNull(result.getOrNull())
        assertEquals("Default", result.getOrElse("Default"))
    }

    @Test
    fun testLoadingResultState() {
        val result: AppResult<String> = AppResult.Loading
        assertFalse(result.isSuccess)
        assertFalse(result.isError)
        assertTrue(result.isLoading)
        assertNull(result.getOrNull())
    }

    @Test
    fun testMapSuccess() {
        val result: AppResult<Int> = AppResult.Success(100)
        val mapped = result.map { it * 2 }
        assertEquals(AppResult.Success(200), mapped)
    }

    @Test
    fun testMapErrorReturnsSameError() {
        val errorResult: AppResult<Int> = AppResult.Error(AppError.Network.Timeout)
        val mapped = errorResult.map { it * 2 }
        assertEquals(AppResult.Error(AppError.Network.Timeout), mapped)
    }
}
