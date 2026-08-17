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
import ru.iteco.fmhandroid.ui.page.CreateEditNewsPage
import ru.iteco.fmhandroid.ui.page.LoginPage
import ru.iteco.fmhandroid.ui.page.NewsControlPanelPage
import ru.iteco.fmhandroid.ui.steps.AuthSteps
import ru.iteco.fmhandroid.ui.steps.MainSteps
import ru.iteco.fmhandroid.ui.steps.NewsSteps

@LargeTest
@RunWith(AllureAndroidJUnit4::class)
class EditingNewsTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AppActivity::class.java)

    private val authSteps = AuthSteps()
    private val mainSteps = MainSteps()
    private val newsSteps = NewsSteps()
    private val loginPage = LoginPage()
    private val createEditPage = CreateEditNewsPage()
    private val controlPanelPage = NewsControlPanelPage()
    private lateinit var decorView: View

    @Before
    fun waitForAppToLoadAndNavigate() {
        mActivityScenarioRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
        createEditPage.setDecorView(decorView)

        try {
            loginPage.waitForLoginScreen()
            authSteps.login(DataHelper.getValidLogin(), DataHelper.getValidPassword())
            authSteps.checkSuccessfulLogin()
        } catch (e: Throwable) { }
        newsSteps.navigateToNewsControlPanel()
    }

    @Test
    @Description("ТК-23. Успешное редактирование новости")
    @Severity(SeverityLevel.CRITICAL)
    fun successfulNewsEditing() {
        val initialTitle = "Новость под редактирование"
        val updatedTitle = "Измененный заголовок"
        val updatedDesc = "Измененное описание"
        val initialDesc = "Оригинальное описание"

        newsSteps.createNews(DataHelper.getCategoryAnnouncement(), initialTitle, initialDesc)
        controlPanelPage.waitForControlPanelScreen()

        newsSteps.editExistingNews(initialTitle, updatedTitle, updatedDesc)

        controlPanelPage.waitForControlPanelScreen()
        controlPanelPage.checkNewsDisplayed(updatedTitle)
    }

    @Test
    @Description("ТК-24. Сохранение новости при пустом обязательном поле в режиме редактирования")
    @Severity(SeverityLevel.NORMAL)
    fun editNewsWithEmptyTitle() {
        val title = "Новость для удаления текста"

        newsSteps.createNews(DataHelper.getCategoryAnnouncement(), title, DataHelper.getDescription())
        controlPanelPage.waitForControlPanelScreen()

        newsSteps.tryEditNewsWithEmptyTitle(title)
        createEditPage.checkErrorMessage_emptyFields()
    }

    @Test
    @Description("TK-26. Сохранение данных после редактирования")
    @Severity(SeverityLevel.CRITICAL)
    fun verifyNewsDataAfterEditing() {
        // Оригинальные данные
        val initialCategory = DataHelper.getCategoryAnnouncement()
        val initialTitle = "Старый заголовок TK-26"
        val initialDesc = "Старое описание"
        val initialDate = DataHelper.getDateWithOffset(2)
        val initialTime = DataHelper.getCurrentTime()

        // Новые данные для замены
        val updatedCategory = DataHelper.getCategoryHoliday()
        val updatedTitle = "Обновленный заголовок TK-26"
        val updatedDesc = "Обновленное описание"
        val updatedDate = DataHelper.getDateWithOffset(5)
        val updatedTime = DataHelper.getCurrentTime()

        newsSteps.createNewsWithExactDate(
            initialCategory, initialTitle, initialDesc, initialDate, initialTime
        )
        controlPanelPage.waitForControlPanelScreen()

        newsSteps.editExistingNewsWithExactDate(
            initialTitle, updatedCategory, updatedTitle, updatedDesc, updatedDate, updatedTime
        )
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.clickEditNewsItem(updatedTitle)
        createEditPage.waitForCreateEditScreen()

        createEditPage.checkCategoryText(updatedCategory)
        createEditPage.checkTitleText(updatedTitle)
        createEditPage.checkDescriptionText(updatedDesc)
        createEditPage.checkDateText(updatedDate)
        createEditPage.checkTimeText(updatedTime)
    }

    @Test
    @Description("TK-20. Изменение категории новости")
    @Severity(SeverityLevel.CRITICAL)
    fun changeNewsCategory() {
        val title = "Тест изменения категории"
        val oldCategory = DataHelper.getCategoryAnnouncement()
        val newCategory = DataHelper.getCategoryHoliday()

        newsSteps.createNews(oldCategory, title, DataHelper.getDescription())
        controlPanelPage.waitForControlPanelScreen()

        newsSteps.editNewsCategory(title, newCategory)
        controlPanelPage.waitForControlPanelScreen()

        // Открываем новость снова и проверяем, что категория успешно обновилась
        controlPanelPage.clickEditNewsItem(title)
        createEditPage.waitForCreateEditScreen()
        createEditPage.checkCategoryText(newCategory)
    }

    @Test
    @Description("TK-22. Изменение статуса новости")
    @Severity(SeverityLevel.CRITICAL)
    fun changeNewsStatus() {
        val title = "Тест изменения статуса"

        newsSteps.createNews(DataHelper.getCategoryAnnouncement(), title, DataHelper.getDescription())
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.checkNewsStatus(title, DataHelper.getStatusActive())

        newsSteps.editNewsStatus(title)
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.checkNewsStatus(title, DataHelper.getStatusNotActive())
    }

    @After
    fun logoutIfNeeded() {
        try {
            createEditPage.cancelAndConfirmExit()
        } catch (e: Throwable) {
        }
        mainSteps.logout()
    }
}