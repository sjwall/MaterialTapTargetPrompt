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
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest
{
    @Before
    public void setup()
    {
        
    }

    @Test
    public void promptFromVariables()
    {
        LinearInterpolator interpolator = new LinearInterpolator();
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
            .setTextSeparation(22f)
            .setPrimaryTextSize(30f)
            .setSecondaryTextSize(20f)
            .setPrimaryTextColour(Color.CYAN)
            .setSecondaryTextColour(Color.GRAY)
            .setFocalToTextPadding(30f)
            .setAnimationInterpolator(interpolator);

        assertTrue(builder.isTargetSet());
        MaterialTapTargetPrompt prompt = builder.show();

        setScreenWidthAndHeight(prompt, 200, 600);

        assertEquals(600f, prompt.mMaxTextWidth, 0.0f);
        assertEquals(50f, prompt.mTextPadding, 0.0f);
        assertEquals(55f, prompt.mBaseFocalRadius, 0.0f);
        assertEquals(5.5f, prompt.mFocalRadius10Percent, 0.0f);
        assertNull(prompt.mTargetView);
        assertEquals(50f, prompt.mBaseLeft, 0.0f);
        assertEquals(40f, prompt.mBaseTop, 0.0f);
        assertEquals(30f, prompt.mPaintPrimaryText.getTextSize(), 0f);
        assertEquals(20f, prompt.mPaintSecondaryText.getTextSize(), 0f);
        assertEquals(Color.CYAN, prompt.mPaintPrimaryText.getColor());
        assertEquals(Color.GRAY, prompt.mPaintSecondaryText.getColor());
        assertEquals(interpolator, prompt.mAnimationInterpolator);
        assertEquals(30f, prompt.mFocalToTextPadding, 0.0f);

        assertEquals("Primary text", prompt.mView.mPrimaryTextLayout.getText());
        assertEquals(Color.BLUE, prompt.mView.mPaintBackground.getColor());
        assertEquals(Color.GREEN, prompt.mView.mPaintFocal.getColor());
        assertEquals(22f, prompt.mView.mTextSeparation, 0.0f);

        prompt.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            prompt.mAnimationCurrent.end();
        }
        assertNull(prompt.mView.getParent());
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
    public void promptCreatedWhenSecondaryTextNotSet()
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(50, 40)
                .setPrimaryText("Primary text");
        MaterialTapTargetPrompt prompt = builder.create();
        assertNotNull(prompt);
        prompt.show();

        assertNull(prompt.mView.mSecondaryTextLayout);

        prompt.finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            prompt.mAnimationCurrent.end();
        }
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptAnimationCancel()
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            prompt.mAnimationCurrent.cancel();
        }
        assertEquals(1f, prompt.mRevealedAmount, 0f);

        prompt.dismiss();
        assertNotNull(prompt.mAnimationCurrent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            prompt.mAnimationCurrent.cancel();
        }
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptCancelFinishAnimation()
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

                    }

                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();

        prompt.finish();
        assertNotNull(prompt.mAnimationCurrent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            prompt.mAnimationCurrent.cancel();
        }
        assertNull(prompt.mAnimationCurrent);
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptTouchEventFocal()
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
            prompt.mAnimationCurrent.end();
        }

        assertFalse(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalCaptureEvent()
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
            prompt.mAnimationCurrent.end();
        }

        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalNoListener()
    {
        Activity activity = createActivity();
        MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            prompt.mAnimationCurrent.end();
        }

        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventBackground()
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
            prompt.mAnimationCurrent.end();
        }

        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
    }

    private Activity createActivity()
    {
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        activity.setContentView(new FrameLayout(activity));
        return activity;
    }

    private void setScreenWidthAndHeight(final MaterialTapTargetPrompt prompt, final int width, final int height)
    {
        final ViewGroup parent = prompt.mParentView;
        //TODO make this work for all versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            parent.setLeft(0);
            parent.setRight(0);
            parent.setRight(width);
            parent.setBottom(height);
        }
        prompt.updateFocalCentrePosition();
    }
}
