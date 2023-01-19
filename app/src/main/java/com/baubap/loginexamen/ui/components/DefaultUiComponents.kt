package com.baubap.loginexamen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.baubap.loginexamen.R
import com.baubap.loginexamen.ui.errors.UiError
import com.baubap.loginexamen.ui.errors.filterSize
import com.baubap.loginexamen.ui.theme.Dimens
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun textFieldColors(): TextFieldColors = TextFieldDefaults
    .outlinedTextFieldColors(
        backgroundColor = MaterialTheme.colors.background,
        cursorColor = MaterialTheme.colors.primary,
        focusedLabelColor = MaterialTheme.colors.primary,
        unfocusedLabelColor = MaterialTheme.colors.primary,
        focusedBorderColor = MaterialTheme.colors.primary,
        unfocusedBorderColor = MaterialTheme.colors.primary,
    )

@Composable
fun DefaultAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        elevation = Dimens.keyLineHalf,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Composable
fun InputError(modifier: Modifier = Modifier, uiError: UiError?) {
    uiError?.let {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = it.getErrorText(),
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun OutlinedTextFieldBackground(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = Dimens.keyLine1)
                .background(
                    color,
                    shape = RoundedCornerShape(Dimens.keyLineHalf)
                ),
        )
        content()
    }
}

@Composable
fun EmailInput(
    boxModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanges: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = textFieldColors()
) {
    OutlinedTextFieldBackground(modifier = boxModifier) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsWithImePadding(),
            value = email,
            onValueChange = onEmailChanges,
            enabled = enabled,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            isError = isError,
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            readOnly = readOnly,
            textStyle = textStyle,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            shape = shape,
            colors = colors,
            label = { Text(text = stringResource(id = R.string.email)) },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
fun PasswordInput(
    boxModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChanges: (String) -> Unit,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.password)) },
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = textFieldColors(),
    limit: Int = 64,
) {
    val passwordVisibility = remember { mutableStateOf(false) }
    OutlinedTextFieldBackground(modifier = boxModifier) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsWithImePadding(),
            value = password,
            onValueChange = {
                onPasswordChanges(it.filterSize(limit))
            },
            enabled = enabled,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            isError = isError,
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            readOnly = readOnly,
            textStyle = textStyle,
            singleLine = singleLine,
            visualTransformation =
            if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            shape = shape,
            colors = colors,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = {
                Image(
                    modifier = Modifier.clickable {
                        passwordVisibility.value = !passwordVisibility.value
                    },
                    imageVector =
                    if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
        )
    }
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier, text: String,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    color: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.onPrimary,
    rounded: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    if (!rounded) {
        Button(
            modifier = modifier,
            onClick = onClick,
            elevation = elevation,
            colors = ButtonDefaults.buttonColors(backgroundColor = color),
            enabled = enabled,
        ) {
            Text(text = text, color = if (enabled) textColor else MaterialTheme.colors.onSurface)
        }
    } else {
        Button(
            modifier = modifier,
            onClick = onClick,
            elevation = elevation,
            shape = RoundedCornerShape(Dimens.keyLine3),
            colors = ButtonDefaults.buttonColors(backgroundColor = color),
            enabled = enabled,
        ) {
            Text(text = text, color = if (enabled) textColor else MaterialTheme.colors.onSurface)
        }
    }
}

@Composable
fun TransparentButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = text, color = textColor)
    }
}

@Composable
fun InfoDialog(
    openDialog: Boolean,
    title: String,
    message: String? = null,
    acceptButtonText: String = stringResource(id = R.string.accept),
    cancelButtonText: String = stringResource(id = R.string.cancel),
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title
                )
            },
            text = {
                if (message != null) {
                    Text(
                        text = message
                    )
                }
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TransparentButton(text = acceptButtonText) {
                    onConfirm()
                    onDismiss()
                }
            },
            dismissButton = {
                TransparentButton(text = cancelButtonText) {
                    onCancel()
                    onDismiss()
                }
            },
        )
    }
}

@Composable
fun TransparentLoadingWrapper(
    color: Color = MaterialTheme.colors.onBackground,
    text: String = stringResource(id = R.string.loading),
    focusManager: FocusManager = LocalFocusManager.current,
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (isLoading) {
            focusManager.clearFocus()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.onBackground.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = text,
                        textAlign = TextAlign.Center,
                        color = color
                    )
                }

            }
        }
    }
}