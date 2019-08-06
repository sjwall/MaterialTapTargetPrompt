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

package uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds;


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
@Config(sdk = 28)
public class RectanglePromptBackgroundUnitTest
{
    private PromptOptions createOptions(Rect clipBounds, RectF focalBounds)
    {
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        final ViewGroup parentView = options.getResourceFinder().getPromptParentView();
        when(parentView.getWidth()).thenReturn(clipBounds.width());
        when(parentView.getRight()).thenReturn(clipBounds.right);
        when(parentView.getLeft()).thenReturn(clipBounds.left);
        options.load(-1);
        final RectanglePromptBackground promptBackground = new RectanglePromptBackground();
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
    public void testRectanglePromptBackground_Top_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 1876, 1080, 1920);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(760, 1436, 1060, 1856), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(1058, promptBackground.mBounds.left, 1);
        assertEquals(1898, promptBackground.mBounds.top, 1);
        assertEquals(1058, promptBackground.mBounds.right, 1);
        assertEquals(1898, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(899, promptBackground.mBounds.left, 1);
        assertEquals(1657, promptBackground.mBounds.top, 1);
        assertEquals(1079, promptBackground.mBounds.right, 1);
        assertEquals(1919, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(1058, 1898));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(740, promptBackground.mBounds.left, 1);
        assertEquals(1416, promptBackground.mBounds.top, 1);
        assertEquals(1100, promptBackground.mBounds.right, 1);
        assertEquals(1940, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(1058, 1898));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testRectanglePromptBackground_Top_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 1876, 44, 1920);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 1436, 320, 1856), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(22, promptBackground.mBounds.left, 1);
        assertEquals(1898, promptBackground.mBounds.top, 1);
        assertEquals(22, promptBackground.mBounds.right, 1);
        assertEquals(1898, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(1, promptBackground.mBounds.left, 1);
        assertEquals(1657, promptBackground.mBounds.top, 1);
        assertEquals(181, promptBackground.mBounds.right, 1);
        assertEquals(1919, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(22, 1898));
        assertFalse(promptBackground.contains(22, 700));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(1416, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1940, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(-20, promptBackground.mBounds.left, 1);
        assertEquals(1416, promptBackground.mBounds.top, 1);
        assertEquals(340, promptBackground.mBounds.right, 1);
        assertEquals(1940, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(22, 1898));
        assertFalse(promptBackground.contains(22, 600));
    }

    @Test
    public void testRectanglePromptBackground_Bottom_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 0, 1080, 44);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(760, 64, 1060, 484), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(1058, promptBackground.mBounds.left, 1);
        assertEquals(22, promptBackground.mBounds.top, 1);
        assertEquals(1058, promptBackground.mBounds.right, 1);
        assertEquals(22, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(899, promptBackground.mBounds.left, 1);
        assertEquals(1, promptBackground.mBounds.top, 1);
        assertEquals(1079, promptBackground.mBounds.right, 1);
        assertEquals(263, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(1058, 22));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(740, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(1100, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(740, promptBackground.mBounds.left, 1);
        assertEquals(-20, promptBackground.mBounds.top, 1);
        assertEquals(1100, promptBackground.mBounds.right, 1);
        assertEquals(504, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(1058, 22));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testRectanglePromptBackground_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 0, 44, 44);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 64, 320, 484), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(22, promptBackground.mBounds.left, 1);
        assertEquals(22, promptBackground.mBounds.top, 1);
        assertEquals(22, promptBackground.mBounds.right, 1);
        assertEquals(22, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(1, promptBackground.mBounds.left, 1);
        assertEquals(1, promptBackground.mBounds.top, 1);
        assertEquals(181, promptBackground.mBounds.right, 1);
        assertEquals(263, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(-20, promptBackground.mBaseBounds.left, 1);
        assertEquals(-20, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(504, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(-20, promptBackground.mBounds.left, 1);
        assertEquals(-20, promptBackground.mBounds.top, 1);
        assertEquals(340, promptBackground.mBounds.right, 1);
        assertEquals(504, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(600, 22));
    }

    @Test
    public void testRectanglePromptBackground_Centre_Top_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(948, 1788, 992, 1832);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(690, 1348, 990, 1768), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(970, promptBackground.mBounds.left, 1);
        assertEquals(1810, promptBackground.mBounds.top, 1);
        assertEquals(970, promptBackground.mBounds.right, 1);
        assertEquals(1810, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(820, promptBackground.mBounds.left, 1);
        assertEquals(1569, promptBackground.mBounds.top, 1);
        assertEquals(991, promptBackground.mBounds.right, 1);
        assertEquals(1831, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(840, 1600));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(670, promptBackground.mBounds.left, 1);
        assertEquals(1328, promptBackground.mBounds.top, 1);
        assertEquals(1012, promptBackground.mBounds.right, 1);
        assertEquals(1852, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(840, 1600));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testRectanglePromptBackground_Centre_Top_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(88, 1788, 132, 1832);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 1348, 320, 1768), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(110, promptBackground.mBounds.left, 1);
        assertEquals(1810, promptBackground.mBounds.top, 1);
        assertEquals(110, promptBackground.mBounds.right, 1);
        assertEquals(1810, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(55, promptBackground.mBounds.left, 1);
        assertEquals(1569, promptBackground.mBounds.top, 1);
        assertEquals(225, promptBackground.mBounds.right, 1);
        assertEquals(1831, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 700));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(1328, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(1852, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(1328, promptBackground.mBounds.top, 1);
        assertEquals(340, promptBackground.mBounds.right, 1);
        assertEquals(1852, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 600));
    }

    @Test
    public void testRectanglePromptBackground_Centre_Bottom_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(948, 88, 992, 132);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(690, 152, 990, 572), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(970, promptBackground.mBounds.left, 1);
        assertEquals(110, promptBackground.mBounds.top, 1);
        assertEquals(970, promptBackground.mBounds.right, 1);
        assertEquals(110, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(820, promptBackground.mBounds.left, 1);
        assertEquals(89, promptBackground.mBounds.top, 1);
        assertEquals(991, promptBackground.mBounds.right, 1);
        assertEquals(351, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(861, 333));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(670, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(1012, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(670, promptBackground.mBounds.left, 1);
        assertEquals(68, promptBackground.mBounds.top, 1);
        assertEquals(1012, promptBackground.mBounds.right, 1);
        assertEquals(592, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(861, 333));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testRectanglePromptBackground_Centre_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(88, 88, 132, 132);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final RectanglePromptBackground promptBackground = (RectanglePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 152, 320, 572), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(0, promptBackground.mBounds.top, 1);
        assertEquals(0, promptBackground.mBounds.right, 1);
        assertEquals(0, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(110, promptBackground.mBounds.left, 1);
        assertEquals(110, promptBackground.mBounds.top, 1);
        assertEquals(110, promptBackground.mBounds.right, 1);
        assertEquals(110, promptBackground.mBounds.bottom, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(55, promptBackground.mBounds.left, 1);
        assertEquals(89, promptBackground.mBounds.top, 1);
        assertEquals(225, promptBackground.mBounds.right, 1);
        assertEquals(351, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(110, 110));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(0, promptBackground.mBaseBounds.left, 1);
        assertEquals(68, promptBackground.mBaseBounds.top, 1);
        assertEquals(340, promptBackground.mBaseBounds.right, 1);
        assertEquals(592, promptBackground.mBaseBounds.bottom, 1);
        assertEquals(0, promptBackground.mBounds.left, 1);
        assertEquals(68, promptBackground.mBounds.top, 1);
        assertEquals(340, promptBackground.mBounds.right, 1);
        assertEquals(592, promptBackground.mBounds.bottom, 1);
        assertTrue(promptBackground.contains(180, 336));
        assertFalse(promptBackground.contains(600, 22));
    }
}
