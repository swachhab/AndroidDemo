package com.mytaxi.android_demo.activities;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.mytaxi.android_demo.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;


/*
Test case to dial driver contact by searching with given characters
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyTaxiTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void myTaxiTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try
        {
            Thread.sleep(5000);
            //Method to check for permission pop up
            Boolean allow_popup = allowPermissionsIfNeeded();

            //Find username textbox and input username
            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.edt_username),
                             childAtPosition(
                                   childAtPosition(
                                         withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                 0),
                         isDisplayed()));
            appCompatEditText2.perform(replaceText("crazydog335"), closeSoftKeyboard());

            //Find password text box and input password
            ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edt_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
            appCompatEditText3.perform(replaceText("venture"), closeSoftKeyboard());

            //Click on Login Button
            ViewInteraction appCompatButton = onView(
                 allOf(withId(R.id.btn_login), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
            appCompatButton.perform(click());
            //Wait till user logged in
            Thread.sleep(8000);
            //Search for Search bar and type "sa"
            ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.textSearch),
                        childAtPosition(
                                allOf(withId(R.id.searchContainer),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                0),
                        isDisplayed()));
            appCompatAutoCompleteTextView.perform(typeText("sa"), closeSoftKeyboard());
            //Wait for the options to open
            Thread.sleep(8000);

            //Click by driver name on popup
            DataInteraction appCompatTextView = onData(anything())
                    .inAdapterView(childAtPosition(
                            withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),0)).atPosition(2);
            onView(withText("Sarah Scott"))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(click());
            //Verify Correct driver
            ViewInteraction textView = onView(
                allOf(withId(R.id.textViewDriverName), withText("Sarah Scott"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                1),
                        isDisplayed()));
            textView.check(matches(withText("Sarah Scott")));
            //Find phone button and click on the same
            ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
            floatingActionButton.perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

//Method to verify for popup, if popup is there, then click allow, It may happen if app build on device for first time
    private Boolean allowPermissionsIfNeeded()  {
        UiDevice mDevice;
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject allowPermissions = mDevice.findObject(new UiSelector().text("Allow"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                    return Boolean.TRUE;
                } catch (UiObjectNotFoundException e) {
                    return Boolean.FALSE;
                }

            }
        }
        return Boolean.FALSE;
    }


}
/*
END OF TEST
 */