package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.WaitUtils;

public class CreateEditNewsPage {
    private final int categoryDropdown = R.id.text_input_end_icon;
    private final int titleField = R.id.news_item_title_text_input_edit_text;
    private final int publishDateField = R.id.news_item_publish_date_text_input_edit_text;
    private final int publishTimeField = R.id.news_item_publish_time_text_input_edit_text;
    private final int descriptionField = R.id.news_item_description_text_input_edit_text;
    private final int switcherActive = R.id.switcher;
    private final int saveButton = R.id.save_button;
    private final int cancelButton = R.id.cancel_button;
    private final int categoryInputLayout = R.id.news_item_category_text_input_layout;
    private final int categoryTextField = R.id.news_item_category_text_auto_complete_text_view;
    private View decorView;

    public void setDecorView(View decorView) {
        this.decorView = decorView;
    }

    @Step("Ожидание экрана создания/редактирования новости")
    public void waitForCreateEditScreen() {
        onView(isRoot()).perform(WaitUtils.waitDisplayed(saveButton, 7000));
    }

    @Step("Выбрать категорию: {category}")
    public void selectCategory(String category) {
        onView(allOf(
                withId(categoryDropdown),
                isDescendantOfA(withId(categoryInputLayout))
        )).perform(click());

        onView(withText(category))
                .inRoot(isPlatformPopup())
                .perform(click());
    }

        @Step("Ввести заголовок: {title}")
    public void enterTitle(String title) {
        onView(withId(titleField)).perform(replaceText(title), closeSoftKeyboard());
    }

    @Step("Ввести описание: {description}")
    public void enterDescription(String description) {
        onView(withId(descriptionField)).perform(replaceText(description), closeSoftKeyboard());
    }

    @Step("Установить текущую дату и время публикации")
    public void setDateAndTime() {
        onView(withId(publishDateField)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(publishTimeField)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Step("Переключить статус (Активна/Не активна)")
    public void switchStatus() {
        onView(withId(switcherActive)).perform(click());
    }

    @Step("Очистить поле заголовка")
    public void clearTitle() {
        onView(withId(titleField)).perform(replaceText(""), closeSoftKeyboard());
    }

    @Step("Нажать кнопку 'Сохранить'")
    public void clickSaveButton() {
        onView(withId(saveButton)).perform(scrollTo(), click());
    }

    @Step("Нажать кнопку 'Отмена' на форме")
    public void clickCancelButton() {
        onView(withId(cancelButton)).perform(scrollTo(), click());
    }

    @Step("Проверить, что отображается диалоговое окно подтверждения")
    public void checkCancelDialogDisplayed() {
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
    }

    @Step("Нажать 'ОК' в диалоговом окне (подтвердить выход)")
    public void clickOkInDialog() {
        onView(withId(android.R.id.button1)).perform(click());
    }
    @Step("Нажать 'Отмена' и подтвердить выход")
    public void cancelAndConfirmExit() {
        onView(withId(cancelButton)).perform(scrollTo(), click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Step("Проверить сообщение об ошибке пустых полей")
    public void checkErrorMessage_emptyFields() {
        onView(withText("Заполните пустые поля"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Step("Проверить, что в поле 'Категория' указано: {expectedCategory}")
    public void checkCategoryText(String expectedCategory) {
        onView(withId(categoryTextField)).check(matches(withText(expectedCategory)));
    }

    @Step("Проверить, что в поле 'Заголовок' указано: {expectedTitle}")
    public void checkTitleText(String expectedTitle) {
        onView(withId(titleField)).check(matches(withText(expectedTitle)));
    }

    @Step("Проверить, что в поле 'Описание' указано: {expectedDescription}")
    public void checkDescriptionText(String expectedDescription) {
        onView(withId(descriptionField)).check(matches(withText(expectedDescription)));
    }

    @Step("Ввести точную дату: {date} и время: {time}")
    public void fillDateAndTimeExact(String date, String time) {
        onView(withId(publishDateField)).perform(replaceText(date), closeSoftKeyboard());
        onView(withId(publishTimeField)).perform(replaceText(time), closeSoftKeyboard());
    }

    @Step("Проверить, что в поле 'Дата публикации' указано: {expectedDate}")
    public void checkDateText(String expectedDate) {
        onView(withId(publishDateField)).check(matches(withText(expectedDate)));
    }

    @Step("Проверить, что в поле 'Время публикации' указано: {expectedTime}")
    public void checkTimeText(String expectedTime) {
        onView(withId(publishTimeField)).check(matches(withText(expectedTime)));
    }
}