package com.baubap.loginexamen.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.baubap.loginexamen.UiTest
import com.baubap.loginexamen.domain.repository.local.UserLocalRepository
import com.baubap.loginexamen.domain.usecases.LoginUseCase
import com.baubap.loginexamen.ui.login.EMAIL_TEST_TAG
import com.baubap.loginexamen.ui.login.LoginScreen
import com.baubap.loginexamen.ui.login.LoginViewModel
import com.baubap.loginexamen.ui.login.PASSWORD_TEST_TAG
import com.baubap.loginexamen.ui.theme.LoginTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest : UiTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val userRepository = UserLocalRepository()

    private val loginUseCase = LoginUseCase(userRepository = userRepository)

    private val loginViewModel = LoginViewModel(
        loginUseCase = loginUseCase
    )

    override fun setContent() {
        composeTestRule.setContent {
            LoginTestTheme {
                LoginScreen(
                    viewModel = loginViewModel
                )
            }
        }
    }

    @Test
    fun showEmptyEmailErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("test")
            },
            asserts = {
                onNodeWithText(
                    "Please enter a value",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showInvalidEmailErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achs")
            },
            asserts = {
                onNodeWithText(
                    "Add a valid email, please",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showValidEmailTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
            },
            asserts = {
                onNodeWithText(
                    "Add a valid email, please",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertDoesNotExist()
            }
        )
    }

    @Test
    fun showEmptyPasswordErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("")
            },
            asserts = {
                onNodeWithText(
                    "Please enter a value",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showMinSizePasswordErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("test")
            },
            asserts = {
                onNodeWithText(
                    "at least 8 characters",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showMaxSizePasswordErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("12345678901234567890123456789012345678901234567890123456789012345")
            },
            asserts = {
                onNodeWithText(
                    "This text must have max 60 characters",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showInvalidCharsPasswordErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("tester1.45")
            },
            asserts = {
                onNodeWithText(
                    "There are some invalid characters",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showValidPasswordTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("Tester$123")
            },
            asserts = {
                onNodeWithText(
                    "Login",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsEnabled()
            }
        )
    }

    @Test
    fun showDialogErrorTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("achscerrillo@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("tester1234")
                onNodeWithText(
                    "Login",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).performClick()
            },
            asserts = {
                runBlocking {
                    delay(2500)
                }
                onNodeWithText(
                    "Invalid credentials",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }

    @Test
    fun showDialogSuccessTest() {
        composeTestRule.uiTest(
            uiActions = {
                onNodeWithTag(
                    EMAIL_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("alex@gmail.com")
                onNodeWithTag(
                    PASSWORD_TEST_TAG,
                    useUnmergedTree = true
                ).performTextInput("Test$123")
                onNodeWithText(
                    "Login",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).performClick()
            },
            asserts = {
                runBlocking {
                    delay(2500)
                }
                onNodeWithText(
                    "Login succeed",
                    ignoreCase = true,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
        )
    }
}