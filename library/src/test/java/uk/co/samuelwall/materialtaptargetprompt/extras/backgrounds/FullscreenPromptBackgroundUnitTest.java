/*
 * Copyright (C) 2017 Dennis van Dalen
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.ViewGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.TestPromptText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public class FullscreenPromptBackgroundUnitTest
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
        final FullscreenPromptBackground promptBackground = new FullscreenPromptBackground();
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
    public void testFullscreenPromptBackground_Top_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 480, 800);
        final RectF focalBounds = new RectF(100, 100, 100, 100);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final FullscreenPromptBackground promptBackground = (FullscreenPromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 120, 320, 540), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(100, promptBackground.mBounds.left, 1);
        assertEquals(100, promptBackground.mBounds.top, 1);
        assertEquals(100, promptBackground.mBounds.right, 1);
        assertEquals(100, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(50, promptBackground.mBounds.left, 1);
        assertEquals(50, promptBackground.mBounds.top, 1);
        assertEquals(290, promptBackground.mBounds.right, 1);
        assertEquals(450, promptBackground.mBounds.bottom, 1);
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(480, promptBackground.mBounds.right, 1);
        assertEquals(800, promptBackground.mBounds.bottom, 1);
    }

    @Test
    public void testFullscreenPromptBackground_Top_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 480, 800);
        final RectF focalBounds = new RectF(300, 100, 100, 700);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final FullscreenPromptBackground promptBackground = (FullscreenPromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 720, 320, 1140), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(200, promptBackground.mBounds.left, 1);
        assertEquals(400, promptBackground.mBounds.top, 1);
        assertEquals(200, promptBackground.mBounds.right, 1);
        assertEquals(400, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(100, promptBackground.mBounds.left, 1);
        assertEquals(200, promptBackground.mBounds.top, 1);
        assertEquals(340, promptBackground.mBounds.right, 1);
        assertEquals(600, promptBackground.mBounds.bottom, 1);
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(480, promptBackground.mBounds.right, 1);
        assertEquals(800, promptBackground.mBounds.bottom, 1);
    }

    @Test
    public void testFullscreenPromptBackground_Bottom_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 480, 800);
        final RectF focalBounds = new RectF(100, 700, 380, 100);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final FullscreenPromptBackground promptBackground = (FullscreenPromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 120, 320, 540), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(240, promptBackground.mBounds.left, 1);
        assertEquals(400, promptBackground.mBounds.top, 1);
        assertEquals(240, promptBackground.mBounds.right, 1);
        assertEquals(400, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(120, promptBackground.mBounds.left, 1);
        assertEquals(200, promptBackground.mBounds.top, 1);
        assertEquals(360, promptBackground.mBounds.right, 1);
        assertEquals(600, promptBackground.mBounds.bottom, 1);
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(480, promptBackground.mBounds.right, 1);
        assertEquals(800, promptBackground.mBounds.bottom, 1);
    }

    @Test
    public void testFullscreenPromptBackground_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 480, 800);
        final RectF focalBounds = new RectF(0, 0, 44, 44);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final FullscreenPromptBackground promptBackground = (FullscreenPromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 64, 320, 484), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(22, promptBackground.mBounds.left, 1);
        assertEquals(22, promptBackground.mBounds.top, 1);
        assertEquals(22, promptBackground.mBounds.right, 1);
        assertEquals(22, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(11, promptBackground.mBounds.left, 1);
        assertEquals(11, promptBackground.mBounds.top, 1);
        assertEquals(251, promptBackground.mBounds.right, 1);
        assertEquals(411, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(0, promptBackground.mBaseBounds.top, 1);
        assertEquals(480, promptBackground.mBaseBounds.right, 1);
        assertEquals(800, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(480, promptBackground.mBounds.right, 1);
        assertEquals(800, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(600, 22));
    }
}
