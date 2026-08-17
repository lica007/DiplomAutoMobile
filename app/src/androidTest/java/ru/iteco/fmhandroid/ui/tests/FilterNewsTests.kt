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
class FilterNewsTests {
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
    @Description("TK-39. Фильтрация по всем параметрам (Категория, Дата, Статус 'Активна')")
    @Severity(SeverityLevel.CRITICAL)
    fun filterByAllParametersActive() {
        val title = "Фильтр все параметры"

        // 1. Создаем АКТИВНУЮ новость с точной датой
        newsSteps.createNewsWithExactDate(DataHelper.getCategoryAnnouncement(), title, "Описание", DataHelper.getDateWithOffset(0), DataHelper.getCurrentTime())
        controlPanelPage.waitForControlPanelScreen()

        // 2. Применяем фильтр (Категория + Даты + ТОЛЬКО Активна)
        newsSteps.filterAdvancedControlPanel(DataHelper.getCategoryAnnouncement(), DataHelper.getDateWithOffset(0), DataHelper.getDateWithOffset(0), true, false)
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.checkNewsDisplayed(title)
    }

    @Test
    @Description("TK-34. Фильтрация по категории и диапазону дат")
    @Severity(SeverityLevel.CRITICAL)
    fun filterNewsByAllParams() {
        val title = "Тест фильтрации по категории и диапазону дат"
        val currentDate = DataHelper.getDateWithOffset(0)

        newsSteps.createNewsWithExactDate(DataHelper.getCategoryAnnouncement(), title, DataHelper.getDescription(), currentDate, DataHelper.getCurrentTime())
        controlPanelPage.waitForControlPanelScreen()

        newsSteps.filterControlPanelByAllParams(DataHelper.getCategoryAnnouncement(), currentDate, currentDate)
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.checkNewsDisplayed(title)
    }

    @Test
    @Description("TK-29. Фильтрация только по категории")
    @Severity(SeverityLevel.CRITICAL)
    fun filterNewsByCategoryOnly() {
        val title = "Тест фильтрации по категории"

        newsSteps.createNews(DataHelper.getCategoryAnnouncement(), title, DataHelper.getDescription())
        controlPanelPage.waitForControlPanelScreen()

        // Фильтр только по категории
        newsSteps.filterControlPanelByCategory(DataHelper.getCategoryAnnouncement())
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.checkNewsDisplayed(title)
    }

    @Test
    @Description("TK-38. Фильтрация с двумя неактивными чекбоксами выдает пустой список")
    @Severity(SeverityLevel.NORMAL)
    fun filterWithBothCheckboxesUnchecked() {
        newsSteps.filterAdvancedControlPanel("", "", "", false, false)

        controlPanelPage.checkEmptyListPlaceholderDisplayed()
    }

    // Баг. TK-31. Диапазон дат: «От» позже «До»"
    @Test
    @Description("TK-31. Ошибка при неверном диапазоне дат")
    @Severity(SeverityLevel.CRITICAL)
    fun filterWithInvalidDateRange() {
        val dateFrom = DataHelper.getDateWithOffset(1)
        val dateTo = DataHelper.getDateWithOffset(-1)
        val textError = "Неверно указан период";

        newsSteps.filterControlPanelByDateRange(dateFrom, dateTo)

        controlPanelPage.checkFilterErrorMessage(textError)
    }

    @Test
    @Description("TK-32. Отмена фильтрации")
    @Severity(SeverityLevel.NORMAL)
    fun cancelFilter() {
        controlPanelPage.clickFilterCancelButton()
        controlPanelPage.waitForControlPanelScreen()
    }

    @Test
    @Description("TK-33. Пустой фильтр")
    @Severity(SeverityLevel.NORMAL)
    fun emptyFilter() {
        newsSteps.applyEmptyFilterInControlPanel()
        controlPanelPage.waitForControlPanelScreen()
    }

    @Test
    @Description("TK-42. Параметры фильтра сохраняются при повторном открытии")
    @Severity(SeverityLevel.NORMAL)
    fun checkFilterParamsAreSaved() {
        newsSteps.filterAdvancedControlPanel(DataHelper.getCategoryAnnouncement(), "", "", true, true)
        controlPanelPage.waitForControlPanelScreen()

        controlPanelPage.clickFilterCancelButton()

        controlPanelPage.checkSavedFilterParams(DataHelper.getCategoryAnnouncement())
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