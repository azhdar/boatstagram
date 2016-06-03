package com.test.boatstagram.ui.list;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import com.test.boatstagram.R;
import com.test.boatstagram.data.BoatStagramItem;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;

public class BoatStagramListActivityTest {

    @Rule
    public final ActivityTestRule<BoatStagramListActivity> boatStagramListActivity =
            new IntentsTestRule<>(BoatStagramListActivity.class);


    @Test
    public void waitAndScrollToPhotoFive() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(instanceOf(BoatStagramItem.class))
                .inAdapterView(allOf(withId(R.id.listView), isDisplayed()))
                .atPosition(5)
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFirstPhotoAndGoBack() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(instanceOf(BoatStagramItem.class))
                .inAdapterView(allOf(withId(R.id.listView), isDisplayed()))
                .atPosition(1)
                .perform(click());

        pressBack();

        onData(instanceOf(BoatStagramItem.class))
                .inAdapterView(allOf(withId(R.id.listView), isDisplayed()))
                .atPosition(1)
                .check(matches(isDisplayed()));
    }
}