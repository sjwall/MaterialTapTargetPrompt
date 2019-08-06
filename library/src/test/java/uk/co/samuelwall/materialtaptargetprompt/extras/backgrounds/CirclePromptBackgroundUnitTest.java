/*
 * Copyright (C) 2017-2018 Samuel Wall
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
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import android.view.ViewGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptText;
import uk.co.samuelwall.materialtaptargetprompt.extras.TestPromptText;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.CirclePromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public class CirclePromptBackgroundUnitTest
{
    private PromptOptions createOptions(@NonNull Rect clipBounds, @NonNull RectF focalBounds)
    {
        return createOptions(clipBounds, focalBounds, new CirclePromptFocal());
    }

    private PromptOptions createOptions(@NonNull Rect clipBounds, @NonNull RectF focalBounds, @NonNull PromptFocal focal)
    {
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        final ViewGroup parentView = options.getResourceFinder().getPromptParentView();
        when(parentView.getWidth()).thenReturn(clipBounds.width());
        when(parentView.getRight()).thenReturn(clipBounds.right);
        when(parentView.getLeft()).thenReturn(clipBounds.left);
        options.load(-1);
        final CirclePromptBackground promptBackground = new CirclePromptBackground();
        options.setPromptBackground(promptBackground);
        options.setPromptFocal(focal);
        options.setPromptText(new TestPromptText(300f));
        options.setPrimaryText("Primary Text");
        options.setSecondaryText("Secondary Text");
        options.setFocalPadding(20f);
        options.setTextPadding(20f);
        options.setMaxTextWidth(300f);
        options.setTextSeparation(20f);

        options.create();

        if (focal instanceof CirclePromptFocal)
        {
            ((CirclePromptFocal) focal).setRadius(focalBounds.width() / 2);
        }
        else if (focal instanceof RectanglePromptFocal)
        {
            ((RectanglePromptFocal) focal).setSize(
                new PointF(focalBounds.width(), focalBounds.height()));
        }
        options.getPromptFocal().prepare(options, focalBounds.centerX(), focalBounds.centerY());
        options.getPromptFocal().update(options, 1, 1);

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
        assertEquals(1603.9606009002, promptBackground.mBasePosition.y, 1);
        assertEquals(840, promptBackground.mPosition.x, 1);
        assertEquals(1603.9606009002, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1603.9606009002, promptBackground.mBasePosition.y, 1);
        assertEquals(970, promptBackground.mPosition.x, 1);
        assertEquals(1810, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1603.9606009002, promptBackground.mBasePosition.y, 1);
        assertEquals(905, promptBackground.mPosition.x, 1);
        assertEquals(1706.9802, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(153.63579, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(840, 1600));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(1603.9606009002, promptBackground.mBasePosition.y, 1);
        assertEquals(840, promptBackground.mPosition.x, 1);
        assertEquals(1603.9606009002, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(307.2715886853, promptBackground.mRadius, 1);
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
        assertEquals(1587.8837874902, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(1587.8837874902, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1587.8837874902, promptBackground.mBasePosition.y, 1);
        assertEquals(110, promptBackground.mPosition.x, 1);
        assertEquals(1810, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1587.8837874902, promptBackground.mBasePosition.y, 1);
        assertEquals(140, promptBackground.mPosition.x, 1);
        assertEquals(1698.9419, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(147.007, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 700));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1587.8837874902, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(1587.8837874902, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(294.0139988175, promptBackground.mRadius, 1);
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
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(316.0393990999, promptBackground.mBasePosition.y, 1);
        assertEquals(840, promptBackground.mPosition.x, 1);
        assertEquals(316.0393990999, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(316.0393990999, promptBackground.mBasePosition.y, 1);
        assertEquals(970, promptBackground.mPosition.x, 1);
        assertEquals(110, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(316.0393990999, promptBackground.mBasePosition.y, 1);
        assertEquals(905, promptBackground.mPosition.x, 1);
        assertEquals(213.01971, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(153, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(861, 333));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(840, promptBackground.mBasePosition.x, 1);
        assertEquals(316.0393990999, promptBackground.mBasePosition.y, 1);
        assertEquals(840, promptBackground.mPosition.x, 1);
        assertEquals(316.0393990999, promptBackground.mPosition.y, 1);
        assertEquals(307.2715886853, promptBackground.mBaseRadius, 1);
        assertEquals(307.2715886853, promptBackground.mRadius, 1);
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
        assertEquals(332.1162125098, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(332.1162125098, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(332.1162125098, promptBackground.mBasePosition.y, 1);
        assertEquals(110, promptBackground.mPosition.x, 1);
        assertEquals(110, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(332.1162125098, promptBackground.mBasePosition.y, 1);
        assertEquals(140, promptBackground.mPosition.x, 1);
        assertEquals(221.0581, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(147.007, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(110, 110));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(332.1162125098, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(332.1162125098, promptBackground.mPosition.y, 1);
        assertEquals(294.0139988175, promptBackground.mBaseRadius, 1);
        assertEquals(294.0139988175, promptBackground.mRadius, 1);
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

        assertEquals(new RectF(20, 648, 320, 1068), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(169, promptBackground.mPosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(606, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(95, promptBackground.mPosition.x, 1);
        assertEquals(705.9996, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(156.16054, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(100, 643));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(169, promptBackground.mPosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(312.3211108015, promptBackground.mRadius, 1);
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

        assertEquals(new RectF(20, 520, 320, 940), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(22, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(96, promptBackground.mPosition.x, 1);
        assertEquals(882.0005, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(156.16055, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 982));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(312.3211108015, promptBackground.mRadius, 1);
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

        assertEquals(new RectF(760, 648, 1060, 1068), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(910, promptBackground.mPosition.x, 1);
        assertEquals(805.99915, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(606, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(984.0, promptBackground.mPosition.x, 1);
        assertEquals(705.9996, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(156.16054, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(931, 825));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mBasePosition.y, 1);
        assertEquals(910, promptBackground.mPosition.x, 1);
        assertEquals(805.9990911231, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(312.3211108015, promptBackground.mRadius, 1);
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

        assertEquals(new RectF(760, 520, 1060, 940), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(910, promptBackground.mPosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(1058, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(984, promptBackground.mPosition.x, 1);
        assertEquals(882.0004, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(156.16054, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(910, promptBackground.mBasePosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mBasePosition.y, 1);
        assertEquals(910, promptBackground.mPosition.x, 1);
        assertEquals(782.0009088769, promptBackground.mPosition.y, 1);
        assertEquals(312.3211108015, promptBackground.mBaseRadius, 1);
        assertEquals(312.3211108015, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_OutOfBounds_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(-85, 961, -41, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 520.5f, 320, 940.5f), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(821.3912032055, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(821.3912032055, promptBackground.mPosition.y, 1);
        assertEquals(345.5944388535, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(821.3912032055, promptBackground.mBasePosition.y, 1);
        assertEquals(-63, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(345.5944388535, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(821.3912032055, promptBackground.mBasePosition.y, 1);
        assertEquals(53, promptBackground.mPosition.x, 1);
        assertEquals(901.94556, promptBackground.mPosition.y, 1);
        assertEquals(345.5944388535, promptBackground.mBaseRadius, 1);
        assertEquals(172.79721, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(53, 779));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(821.3912032055, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(821.3911743164062, promptBackground.mPosition.y, 1);
        assertEquals(345.5944388535, promptBackground.mBaseRadius, 1);
        assertEquals(345.5944388535, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(53, 779));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_OutOfBounds_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1160, 961, 1204, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(760, 520.5f, 1060, 940.5f), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(982, promptBackground.mBasePosition.x, 1);
        assertEquals(767.5992646718, promptBackground.mBasePosition.y, 1);
        assertEquals(982, promptBackground.mPosition.x, 1);
        assertEquals(767.5992646718, promptBackground.mPosition.y, 1);
        assertEquals(345.8642025439, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(982, promptBackground.mBasePosition.x, 1);
        assertEquals(767.5992646718, promptBackground.mBasePosition.y, 1);
        assertEquals(1182, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(345.8642025439, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(982, promptBackground.mBasePosition.x, 1);
        assertEquals(767.5992646718, promptBackground.mBasePosition.y, 1);
        assertEquals(1082.0, promptBackground.mPosition.x, 1);
        assertEquals(875.0496826171875, promptBackground.mPosition.y, 1);
        assertEquals(345.8642025439, promptBackground.mBaseRadius, 1);
        assertEquals(172.93211364746094, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1046, 779));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(982, promptBackground.mBasePosition.x, 1);
        assertEquals(767.5992646718, promptBackground.mBasePosition.y, 1);
        assertEquals(982.0, promptBackground.mPosition.x, 1);
        assertEquals(767.5992646718, promptBackground.mPosition.y, 1);
        assertEquals(345.8642025439, promptBackground.mBaseRadius, 1);
        assertEquals(345.8642025439, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_Text_OutOfFocalBounds()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(894, 1860, 970,1904);
        final RectF textBounds = new RectF(560, 1420, 880, 1840);
        final PromptOptions options = createOptions(clipBounds, focalBounds);
        final PromptText promptText = spy(new PromptText());
        when(promptText.getBounds()).thenReturn(textBounds);
        options.setPromptText(promptText);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(560, 1420, 880, 1840), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(765, promptBackground.mBasePosition.x, 1);
        assertEquals(1686.814440166, promptBackground.mBasePosition.y, 1);
        assertEquals(765, promptBackground.mPosition.x, 1);
        assertEquals(1686.814440166, promptBackground.mPosition.y, 1);
        assertEquals(349.0199786274, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(765, promptBackground.mBasePosition.x, 1);
        assertEquals(1686.814440166, promptBackground.mBasePosition.y, 1);
        assertEquals(932, promptBackground.mPosition.x, 1);
        assertEquals(1882, promptBackground.mPosition.y, 1);
        assertEquals(349.0199786274, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(765, promptBackground.mBasePosition.x, 1);
        assertEquals(1686.814440166, promptBackground.mBasePosition.y, 1);
        assertEquals(848, promptBackground.mPosition.x, 1);
        assertEquals(1784.4072265625, promptBackground.mPosition.y, 1);
        assertEquals(349.0199786274, promptBackground.mBaseRadius, 1);
        assertEquals(174.50999450683594, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(765, 1700));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(765, promptBackground.mBasePosition.x, 1);
        assertEquals(1686.814440166, promptBackground.mBasePosition.y, 1);
        assertEquals(765, promptBackground.mPosition.x, 1);
        assertEquals(1686.814440166, promptBackground.mPosition.y, 1);
        assertEquals(349.0199786274, promptBackground.mBaseRadius, 1);
        assertEquals(349.0199786274, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1046, 1800));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 0, 144, 44);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 72, 320, 492), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(72, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(72, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(541.0397397604, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(72, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(72, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(541.0397397604, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(72, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(72, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(541.0397397604, promptBackground.mBaseRadius, 1);
        assertEquals(270.51987, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(72, promptBackground.mBasePosition.x, 1);
        assertEquals(22, promptBackground.mBasePosition.y, 1);
        assertEquals(72, promptBackground.mPosition.x, 1);
        assertEquals(22, promptBackground.mPosition.y, 1);
        assertEquals(541.0397397604, promptBackground.mBaseRadius, 1);
        assertEquals(541.0397397604, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 22));
        assertFalse(promptBackground.contains(700, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Centre_Top_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(948, 1788, 1092, 1832);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(760, 1340, 1060, 1760), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(1591.3725490196, promptBackground.mBasePosition.y, 1);
        assertEquals(930, promptBackground.mPosition.x, 1);
        assertEquals(1591.3725490196, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(1591.3725490196, promptBackground.mBasePosition.y, 1);
        assertEquals(1020, promptBackground.mPosition.x, 1);
        assertEquals(1810, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(1591.3725490196, promptBackground.mBasePosition.y, 1);
        assertEquals(975, promptBackground.mPosition.x, 1);
        assertEquals(1700.686279296875, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(157.5501251220703, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(940, 1600));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(1591.3725490196, promptBackground.mBasePosition.y, 1);
        assertEquals(930, promptBackground.mPosition.x, 1);
        assertEquals(1591.3725490196, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(315.1002354817, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(840, 1600));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Centre_Top_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(88, 1788, 232, 1832);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 1340, 320, 1760), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1576.4705882353, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(1576.4705882353, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1576.4705882353, promptBackground.mBasePosition.y, 1);
        assertEquals(160, promptBackground.mPosition.x, 1);
        assertEquals(1810, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(22, 1898));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1576.4705882353, promptBackground.mBasePosition.y, 1);
        assertEquals(165, promptBackground.mPosition.x, 1);
        assertEquals(1693.2353515625, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(145.61793518066406, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 700));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(1576.4705882353, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(1576.4705882353, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(291.2358822335, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 1572));
        assertFalse(promptBackground.contains(22, 600));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Centre_Bottom_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(948, 88, 1092, 132);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(760, 160, 1060, 580), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(328.6274509804, promptBackground.mBasePosition.y, 1);
        assertEquals(930, promptBackground.mPosition.x, 1);
        assertEquals(328.6274509804, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(328.6274509804, promptBackground.mBasePosition.y, 1);
        assertEquals(1020, promptBackground.mPosition.x, 1);
        assertEquals(110, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(328.62744140625, promptBackground.mBasePosition.y, 1);
        assertEquals(975, promptBackground.mPosition.x, 1);
        assertEquals(219.313720703125, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(157.55013, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(861, 233));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(930, promptBackground.mBasePosition.x, 1);
        assertEquals(328.6274509804, promptBackground.mBasePosition.y, 1);
        assertEquals(930, promptBackground.mPosition.x, 1);
        assertEquals(328.6274509804, promptBackground.mPosition.y, 1);
        assertEquals(315.1002354817, promptBackground.mBaseRadius, 1);
        assertEquals(315.1002354817, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(861, 333));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Centre_Bottom_Right()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(88, 88, 232, 132);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 160, 320, 580), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(343.5294117647, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(343.5294117647, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(343.5294117647, promptBackground.mBasePosition.y, 1);
        assertEquals(160, promptBackground.mPosition.x, 1);
        assertEquals(110, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(343.5294117647, promptBackground.mBasePosition.y, 1);
        assertEquals(165, promptBackground.mPosition.x, 1);
        assertEquals(226.76470947265625, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(145.61793518066406, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(110, 110));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(343.5294117647, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(343.5294117647, promptBackground.mPosition.y, 1);
        assertEquals(291.2358822335, promptBackground.mBaseRadius, 1);
        assertEquals(291.2358822335, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(180, 336));
        assertFalse(promptBackground.contains(600, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Left_Above()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 584, 144, 628);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 656, 320, 1076), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(814.6823529412, promptBackground.mBasePosition.y, 1);
        assertEquals(169, promptBackground.mPosition.x, 1);
        assertEquals(814.6823529412, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(814.6823529412, promptBackground.mBasePosition.y, 1);
        assertEquals(72, promptBackground.mPosition.x, 1);
        assertEquals(606, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(814.6823529412, promptBackground.mBasePosition.y, 1);
        assertEquals(121, promptBackground.mPosition.x, 1);
        assertEquals(710.3411865234375, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(156.16054, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(100, 643));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(169, promptBackground.mBasePosition.x, 1);
        assertEquals(814.6823529412, promptBackground.mBasePosition.y, 1);
        assertEquals(169, promptBackground.mPosition.x, 1);
        assertEquals(814.6823529412, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(311.7481558315, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(179, 747));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Left_Below()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(0, 960, 144, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 512, 320, 932), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(773.3176470588, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(773.3176470588, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(773.3176470588, promptBackground.mBasePosition.y, 1);
        assertEquals(72, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(773.3176470588, promptBackground.mBasePosition.y, 1);
        assertEquals(121, promptBackground.mPosition.x, 1);
        assertEquals(877.6588134765625, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(156.16055, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 982));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(773.3176470588, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(773.3176470588, promptBackground.mPosition.y, 1);
        assertEquals(311.7481558315, promptBackground.mBaseRadius, 1);
        assertEquals(311.7481558315, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(22, 982));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Right_Above()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 584, 1180, 628);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(760, 656, 1060, 1076), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(825.4901960784, promptBackground.mBasePosition.y, 1);
        assertEquals(974, promptBackground.mPosition.x, 1);
        assertEquals(825.4901960784, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(825.4901960784, promptBackground.mBasePosition.y, 1);
        assertEquals(1108, promptBackground.mPosition.x, 1);
        assertEquals(606, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(825.4901960784, promptBackground.mBasePosition.y, 1);
        assertEquals(1041, promptBackground.mPosition.x, 1);
        assertEquals(715.7451171875, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(171.39952087402344, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(931, 825));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(825.4901960784, promptBackground.mBasePosition.y, 1);
        assertEquals(974, promptBackground.mPosition.x, 1);
        assertEquals(825.4901960784, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(342.7990108808, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(931, 825));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Right_Below()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(1036, 960, 1180, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(760, 512, 1060, 932), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(762.5098039216, promptBackground.mBasePosition.y, 1);
        assertEquals(974, promptBackground.mPosition.x, 1);
        assertEquals(762.5098039216, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(762.5098039216, promptBackground.mBasePosition.y, 1);
        assertEquals(1108, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(762.5098039216, promptBackground.mBasePosition.y, 1);
        assertEquals(1041.0, promptBackground.mPosition.x, 1);
        assertEquals(872.2548828125, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(171.39952087402344, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(974, promptBackground.mBasePosition.x, 1);
        assertEquals(762.5098039216, promptBackground.mBasePosition.y, 1);
        assertEquals(974, promptBackground.mPosition.x, 1);
        assertEquals(762.5098039216, promptBackground.mPosition.y, 1);
        assertEquals(342.7990108808, promptBackground.mBaseRadius, 1);
        assertEquals(342.7990108808, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(910, 779));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_OutOfBounds_Left()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(-185, 961, -41, 1004);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(20, 513, 320, 933), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(875.7799607073, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(875.7799607073, promptBackground.mPosition.y, 1);
        assertEquals(400.6361190541, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(875.7799607073, promptBackground.mBasePosition.y, 1);
        assertEquals(-113, promptBackground.mPosition.x, 1);
        assertEquals(982, promptBackground.mPosition.y, 1);
        assertEquals(400.6361190541, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(875.7799607073, promptBackground.mBasePosition.y, 1);
        assertEquals(28.5, promptBackground.mPosition.x, 1);
        assertEquals(929.1400146484375, promptBackground.mPosition.y, 1);
        assertEquals(400.6361190541, promptBackground.mBaseRadius, 1);
        assertEquals(200.3180694580078, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(53, 779));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(170, promptBackground.mBasePosition.x, 1);
        assertEquals(875.7799607073, promptBackground.mBasePosition.y, 1);
        assertEquals(170, promptBackground.mPosition.x, 1);
        assertEquals(875.7799607073, promptBackground.mPosition.y, 1);
        assertEquals(400.6361190541, promptBackground.mBaseRadius, 1);
        assertEquals(400.6361190541, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(53, 779));
        assertFalse(promptBackground.contains(200, 22));
    }

    @Test
    public void testCirclePromptBackground_BaseFocal_Text_OutOfFocalBounds()
    {
        final Canvas canvas = mock(Canvas.class);
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF focalBounds = new RectF(894, 1860, 970,1904);
        final RectF textBounds = new RectF(460, 1420, 880, 1840);
        final PromptOptions options = createOptions(clipBounds, focalBounds, new RectanglePromptFocal());
        final PromptText promptText = spy(new PromptText());
        when(promptText.getBounds()).thenReturn(textBounds);
        options.setPromptText(promptText);
        final CirclePromptBackground promptBackground = (CirclePromptBackground) options.getPromptBackground();

        assertEquals(new RectF(460, 1420, 880, 1840), options.getPromptText().getBounds());

        promptBackground.prepare(options, false, clipBounds);
        promptBackground.draw(canvas);
        assertEquals(719, promptBackground.mBasePosition.x, 1);
        assertEquals(1665.5418326693, promptBackground.mBasePosition.y, 1);
        assertEquals(719, promptBackground.mPosition.x, 1);
        assertEquals(1665.5418326693, promptBackground.mPosition.y, 1);
        assertEquals(371.6608556071, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0, 0);
        promptBackground.draw(canvas);
        assertEquals(719, promptBackground.mBasePosition.x, 1);
        assertEquals(1665.5418326693, promptBackground.mBasePosition.y, 1);
        assertEquals(932, promptBackground.mPosition.x, 1);
        assertEquals(1882, promptBackground.mPosition.y, 1);
        assertEquals(371.6608556071, promptBackground.mBaseRadius, 1);
        assertEquals(0, promptBackground.mRadius, 1);
        assertFalse(promptBackground.contains(1058, 22));
        promptBackground.update(options, 0.5f, 0.5f);
        promptBackground.draw(canvas);
        assertEquals(719, promptBackground.mBasePosition.x, 1);
        assertEquals(1665.5418326693, promptBackground.mBasePosition.y, 1);
        assertEquals(825.5, promptBackground.mPosition.x, 1);
        assertEquals(1773.77099609375, promptBackground.mPosition.y, 1);
        assertEquals(371.6608556071, promptBackground.mBaseRadius, 1);
        assertEquals(185.8304443359375, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(765, 1700));
        assertFalse(promptBackground.contains(700, 22));
        promptBackground.update(options, 1, 1);
        promptBackground.draw(canvas);
        assertEquals(719, promptBackground.mBasePosition.x, 1);
        assertEquals(1665.5418326693, promptBackground.mBasePosition.y, 1);
        assertEquals(719, promptBackground.mPosition.x, 1);
        assertEquals(1665.5418326693, promptBackground.mPosition.y, 1);
        assertEquals(371.6608556071, promptBackground.mBaseRadius, 1);
        assertEquals(371.6608556071, promptBackground.mRadius, 1);
        assertTrue(promptBackground.contains(1046, 1800));
        assertFalse(promptBackground.contains(200, 22));
    }
}
