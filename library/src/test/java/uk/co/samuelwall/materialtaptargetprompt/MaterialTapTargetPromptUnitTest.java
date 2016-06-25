/*
 * Copyright (C) 2016 Samuel Wall
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
import android.text.StaticLayout;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest
{
    Field mPromptView, mPromptViewPrimaryTextLayout;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException
    {
        mPromptView = setFieldAccessible(MaterialTapTargetPrompt.class, "mView");

        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        activity.setContentView(new FrameLayout(activity));
        MaterialTapTargetPrompt dummyPrompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(0, 0)
                .setPrimaryText("")
                .show();

        View view = (View) mPromptView.get(dummyPrompt);
        mPromptViewPrimaryTextLayout = setFieldAccessible(view.getClass(), "mPrimaryTextLayout");
    }

    @Test
    public void promptFromVariables() throws IllegalAccessException
    {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        activity.setContentView(new FrameLayout(activity));
        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
            .setTarget(50, 40)
            .setPrimaryText("Primary text")
            .setSecondaryText("Secondary text");
        MaterialTapTargetPrompt prompt = builder.create();

        prompt.show();

        View promptView = (View) mPromptView.get(prompt);
        assertEquals("Primary text", ((StaticLayout) mPromptViewPrimaryTextLayout.get(promptView)).getText());
    }

    private Field setFieldAccessible(final Class c, final String fieldName) throws NoSuchFieldException
    {
        final Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
