package ru.iteco.fmhandroid.ui.data;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import java.util.concurrent.TimeoutException;

public class WaitUtils {

        public static ViewAction waitDisplayed(final int viewId, final long millis) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isRoot();
                }

                @Override
                public String getDescription() {
                    return "wait for a specific view with id <" + viewId + "> has been displayed during " + millis + " millis.";
                }

                @Override
                public void perform(final UiController uiController, final View view) {
                    uiController.loopMainThreadUntilIdle();
                    final long startTime = System.currentTimeMillis();
                    final long endTime = startTime + millis;
                    final Matcher<View> matchId = withId(viewId);
                    final Matcher<View> matchDisplayed = isDisplayed();

                    do {
                        for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                            if (matchId.matches(child) && matchDisplayed.matches(child)) {
                                return;
                            }
                        }

                        uiController.loopMainThreadForAtLeast(50);
                    }
                    while (System.currentTimeMillis() < endTime);

                    // timeout happens
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(new TimeoutException())
                            .build();
                }
            };
        }
        // извлечения текста
        public static String getText(final Matcher<View> matcher) {
            final String[] text = {null};
            onView(matcher).perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isAssignableFrom(TextView.class);
                }

                @Override
                public String getDescription() {
                    return "Get text from view";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    text[0] = ((TextView) view).getText().toString();
                }
            });
            return text[0];
        }

    }