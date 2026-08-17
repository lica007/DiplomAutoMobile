package ru.iteco.fmhandroid.ui.steps;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.ui.page.LoginPage;
import ru.iteco.fmhandroid.ui.page.MainPage;

public class AuthSteps {

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    @Step("Авторизация пользователя")
    public void login(String login, String password) {
        loginPage.enterLogin(login);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    @Step("Проверить успешную авторизацию")
    public void checkSuccessfulLogin() {
        mainPage.waitForMainScreen();
    }
}