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
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class CirclePromptBackgroundUnitTest
{

    private PromptOptions createOptions(Rect clipBounds, RectF focalBounds)
    {
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        final ViewGroup parentView = options.getResourceFinder().getPromptParentView();
        when(parentView.getWidth()).thenReturn(clipBounds.width());
        when(parentView.getRight()).thenReturn(clipBounds.right);
        when(parentView.getLeft()).thenReturn(clipBounds.left);
        options.load(-1);
        final CirclePromptBackground promptBackground = new CirclePromptBackground();
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
    public void testCirclePromptBackground_Top_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 1876, 1080, 1920);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(760, 1436, 1060, 1856), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(280, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1058, 1898));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(560, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1058, 1898));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Top_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 1876, 44, 1920);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 1436, 320, 1856), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(280, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 1898));
        assertFalse(promptBackground.contains(22, 700));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(1898, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(1898, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(560, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 1898));
        assertFalse(promptBackground.contains(22, 600));
    }

    @Test
    public void testCirclePromptBackground_Bottom_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 0, 1080, 44);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(760, 64, 1060, 484), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(280, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1058, 22));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(1058, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(560, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1058, 22));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 0, 44, 44);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 64, 320, 484), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(280, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(22, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(560, promptBackground.mBaseRadius, 1);
        assertEquals(560, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(600, 22));
    }

    @Test
    public void testCirclePromptBackground_Centre_Top_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(948, 1788, 992, 1832);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(690, 1348, 990, 1768), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1600, promptBackground.mBasePosition.y, 1);
        assertEquals(840, promptBackground.mPosition.x, 1);
        assertEquals(1600, promptBackground.mPosition.y, 1);
        assertEquals(304, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1600, promptBackground.mBasePosition.y, 1);
        assertEquals(970, promptBackground.mPosition.x, 1);
        assertEquals(1810, promptBackground.mPosition.y, 1);
        assertEquals(304, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1600, promptBackground.mBasePosition.y, 1);
        assertEquals(905, promptBackground.mPosition.x, 1);
        assertEquals(1705, promptBackground.mPosition.y, 1);
        assertEquals(304, promptBackground.mBaseRadius, 1);
        assertEquals(152, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(840, 1600));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1600, promptBackground.mBasePosition.y, 1);
        assertEquals(840, promptBackground.mPosition.x, 1);
        assertEquals(1600, promptBackground.mPosition.y, 1);
        assertEquals(304, promptBackground.mBaseRadius, 1);
        assertEquals(304, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(840, 1600));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Centre_Top_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(88, 1788, 132, 1832);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 1348, 320, 1768), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1574, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(1574, promptBackground.mPosition.y, 1);
        assertEquals(283, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1574, promptBackground.mBasePosition.y, 1);
        assertEquals(110, promptBackground.mPosition.x, 1);
        assertEquals(1810, promptBackground.mPosition.y, 1);
        assertEquals(283, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1574, promptBackground.mBasePosition.y, 1);
        assertEquals(140, promptBackground.mPosition.x, 1);
        assertEquals(1692, promptBackground.mPosition.y, 1);
        assertEquals(283, promptBackground.mBaseRadius, 1);
        assertEquals(142, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 700));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1574, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(1574, promptBackground.mPosition.y, 1);
        assertEquals(283, promptBackground.mBaseRadius, 1);
        assertEquals(283, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 600));
    }

    @Test
    public void testCirclePromptBackground_Centre_Bottom_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(948, 88, 992, 132);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(690, 152, 990, 572), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(861, promptBackground.mBasePosition.x, 1);
        assertEquals(333, promptBackground.mBasePosition.y, 1);
        assertEquals(861, promptBackground.mPosition.x, 1);
        assertEquals(333, promptBackground.mPosition.y, 1);
        assertEquals(305, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(861, promptBackground.mBasePosition.x, 1);
        assertEquals(333, promptBackground.mBasePosition.y, 1);
        assertEquals(970, promptBackground.mPosition.x, 1);
        assertEquals(110, promptBackground.mPosition.y, 1);
        assertEquals(305, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(861, promptBackground.mBasePosition.x, 1);
        assertEquals(333, promptBackground.mBasePosition.y, 1);
        assertEquals(915, promptBackground.mPosition.x, 1);
        assertEquals(221, promptBackground.mPosition.y, 1);
        assertEquals(305, promptBackground.mBaseRadius, 1);
        assertEquals(153, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(861, 333));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(861, promptBackground.mBasePosition.x, 1);
        assertEquals(333, promptBackground.mBasePosition.y, 1);
        assertEquals(861, promptBackground.mPosition.x, 1);
        assertEquals(333, promptBackground.mPosition.y, 1);
        assertEquals(305, promptBackground.mBaseRadius, 1);
        assertEquals(305, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(861, 333));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Centre_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(88, 88, 132, 132);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();
        assertEquals(new RectF(20, 152, 320, 572), options.getPromptText().getBounds());
        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(334, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(334, promptBackground.mPosition.y, 1);
        assertEquals(292, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(334, promptBackground.mBasePosition.y, 1);
        assertEquals(110, promptBackground.mPosition.x, 1);
        assertEquals(110, promptBackground.mPosition.y, 1);
        assertEquals(292, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(334, promptBackground.mBasePosition.y, 1);
        assertEquals(140, promptBackground.mPosition.x, 1);
        assertEquals(223, promptBackground.mPosition.y, 1);
        assertEquals(292, promptBackground.mBaseRadius, 1);
        assertEquals(146, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(110, 110));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(334, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(334, promptBackground.mPosition.y, 1);
        assertEquals(292, promptBackground.mBaseRadius, 1);
        assertEquals(292, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 336));
        assertFalse(promptBackground.contains(600, 22));
    }

    @Test
    public void testCirclePromptBackground_Left_Above()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 584, 44, 628);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(813, promptBackground.mBasePosition.y, 1);
        assertEquals(169, promptBackground.mPosition.x, 1);
        assertEquals(813, promptBackground.mPosition.y, 1);
        assertEquals(306, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(813, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(606, promptBackground.mPosition.y, 1);
        assertEquals(306, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(813, promptBackground.mBasePosition.y, 1);
        assertEquals(95, promptBackground.mPosition.x, 1);
        assertEquals(709, promptBackground.mPosition.y, 1);
        assertEquals(306, promptBackground.mBaseRadius, 1);
        assertEquals(153, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(100, 643));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(813, promptBackground.mBasePosition.y, 1);
        assertEquals(169, promptBackground.mPosition.x, 1);
        assertEquals(813, promptBackground.mPosition.y, 1);
        assertEquals(306, promptBackground.mBaseRadius, 1);
        assertEquals(306, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(179, 747));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Left_Below()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 960, 44, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(779, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(96, promptBackground.mPosition.x, 1);
        assertEquals(880, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(154, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 982));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(779, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(309, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 982));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Right_Above()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 584, 1080, 628);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(931, promptBackground.mBasePosition.x, 1);
        assertEquals(825, promptBackground.mBasePosition.y, 1);
        assertEquals(931, promptBackground.mPosition.x, 1);
        assertEquals(825, promptBackground.mPosition.y, 1);
        assertEquals(308, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(931, promptBackground.mBasePosition.x, 1);
        assertEquals(825, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(606, promptBackground.mPosition.y, 1);
        assertEquals(308, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(931, promptBackground.mBasePosition.x, 1);
        assertEquals(825, promptBackground.mBasePosition.y, 1);
        assertEquals(994, promptBackground.mPosition.x, 1);
        assertEquals(715, promptBackground.mPosition.y, 1);
        assertEquals(308, promptBackground.mBaseRadius, 1);
        assertEquals(154, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(931, 825));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(931, promptBackground.mBasePosition.x, 1);
        assertEquals(825, promptBackground.mBasePosition.y, 1);
        assertEquals(931, promptBackground.mPosition.x, 1);
        assertEquals(825, promptBackground.mPosition.y, 1);
        assertEquals(308, promptBackground.mBaseRadius, 1);
        assertEquals(308, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(931, 825));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Right_Below()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 960, 1080, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(910, promptBackground.mPosition.x, 1);
        assertEquals(779, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(984, promptBackground.mPosition.x, 1);
        assertEquals(880, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(154, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(779, promptBackground.mBasePosition.y, 1);
        assertEquals(910, promptBackground.mPosition.x, 1);
        assertEquals(779, promptBackground.mPosition.y, 1);
        assertEquals(309, promptBackground.mBaseRadius, 1);
        assertEquals(309, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(200, 22));
    }
}
