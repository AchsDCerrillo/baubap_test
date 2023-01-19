package com.baubap.loginexamen.ui.errors

import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.baubap.loginexamen.R

sealed interface UiError {
    @ReadOnlyComposable
    @Composable
    fun getErrorText(): String
    fun isValid(): Boolean
}

data class EmptyValueError(private val value: String?) : UiError {
    @ReadOnlyComposable
    @Composable
    override fun getErrorText(): String = stringResource(id = R.string.empty_error)
    override fun isValid(): Boolean = !value.isNullOrEmpty()
}

data class InvalidEmailError(private val email: String?) : UiError {
    @ReadOnlyComposable
    @Composable
    override fun getErrorText(): String = stringResource(id = R.string.invalid_email)
    override fun isValid(): Boolean = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$").matches(email.orEmpty())
}

data class MaximumSizeFieldError(
    private val value: String?,
    private val maxValue: Int = 60
) : UiError {
    @ReadOnlyComposable
    @Composable
    override fun getErrorText(): String = stringResource(id = R.string.max_size_error, maxValue)

    override fun isValid(): Boolean {
        val length = value?.length ?: 0
        return length <= maxValue
    }
}


data class MinimumSizeFieldError(
    private val value: String?,
    private val minimumSize: Int = 1
) : UiError {
    @ReadOnlyComposable
    @Composable
    override fun getErrorText(): String = stringResource(id = R.string.min_size_error, minimumSize)
    override fun isValid(): Boolean {
        val length = value?.length ?: 0
        return length >= minimumSize
    }
}

data class AllowedCharactersError(
    private val value: String?,
    private val allowedChars: String = "[\\w\\d@$%^&]{8,}"
) : UiError {
    @ReadOnlyComposable
    @Composable
    override fun getErrorText(): String = stringResource(id = R.string.invalid_characters_error)
    override fun isValid(): Boolean = Regex(allowedChars).matches(value.orEmpty())
}