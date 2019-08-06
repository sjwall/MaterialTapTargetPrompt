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

import android.os.Build;

import androidx.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceItem;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceItemShowFor;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceState;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceStatePromptOptions;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
public class MaterialTapTargetSequenceTest extends BaseTestStateProgress
{
    @Test
    public void emptyTest()
    {
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.show();
    }

    @Test
    public void emptyWithListenerTest()
    {
        expectedStateProgress = 1;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                actualStateProgress++;
            }
        });
        assertEquals(0, sequence.size());
        sequence.show();
    }

    @Test
    public void singlePromptTest()
    {
        expectedStateProgress = 2;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();

        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        actualStateProgress++;
                    }
                })
                .create();
        assertNotNull(prompt);
        sequence.addPrompt(prompt);
        assertEquals(1, sequence.size());
        assertEquals(prompt, sequence.get(0).getState().getPrompt());
        sequence.setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                actualStateProgress++;
            }
        });
        sequence.show();
    }

    @Test
    public void threePromptTest()
    {
        expectedStateProgress = 4;
        new MaterialTapTargetSequence()
            .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 1")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    actualStateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create())
            .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 2")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    actualStateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_FINISHED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create())
            .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    actualStateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create())
            .setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                actualStateProgress++;
            }
        })
            .show();
    }

    @Test
    public void threePromptNoCompleteListenerTest()
    {
        expectedStateProgress = 3;
        new MaterialTapTargetSequence()
            .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 1")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    actualStateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create())
            .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 2")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    actualStateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_FINISHED);
                                    prompt.dismiss();
                                }
                            }
                        })
                        .create())
            .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    actualStateProgress++;
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                                    prompt.dismiss();
                                }
                            }
                        }))
            .show();
    }

    @Test
    public void testAddPrompt()
    {
        expectedStateProgress = 5;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            actualStateProgress++;
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                        }
                    }
                }), 5000);
        assertTrue(sequence.get(0) instanceof SequenceItemShowFor);
        assertTrue(sequence.get(0).getState() instanceof SequenceStatePromptOptions);

        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            actualStateProgress++;
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                        }
                    }
                })
                .create(), 5000);
        assertTrue(sequence.get(1) instanceof SequenceItemShowFor);
        assertNotNull(sequence.get(1).getState());

        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            actualStateProgress++;
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                        }
                    }
                }));
        assertNotNull(sequence.get(2));
        assertTrue(sequence.get(2).getState() instanceof SequenceStatePromptOptions);

        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            actualStateProgress++;
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                        }
                    }
                })
                .create());
        assertNotNull(sequence.get(3));
        assertNotNull(sequence.get(3).getState());

        final SequenceItem sequenceItem = new SequenceItem(new SequenceState(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            actualStateProgress++;
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                        }
                    }
                })
                .create()));
        sequence.addPrompt(sequenceItem);
        assertEquals(sequenceItem, sequence.get(4));
        sequence.setSequenceCompleteListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                actualStateProgress++;
            }
        });
        sequence.show();
    }

    @Test
    public void testNullPrompt()
    {
        expectedStateProgress = 2;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                            actualStateProgress++;
                        }
                    }
                })
                .create())
            .addPrompt((MaterialTapTargetPrompt) null)
            .addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                            actualStateProgress++;
                        }
                    }
                }))
            .show();
    }

    @Test
    public void testFinishSequence()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.finish();
                        }
                        actualStateProgress++;
                    }
                })
            .create())
            .addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        actualStateProgress++;
                    }
                }))
            .show();
    }

    @Test
    public void testDismissSequence()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.dismiss();
                        }
                        actualStateProgress++;
                    }
                })
            .create())
            .addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                            prompt.dismiss();
                        }
                        actualStateProgress++;
                    }
                }))
            .show();
    }

    @Test
    public void testFinishSequenceBeforeShow()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.finish();
                        }
                        actualStateProgress++;
                    }
                })
                .create())
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                actualStateProgress++;
                            }
                        }))
                .finish()
                .show();
    }

    @Test
    public void testDismissSequenceBeforeShow()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.dismiss();
                        }
                        actualStateProgress++;
                    }
                })
                .create())
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                                    prompt.dismiss();
                                }
                                actualStateProgress++;
                            }
                        }))
                .dismiss()
                .show();
    }

    @Test
    public void testFinishSequenceBeforeShowOutOfRange()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.finish();
                        }
                        actualStateProgress++;
                    }
                })
                .create())
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                actualStateProgress++;
                            }
                        }));
        sequence.nextPromptIndex = 1000;
        sequence.finish()
            .show();
    }

    @Test
    public void testDismissSequenceBeforeShowOutOfRange()
    {
        expectedStateProgress = 4;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.dismiss();
                        }
                        actualStateProgress++;
                    }
                })
                .create())
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                                {
                                    prompt.onPromptStateChanged(MaterialTapTargetPrompt.STATE_DISMISSED);
                                    prompt.dismiss();
                                }
                                actualStateProgress++;
                            }
                        }));
        sequence.nextPromptIndex = 1000;
        sequence.dismiss()
            .show();
    }



    @Test
    public void testFinishSequenceBeforeShowNullPrompt()
    {
        expectedStateProgress = 2;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(new SequenceItem(new SequenceState(null)))
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                actualStateProgress++;
                            }
                        }));
        sequence.nextPromptIndex = 0;
        sequence.finish()
                .show();
    }

    @Test
    public void testDismissSequenceBeforeShowNullPrompt()
    {
        expectedStateProgress = 2;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(new SequenceItem(new SequenceState(null)))
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                actualStateProgress++;
                            }
                        }));
        sequence.nextPromptIndex = 0;
        sequence.dismiss()
            .show();
    }

    @Test
    public void testShowSequenceFromIndex()
    {
        expectedStateProgress = 6;
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 1")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_REVEALED)
                        {
                            sequence.showFromIndex(2);
                        }
                        actualStateProgress++;
                    }
                })
                .create())
                .addPrompt(UnitTestUtils.createPromptOptions()
                        .setTarget(0,0)
                        .setPrimaryText("Test 3")
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                             int state)
                            {
                                fail();
                            }
                        }))
            .addPrompt(UnitTestUtils.createPromptOptions()
                .setTarget(0,0)
                .setPrimaryText("Test 3")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt,
                                                     int state)
                    {
                        actualStateProgress++;
                    }
                }))
            .show();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShowSequenceFromIndexOutOfRange()
    {
        final MaterialTapTargetSequence sequence = new MaterialTapTargetSequence();
        sequence.showFromIndex(-1);
    }
}
