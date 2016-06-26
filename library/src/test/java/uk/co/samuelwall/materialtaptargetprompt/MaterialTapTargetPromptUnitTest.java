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
import android.graphics.Color;
import android.graphics.Paint;
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

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest
{
    Field mPromptView, mPromptViewPrimaryTextLayout, mPromptViewSecondaryTextLayout, mPromptViewPaintBackground, mPromptViewPaintFocal,
            mMaxTextWidth, mTextPadding, mBaseFocalRadius, mFocalRadius10Percent, mTargetView, mBaseLeft, mBaseTop;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException
    {
        mPromptView = setFieldAccessible(MaterialTapTargetPrompt.class, "mView");

        Activity activity = createActivity();
        MaterialTapTargetPrompt dummyPrompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(0, 0)
                .setPrimaryText("")
                .show();

        mMaxTextWidth = setFieldAccessible(MaterialTapTargetPrompt.class, "mMaxTextWidth");
        mTextPadding = setFieldAccessible(MaterialTapTargetPrompt.class, "mTextPadding");
        mBaseFocalRadius = setFieldAccessible(MaterialTapTargetPrompt.class, "mBaseFocalRadius");
        mFocalRadius10Percent = setFieldAccessible(MaterialTapTargetPrompt.class, "mFocalRadius10Percent");
        mTargetView = setFieldAccessible(MaterialTapTargetPrompt.class, "mTargetView");
        mBaseLeft = setFieldAccessible(MaterialTapTargetPrompt.class, "mBaseLeft");
        mBaseTop = setFieldAccessible(MaterialTapTargetPrompt.class, "mBaseTop");

        View view = (View) mPromptView.get(dummyPrompt);
        mPromptViewPrimaryTextLayout = setFieldAccessible(view.getClass(), "mPrimaryTextLayout");
        mPromptViewSecondaryTextLayout = setFieldAccessible(view.getClass(), "mSecondaryTextLayout");
        mPromptViewPaintBackground = setFieldAccessible(view.getClass(), "mPaintBackground");
        mPromptViewPaintFocal = setFieldAccessible(view.getClass(), "mPaintFocal");
    }

    @Test
    public void promptFromVariables() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
            .setTarget(50, 40)
            .setPrimaryText("Primary text")
            .setSecondaryText("Secondary text")
            .setMaxTextWidth(600f)
            .setTextPadding(50f)
            .setBackgroundColour(Color.BLUE)
            .setFocalColour(Color.GREEN)
            .setFocalRadius(55f);
        MaterialTapTargetPrompt prompt = builder.create();

        prompt.show();

        assertEquals(600f, mMaxTextWidth.get(prompt));
        assertEquals(50f, mTextPadding.get(prompt));
        assertEquals(55f, mBaseFocalRadius.get(prompt));
        assertEquals(5.5f, mFocalRadius10Percent.get(prompt));
        assertNull(mTargetView.get(prompt));
        assertEquals(50f, mBaseLeft.get(prompt));
        assertEquals(40f, mBaseTop.get(prompt));

        View promptView = (View) mPromptView.get(prompt);
        assertEquals("Primary text", ((StaticLayout) mPromptViewPrimaryTextLayout.get(promptView)).getText());
        assertEquals(Color.BLUE, ((Paint) mPromptViewPaintBackground.get(promptView)).getColor());
        assertEquals(Color.GREEN, ((Paint) mPromptViewPaintFocal.get(promptView)).getColor());
    }

    @Test
    public void promptNotCreatedWhenTargetNotSet()
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
                .setPrimaryText("Primary text")
                .setSecondaryText("Secondary text");
        assertNull(builder.create());
    }

    @Test
    public void promptNotCreatedWhenPrimaryTextNotSet()
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(50, 40)
                .setSecondaryText("Secondary text");
        assertNull(builder.create());
    }

    @Test
    public void promptCreatedWhenSecondaryTextNotSet() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(50, 40)
                .setPrimaryText("Primary text");
        MaterialTapTargetPrompt prompt = builder.create();
        assertNotNull(prompt);
        View view = (View) mPromptView.get(prompt);
        assertNull(mPromptViewSecondaryTextLayout.get(view));
    }

    private Activity createActivity()
    {
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        activity.setContentView(new FrameLayout(activity));
        return activity;
    }

    private Field setFieldAccessible(final Class c, final String fieldName) throws NoSuchFieldException
    {
        final Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
