package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.page.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.page.NewsControlPanelPage;
import ru.iteco.fmhandroid.ui.page.NewsPage;

public class NewsSteps {
    private final NewsPage newsPage = new NewsPage();
    private final NewsControlPanelPage controlPanelPage = new NewsControlPanelPage();
    private final CreateEditNewsPage createEditPage = new CreateEditNewsPage();

    @Step("Перейти в 'Панель управления новостями'")
    public void navigateToNewsControlPanel() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        onView(withText("Новости")).perform(click());

        newsPage.waitForNewsScreen();
        newsPage.clickControlPanelButton();

        controlPanelPage.waitForControlPanelScreen();
    }

    @Step("Создать новость: категория '{category}', заголовок '{title}'")
    public void createNews(String category, String title, String description) {
        controlPanelPage.clickAddNewsButton();
        createEditPage.waitForCreateEditScreen();

        createEditPage.selectCategory(category);
        createEditPage.enterTitle(title);
        createEditPage.setDateAndTime();
        createEditPage.enterDescription(description);

        createEditPage.clickSaveButton();
    }

    @Step("Попытка создать новость с пустыми полями")
    public void tryCreateNewsWithEmptyFields() {
        controlPanelPage.clickAddNewsButton();
        createEditPage.waitForCreateEditScreen();

        createEditPage.clickSaveButton();
    }

    @Step("Отредактировать существующую новость (Новый заголовок: '{newTitle}')")
    public void editExistingNews(String oldTitle, String newTitle, String newDescription) {
        controlPanelPage.clickEditNewsItem(oldTitle);
        createEditPage.waitForCreateEditScreen();

        createEditPage.enterTitle(newTitle);
        createEditPage.enterDescription(newDescription);

        createEditPage.clickSaveButton();
    }

    @Step("Попытка сохранить редактируемую новость с пустым заголовком")
    public void tryEditNewsWithEmptyTitle(String title) {
        controlPanelPage.clickEditNewsItem(title);
        createEditPage.waitForCreateEditScreen();

        createEditPage.clearTitle();
        createEditPage.clickSaveButton();
    }

    @Step("Заполнить новость, нажать Отмена и выйти")
    public void cancelNewsCreationAndStay(String category, String title, String description) {
        controlPanelPage.clickAddNewsButton();
        createEditPage.waitForCreateEditScreen();

        createEditPage.selectCategory(category);
        createEditPage.enterTitle(title);
        createEditPage.setDateAndTime();
        createEditPage.enterDescription(description);

        createEditPage.clickCancelButton();
        createEditPage.checkCancelDialogDisplayed();
        createEditPage.clickOkInDialog();
    }

    @Step("Создать новость: '{title}' с точной датой '{date}' и временем '{time}'")
    public void createNewsWithExactDate(String category, String title, String description, String date, String time) {
        controlPanelPage.clickAddNewsButton();
        createEditPage.waitForCreateEditScreen();

        createEditPage.selectCategory(category);
        createEditPage.enterTitle(title);
        createEditPage.enterDescription(description);

        createEditPage.fillDateAndTimeExact(date, time);

        createEditPage.clickSaveButton();
    }

    @Step("Отредактировать новость '{oldTitle}' (Новые данные: Заголовок '{newTitle}', Категория '{newCategory}')")
    public void editExistingNewsWithExactDate(String oldTitle, String newCategory, String newTitle, String newDescription, String newDate, String newTime) {
        controlPanelPage.clickEditNewsItem(oldTitle);
        createEditPage.waitForCreateEditScreen();

        createEditPage.selectCategory(newCategory);
        createEditPage.enterTitle(newTitle);
        createEditPage.enterDescription(newDescription);
        createEditPage.fillDateAndTimeExact(newDate, newTime);

        createEditPage.clickSaveButton();
    }

    @Step("Изменить категорию новости '{title}' на '{newCategory}'")
    public void editNewsCategory(String title, String newCategory) {
        controlPanelPage.clickEditNewsItem(title);
        createEditPage.waitForCreateEditScreen();

        createEditPage.selectCategory(newCategory);
        createEditPage.clickSaveButton();
    }

    @Step("Изменить статус новости '{title}' (нажать переключатель)")
    public void editNewsStatus(String title) {
        controlPanelPage.clickEditNewsItem(title);
        createEditPage.waitForCreateEditScreen();

        createEditPage.switchStatus();
        createEditPage.clickSaveButton();
    }

    // Фильтрация

    @Step("Отфильтровать панель управления по категории '{category}'")
    public void filterControlPanelByCategory(String category) {
        // Передаем true, true, чтобы статусы не отсекали результаты
        controlPanelPage.fillAdvancedFilter(category, "", "", true, true);
        controlPanelPage.clickFilterApplyButton();
    }

    @Step("Отфильтровать панель управления по периоду: с '{startDate}' по '{endDate}'")
    public void filterControlPanelByDateRange(String startDate, String endDate) {
        controlPanelPage.fillAdvancedFilter("", startDate, endDate, true, true);
        controlPanelPage.clickFilterApplyButton();
    }

    @Step("Применить фильтр: Категория '{category}', с '{startDate}' по '{endDate}'")
    public void filterControlPanelByAllParams(String category, String startDate, String endDate) {
        controlPanelPage.fillAdvancedFilter(category, startDate, endDate, true, true);
        controlPanelPage.clickFilterApplyButton();
    }

    @Step("Применить фильтр без параметров")
    public void applyEmptyFilterInControlPanel() {
        controlPanelPage.fillAdvancedFilter("", "", "", true, true);
        controlPanelPage.clickFilterApplyButton();
    }

    @Step("Отфильтровать: Категория '{category}', Даты '{startDate}'-'{endDate}', Активна '{isActive}', Не активна '{isNotActive}'")
    public void filterAdvancedControlPanel(String category, String startDate, String endDate, boolean isActive, boolean isNotActive) {
        controlPanelPage.fillAdvancedFilter(category, startDate, endDate, isActive, isNotActive);
        controlPanelPage.clickFilterApplyButton();
    }
}
