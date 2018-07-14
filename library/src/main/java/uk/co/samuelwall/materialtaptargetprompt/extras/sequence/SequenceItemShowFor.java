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

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Shows a prompt in a sequence for the supplied milliseconds.
 */
public class SequenceItemShowFor extends SequenceItem
{
    /**
     * The number of milliseconds to show the prompt for.
     */
    private final long milliseconds;

    /**
     * Constructor.
     *
     * @param state The prompt that this item will show.
     * @param milliseconds The number of milliseconds to show the prompt for.
     */
    public SequenceItemShowFor(@NonNull final SequenceState state, final long milliseconds)
    {
        super(state);
        this.milliseconds = milliseconds;
    }

    @Override
    protected void show(@NonNull final MaterialTapTargetPrompt prompt)
    {
        prompt.showFor(milliseconds);
    }
}
