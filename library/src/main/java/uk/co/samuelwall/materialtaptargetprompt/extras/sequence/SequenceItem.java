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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

/**
 * Represents a prompt to display in a sequence.
 */
public class SequenceItem implements MaterialTapTargetPrompt.PromptStateChangeListener
{
    /**
     * Defines the prompt in this sequence item.
     */
    @NonNull
    private final SequenceState sequenceState;

    /**
     * Lists the state changes that trigger this sequence item to complete.
     */
    @NonNull
    final List<Integer> stateChangers = new ArrayList<>();

    /**
     * Listener for this sequence item completing.
     */
    @Nullable
    private MaterialTapTargetSequence.SequenceCompleteListener sequenceListener;

    /**
     * Constructor.
     *
     * @param state The prompt that this item will show.
     */
    public SequenceItem(@NonNull final SequenceState state)
    {
        this.sequenceState = state;
    }

    /**
     * Add a state that will trigger the sequence to move on.
     *
     * @see MaterialTapTargetPrompt#STATE_REVEALING
     * @see MaterialTapTargetPrompt#STATE_REVEALED
     * @see MaterialTapTargetPrompt#STATE_FOCAL_PRESSED
     * @see MaterialTapTargetPrompt#STATE_FINISHING
     * @see MaterialTapTargetPrompt#STATE_FINISHED
     * @see MaterialTapTargetPrompt#STATE_NON_FOCAL_PRESSED
     * @see MaterialTapTargetPrompt#STATE_DISMISSING
     * @see MaterialTapTargetPrompt#STATE_DISMISSED
     *
     * @param state The state that triggers the sequence to move on.
     */
    public void addStateChanger(final int state)
    {
        this.stateChangers.add(state);
    }

    /**
     * Remove a specific state changer.
     *
     * @see MaterialTapTargetPrompt#STATE_REVEALING
     * @see MaterialTapTargetPrompt#STATE_REVEALED
     * @see MaterialTapTargetPrompt#STATE_FOCAL_PRESSED
     * @see MaterialTapTargetPrompt#STATE_FINISHING
     * @see MaterialTapTargetPrompt#STATE_FINISHED
     * @see MaterialTapTargetPrompt#STATE_NON_FOCAL_PRESSED
     * @see MaterialTapTargetPrompt#STATE_DISMISSING
     * @see MaterialTapTargetPrompt#STATE_DISMISSED
     *
     * @param state The state to remove.
     */
    public void removeStateChanger(final int state)
    {
        this.stateChangers.remove((Integer) state);
    }

    /**
     * Remove all state changers.
     */
    public void clearStateChangers()
    {
        this.stateChangers.clear();
    }

    /**
     * Set the listener for this sequence item completing.
     *
     * @param listener The item finish listener.
     */
    public void setSequenceListener(@Nullable final MaterialTapTargetSequence.SequenceCompleteListener listener)
    {
        this.sequenceListener = listener;
    }

    /**
     * Get the prompt state that this sequence item uses.
     *
     * @return The prompt state.
     */
    @NonNull
    public SequenceState getState()
    {
        return this.sequenceState;
    }

    /**
     * Show this sequence item.
     */
    public void show()
    {
        final MaterialTapTargetPrompt prompt = this.sequenceState.getPrompt();
        if (prompt != null)
        {
            this.show(prompt);
        }
        else
        {
            this.onItemComplete();
        }
    }

    /**
     * Calls {@link MaterialTapTargetPrompt#finish()} on this items states prompt.
     */
    public void finish()
    {
        final MaterialTapTargetPrompt prompt = this.sequenceState.getPrompt();
        if (prompt != null)
        {
            prompt.finish();
        }
    }

    /**
     * Calls {@link MaterialTapTargetPrompt#dismiss()} on this items states prompt.
     */
    public void dismiss()
    {
        final MaterialTapTargetPrompt prompt = this.sequenceState.getPrompt();
        if (prompt != null)
        {
            prompt.dismiss();
        }
    }

    /**
     * Show the created prompt for this sequence item.
     *
     * @param prompt The prompt to show, this will never be null here.
     */
    protected void show(@NonNull final MaterialTapTargetPrompt prompt)
    {
        prompt.show();
    }

    @Override
    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
    {
        if (this.stateChangers.contains(state))
        {
            this.onItemComplete();
        }
    }

    /**
     * Emits the {@link MaterialTapTargetSequence.SequenceCompleteListener#onSequenceComplete()} event if the listener
     * is set.
     */
    protected void onItemComplete()
    {
        if (this.sequenceListener != null)
        {
            this.sequenceListener.onSequenceComplete();
        }
    }
}
