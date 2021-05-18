/*
 * Copyright (C) 2016-2018 Samuel Wall
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

package uk.co.samuelwall.materialtaptargetprompt.sample;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class MainActivityTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void navigationPromptTest()
    {
        // Type text and then press the button.
        onView(withId(R.id.btn_navigation_prompt))
                .perform(click());
        MaterialTapTargetPrompt.PromptView promptView = mActivityRule.getActivity().findViewById(uk.co.samuelwall.materialtaptargetprompt.R.id.material_target_prompt_view);

        Assert.assertEquals(mActivityRule.getActivity().getString(R.string.menu_prompt_title), promptView.getPromptOptions().getPrimaryText());

        // Check that the text was changed.
        onView(withId(uk.co.samuelwall.materialtaptargetprompt.R.id.material_target_prompt_view))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(doesNotExist());

    }
}
