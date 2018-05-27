/*
 * Copyright (C) 2016-2018 Samuel Wall
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

import android.app.Activity;
import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetSequenceTest
{
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
        if (lastStateValue > 0)
        {
            Assert.assertEquals(lastStateValue, stateProgress);
        }
        stateProgress = -1;
        lastStateValue = 0;
    }

    @Test
    public void emptyTest()
    {
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.show();
    }

    @Test
    public void emptyWithListenerTest()
    {
        lastStateValue = 1;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                stateProgress++;
            }
        });
        sequence.show();
    }

    @Test
    public void singlePromptTest()
    {
        lastStateValue = 2;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(
            new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                .setTarget(0,0)
                .setPrimaryText("Test")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        stateProgress++;
                    }
                })
                .create());
        sequence.setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                stateProgress++;
            }
        });
        sequence.show();
    }

    @Test
    public void threePromptTest()
    {
        lastStateValue = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(
                new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                        .setTarget(0,0)
                        .setPrimaryText("Test 1")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    stateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create());
        sequence.addPrompt(
                new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                        .setTarget(0,0)
                        .setPrimaryText("Test 2")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    stateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create());
        sequence.addPrompt(
                new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    stateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create());
        sequence.setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                stateProgress++;
            }
        });
        sequence.show();
    }

    @Test
    public void threePromptNoCompleteListenerTest()
    {
        lastStateValue = 3;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(
                new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                        .setTarget(0,0)
                        .setPrimaryText("Test 1")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    stateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create());
        sequence.addPrompt(
                new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                        .setTarget(0,0)
                        .setPrimaryText("Test 2")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    stateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create());
        sequence.addPrompt(
                new MaterialTapTargetPrompt.Builder(Robolectric.buildActivity(Activity.class).create().get())
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    stateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create());
        sequence.show();
    }
}
