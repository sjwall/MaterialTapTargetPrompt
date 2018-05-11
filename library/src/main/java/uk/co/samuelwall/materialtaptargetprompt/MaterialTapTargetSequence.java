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

    public void addPrompt(MaterialTapTargetPrompt prompt){

        // add the listener to trigger the next in the sequence
        prompt.mView.mPromptOptions.setExtraPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
            @Override
            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {

                if (state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED ||
                        state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)

                    if (mPrompts.size() > nextPromptIndex) {
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


