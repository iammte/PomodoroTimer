package org.hyperskill.pomodoro.Matchers;

import android.view.View;

import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hyperskill.pomodoro.Timer.TimerView;

public class ColorTimerMatcher extends BoundedMatcher<View, TimerView> {

    private int color;

    public ColorTimerMatcher(int color) {
        super(TimerView.class);
        this.color = color;
    }

    public static ColorTimerMatcher withColor(int color) {
        return new ColorTimerMatcher(color);
    }

    @Override
    protected boolean matchesSafely(TimerView item) {
        return item.getColor() == color;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("getColor should return ").appendValue(color);
    }
}
