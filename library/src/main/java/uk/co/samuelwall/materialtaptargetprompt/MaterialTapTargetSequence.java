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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceItem;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceItemShowFor;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceState;
import uk.co.samuelwall.materialtaptargetprompt.extras.sequence.SequenceStatePromptOptions;

/**
 * A Sequence of prompts to be shown one after another
 */
public class MaterialTapTargetSequence
{
    /**
     * The list of prompts to display when the sequence is shown
     */
    @NonNull
    private final List<SequenceItem> items = new ArrayList<>();

    /**
     * Pointer to the next prompt to be shown
     */
    int nextPromptIndex = -1;

    /**
     * Listener added to a sequence item for it completing.
     */
    @NonNull
    SequenceCompleteListener itemListener = new SequenceCompleteListener()
    {
        @Override
        public void onSequenceComplete()
        {
            // Cleanup current prompt
            final SequenceItem currentItem = items.get(nextPromptIndex);
            currentItem.setSequenceListener(null);
            final MaterialTapTargetPrompt prompt = currentItem.getState().getPrompt();
            if (prompt != null)
            {
                prompt.mView.mPromptOptions.setSequenceListener(null);
            }
            nextPromptIndex++;
            // Check if there is another prompt to show
            if (items.size() > nextPromptIndex)
            {
                show(nextPromptIndex);
            }
            else if (mOnCompleteListener != null)
            {
                mOnCompleteListener.onSequenceComplete();
                nextPromptIndex = -1;
            }
        }
    };

    /**
     * The listener to call when this sequence completes
     */
    @Nullable
    private SequenceCompleteListener mOnCompleteListener;

    /**
     * Set the listener to listen with the action to call when the sequence ends
     * @param listener the listener with the action to execute
     */
    @NonNull
    public MaterialTapTargetSequence setSequenceCompleteListener(@Nullable SequenceCompleteListener listener)
    {
        mOnCompleteListener = listener;
        return this;
    }

    /**
     * Add a prompt to the end of the sequence.
     *
     * @param prompt The prompt to add.
     */
    @NonNull
    public MaterialTapTargetSequence addPrompt(@Nullable MaterialTapTargetPrompt prompt)
    {
        this.addItem(new SequenceItem(new SequenceState(prompt)));
        return this;
    }

    /**
     * Add a show for time prompt to the end of the sequence.
     *
     * @param prompt The prompt to add.
     * @param milliseconds The number of milliseconds to show the prompt for.
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence addPrompt(@Nullable MaterialTapTargetPrompt prompt,
                                               final long milliseconds)
    {
        this.addItem(new SequenceItemShowFor(new SequenceState(prompt), milliseconds));
        return this;
    }

    /**
     * Add a prompt to the end of the sequence.
     *
     * @param promptOptions The prompt to add.
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence addPrompt(@NonNull PromptOptions promptOptions)
    {
        this.addItem(new SequenceItem(new SequenceStatePromptOptions(promptOptions)));
        return this;
    }

    /**
     * Add a show for time prompt to the end of the sequence.
     *
     * @param promptOptions The prompt to add.
     * @param milliseconds The number of milliseconds to show the prompt for.
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence addPrompt(@NonNull PromptOptions promptOptions,
                                               final long milliseconds)
    {
        this.addItem(new SequenceItemShowFor(new SequenceStatePromptOptions(promptOptions), milliseconds));
        return this;
    }

    /**
     * Adds a sequence item to the end of the sequence.
     * This sequence item must have state changers added to it by calling
     * {@link SequenceItem#addStateChanger(int)}.
     *
     * @param item The already created sequence item to add.
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence addPrompt(@NonNull final SequenceItem item)
    {
        this.items.add(item);
        return this;
    }

    /**
     * Adds common state changers and adds the item to the list.
     *
     * @param sequenceItem The item to add the state changers to and adds it to the item list.
     */
    private void addItem(@NonNull final SequenceItem sequenceItem)
    {
        sequenceItem.addStateChanger(MaterialTapTargetPrompt.STATE_FINISHED);
        sequenceItem.addStateChanger(MaterialTapTargetPrompt.STATE_DISMISSED);
        this.items.add(sequenceItem);
    }

    /**
     * Get the number of prompts in this sequence.
     *
     * @return The number of prompts in this sequence.
     */
    public int size()
    {
        return this.items.size();
    }

    /**
     * Gets a prompt at a position in this sequence.
     *
     * @param index The prompt 0 based index.
     * @return The prompt at the specified position in this sequence.
     */
    @NonNull
    public SequenceItem get(final int index)
    {
        return this.items.get(index);
    }

    /***
     * Start the sequence by showing the first prompt.
     *
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence show()
    {
        this.nextPromptIndex = 0;
        if (!this.items.isEmpty())
        {
            this.show(0);
        }
        else if (mOnCompleteListener != null)
        {
            mOnCompleteListener.onSequenceComplete();
        }
        return this;
    }

    /**
     * Shows a prompt from a sequence item at the supplied index.
     *
     * @param index The 0 based index for the sequence item to show.
     */
    private void show(final int index)
    {
        final SequenceItem sequenceItem = this.items.get(index);
        sequenceItem.setSequenceListener(this.itemListener);
        final MaterialTapTargetPrompt prompt = sequenceItem.getState().getPrompt();
        if (prompt != null)
        {
            // add the listener to trigger the next in the sequence
            prompt.mView.mPromptOptions.setSequenceListener(sequenceItem);
        }
        sequenceItem.show();
    }

    /**
     * Removes the currently displayed prompt in the sequence from view using the finish action and stops the sequence
     * from continuing.
     *
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence finish()
    {
        if (this.nextPromptIndex > -1 && this.nextPromptIndex < this.items.size())
        {
            final SequenceItem sequenceItem = this.items.get(nextPromptIndex);
            sequenceItem.setSequenceListener(null);
            final MaterialTapTargetPrompt prompt = sequenceItem.getState().getPrompt();
            if (prompt != null)
            {
                prompt.mView.mPromptOptions.setSequenceListener(null);
            }
            sequenceItem.finish();
        }
        return this;
    }

    /**
     * Removes the currently displayed prompt in the sequence from view using the dismiss action and stops the sequence
     * from continuing.
     *
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence dismiss()
    {
        if (this.nextPromptIndex > -1 && this.nextPromptIndex < this.items.size())
        {
            final SequenceItem sequenceItem = this.items.get(nextPromptIndex);
            sequenceItem.setSequenceListener(null);
            final MaterialTapTargetPrompt prompt = sequenceItem.getState().getPrompt();
            if (prompt != null)
            {
                prompt.mView.mPromptOptions.setSequenceListener(null);
            }
            sequenceItem.dismiss();
        }
        return this;
    }

    /**
     * Shows or continues to show this sequence from the prompt at the index supplied.
     *
     * @param index The index to show from.
     * @return This.
     */
    @NonNull
    public MaterialTapTargetSequence showFromIndex(final int index)
    {
        this.dismiss();
        this.show(index);
        return this;
    }

    /**
     * Interface definition for a callback to be invoked when a sequence completes.
     */
    public interface SequenceCompleteListener
    {
        /**
         * Called after the final prompt is closed
         */
        void onSequenceComplete();
    }
}
