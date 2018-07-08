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

package uk.co.samuelwall.materialtaptargetprompt.extras.sequence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.BaseTestStateProgress;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;
import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class SequenceItemTest extends BaseTestStateProgress
{
    @Test
    public void testConstructor()
    {
        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(32, 43)
                .setPrimaryText("Test")
                .create();
        assertNotNull(prompt);
        final SequenceState state = new SequenceState(prompt);
        final SequenceItem sequenceItem = new SequenceItem(state);
        assertEquals(state, sequenceItem.getState());
    }

    @Test
    public void testGettersAndSetters()
    {
        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(32, 43)
                .setPrimaryText("Test")
                .create();
        assertNotNull(prompt);
        final SequenceItem sequenceItem = new SequenceItem(new SequenceState(prompt));
        assertTrue(sequenceItem.stateChangers.isEmpty());
        sequenceItem.addStateChanger(MaterialTapTargetPrompt.STATE_DISMISSED);
        assertEquals(1, sequenceItem.stateChangers.size());
        sequenceItem.removeStateChanger(MaterialTapTargetPrompt.STATE_FINISHED);
        assertEquals(1, sequenceItem.stateChangers.size());
        sequenceItem.removeStateChanger(MaterialTapTargetPrompt.STATE_DISMISSED);
        assertTrue(sequenceItem.stateChangers.isEmpty());
        sequenceItem.addStateChanger(MaterialTapTargetPrompt.STATE_FINISHED);
        assertEquals(1, sequenceItem.stateChangers.size());
        sequenceItem.clearStateChangers();
        assertTrue(sequenceItem.stateChangers.isEmpty());
    }

    @Test
    public void testShow()
    {
        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(32, 43)
                .setPrimaryText("Test")
                .create();
        assertNotNull(prompt);
        final SequenceState state = new SequenceState(prompt);
        final SequenceItem sequenceItem = new SequenceItem(state);
        sequenceItem.show();
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
    }

    @Test
    public void testDismiss()
    {
        expectedStateProgress = 1;
        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(32, 43)
                .setPrimaryText("Test")
                .create();
        assertNotNull(prompt);
        final SequenceState state = new SequenceState(prompt);
        final SequenceItem sequenceItem = new SequenceItem(state);
        sequenceItem.addStateChanger(MaterialTapTargetPrompt.STATE_DISMISSED);
        sequenceItem.setSequenceListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                actualStateProgress++;
            }
        });
        UnitTestUtils.initSequenceItem(prompt, sequenceItem);
        sequenceItem.show();
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
        prompt.dismiss();
        UnitTestUtils.endCurrentAnimation(prompt);
    }

    @Test
    public void testNoStateChangers()
    {
        expectedStateProgress = 0;
        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(32, 43)
                .setPrimaryText("Test")
                .create();
        assertNotNull(prompt);
        final SequenceState state = new SequenceState(prompt);
        final SequenceItem sequenceItem = new SequenceItem(state);
        sequenceItem.setSequenceListener(new MaterialTapTargetSequence.SequenceCompleteListener() {
            @Override
            public void onSequenceComplete()
            {
                actualStateProgress++;
            }
        });
        UnitTestUtils.initSequenceItem(prompt, sequenceItem);
        sequenceItem.show();
        assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, prompt.getState());
        prompt.dismiss();
        UnitTestUtils.endCurrentAnimation(prompt);
    }

    @Test
    public void testNullPrompt()
    {
        final SequenceItem sequenceItem = new SequenceItem(new SequenceState(null));
        sequenceItem.show();
    }

    @Test
    public void testNullListener()
    {
        final MaterialTapTargetPrompt prompt = UnitTestUtils.createPromptOptions()
                .setTarget(32, 43)
                .setPrimaryText("Test")
                .create();
        assertNotNull(prompt);
        final SequenceItem sequenceItem = new SequenceItem(new SequenceState(prompt));
        UnitTestUtils.initSequenceItem(prompt, sequenceItem);
        prompt.show();
        prompt.dismiss();
    }

    @Test
    public void testNullPromptFinish()
    {
        final SequenceState state = new SequenceState(null);
        final SequenceItem sequenceItem = new SequenceItem(state);
        sequenceItem.finish();
    }

    @Test
    public void testNullPromptDismiss()
    {
        final SequenceState state = new SequenceState(null);
        final SequenceItem sequenceItem = new SequenceItem(state);
        sequenceItem.dismiss();
    }
}
