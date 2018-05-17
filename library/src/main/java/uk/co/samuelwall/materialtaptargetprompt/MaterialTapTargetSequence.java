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

/**
 * A Sequence of prompts to be shown one after another
 */
public class MaterialTapTargetSequence {

    /**
     * The list of prompts to display when the sequence is shown
     */
    private List<MaterialTapTargetPrompt> mPrompts = new ArrayList<>();

    /**
     * Pointer to the next prompt to be shown
     */
    private int nextPromptIndex = 0;

    /**
     * The listener to call when this sequence completes
     */
    @Nullable private SequenceCompleteListener mOnCompleteListener;

    /**
     * Set the listener to listen with the action to call when the sequence ends
     * @param listener the listener with the action to execute
     */
    public void setSequenceCompleteListener(@Nullable SequenceCompleteListener listener){
        mOnCompleteListener = listener;
    }

    /**
     * Add a prompt to the end of the sequence.
     * @param prompt The prompt to add
     */
    public void addPrompt(MaterialTapTargetPrompt prompt){

        // add the listener to trigger the next in the sequence
        prompt.mView.mPromptOptions.setSequenceListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
            @Override
            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {

                if (state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED ||
                        state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)

                    if (mPrompts.size() > nextPromptIndex + 1) {
                        mPrompts.get(++nextPromptIndex).show();
                    }else{
                        if (mOnCompleteListener != null) {
                            mOnCompleteListener.onSequenceComplete();
                        }
                    }
            }
        });

        mPrompts.add(prompt);
    }

    /***
     * Start the sequence by showing the first prompt
     */
    public void show() {
        if (mPrompts != null && mPrompts.size() > 0){
            mPrompts.get(0).show(); // todo how about timed show?
        }else{
            if (mOnCompleteListener != null) {
                mOnCompleteListener.onSequenceComplete();
            }
        }
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


