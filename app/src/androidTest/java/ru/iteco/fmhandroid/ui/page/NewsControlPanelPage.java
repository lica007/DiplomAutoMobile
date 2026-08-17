package ru.iteco.fmhandroid.ui.page;

import ru.iteco.fmhandroid.R;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matchers;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.WaitUtils;

public class NewsControlPanelPage {

    private final int addNewsButton = R.id.add_news_image_view;
    private final int newsItemTitle = R.id.news_item_title_text_view;
    private final int editNewsItemButton = R.id.edit_news_item_image_view;
    private final int newsRecyclerView = R.id.news_list_recycler_view;
    private final int newsItemStatus = R.id.news_item_published_text_view;
    private final int filterNewsButton = R.id.filter_news_material_button;
    private final int filterCategoryField = R.id.news_item_category_text_auto_complete_text_view;
    private final int filterStartDate = R.id.news_item_publish_date_start_text_input_edit_text;
    private final int filterEndDate = R.id.news_item_publish_date_end_text_input_edit_text;
    private final int filterSaveButton = R.id.filter_button;
    private final int filterCancelButton = R.id.cancel_button;
    private final int filterActiveCheckbox = R.id.filter_news_active_material_check_box;
    private final int filterInactiveCheckbox = R.id.filter_news_inactive_material_check_box;
    private final int emptyNewsListText = R.id.control_panel_empty_news_list_text_view;

    private View decorView;

    public void setDecorView(View decorView) {
        this.decorView = decorView;
    }

    @Step("Ожидание загрузки экрана 'Панель управления новостями'")
    public void waitForControlPanelScreen() {
        onView(isRoot()).perform(WaitUtils.waitDisplayed(addNewsButton, 7000));
    }

    @Step("Нажать кнопку добавления новости (+)")
    public void clickAddNewsButton() {
        onView(withId(addNewsButton)).perform(click());
    }

    @Step("Проверить, что новость с заголовком '{title}' отображается в списке")
    public void checkNewsDisplayed(String title) {
        onView(withId(newsRecyclerView))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(allOf(withId(newsItemTitle), withText(title)))
                ));

        onView(allOf(withId(newsItemTitle), withText(title)))
                .check(matches(isDisplayed()));
    }

    @Step("Нажать иконку редактирования у новости '{title}'")
    public void clickEditNewsItem(String title) {
        onView(withId(newsRecyclerView))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(allOf(withId(newsItemTitle), withText(title)))
                ));
        onView(allOf(
                withId(editNewsItemButton),
                hasSibling(allOf(withId(newsItemTitle), withText(title)))
        )).perform(click());
    }

    @Step("Проверить, что у новости '{title}' отображается статус '{expectedStatus}'")
    public void checkNewsStatus(String title, String expectedStatus) {
        onView(withId(newsRecyclerView))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(allOf(withId(newsItemTitle), withText(title)))
                ));

        onView(allOf(
                withId(newsItemStatus),
                hasSibling(allOf(withId(newsItemTitle), withText(title)))
        )).check(matches(withText(expectedStatus)));
    }

    @Step("Проверить, что новость '{title}' НЕ отображается в списке")
    public void checkNewsNotDisplayed(String title) {
        onView(allOf(withId(newsItemTitle), withText(title)))
                .check(doesNotExist());
    }

    // Фильтрация
    @Step("Заполнить форму фильтра: категория '{category}', с '{startDate}' по '{endDate}'")
    public void fillFilterForm(String category, String startDate, String endDate) {
        // Открываем фильтр
        onView(withId(filterNewsButton)).perform(click());

        // Заполняем (закрывая клавиатуру после каждого ввода)
        onView(withId(filterCategoryField)).perform(replaceText(category), ViewActions.closeSoftKeyboard());
        onView(withId(filterStartDate)).perform(replaceText(startDate), ViewActions.closeSoftKeyboard());
        onView(withId(filterEndDate)).perform(replaceText(endDate), ViewActions.closeSoftKeyboard());
    }

    @Step("Нажать кнопку 'Фильтровать'")
    public void clickFilterApplyButton() {
        onView(withId(filterSaveButton)).perform(click());
    }

    @Step("Нажать кнопку 'Отмена' в окне фильтрации")
    public void clickFilterCancelButton() {
        // Открываем фильтр
        onView(withId(filterNewsButton)).perform(click());
        // Нажимаем отмену
        onView(withId(filterCancelButton)).perform(click());
    }

    @Step("Проверить сообщение об ошибке фильтрации: {text}")
    public void checkFilterErrorMessage(String text) {
        onView(withText(text))
                .inRoot(withDecorView(Matchers.not(decorView)));

    }

    @Step("Заполнить форму фильтра: категория '{category}', с '{startDate}' по '{endDate}', Активна: {isActive}, Не активна: {isNotActive}")
    public void fillAdvancedFilter(String category, String startDate, String endDate, boolean isActive, boolean isNotActive) {
        onView(withId(filterNewsButton)).perform(click());

        onView(withId(filterCategoryField)).perform(replaceText(category), ViewActions.closeSoftKeyboard());
        onView(withId(filterStartDate)).perform(replaceText(startDate), ViewActions.closeSoftKeyboard());
        onView(withId(filterEndDate)).perform(replaceText(endDate), ViewActions.closeSoftKeyboard());
        setCheckboxState(filterActiveCheckbox, isActive);
        setCheckboxState(filterInactiveCheckbox, isNotActive);
    }

    private void setCheckboxState(int checkboxId, boolean shouldBeChecked) {
        try {
            // Проверяем, стоит ли сейчас галочка
            onView(withId(checkboxId)).check(matches(isChecked()));
            // Если мы тут, значит галочка СТОИТ. Если нам нужно чтобы её НЕ БЫЛО - кликаем.
            if (!shouldBeChecked) {
                onView(withId(checkboxId)).perform(click());
            }
        } catch (Throwable e) {
            // Если вылетело исключение, значит галочка НЕ СТОИТ. Если нам нужно чтобы она БЫЛА - кликаем.
            if (shouldBeChecked) {
                onView(withId(checkboxId)).perform(click());
            }
        }
    }

    @Step("Проверить, что отображается заглушка 'Здесь пока ничего нет…'")
    public void checkEmptyListPlaceholderDisplayed() {
        onView(withId(emptyNewsListText))
                .check(matches(isDisplayed()))
                .check(matches(withText("Здесь пока ничего нет")));
    }

    @Step("Проверить сохраненные параметры в открытом фильтре")
    public void checkSavedFilterParams(String expectedCategory) {
        onView(withId(filterCategoryField)).check(matches(withText(expectedCategory)));
    }
}