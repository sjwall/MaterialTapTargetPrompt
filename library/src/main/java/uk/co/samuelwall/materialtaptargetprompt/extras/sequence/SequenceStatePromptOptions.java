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

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;

/**
 * A {@link SequenceState} where the prompt is created the first time that {@link #getPrompt()} is
 * called.
 */
public class SequenceStatePromptOptions extends SequenceState
{
    /**
     * Builder to create the prompt from.
     */
    @NonNull
    private final PromptOptions promptOptions;

    /**
     * Constructor.
     *
     * @param promptOptions The builder to create the prompt from.
     */
    public SequenceStatePromptOptions(@NonNull final PromptOptions promptOptions)
    {
        super(null);
        this.promptOptions = promptOptions;
    }

    @Nullable
    @Override
    public MaterialTapTargetPrompt getPrompt()
    {
        if (this.prompt == null)
        {
            this.prompt = this.promptOptions.create();
        }
        return this.prompt;
    }
}
