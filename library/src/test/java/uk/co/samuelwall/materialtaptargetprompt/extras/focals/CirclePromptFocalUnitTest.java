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

package uk.co.samuelwall.materialtaptargetprompt.extras.focals;

import android.graphics.Canvas;
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
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.TestPromptText;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class CirclePromptFocalUnitTest
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
        options.setPromptFocal(new CirclePromptFocal());
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
    public void testCirclePromptFocal()
    {
        final Rect clipBounds = new Rect(0, 0, 1080, 1920);
        final RectF targetBounds = new RectF(1036, 1876, 1080, 1920);
        final Canvas canvas = mock(Canvas.class);
        final PromptOptions options = createOptions(clipBounds, targetBounds);
        final CirclePromptFocal promptFocal = (CirclePromptFocal) options.getPromptFocal();
        promptFocal.prepare(options, options.getTargetView(), new int[]{0,0});
        promptFocal.draw(canvas);
        assertEquals(998, promptFocal.getBounds().left, 0);
        assertEquals(1838, promptFocal.getBounds().top, 0);
        assertEquals(1118, promptFocal.getBounds().right, 0);
        assertEquals(1958, promptFocal.getBounds().bottom, 0);
        assertEquals(60f, promptFocal.mBaseRadius, 0);
        assertEquals(0, promptFocal.mRadius, 0);
        promptFocal.update(options, 0, 0);
        promptFocal.draw(canvas);
        assertEquals(998, promptFocal.getBounds().left, 0);
        assertEquals(1838, promptFocal.getBounds().top, 0);
        assertEquals(1118, promptFocal.getBounds().right, 0);
        assertEquals(1958, promptFocal.getBounds().bottom, 0);
        assertEquals(60f, promptFocal.mBaseRadius, 0);
        assertEquals(0, promptFocal.mRadius, 0);
        promptFocal.update(options, 0.5f, 0.5f);
        promptFocal.draw(canvas);
        assertEquals(998, promptFocal.getBounds().left, 0);
        assertEquals(1838, promptFocal.getBounds().top, 0);
        assertEquals(1118, promptFocal.getBounds().right, 0);
        assertEquals(1958, promptFocal.getBounds().bottom, 0);
        assertEquals(60f, promptFocal.mBaseRadius, 0);
        assertEquals(30f, promptFocal.mRadius, 0);
        promptFocal.update(options, 1, 1);
        promptFocal.draw(canvas);
        assertEquals(998, promptFocal.getBounds().left, 0);
        assertEquals(1838, promptFocal.getBounds().top, 0);
        assertEquals(1118, promptFocal.getBounds().right, 0);
        assertEquals(1958, promptFocal.getBounds().bottom, 0);
        assertEquals(60f, promptFocal.mBaseRadius, 0);
        assertEquals(60f, promptFocal.mRadius, 0);
        promptFocal.setDrawRipple(false);
        promptFocal.draw(canvas);
    }
}
