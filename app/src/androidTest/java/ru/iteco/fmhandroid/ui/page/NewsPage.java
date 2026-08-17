package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.WaitUtils;

public class NewsPage {

    private final int controlPanelButton = R.id.edit_news_material_button;

    @Step("Ожидание загрузки экрана 'Новости'")
    public void waitForNewsScreen() {
        onView(isRoot()).perform(WaitUtils.waitDisplayed(controlPanelButton, 7000));
    }

    @Step("Нажать на кнопку перехода в Панель управления (карандаш)")
    public void clickControlPanelButton() {
        onView(withId(controlPanelButton)).perform(click());
    }
}
