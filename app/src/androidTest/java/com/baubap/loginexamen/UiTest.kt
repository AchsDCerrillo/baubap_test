package com.baubap.loginexamen

import androidx.compose.ui.test.junit4.ComposeContentTestRule

abstract class UiTest {
    protected abstract fun setContent()

    protected fun ComposeContentTestRule.uiTest(
        arrange: () -> Unit = {},
        uiActions: ComposeContentTestRule.() -> Unit,
        asserts: ComposeContentTestRule.() -> Unit
    ) {
        arrange()
        setContent()
        uiActions()
        asserts()
    }
}