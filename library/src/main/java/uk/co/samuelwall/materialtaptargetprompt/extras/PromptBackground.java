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

import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Used to render the prompt background.
 */
public abstract class PromptBackground implements PromptUIElement
{
    /**
     * Sets the colour to use for the background.
     *
     * @param colour Colour integer representing the colour.
     */
    public abstract void setColour(@ColorInt int colour);

    /**
     * Prepares the background for drawing.
     *
     * @param options The options from which the prompt was created.
     * @param clipToBounds Should the prompt be clipped to the supplied clipBounds.
     * @param clipBounds The bounds to clip the drawing to.
     */
    public abstract void prepare(@NonNull final PromptOptions options,
                                 boolean clipToBounds, @NonNull Rect clipBounds);
}
