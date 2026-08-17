package ru.iteco.fmhandroid.ui.steps;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.ui.page.LoginPage;
import ru.iteco.fmhandroid.ui.page.MainPage;

public class MainSteps {

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    @Step("Выход из учетной записи")
    public void logout() {
        if (mainPage.isUserIconDisplayed()) {
            mainPage.clickUserIcon();
            mainPage.pressExitButton();
            loginPage.checkDisplayAuthorizationHeader();
        }
    }
}
