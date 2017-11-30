/*
 * Copyright (C) 2017 Samuel Wall
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

package uk.co.samuelwall.materialtaptargetprompt.extras;

import android.graphics.Canvas;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

/**
 * Base interface containing common methods for UI elements in a prompt.
 */
interface PromptUIElement
{
    /**
     * Update the current prompt rendering state based on the prompt options and current reveal & alpha scales.
     *
     * @param options        The options used to create the prompt.
     * @param revealModifier The current size/revealed scale from 0 - 1.
     * @param alphaModifier  The current colour alpha scale from 0 - 1.
     */
    void update(@NonNull final PromptOptions options,
                @FloatRange(from = 0, to = 2) float revealModifier,
                @FloatRange(from = 0, to = 1) float alphaModifier);

    /**
     * Draw the element.
     *
     * @param canvas The canvas to draw to.
     */
    void draw(@NonNull Canvas canvas);

    /**
     * Does the element contain the point.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     * @return True if the element contains the point, false otherwise.
     */
    boolean contains(float x, float y);
}
