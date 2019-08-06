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

package uk.co.samuelwall.materialtaptargetprompt.extras.focals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.TestPromptText;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class RectanglePromptFocalUnitTest
{

    private PromptOptions createOptions(Rect clipBounds, final RectF targetBounds)
    {
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        final ViewGroup parentView = options.getResourceFinder().getPromptParentView();
        when(parentView.getWidth()).thenReturn(clipBounds.width());
        when(parentView.getRight()).thenReturn(clipBounds.right);
        when(parentView.getLeft()).thenReturn(clipBounds.left);
        options.load(-1);
        final View target = mock(View.class);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                final int[] position = (int[]) args[0];
                position[0] = (int) targetBounds.left;
                position[1] = (int) targetBounds.top;
                return null;
            }
        }).when(target).getLocationInWindow(new int[2]);
        when(target.getWidth()).thenReturn((int) targetBounds.width());
        when(target.getHeight()).thenReturn((int) targetBounds.height());
        options.setTarget(target);
        options.setPromptFocal(new RectanglePromptFocal());
        options.setPromptText(new TestPromptText(300f));
        options.setPrimaryText("Primary Text");
        options.setSecondaryText("Secondary Text");
        options.setFocalPadding(20f);
        options.setTextPadding(20f);
        options.setMaxTextWidth(300f);
        options.setTextSeparation(20f);
        options.setFocalRadius(60f);

        options.create();

        options.getPromptText().prepare(options, false, clipBounds);
        options.getPromptText().update(options, 1, 1);
        return options;
    }

    @Test
    public void testRectanglePromptFocal()
    {
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF targetBounds = new RectF(1036, 1876, 1080, 1920);
        final Canvas canvas = mock(Canvas.class);
        final PromptOptions options = createOptions(clipBounds, targetBounds);
        final RectanglePromptFocal promptFocal = (RectanglePromptFocal) options.getPromptFocal();
        promptFocal.setColour(Color.GREEN);
        promptFocal.setTargetPadding(40);
        promptFocal.setCornerRadius(40, 40);
        promptFocal.setSize(null);
        promptFocal.prepare(options, options.getTargetView(), new int[]{0,0});
        promptFocal.draw(canvas);
        assertEquals(996, promptFocal.mBaseBounds.left, 1);
        assertEquals(1836, promptFocal.mBaseBounds.top, 1);
        assertEquals(1120, promptFocal.mBaseBounds.right, 1);
        assertEquals(1960, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(0, promptFocal.mBounds.left, 1);
        assertEquals(0, promptFocal.mBounds.top, 1);
        assertEquals(0, promptFocal.mBounds.right, 1);
        assertEquals(0, promptFocal.mBounds.bottom, 1);
        assertEquals(0, promptFocal.mRippleBounds.left, 1);
        assertEquals(0, promptFocal.mRippleBounds.top, 1);
        assertEquals(0, promptFocal.mRippleBounds.right, 1);
        assertEquals(0, promptFocal.mRippleBounds.bottom, 1);
        promptFocal.update(options, 0, 0);
        promptFocal.updateRipple(0, 0);
        promptFocal.draw(canvas);
        assertEquals(996, promptFocal.mBaseBounds.left, 1);
        assertEquals(1836, promptFocal.mBaseBounds.top, 1);
        assertEquals(1120, promptFocal.mBaseBounds.right, 1);
        assertEquals(1960, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(1058, promptFocal.mBounds.left, 1);
        assertEquals(1898, promptFocal.mBounds.top, 1);
        assertEquals(1058, promptFocal.mBounds.right, 1);
        assertEquals(1898, promptFocal.mBounds.bottom, 1);
        assertEquals(1058, promptFocal.mRippleBounds.left, 1);
        assertEquals(1898, promptFocal.mRippleBounds.top, 1);
        assertEquals(1058, promptFocal.mRippleBounds.right, 1);
        assertEquals(1898, promptFocal.mRippleBounds.bottom, 1);
        promptFocal.update(options, 0.5f, 0.5f);
        promptFocal.updateRipple(0.5f, 0.5f);
        promptFocal.draw(canvas);
        assertEquals(996, promptFocal.mBaseBounds.left, 1);
        assertEquals(1836, promptFocal.mBaseBounds.top, 1);
        assertEquals(1120, promptFocal.mBaseBounds.right, 1);
        assertEquals(1960, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(1027, promptFocal.mBounds.left, 1);
        assertEquals(1867, promptFocal.mBounds.top, 1);
        assertEquals(1089, promptFocal.mBounds.right, 1);
        assertEquals(1929, promptFocal.mBounds.bottom, 1);
        assertEquals(1027, promptFocal.mRippleBounds.left, 1);
        assertEquals(1867, promptFocal.mRippleBounds.top, 1);
        assertEquals(1089, promptFocal.mRippleBounds.right, 1);
        assertEquals(1929, promptFocal.mRippleBounds.bottom, 1);
        promptFocal.update(options, 1, 1);
        promptFocal.updateRipple(1, 1);
        promptFocal.draw(canvas);
        assertEquals(996, promptFocal.mBaseBounds.left, 1);
        assertEquals(1836, promptFocal.mBaseBounds.top, 1);
        assertEquals(1120, promptFocal.mBaseBounds.right, 1);
        assertEquals(1960, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(996, promptFocal.mBounds.left, 1);
        assertEquals(1836, promptFocal.mBounds.top, 1);
        assertEquals(1120, promptFocal.mBounds.right, 1);
        assertEquals(1960, promptFocal.mBounds.bottom, 1);
        assertEquals(996, promptFocal.mRippleBounds.left, 1);
        assertEquals(1836, promptFocal.mRippleBounds.top, 1);
        assertEquals(1120, promptFocal.mRippleBounds.right, 1);
        assertEquals(1960, promptFocal.mRippleBounds.bottom, 1);
        promptFocal.updateRipple(1.6f, 1.6f);
        promptFocal.draw(canvas);
        assertEquals(996, promptFocal.mBaseBounds.left, 1);
        assertEquals(1836, promptFocal.mBaseBounds.top, 1);
        assertEquals(1120, promptFocal.mBaseBounds.right, 1);
        assertEquals(1960, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(996, promptFocal.mBounds.left, 1);
        assertEquals(1836, promptFocal.mBounds.top, 1);
        assertEquals(1120, promptFocal.mBounds.right, 1);
        assertEquals(1960, promptFocal.mBounds.bottom, 1);
        assertEquals(958, promptFocal.mRippleBounds.left, 1);
        assertEquals(1798, promptFocal.mRippleBounds.top, 1);
        assertEquals(1157, promptFocal.mRippleBounds.right, 1);
        assertEquals(1997, promptFocal.mRippleBounds.bottom, 1);
        promptFocal.setDrawRipple(false);
        promptFocal.draw(canvas);
        promptFocal.setSize(null);
        try
        {
            promptFocal.prepare(options, 10, 10);
            fail();
        }
        catch (final UnsupportedOperationException e)
        {
            // Catch the error
        }
    }

    @Test
    public void testRectanglePromptFocal_Size()
    {
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF targetBounds = new RectF(1036, 1876, 1080, 1920);
        final Canvas canvas = mock(Canvas.class);
        final PromptOptions options = createOptions(clipBounds, targetBounds);
        final RectanglePromptFocal promptFocal = (RectanglePromptFocal) options.getPromptFocal();
        promptFocal.setTargetPadding(40);
        promptFocal.setCornerRadius(40, 40);
        promptFocal.setSize(new PointF(100, 80));
        promptFocal.prepare(options, options.getTargetView(), new int[]{0,0});
        promptFocal.draw(canvas);
        assertEquals(968, promptFocal.mBaseBounds.left, 1);
        assertEquals(1818, promptFocal.mBaseBounds.top, 1);
        assertEquals(1148, promptFocal.mBaseBounds.right, 1);
        assertEquals(1979, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(0, promptFocal.mBounds.left, 1);
        assertEquals(0, promptFocal.mBounds.top, 1);
        assertEquals(0, promptFocal.mBounds.right, 1);
        assertEquals(0, promptFocal.mBounds.bottom, 1);
        assertEquals(0, promptFocal.mRippleBounds.left, 1);
        assertEquals(0, promptFocal.mRippleBounds.top, 1);
        assertEquals(0, promptFocal.mRippleBounds.right, 1);
        assertEquals(0, promptFocal.mRippleBounds.bottom, 1);
        assertFalse(promptFocal.contains(1058, 1898));
        promptFocal.update(options, 0, 0);
        promptFocal.updateRipple(0, 0);
        promptFocal.draw(canvas);
        assertEquals(968, promptFocal.mBaseBounds.left, 1);
        assertEquals(1818, promptFocal.mBaseBounds.top, 1);
        assertEquals(1148, promptFocal.mBaseBounds.right, 1);
        assertEquals(1979, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(1058, promptFocal.mBounds.left, 1);
        assertEquals(1898, promptFocal.mBounds.top, 1);
        assertEquals(1058, promptFocal.mBounds.right, 1);
        assertEquals(1898, promptFocal.mBounds.bottom, 1);
        assertEquals(1058, promptFocal.mRippleBounds.left, 1);
        assertEquals(1898, promptFocal.mRippleBounds.top, 1);
        assertEquals(1058, promptFocal.mRippleBounds.right, 1);
        assertEquals(1898, promptFocal.mRippleBounds.bottom, 1);
        assertFalse(promptFocal.contains(1058, 1898));
        promptFocal.update(options, 0.5f, 0.5f);
        promptFocal.updateRipple(0.5f, 0.5f);
        promptFocal.draw(canvas);
        assertEquals(968, promptFocal.mBaseBounds.left, 1);
        assertEquals(1818, promptFocal.mBaseBounds.top, 1);
        assertEquals(1148, promptFocal.mBaseBounds.right, 1);
        assertEquals(1979, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(1013, promptFocal.mBounds.left, 1);
        assertEquals(1858, promptFocal.mBounds.top, 1);
        assertEquals(1103, promptFocal.mBounds.right, 1);
        assertEquals(1938, promptFocal.mBounds.bottom, 1);
        assertEquals(1013, promptFocal.mRippleBounds.left, 1);
        assertEquals(1858, promptFocal.mRippleBounds.top, 1);
        assertEquals(1103, promptFocal.mRippleBounds.right, 1);
        assertEquals(1938, promptFocal.mRippleBounds.bottom, 1);
        assertTrue(promptFocal.contains(1058, 1898));
        promptFocal.update(options, 1, 1);
        promptFocal.updateRipple(1, 1);
        promptFocal.draw(canvas);
        assertEquals(968, promptFocal.mBaseBounds.left, 1);
        assertEquals(1818, promptFocal.mBaseBounds.top, 1);
        assertEquals(1148, promptFocal.mBaseBounds.right, 1);
        assertEquals(1979, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(968, promptFocal.mBounds.left, 1);
        assertEquals(1818, promptFocal.mBounds.top, 1);
        assertEquals(1148, promptFocal.mBounds.right, 1);
        assertEquals(1978, promptFocal.mBounds.bottom, 1);
        assertEquals(968, promptFocal.mRippleBounds.left, 1);
        assertEquals(1818, promptFocal.mRippleBounds.top, 1);
        assertEquals(1148, promptFocal.mRippleBounds.right, 1);
        assertEquals(1978, promptFocal.mRippleBounds.bottom, 1);
        assertTrue(promptFocal.contains(1058, 1898));
        promptFocal.updateRipple(1.6f, 1.6f);
        promptFocal.draw(canvas);
        assertEquals(968, promptFocal.mBaseBounds.left, 1);
        assertEquals(1818, promptFocal.mBaseBounds.top, 1);
        assertEquals(1148, promptFocal.mBaseBounds.right, 1);
        assertEquals(1979, promptFocal.mBaseBounds.bottom, 1);
        assertEquals(968, promptFocal.mBounds.left, 1);
        assertEquals(1818, promptFocal.mBounds.top, 1);
        assertEquals(1148, promptFocal.mBounds.right, 1);
        assertEquals(1978, promptFocal.mBounds.bottom, 1);
        assertEquals(920, promptFocal.mRippleBounds.left, 1);
        assertEquals(1770, promptFocal.mRippleBounds.top, 1);
        assertEquals(1196, promptFocal.mRippleBounds.right, 1);
        assertEquals(2026, promptFocal.mRippleBounds.bottom, 1);
        assertTrue(promptFocal.contains(1058, 1898));
        promptFocal.setDrawRipple(false);
        promptFocal.draw(canvas);
    }
}
