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

package uk.co.samuelwall.materialtaptargetprompt;

import android.graphics.Canvas;

public abstract class PromptBackground
{
    abstract void setBackgroundColour(int colour);

    abstract void prepare(final MaterialTapTargetPrompt prompt, final float maxTextWidth);

    abstract void update(final MaterialTapTargetPrompt prompt, float revealAmount, float alphaModifier);

    abstract void draw(Canvas canvas);

    abstract boolean isPointInShape(float x, float y);
}
