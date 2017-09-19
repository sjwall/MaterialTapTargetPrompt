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


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class PromptViewUnitTest
{
    private MaterialTapTargetPrompt.PromptView createPromptView()
    {
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        final MaterialTapTargetPrompt.PromptView promptView = spy(new MaterialTapTargetPrompt.PromptView(activity));
        promptView.mPromptOptions = new PromptOptions(new ActivityResourceFinder(activity));
        return promptView;
    }

    private MotionEvent createMotionEvent(final float x, final float y)
    {
        final MotionEvent event = spy(MotionEvent.obtain(1, SystemClock.uptimeMillis(), 0, x, y, 1, 1, 1, 1, 0, 0, 0));
        when(event.getX()).thenReturn(x);
        when(event.getY()).thenReturn(y);
        return event;
    }

    @Test
    public void testPromptView_TouchEvent_OutsideClipBounds()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipBounds = new Rect(0, 0, 1080, 1920);
        promptView.mClipToBounds = true;
        final MotionEvent event = createMotionEvent(1081, 500);
        assertFalse(promptView.onTouchEvent(event));
    }

    @Test
    public void testPromptView_TouchEvent_OutsideBackground()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipBounds = new Rect(0, 0, 1080, 1920);
        promptView.mClipToBounds = true;
        promptView.mPromptOptions.setPromptBackground(spy(new RectanglePromptBackground()));
        final MotionEvent event = createMotionEvent(10, 10);
        when(promptView.mPromptOptions.getPromptBackground().contains(10, 10)).thenReturn(false);
        assertFalse(promptView.onTouchEvent(event));
    }

    @Test
    public void testPromptView_TouchEvent_OutsideBackground_Capture()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipBounds = new Rect(0, 0, 1080, 1920);
        promptView.mClipToBounds = true;
        promptView.mPromptOptions.setCaptureTouchEventOutsidePrompt(true);
        promptView.mPromptOptions.setPromptBackground(spy(new RectanglePromptBackground()));
        final MotionEvent event = createMotionEvent(10, 10);
        when(promptView.mPromptOptions.getPromptBackground().contains(10, 10)).thenReturn(false);
        assertTrue(promptView.onTouchEvent(event));
    }

    @Test
    public void testPromptView_TouchEvent_NoCapture()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipToBounds = false;
        promptView.mPromptOptions.setCaptureTouchEventOnFocal(false);
        promptView.mPromptOptions.setPromptBackground(spy(new RectanglePromptBackground()));
        promptView.mPromptOptions.setPromptFocal(spy(new RectanglePromptFocal()));
        final MotionEvent event = createMotionEvent(10, 10);
        when(promptView.mPromptOptions.getPromptBackground().contains(10, 10)).thenReturn(true);
        when(promptView.mPromptOptions.getPromptFocal().contains(10, 10)).thenReturn(true);
        assertFalse(promptView.onTouchEvent(event));
    }

    @Test
    public void testPromptView_TouchEvent_NullListener()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipToBounds = false;
        promptView.mPromptOptions.setCaptureTouchEventOnFocal(true);
        promptView.mPromptOptions.setPromptBackground(spy(new RectanglePromptBackground()));
        promptView.mPromptOptions.setPromptFocal(spy(new RectanglePromptFocal()));
        final MotionEvent event = createMotionEvent(10, 10);
        when(promptView.mPromptOptions.getPromptBackground().contains(10, 10)).thenReturn(true);
        when(promptView.mPromptOptions.getPromptFocal().contains(10, 10)).thenReturn(true);
        assertTrue(promptView.onTouchEvent(event));
    }

    @Test
    public void testPromptView_BackButton_NoAutoDismiss_Handled()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptOptions.setAutoDismiss(false);
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
        event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0);
        when(promptView.onKeyPreIme(KeyEvent.KEYCODE_BACK, event)).thenReturn(true);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton_NoAutoDismiss()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptOptions.setAutoDismiss(false);
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
        event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton_NullListener()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
        event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        final BackButtonTestPromptTouchListener listener = new BackButtonTestPromptTouchListener();
        promptView.mPromptTouchedListener = listener;
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
        event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0);
        assertTrue(promptView.dispatchKeyEventPreIme(event));
        assertTrue(listener.success);
    }

    private static class BackButtonTestPromptTouchListener implements MaterialTapTargetPrompt.PromptView.PromptTouchedListener
    {
        public boolean success;

        @Override
        public void onFocalPressed()
        {
            fail();
        }

        @Override
        public void onNonFocalPressed()
        {
            success = true;
        }
    }

    @Test
    public void testPromptView_BackButton_UpCancelled()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptTouchedListener = new MaterialTapTargetPrompt.PromptView.PromptTouchedListener() {
            @Override
            public void onFocalPressed()
            {
                fail();
            }

            @Override
            public void onNonFocalPressed()
            {
                fail();
            }
        };
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        KeyEvent event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK, 1);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
        event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0, 0, 0, 0, KeyEvent.FLAG_CANCELED);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton_RepeatEvent()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptTouchedListener = new MaterialTapTargetPrompt.PromptView.PromptTouchedListener() {
            @Override
            public void onFocalPressed()
            {
                fail();
            }

            @Override
            public void onNonFocalPressed()
            {
                fail();
            }
        };
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        KeyEvent event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK, 1);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
        event = new KeyEvent(1, System.currentTimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton_NullDispatcherState()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptTouchedListener = new MaterialTapTargetPrompt.PromptView.PromptTouchedListener() {
            @Override
            public void onFocalPressed()
            {
                fail();
            }

            @Override
            public void onNonFocalPressed()
            {
                fail();
            }
        };
        when(promptView.getKeyDispatcherState()).thenReturn(null);
        final KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton_NotBackKey()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptTouchedListener = new MaterialTapTargetPrompt.PromptView.PromptTouchedListener() {
            @Override
            public void onFocalPressed()
            {
                fail();
            }

            @Override
            public void onNonFocalPressed()
            {
                fail();
            }
        };
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        final KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_1);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
    }

    @Test
    public void testPromptView_BackButton_Disabled()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mPromptOptions.setBackButtonDismissEnabled(false);
        promptView.mPromptTouchedListener = new MaterialTapTargetPrompt.PromptView.PromptTouchedListener() {
            @Override
            public void onFocalPressed()
            {
                fail();
            }

            @Override
            public void onNonFocalPressed()
            {
                fail();
            }
        };
        KeyEvent.DispatcherState state = new KeyEvent.DispatcherState();
        when(promptView.getKeyDispatcherState()).thenReturn(state);
        final KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        assertFalse(promptView.dispatchKeyEventPreIme(event));
    }

    @SuppressLint("WrongCall")
    @Test
    public void testPromptView_Draw_IconDrawable()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipToBounds = false;
        promptView.mIconDrawable = mock(Drawable.class);
        promptView.onDraw(mock(Canvas.class));
    }

    @SuppressLint("WrongCall")
    @Test
    public void testPromptView_Draw_RenderView()
    {
        final MaterialTapTargetPrompt.PromptView promptView = createPromptView();
        promptView.mClipToBounds = false;
        promptView.mTargetRenderView = mock(View.class);
        promptView.onDraw(mock(Canvas.class));
    }
}
