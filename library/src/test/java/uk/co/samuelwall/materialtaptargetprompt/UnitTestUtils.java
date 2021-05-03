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

package uk.co.samuelwall.materialtaptargetprompt;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Common method calls required to run various tests.
 */
public class UnitTestUtils
{
    private UnitTestUtils() {}

    /**
     * Creates a new {@link PromptOptions} by calling {@link #createPromptOptions(boolean)} with
     * false as parameter.
     *
     * @return The created prompt.
     */
    public static PromptOptions createPromptOptions()
    {
        return createPromptOptions(false);
    }

    /**
     * Creates a new {@link PromptOptions}.
     * With mock as false a {@link ActivityResourceFinder} will be used.
     * With mock as true a mocked {@link ResourceFinder} will be created with the
     * {@link ResourceFinder#getResources()} method mocked.
     *
     * @param mock True to mock the {@link ResourceFinder} or use a {@link ActivityResourceFinder}.
     * @return The created prompt.
     */
    public static PromptOptions createPromptOptions(final boolean mock)
    {
        final ResourceFinder resourceFinder;
        if (mock)
        {
            resourceFinder = mock(ResourceFinder.class);
            final Resources resources = mock(Resources.class);
            when(resourceFinder.getResources()).thenReturn(resources);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        }
        else
        {
            resourceFinder = new ActivityResourceFinder(Robolectric.buildActivity(Activity.class)
                    .create().get());
        }
        return new PromptOptions(resourceFinder);
    }

    /**
     * Creates a new {@link PromptOptions} with {@link TestResourceFinder}.
     *
     * @return The created options.
     */
    public static PromptOptions createPromptOptionsWithTestResourceFinder()
    {
        return new PromptOptions(new TestResourceFinder(Robolectric.buildActivity(Activity.class)
                .create().get()));
    }

    /**
     * Calls {@link PromptOptions#setSequenceListener(MaterialTapTargetPrompt.PromptStateChangeListener)}
     * with the supplied sequence item.
     *
     * @param prompt The prompt to add the listener to.
     * @param item The listener to add.
     */
    public static void initSequenceItem(final MaterialTapTargetPrompt prompt, final SequenceItem item)
    {
        prompt.mView.mPromptOptions.setSequenceListener(item);
    }

    /**
     * Robolectric isn't calling {@link Animator.AnimatorListener#onAnimationEnd(Animator)} so for
     * those use cases end needs to be manually called.
     *
     * @param prompt The prompt to listen to the current animation.
     */
    public static void endCurrentAnimation(final MaterialTapTargetPrompt prompt)
    {
        if (prompt.mAnimationCurrent != null)
        {
            if (!prompt.mAnimationCurrent.isStarted())
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
            else
            {
                prompt.mAnimationCurrent.end();
            }
        }
    }

    /**
     * Calls the show for timeout run method.
     *
     * @param prompt The prompt to call the timeout on.
     */
    public static void runPromptTimeOut(final MaterialTapTargetPrompt prompt)
    {
        // Post to prevent recursion
        prompt.mView.post(new Runnable()
        {
            @Override
            public void run()
            {
                // Manually run because the test won't wait
                prompt.mTimeoutRunnable.run();
            }
        });
    }

    /**
     * Calls the show for timeout run method.
     *
     * @param prompt The prompt to call the timeout on.
     */
    public static void runPromptMainLooperTimeOut(final MaterialTapTargetPrompt prompt)
    {
        shadowOf(Looper.getMainLooper()).postAtFrontOfQueue(prompt.mTimeoutRunnable);
    }
}
