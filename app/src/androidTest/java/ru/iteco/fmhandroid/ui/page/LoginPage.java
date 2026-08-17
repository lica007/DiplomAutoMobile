package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.not;

import android.app.Activity;
import android.view.View;

import org.hamcrest.Matchers;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.WaitUtils;

public class LoginPage {
    private final int loginField = R.id.login_edit_text;
    private final int passwordField = R.id.password_edit_text;
    private final int loginButton = R.id.enter_button;
    private View decorView;


    public void setDecorView(View decorView) {
        this.decorView = decorView;
    }

    @Step("Ожидание экрана авторизации")
    public void waitForLoginScreen() {
        onView(isRoot()).perform(WaitUtils.waitDisplayed(R.id.enter_button, 7000));
    }

    @Step("Ввести логин: {login}")
    public void enterLogin(String login) {
        onView(withId(loginField))
                .perform(replaceText(login), closeSoftKeyboard());
    }

    @Step("Ввести пароль")
    public void enterPassword(String password) {
        onView(withId(passwordField))
                .perform(replaceText(password), closeSoftKeyboard());
    }

    @Step("Нажать кнопку 'Войти'")
    public void clickLogin() {
        onView(withId(loginButton)).perform(click());
    }

    @Step("Проверка отображения заголовка 'Авторизация'")
    public void checkDisplayAuthorizationHeader() {
        onView(withText("Авторизация"))
                .check(matches(isDisplayed()));
    }

    @Step("Проверить сообщение об ошибке пустых полей")
    public void checkErrorMessage_emptyField() {
        onView(withText("Логин и пароль не могут быть пустыми"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Step("Проверить сообщение об ошибке неверно введенных данных")
    public void checkErrorMessage_incorrectData() {
        onView(withText("Неверно введен логин или пароль"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }
}