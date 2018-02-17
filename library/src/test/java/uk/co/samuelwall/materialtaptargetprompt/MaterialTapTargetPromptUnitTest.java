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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Timer;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest
{
   private static int SCREEN_WIDTH = 1080;
    private static int SCREEN_HEIGHT = 1920;

    private int stateProgress;
    private int lastStateValue;

    @Before
    public void setup()
    {
        stateProgress = 0;
    }

    @After
    public void after()
    {
        if (stateProgress > 0)
        {
            Assert.assertEquals(lastStateValue, stateProgress);
        }
        stateProgress = -1;
    }

    @Test
    public void promptAnimationCancel()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .show();
        prompt.cleanUpAnimation();
        //assertEquals(1f, prompt.mRevealedAmount, 0f);

        prompt.dismiss();
        assertNotNull(prompt.mAnimationCurrent);
        prompt.mAnimationCurrent.cancel();
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptCancelFinishAnimation()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {

                    }
                })
                .show();

        prompt.finish();
        assertNotNull(prompt.mAnimationCurrent);
        prompt.mAnimationCurrent.cancel();
        assertNull(prompt.mAnimationCurrent);
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptTouchEventFocal()
    {
        lastStateValue = 5;
        final MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, prompt.getState());
                            endCurrentAnimation(prompt);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    stateProgress, state));
                        }
                        stateProgress++;
                    }
                })
                .show();
        assertFalse(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalCaptureEvent()
    {
        lastStateValue = 5;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, prompt.getState());
                            endCurrentAnimation(prompt);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    stateProgress, state));
                        }
                        stateProgress++;
                    }
                })
                .show();
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalNoListener()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .show();
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventBackground()
    {
        lastStateValue = 5;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                            endCurrentAnimation(prompt);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    stateProgress, state));
                        }
                        stateProgress++;
                    }
                })
                .show();
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
    }

    @Test
    public void testPromptBackButtonDismiss()
    {
        lastStateValue = 5;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, prompt.getState());
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                            endCurrentAnimation(prompt);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    stateProgress, state));
                        }
                        stateProgress++;
                    }
                })
                .show();
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
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.dismiss();
        prompt.show();
    }

    @Test
    public void testShowWhileDismissing() {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.dismiss();
        prompt.show();
        assertTrue(prompt.isStarting());
    }

    @Test
    public void testShowWhileShowing()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.mState = MaterialTapTargetPrompt.STATE_REVEALED;
        prompt.show();
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.mState);
    }

    @Test
    public void testShowForWhileShowing()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.mState = MaterialTapTargetPrompt.STATE_REVEALED;
        final Timer timer = new Timer();
        prompt.mShowForTimer = timer;
        prompt.showFor(2000);
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.mState);
        assertEquals(timer, prompt.mShowForTimer);
    }

    @Test
    public void testFinishWhileFinished()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState = MaterialTapTargetPrompt.STATE_FINISHED;
        prompt.finish();
        assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, prompt.mState);
    }

    @Test
    public void testShowFor()
    {
        lastStateValue = 5;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, prompt.getState());
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
                            // timer doesn't work in junit so run manually
                            prompt.mTimerTask.run();
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_SHOW_FOR_TIMEOUT, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_SHOW_FOR_TIMEOUT, prompt.getState());
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, prompt.getState());
                            endCurrentAnimation(prompt);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, prompt.getState());
                        }
                        else
                        {
                            fail(String.format("Incorrect state progress %s for state %s",
                                    stateProgress, state));
                        }
                        stateProgress++;
                    }
                })
                .showFor(1000);
        assertNotNull(prompt.mShowForTimer);
    }

    @Test
    public void testCancelShowFor()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .showFor(2000);
        assertNotNull(prompt.mShowForTimer);
        prompt.cancelShowForTimer();
        assertNull(prompt.mShowForTimer);
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
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState = MaterialTapTargetPrompt.STATE_NOT_SHOWN;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_REVEALING()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_REVEALING;
        assertTrue(prompt.isStarting());
        assertFalse(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_REVEALED()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_REVEALED;
        assertTrue(prompt.isStarting());
        assertFalse(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_PRESSED()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_FOCAL_PRESSED;
        assertFalse(prompt.isStarting());
        assertFalse(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_FINISHED()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_FINISHED;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertTrue(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_DISMISSING()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_DISMISSING;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertTrue(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_DISMISSED()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_DISMISSED;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertTrue(prompt.isDismissed());
        assertFalse(prompt.isDismissing());
    }

    @Test
    public void testStateGetters_FINISHING()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 0)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .create();
        prompt.show();
        prompt.mState =  MaterialTapTargetPrompt.STATE_FINISHING;
        assertFalse(prompt.isStarting());
        assertTrue(prompt.isComplete());
        assertFalse(prompt.isDismissed());
        assertTrue(prompt.isDismissing());
    }

    private MaterialTapTargetPrompt.Builder createBuilder(final int screenWidth,
                                              final int screenHeight, final float primaryTextWidth)
    {
        final Activity activity = spy(Robolectric.buildActivity(Activity.class).create().get());
        final FrameLayout layout = spy(new FrameLayout(activity));
        final ResourceFinder resourceFinder = spy(new ActivityResourceFinder(activity));
        activity.setContentView(layout);
        setViewBounds(layout, screenWidth, screenHeight);
        final MaterialTapTargetPrompt.Builder builder = spy(new MaterialTapTargetPrompt.Builder(resourceFinder, 0));
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
                        /*when(prompt.calculateMaxTextWidth(prompt.mView.mPrimaryTextLayout))
                                .thenReturn(primaryTextWidth);*/


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
                                prompt.mGlobalLayoutListener.onGlobalLayout();
                                prompt.prepare();
                                final Canvas canvas = mock(Canvas.class);
                                prompt.mView.onDraw(canvas);
                                prompt.mView.mPromptOptions.getPromptFocal().update(prompt.mView.mPromptOptions, 1, 1);
                                prompt.mView.mPromptOptions.getPromptFocal().updateRipple(1, 1);
                                prompt.mView.mPromptOptions.getPromptBackground().update(prompt.mView.mPromptOptions, 1, 1);
                                prompt.mView.mPromptOptions.getPromptText().update(prompt.mView.mPromptOptions, 1, 1);
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

    /**
     * Robolectric isn't calling {@link Animator.AnimatorListener#onAnimationEnd(Animator)} so for
     * those use cases end needs to be manually called.
     *
     * @param prompt The prompt to listen to the current animation.
     */
    private void endCurrentAnimation(final MaterialTapTargetPrompt prompt)
    {
        if (prompt.mAnimationCurrent != null)
        {
            prompt.mAnimationCurrent.addListener(
                new MaterialTapTargetPrompt.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {
                        animation.end();
                    }
                });
        }
    }
}
