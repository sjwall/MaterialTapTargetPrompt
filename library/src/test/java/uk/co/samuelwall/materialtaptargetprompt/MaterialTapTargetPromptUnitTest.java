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

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.StaticLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest
{
    Field mPromptView, mPromptViewPrimaryTextLayout, mPromptViewSecondaryTextLayout, mPromptViewPaintBackground, mPromptViewPaintFocal,
            mMaxTextWidth, mTextPadding, mBaseFocalRadius, mFocalRadius10Percent, mTargetView, mBaseLeft, mBaseTop, mAnimationCurrent,
            mRevealedAmount, mPaintPrimaryText, mPaintSecondaryText;

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
        mAnimationCurrent = setFieldAccessible(MaterialTapTargetPrompt.class, "mAnimationCurrent");
        mRevealedAmount = setFieldAccessible(MaterialTapTargetPrompt.class, "mRevealedAmount");
        mPaintPrimaryText = setFieldAccessible(MaterialTapTargetPrompt.class, "mPaintPrimaryText");
        mPaintSecondaryText = setFieldAccessible(MaterialTapTargetPrompt.class, "mPaintSecondaryText");

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
            .setFocalRadius(55f)
            .setPrimaryTextSize(30f)
            .setSecondaryTextSize(20f)
            .setPrimaryTextColour(Color.CYAN)
            .setSecondaryTextColour(Color.GRAY);

        assertTrue(builder.isTargetSet());
        MaterialTapTargetPrompt prompt = builder.show();

        assertEquals(600f, mMaxTextWidth.get(prompt));
        assertEquals(50f, mTextPadding.get(prompt));
        assertEquals(55f, mBaseFocalRadius.get(prompt));
        assertEquals(5.5f, mFocalRadius10Percent.get(prompt));
        assertNull(mTargetView.get(prompt));
        assertEquals(50f, mBaseLeft.get(prompt));
        assertEquals(40f, mBaseTop.get(prompt));
        assertEquals(30f, ((Paint) mPaintPrimaryText.get(prompt)).getTextSize(), 0f);
        assertEquals(20f, ((Paint) mPaintSecondaryText.get(prompt)).getTextSize(), 0f);
        assertEquals(Color.CYAN, ((Paint) mPaintPrimaryText.get(prompt)).getColor());
        assertEquals(Color.GRAY, ((Paint) mPaintSecondaryText.get(prompt)).getColor());

        View promptView = (View) mPromptView.get(prompt);
        assertEquals("Primary text", ((StaticLayout) mPromptViewPrimaryTextLayout.get(promptView)).getText());
        assertEquals(Color.BLUE, ((Paint) mPromptViewPaintBackground.get(promptView)).getColor());
        assertEquals(Color.GREEN, ((Paint) mPromptViewPaintFocal.get(promptView)).getColor());

        prompt.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).end();
        }
        assertNull(promptView.getParent());
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
        prompt.show();

        View view = (View) mPromptView.get(prompt);
        assertNull(mPromptViewSecondaryTextLayout.get(view));

        prompt.finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).end();
        }
        assertNull(view.getParent());
    }

    @Test
    public void promptAnimationCancel() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).cancel();
        }
        assertEquals(1f, mRevealedAmount.getFloat(prompt), 0f);
        assertNull(mAnimationCurrent.get(prompt));

        View promptView = (View) mPromptView.get(prompt);
        prompt.dismiss();
        assertNotNull(mAnimationCurrent.get(prompt));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).cancel();
        }
        assertNull(mAnimationCurrent.get(prompt));
        assertNull(promptView.getParent());
    }

    @Test
    public void promptCancelFinishAnimation() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .show();

        View promptView = (View) mPromptView.get(prompt);
        prompt.finish();
        assertNotNull(mAnimationCurrent.get(prompt));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).cancel();
        }
        assertNull(mAnimationCurrent.get(prompt));
        assertNull(promptView.getParent());
    }

    @Test
    public void promptTouchEventFocal() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                    {
                        assertTrue(tappedTarget);
                    }

                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).end();
        }

        View promptView = (View) mPromptView.get(prompt);
        assertFalse(promptView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalCaptureEvent() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                    {
                        assertTrue(tappedTarget);
                    }

                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).end();
        }

        View promptView = (View) mPromptView.get(prompt);
        assertTrue(promptView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventBackground() throws IllegalAccessException
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                    {
                        assertFalse(tappedTarget);
                    }

                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ((ValueAnimator) mAnimationCurrent.get(prompt)).end();
        }

        View promptView = (View) mPromptView.get(prompt);
        assertTrue(promptView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
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
