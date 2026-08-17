package ru.iteco.fmhandroid.ui.tests

import android.view.View
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Description
import io.qameta.allure.kotlin.Severity
import io.qameta.allure.kotlin.SeverityLevel
import org.junit.After
import org.junit.Before
import org.junit.Ignore
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
class CreateNewsTests {

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
    @Description("ТК-12. Успешное создание новости")
    @Severity(SeverityLevel.CRITICAL)
    fun successfulNewsCreation() {
        val title = "Новая тестовая новость"

        newsSteps.createNews(DataHelper.getCategoryAnnouncement(), title, DataHelper.getDescription())

        controlPanelPage.waitForControlPanelScreen()
        controlPanelPage.checkNewsDisplayed(title)
    }

    //Баг: Ошибка при создании новости со статусом 'Не активна'. Тест-кейс TK-13
    @Test
    @Description("TK-13. Создание новости со статусом «Не активна»")
    @Severity(SeverityLevel.NORMAL)
    fun createInactiveNews() {
        val title = "Неактивная новость"

        controlPanelPage.clickAddNewsButton()
        createEditPage.waitForCreateEditScreen()

        createEditPage.selectCategory(DataHelper.getCategoryAnnouncement())
        createEditPage.enterTitle(title)
        createEditPage.setDateAndTime()
        createEditPage.enterDescription(DataHelper.getDescription())
        createEditPage.switchStatus()
        createEditPage.clickSaveButton()

        controlPanelPage.waitForControlPanelScreen()
        controlPanelPage.checkNewsDisplayed(title)
        controlPanelPage.checkNewsStatus(title, DataHelper.getStatusNotActive())
    }

    @Test
    @Description("ТК-14. Создание новости без заполнения обязательных полей")
    @Severity(SeverityLevel.CRITICAL)
    fun createNewsWithEmptyFields() {
        newsSteps.tryCreateNewsWithEmptyFields()
        createEditPage.checkErrorMessage_emptyFields()
    }

    @Test
    @Description("TK-16. Отмена создания новости")
    @Severity(SeverityLevel.NORMAL)
    fun cancelNewsCreation() {
        val title = "Новость под отмену"

        // Заполняем новость, жмем Отмена, жмем Ок в диалоге
        newsSteps.cancelNewsCreationAndStay(DataHelper.getCategoryAnnouncement(), title, DataHelper.getDescription())

        // Проверка: вернулись в Панель управления и новость не создалась
        controlPanelPage.waitForControlPanelScreen()
        controlPanelPage.checkNewsNotDisplayed(title)
    }

    @Test
    @Description("TK-18. Сохранение данных после создания")
    @Severity(SeverityLevel.CRITICAL)
    fun verifyNewsDataAfterCreation() {
        val title = "Проверка сохранения данных"
        val expectedDate = DataHelper.getDateWithOffset(5)
        val expectedTime = DataHelper.getCurrentTime()

        newsSteps.createNewsWithExactDate(
            DataHelper.getCategoryAnnouncement(),
            title,
            DataHelper.getDescription(),
            expectedDate,
            expectedTime)
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.clickEditNewsItem(title)
        createEditPage.waitForCreateEditScreen()

        createEditPage.checkCategoryText(DataHelper.getCategoryAnnouncement())
        createEditPage.checkTitleText(title)
        createEditPage.checkDescriptionText(DataHelper.getDescription())
        createEditPage.checkDateText(expectedDate)
        createEditPage.checkTimeText(expectedTime)
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