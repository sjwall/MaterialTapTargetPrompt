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

package uk.co.samuelwall.materialtaptargetprompt;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.FrameLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.AccessibilityUtil;
import org.robolectric.annotation.AccessibilityChecks;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest extends BaseTestStateProgress
{
    private static int SCREEN_WIDTH = 1080;
    private static int SCREEN_HEIGHT = 1920;

    @Test
    public void targetView()
    {
        final MaterialTapTargetPrompt.Builder builder = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setPrimaryText("test");
        final Button button = mock(Button.class);
        builder.getResourceFinder().getPromptParentView().addView(button);
        builder.setTarget(button);
        final MaterialTapTargetPrompt prompt = builder.create();
        assertNotNull(prompt);
        prompt.show();
    }

    @Test
    public void targetViewBelowKitKat()
    {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", Build.VERSION_CODES.JELLY_BEAN_MR2);
        final MaterialTapTargetPrompt.Builder builder = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setPrimaryText("test");
        final Button button = mock(Button.class);
        when(button.getWindowToken()).then(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation)
            {
                return mock(IBinder.class);
            }
        });
        builder.getResourceFinder().getPromptParentView().addView(button);
        builder.setTarget(button);
        final MaterialTapTargetPrompt prompt = builder.create();
        assertNotNull(prompt);
        prompt.show();
    }

    @Test
    public void targetViewNotAttached()
    {
        final MaterialTapTargetPrompt.Builder builder = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setPrimaryText("test");
        final Button button = mock(Button.class);
        builder.setTarget(button);
        final MaterialTapTargetPrompt prompt = builder.show();
        assertNotNull(prompt);
    }

    @Test
    public void targetViewBelowKitKatNotAttached()
    {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", Build.VERSION_CODES.JELLY_BEAN_MR2);
        final MaterialTapTargetPrompt.Builder builder = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setPrimaryText("test");
        final Button button = mock(Button.class);
        builder.setTarget(button);
        final MaterialTapTargetPrompt prompt = builder.show();
        assertNotNull(prompt);
    }

    @Test
    public void promptTouchEventFocal()
    {
        expectedStateProgress = 5;
        final MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else if (actualStateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .show();
        assertNotNull(prompt);
        assertFalse(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }


    @Test
    public void promptTouchEventFocalDismissing()
    {
        expectedStateProgress = 4;
        createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                        if (actualStateProgress == 2)
                        {
                            prompt.dismiss();
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertFalse(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
                        }
                    }
                })
                .show();
    }

    @Test
    public void promptTouchEventFocalNoFinish()
    {
        expectedStateProgress = 3;
        final MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setAutoFinish(false)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .show();
        assertNotNull(prompt);
        assertFalse(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalCaptureEvent()
    {
        expectedStateProgress = 5;
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else if (actualStateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .show();
        assertNotNull(prompt);
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalNoListener()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .show();
        assertNotNull(prompt);
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventBackground()
    {
        expectedStateProgress = 5;
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else if (actualStateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .show();
        assertNotNull(prompt);
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
    }

    @Test
    public void promptTouchEventBackgroundDismissing()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                        if (actualStateProgress == 2)
                        {
                            prompt.finish();
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
                        }
                    }
                })
                .show();
        assertNotNull(prompt);
    }

    @Test
    public void promptTouchEventBackgroundNoDismiss()
    {
        expectedStateProgress = 3;
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setAutoDismiss(false)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .show();
        assertNotNull(prompt);
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
    }

    @Test
    public void testPromptBackButtonDismiss()
    {
        expectedStateProgress = 5;
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else if (actualStateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .show();
        assertNotNull(prompt);
        final KeyEvent.DispatcherState dispatchState = new KeyEvent.DispatcherState();
        Mockito.doAnswer(new Answer<KeyEvent.DispatcherState>()
        {
            @Override
            public KeyEvent.DispatcherState answer(final InvocationOnMock invocation)
            {
                return dispatchState;
            }
        })
        .when(prompt.mView).getKeyDispatcherState();
        assertTrue(prompt.mView.dispatchKeyEventPreIme(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)));
        assertTrue(prompt.mView.dispatchKeyEventPreIme(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK)));
    }

    @Test
    public void testDismissBeforeShow() {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.dismiss();
        prompt.show();
    }

    @Test
    public void testShowWhileDismissing() {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.show();
        prompt.dismiss();
        prompt.show();
        assertTrue(prompt.isStarting());
    }

    @Test
    public void testShowWhileShowing()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState = MaterialTapTargetPrompt.STATE_REVEALED;
        prompt.show();
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.mState);
    }

    @Test
    public void testShowWhileShowingWithPress()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.show();
        prompt.mState = MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED;
        prompt.show();
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.mState);
    }

    @Test
    public void testShowForWhileShowing()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState = MaterialTapTargetPrompt.STATE_REVEALED;
        prompt.showFor(2000);
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.mState);
    }

    @Test
    public void testFinishWhileFinished()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.show();
        prompt.mState = MaterialTapTargetPrompt.STATE_FINISHED;
        prompt.finish();
        assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.mState);
    }

    @Test
    public void testShowFor()
    {
        expectedStateProgress = 5;
        createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (actualStateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (actualStateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                            UnitTestUtils.runPromptTimeOut(prompt);
                        }
                        else if (actualStateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_SHOW_FOR_TIMEOUT, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_SHOW_FOR_TIMEOUT, prompt.getState());
                        }
                        else if (actualStateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                            UnitTestUtils.endCurrentAnimation(prompt);
                        }
                        else if (actualStateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    actualStateProgress, state));
                        }
                        actualStateProgress++;
                    }
                })
                .showFor(1000);
    }

    @Test
    public void testCancelShowFor()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .showFor(2000);
        assertNotNull(prompt);
        prompt.cancelShowForTimer();
        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.mState);
    }

    @Test
    public void testStateGetters_NOT_SHOWN()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState = MaterialTapTargetPrompt.STATE_NOT_SHOWN;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_REVEALING()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState =  MaterialTapTargetPrompt.STATE_REVEALING;
        assertTrue(prompt.isStarting());
        assertFalse(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_REVEALED()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState =  MaterialTapTargetPrompt.STATE_REVEALED;
        assertTrue(prompt.isStarting());
        assertFalse(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_PRESSED()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState =  MaterialTapTargetPrompt.STATE_FOCAL_PRESSED;
        assertFalse(prompt.isStarting());
        assertFalse(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_FINISHED()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState =  MaterialTapTargetPrompt.STATE_FINISHED;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertTrue(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_DISMISSING()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState =  MaterialTapTargetPrompt.STATE_DISMISSING;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertTrue(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_DISMISSED()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        assertNotNull(prompt);
        prompt.mState =  MaterialTapTargetPrompt.STATE_DISMISSED;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertTrue(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_FINISHING()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .show();
        assertNotNull(prompt);
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_FINISHING;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertTrue(prompt.isDismissing());
    }

    @Test
    public void testNullFocalPath()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptFocal(new PromptFocal() {
                    @Override
                    public void setColour(int colour)
                    {

                    }

                    @NonNull
                    @Override
                    public RectF getBounds()
                    {
                        return new RectF(0, 0, 10, 10);
                    }

                    @Override
                    public void prepare(@NonNull PromptOptions options, @NonNull View target,
                                        int[] promptViewPosition)
                    {

                    }

                    @Override
                    public void prepare(@NonNull PromptOptions options, float targetX,
                                        float targetY)
                    {

                    }

                    @Override
                    public void updateRipple(float revealModifier, float alphaModifier)
                    {

                    }

                    @Override
                    public void update(@NonNull PromptOptions options, float revealModifier,
                                       float alphaModifier)
                    {

                    }

                    @Override
                    public void draw(@NonNull Canvas canvas)
                    {

                    }

                    @Override
                    public boolean contains(float x, float y)
                    {
                        return false;
                    }
                })
                .show();
        assertNotNull(prompt);
        prompt.show();
        assertTrue(prompt.isStarting());
    }

    @Test
    public void testIdleAnimationDisabled()
    {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setIdleAnimationEnabled(false)
                .create();
        assertNotNull(prompt);
        prompt.show();
        assertNull(prompt.mAnimationFocalBreathing);
    }

    @Test
    @AccessibilityChecks
    public void testPromptAccessibility() {
        MaterialTapTargetPrompt prompt = createMockBuilder(SCREEN_WIDTH, SCREEN_HEIGHT)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setIdleAnimationEnabled(false)
                .create();
        assertNotNull(prompt);
        prompt.show();

        AccessibilityUtil.setThrowExceptionForErrors(true);
        AccessibilityUtil.checkView(prompt.mView);

        prompt.mView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    @Test
    @Deprecated
    public void testAnimatorListener()
    {
        final MaterialTapTargetPrompt.AnimatorListener al = new MaterialTapTargetPrompt.AnimatorListener();
        final Animator animator = new ValueAnimator();
        al.onAnimationStart(animator);
        al.onAnimationRepeat(animator);
        al.onAnimationEnd(animator);
        al.onAnimationCancel(animator);
    }

    private MaterialTapTargetPrompt.Builder createMockBuilder(final int screenWidth,
                                                              final int screenHeight)
    {
        final MaterialTapTargetPrompt.Builder builder = spy(this.createBuilder(screenWidth, screenHeight));
        Mockito.doAnswer(new Answer<MaterialTapTargetPrompt>()
            {
                @Override
                public MaterialTapTargetPrompt answer(final InvocationOnMock invocation)
                        throws Throwable
                {
                    final MaterialTapTargetPrompt basePrompt = (MaterialTapTargetPrompt) invocation.callRealMethod();
                    if (basePrompt != null)
                    {
                        final MaterialTapTargetPrompt prompt = spy(basePrompt);
                        prompt.mView = spy(prompt.mView);


                        Mockito.doAnswer(new Answer<Void>()
                        {
                            public Void answer(InvocationOnMock invocation)
                            {
                                try
                                {
                                    invocation.callRealMethod();
                                }
                                catch (final Throwable throwable)
                                {
                                    throwable.printStackTrace();
                                }
                                prompt.mView.mClipToBounds = true;
                                prompt.mView.mClipBounds.set(0, 0, screenWidth, screenHeight);
                                return null;
                            }
                        }).when(prompt).updateClipBounds();

                        Mockito.doAnswer(new Answer<Void>()
                        {
                            @SuppressLint("WrongCall")
                            public Void answer(InvocationOnMock invocation)
                            {
                                try
                                {
                                    invocation.callRealMethod();
                                }
                                catch (Throwable throwable)
                                {
                                    throwable.printStackTrace();
                                }
                                assertNotNull(prompt.mGlobalLayoutListener);
                                prompt.mGlobalLayoutListener.onGlobalLayout();
                                prompt.prepare();
                                final Canvas canvas = mock(Canvas.class);
                                prompt.mView.onDraw(canvas);
                                prompt.mView.mPromptOptions.getPromptFocal()
                                        .update(prompt.mView.mPromptOptions, 1, 1);
                                prompt.mView.mPromptOptions.getPromptFocal()
                                        .updateRipple(1, 1);
                                prompt.mView.mPromptOptions.getPromptBackground()
                                        .update(prompt.mView.mPromptOptions, 1, 1);
                                prompt.mView.mPromptOptions.getPromptText()
                                        .update(prompt.mView.mPromptOptions, 1, 1);
                                prompt.mView.onDraw(canvas);
                                return null;
                            }
                        }).when(prompt).show();
                        assertEquals(MaterialTapTargetPrompt.STATE_NOT_SHOWN, prompt.getState());
                        return prompt;
                    }
                    return null;
                }
            }).when(builder).create();
        return builder;
    }

    private MaterialTapTargetPrompt.Builder createBuilder(final int screenWidth,
                                                          final int screenHeight)
    {
        final Activity activity = spy(Robolectric.buildActivity(Activity.class).create().get());
        final FrameLayout layout = spy(new FrameLayout(activity));
        final ResourceFinder resourceFinder = spy(new ActivityResourceFinder(activity));
        activity.setContentView(layout);
        setViewBounds(layout, screenWidth, screenHeight);
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(
                resourceFinder, 0);
        builder.setClipToView(null);
        return builder;
    }

    private void setViewBounds(final View view, final int width, final int height)
    {
        //TODO make this work for all versions
        view.setLeft(0);
        view.setRight(0);
        view.setRight(width);
        view.setBottom(height);
        final ViewParent parent = view.getParent();
        if (parent != null && ((View) parent).getRight() != 0 && ((View) parent).getBottom() != 0)
        {
            setViewBounds(((View) parent), width, height);
        }
    }
}
