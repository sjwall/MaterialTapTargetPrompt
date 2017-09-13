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

import android.app.Activity;

import org.robolectric.Robolectric;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;

import static org.mockito.Mockito.mock;

public class UnitTestUtils
{
    private UnitTestUtils() {}

    public static PromptOptions createPromptOptions()
    {
        return createPromptOptions(false);
    }

    public static PromptOptions createPromptOptions(final boolean mock)
    {
        final ResourceFinder resourceFinder;
        if (mock)
        {
            resourceFinder = mock(ResourceFinder.class);
        }
        else
        {
            resourceFinder = new ActivityResourceFinder(Robolectric.buildActivity(Activity.class)
                    .create().get());
        }
        return createPromptOptions(resourceFinder);
    }

    public static PromptOptions createPromptOptions(final ResourceFinder resourceFinder)
    {
        return new PromptOptions(resourceFinder);
    }
}
