/*
 * Copyright (C) 2018 Samuel Wall
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

package uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.TestPromptText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ImmersiveModeCompatPromptBackgroundUnitTest
{
    private PromptOptions createOptions(Rect clipBounds, RectF focalBounds)
    {
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        final ViewGroup parentView = options.getResourceFinder().getPromptParentView();
        when(parentView.getWidth()).thenReturn(clipBounds.width());
        when(parentView.getRight()).thenReturn(clipBounds.right);
        when(parentView.getLeft()).thenReturn(clipBounds.left);
        Resources.getSystem().getDisplayMetrics().widthPixels = 480;
        Resources.getSystem().getDisplayMetrics().heightPixels = 800;
        options.load(-1);
        final TestWindowManager windowManager = spy(new TestWindowManager());
        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(DisplayMetrics.class);
        doAnswer(new Answer()
        {
            @Override
            public Object answer(InvocationOnMock invocation)
            {
                final DisplayMetrics displayMetrics = invocation.getArgument(0);
                displayMetrics.widthPixels = 480;
                displayMetrics.heightPixels = 800;
                return null;
            }
        }).when(windowManager.display).getMetrics((DisplayMetrics) valueCapture.capture());
        doAnswer(new Answer()
        {
            @Override
            public Object answer(InvocationOnMock invocation)
            {
                final DisplayMetrics displayMetrics = invocation.getArgument(0);
                displayMetrics.widthPixels = 480;
                displayMetrics.heightPixels = 800;
                return null;
            }
        }).when(windowManager.display).getRealMetrics((DisplayMetrics) valueCapture.capture());
        final ImmersiveModeCompatPromptBackground promptBackground = new ImmersiveModeCompatPromptBackground(windowManager);
        promptBackground.setColour(Color.YELLOW);
        promptBackground.setCornerRadius(12, 14);
        options.setPromptBackground(promptBackground);
        options.setPromptFocal(mock(PromptFocal.class));
        when(options.getPromptFocal().getBounds()).thenReturn(focalBounds);
        options.setPromptText(new TestPromptText(300f));
        options.setPrimaryText("Primary Text");
        options.setSecondaryText("Secondary Text");
        options.setFocalPadding(20f);
        options.setTextPadding(20f);
        options.setMaxTextWidth(300f);
        options.setTextSeparation(20f);

        options.create();

        options.getPromptText().prepare(options, false, clipBounds);
        options.getPromptText().update(options, 1, 1);
        return options;
    }

    @Test
    public void testImmersiveModeCompatPromptBackground_getDisplayMetrics()
    {
        final Rect clipBounds = new Rect(0, 0, 480, 800);
        final RectF focalBounds = new RectF(300, 100, 100, 700);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final ImmersiveModeCompatPromptBackground promptBackground =
                (ImmersiveModeCompatPromptBackground) options.getPromptBackground();
        assertNotNull(promptBackground.getDisplayMetrics());
        assertEquals(480, promptBackground.getDisplayMetrics().widthPixels);
        assertEquals(800, promptBackground.getDisplayMetrics().heightPixels);
    }

    @Test
    public void testImmersiveModeCompatPromptBackground_getDisplayMetrics_JELLY_BEAN()
    {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", Build.VERSION_CODES.JELLY_BEAN);
        final Rect clipBounds = new Rect(0, 0, 480, 800);
        final RectF focalBounds = new RectF(300, 100, 100, 700);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final ImmersiveModeCompatPromptBackground promptBackground =
                (ImmersiveModeCompatPromptBackground) options.getPromptBackground();
        assertNotNull(promptBackground.getDisplayMetrics());
        assertEquals(480, promptBackground.getDisplayMetrics().widthPixels);
        assertEquals(800, promptBackground.getDisplayMetrics().heightPixels);
    }


    class TestWindowManager implements WindowManager
    {
        public Display display = mock(Display.class);

        @Override
        public Display getDefaultDisplay()
        {
            return display;
        }

        @Override
        public void removeViewImmediate(View view)
        {

        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams params)
        {

        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params)
        {

        }

        @Override
        public void removeView(View view)
        {

        }
    }
}
