/*
 * Copyright (C) 2017 Samuel Wall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.samuelwall.materialtaptargetprompt;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.internal.util.Checks;
import android.text.Layout;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.PromptView;

/**
 * Helper class for testing prompts.
 */
public class PromptMatcher
{
    private PromptMatcher() {}

    public static ViewInteraction promptIsDisplayed()
    {
        return Espresso.onView(ViewMatchers.withId(R.id.material_target_prompt_view));
    }

    /**
     * Returns a matcher that matches {@link View}s that are {@link PromptView}
     *  and have target view set to the supplied value.
     */
    public static void promptHasTargetView(final Matcher<View> viewMatcher)
    {
        Checks.checkNotNull(viewMatcher);
        promptIsDisplayed().check(ViewAssertions.matches(new PromptViewMatcher()
        {
            @Override
            public void describeTo(Description description)
            {
                viewMatcher.describeTo(description);
            }

            @Override
            public void describeMismatchSafely(PromptView view, Description description)
            {
                description.appendText("target view was")
                        .appendValue(view.mTargetView);
            }

            @Override
            protected boolean matchesSafely(PromptView view)
            {
                return viewMatcher.matches(view.mTargetView);
            }
        }));
    }

    /**
     * Returns a matcher that matches {@link View}s that are {@link PromptView}
     *  and have primary text set to the supplied value.
     */
    public static void promptHasPrimaryText(final Matcher<String> stringMatcher)
    {
        Checks.checkNotNull(stringMatcher);
        promptIsDisplayed().check(ViewAssertions.matches(new PromptViewMatcher()
        {
            @Override
            public void describeTo(Description description)
            {
                stringMatcher.describeTo(description);
            }

            @Override
            public void describeMismatchSafely(PromptView view, Description description)
            {
                description.appendText("primary text was")
                        .appendValue(getTextLayoutTextSafely(view.mPrimaryTextLayout));
            }

            @Override
            protected boolean matchesSafely(PromptView view)
            {
                return stringMatcher.matches(getTextLayoutTextSafely(view.mPrimaryTextLayout));
            }
        }));
    }

    /**
     * Returns a matcher that matches {@link View}s that are {@link PromptView}
     *  and have secondary text set to the supplied value.
     */
    public static void promptHasSecondaryText(final Matcher<String> stringMatcher)
    {
        Checks.checkNotNull(stringMatcher);
        promptIsDisplayed().check(ViewAssertions.matches(new PromptViewMatcher()
        {
            @Override
            public void describeTo(Description description)
            {
                stringMatcher.describeTo(description);
            }

            @Override
            public void describeMismatchSafely(PromptView view, Description description)
            {
                description.appendText("secondary text was")
                        .appendValue(getTextLayoutTextSafely(view.mSecondaryTextLayout));
            }

            @Override
            protected boolean matchesSafely(PromptView view)
            {
                return stringMatcher.matches(getTextLayoutTextSafely(view.mSecondaryTextLayout));
            }
        }));
    }

    private static CharSequence getTextLayoutTextSafely(final Layout layout)
    {
        CharSequence text = null;
        if (layout != null)
        {
            text = layout.getText();
        }
        return text;
    }

    private static abstract class PromptViewMatcher extends BoundedMatcher<View, PromptView>
    {
        public PromptViewMatcher()
        {
            super(PromptView.class);
        }

        @Override
        public void describeMismatch(Object item, Description description)
        {
            if (item instanceof PromptView)
            {
                describeMismatchSafely((PromptView) item, description);
            }
            else
            {
                description.appendText("was").appendValue(item);
            }
        }

        abstract void describeMismatchSafely(PromptView view, Description description);
    }
}
