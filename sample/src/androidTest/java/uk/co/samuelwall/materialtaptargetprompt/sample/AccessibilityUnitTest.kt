/*
 * Copyright (C) 2016-2021 Samuel Wall
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

package uk.co.samuelwall.materialtaptargetprompt.sample

import androidx.test.espresso.Espresso
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResultUtils.matchesCheckNames
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResultUtils.matchesViews
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AccessibilityUnitTest {

    init {
        AccessibilityChecks.enable()
                .setRunChecksFromRootView(true)
                .apply {
                    setSuppressingResultMatcher(
                            allOf(
                                    matchesCheckNames(`is`("TouchTargetSizeViewCheck")),
                                    matchesViews(withId(-1))
                            )
                    )

                }
    }

    @Rule @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testPromptAccessibility() {
        // Type text and then press the button.
        Espresso.onView(withId(R.id.btn_navigation_prompt))
                .perform(ViewActions.click())

        // Check that the text was changed.
        Espresso.onView(withId(uk.co.samuelwall.materialtaptargetprompt.R.id.material_target_prompt_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
                .check(ViewAssertions.doesNotExist())
    }
}