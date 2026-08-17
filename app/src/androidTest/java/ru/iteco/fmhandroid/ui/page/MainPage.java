package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.WaitUtils;

public class MainPage {

    public boolean isUserIconDisplayed() {
        try {
            onView(withId(R.id.authorization_image_button))
                    .check(matches(isDisplayed()));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Step("Проверить отображение главного экрана")
    public void waitForMainScreen() {
        onView(isRoot()).perform(WaitUtils.waitDisplayed(R.id.main_swipe_refresh, 7000));
    }

    @Step("Нажать на иконку пользователя")
    public void clickUserIcon() {
        onView(withId(R.id.authorization_image_button))
                .perform(click());
    }

    @Step("Нажать кнопку Выйти")
    public void pressExitButton() {
        onView(withText("Выйти")).perform(click());
    }
}