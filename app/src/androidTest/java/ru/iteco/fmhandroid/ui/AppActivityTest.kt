package ru.iteco.fmhandroid.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AppActivity::class.java)

//    @Test
//    fun appActivityTest() {
//        val textInputEditText = onView(withId(R.id.login_text_input_layout));
//        textInputEditText.check(matches(isDisplayed()));
//        textInputEditText.perform(replaceText("login2"), closeSoftKeyboard())
//
//        val textInputEditText2 = onView(withId(R.id.password_text_input_layout));
//        textInputEditText2.check(matches(isDisplayed()));
//        textInputEditText2.perform(replaceText("password2"), closeSoftKeyboard())
//
//        val materialButton = onView(withId(R.id.enter_button));
//        materialButton.check(matches(isDisplayed()));
//        materialButton.perform(click())
//
//        val imageView = onView(
//            allOf(
//                withContentDescription("app background image"),
//                withParent(withParent(withId(R.id.main_swipe_refresh))),
//                isDisplayed()
//            )
//        )
//        imageView.check(matches(isDisplayed()))
//
//        val imageView2 = onView(
//            allOf(
//                withId(R.id.trademark_image_view),
//                withParent(
//                    allOf(
//                        withId(R.id.container_custom_app_bar_include_on_fragment_main),
//                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
//                    )
//                ),
//                isDisplayed()
//            )
//        )
//        imageView2.check(matches(isDisplayed()))
//    }
}
