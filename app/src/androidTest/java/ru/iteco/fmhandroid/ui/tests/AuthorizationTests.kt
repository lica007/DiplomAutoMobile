package ru.iteco.fmhandroid.ui.tests

import android.view.View
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import io.qameta.allure.kotlin.Description
import io.qameta.allure.kotlin.Severity
import io.qameta.allure.kotlin.SeverityLevel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.ui.AppActivity
import ru.iteco.fmhandroid.ui.data.DataHelper
import ru.iteco.fmhandroid.ui.page.LoginPage
import ru.iteco.fmhandroid.ui.steps.AuthSteps
import ru.iteco.fmhandroid.ui.steps.MainSteps

@LargeTest
@RunWith(AllureAndroidJUnit4::class)
class AuthorizationTests {
    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AppActivity::class.java)
    private val authSteps = AuthSteps()
    private val loginPage = LoginPage()
    private val mainSteps = MainSteps()
    private lateinit var decorView: View

    @Before
    fun waitForAppToLoad() {
        mActivityScenarioRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }

        loginPage.setDecorView(decorView)
        loginPage.waitForLoginScreen()
    }

    @Test
    @Description("Успешная авторизация пользователя")
    @Severity(SeverityLevel.CRITICAL)
    fun successfulLogin() {
        authSteps.login(DataHelper.getValidLogin(), DataHelper.getValidPassword())
        authSteps.checkSuccessfulLogin()
    }

    @Test
    @Description("Авторизация с логином, отсутствующим в системе")
    @Severity(SeverityLevel.CRITICAL)
    fun authIncorrectLogin() {
        authSteps.login(DataHelper.getIncorrectLogin(), DataHelper.getValidPassword())
        loginPage.checkErrorMessage_incorrectData();
    }

    @Test
    @Description("Авторизация с неверным паролем")
    @Severity(SeverityLevel.CRITICAL)
    fun authIncorrectPassword() {
        authSteps.login(DataHelper.getValidLogin(), DataHelper.getIncorrectPassword())
        loginPage.checkErrorMessage_incorrectData();
    }

    @Test
    @Description("Авторизация с пустыми полями")
    @Severity(SeverityLevel.CRITICAL)
    fun authEmptyFields() {
        authSteps.login("", "")
        loginPage.checkErrorMessage_emptyField();
    }

    @Test
    @Description("Авторизация с пустым полем логина")
    @Severity(SeverityLevel.CRITICAL)
    fun authEmptyFieldLogin() {
        authSteps.login("", DataHelper.getValidPassword())
        loginPage.checkErrorMessage_emptyField();
    }

    @Test
    @Description("Авторизация с пустым полем пароля")
    @Severity(SeverityLevel.CRITICAL)
    fun authEmptyFieldPassword() {
        authSteps.login(DataHelper.getValidLogin(), "")
        loginPage.checkErrorMessage_emptyField();
    }

    @Test
    @Description("Авторизация с данными незарегестрированного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    fun authIncorrectUser() {
        authSteps.login(DataHelper.getIncorrectLogin(), DataHelper.getIncorrectPassword())
        loginPage.checkErrorMessage_incorrectData();
    }

    @After
    fun logoutIfNeeded() {
            mainSteps.logout()
    }
}