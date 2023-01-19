package com.baubap.loginexamen.ui.login

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.baubap.loginexamen.R
import com.baubap.loginexamen.data.user.InvalidLoginException
import com.baubap.loginexamen.ui.components.*
import com.baubap.loginexamen.ui.theme.Dimens

const val EMAIL_TEST_TAG = "EMAIL_TEST_TAG"
const val PASSWORD_TEST_TAG = "PASSWORD_TEST_TAG"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val state by viewModel.loginState.collectAsState()
    val focusManager = LocalFocusManager.current

    InfoDialog(
        openDialog = state is LoginSuccess,
        title = stringResource(id = R.string.success),
        onDismiss = {
            viewModel.clearAll()
            focusManager.clearFocus()
        }
    )

    InfoDialog(
        openDialog = state is LoginError,
        title = when ((state as? LoginError)?.error) {
            is InvalidLoginException -> stringResource(R.string.error_login)
            else -> ""
        },
        onDismiss = viewModel::clearState
    )

    Scaffold(
        topBar = {
            DefaultAppBar(title = stringResource(id = R.string.welcome))
        }
    ) { padding ->
        TransparentLoadingWrapper(
            isLoading = state is LoginLoading,
            focusManager = focusManager
        ) {
            LoginContent(
                paddingValues = padding,
                email = email,
                password = password,
                loginState = state,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onLoginClick = viewModel::onLoginClick
            )
        }
    }
}

@Composable
fun LoginContent(
    paddingValues: PaddingValues,
    email: String,
    password: String,
    loginState: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val emailError = (loginState as? LoginUiInvalid)?.emailError
    val passwordError = (loginState as? LoginUiInvalid)?.passwordError

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = Dimens.keyLine2)
    ) {
        val (
            emailRef, emailErrorRef,
            passwordRef, passwordErrorRef, loginRef,
        ) = createRefs()

        EmailInput(
            boxModifier = Modifier.constrainAs(emailRef) {
                top.linkTo(parent.top, margin = Dimens.keyLine1)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            modifier = Modifier.testTag(EMAIL_TEST_TAG),
            email = email,
            isError = emailError != null,
            onEmailChanges = onEmailChange,
            enabled = loginState !is LoginLoading
        )
        InputError(
            modifier = Modifier.constrainAs(emailErrorRef) {
                top.linkTo(emailRef.bottom, margin = Dimens.keyLine1)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            uiError = emailError
        )
        PasswordInput(
            boxModifier = Modifier.constrainAs(passwordRef) {
                if (emailError != null) top.linkTo(emailErrorRef.bottom, margin = Dimens.keyLine2)
                else top.linkTo(emailRef.bottom, margin = Dimens.keyLine1)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            modifier = Modifier.testTag(PASSWORD_TEST_TAG),
            password = password,
            isError = passwordError != null,
            onPasswordChanges = onPasswordChange,
            enabled = loginState !is LoginLoading
        )
        InputError(
            modifier = Modifier.constrainAs(passwordErrorRef) {
                top.linkTo(passwordRef.bottom, margin = Dimens.keyLine1)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            uiError = passwordError
        )
        MainButton(
            modifier = Modifier.constrainAs(loginRef) {
                if (passwordError != null) top.linkTo(
                    passwordErrorRef.bottom,
                    margin = Dimens.keyLine2
                )
                else top.linkTo(passwordRef.bottom, margin = Dimens.keyLine1)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.login),
            enabled = loginState is LoginUiValid,
            onClick = onLoginClick,
        )
    }
}