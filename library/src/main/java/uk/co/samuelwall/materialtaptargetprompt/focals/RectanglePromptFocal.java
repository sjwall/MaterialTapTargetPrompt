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

package uk.co.samuelwall.materialtaptargetprompt.focals;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.PromptFocal;

public class RectanglePromptFocal extends PromptFocal
{
    @Override
    public void setColour(int colour)
    {

    }

    @Override
    public void update(MaterialTapTargetPrompt prompt, float revealModifier,
                       float alphaModifier)
    {

    }

    @Override
    public void draw(Canvas canvas)
    {

    }

    @Override
    public boolean contains(float x, float y)
    {
        return false;
    }

    @Override
    public RectF getBounds()
    {
        return null;
    }

    @Override
    public void prepare(MaterialTapTargetPrompt prompt, View target)
    {

    }

    @Override
    public void prepare(MaterialTapTargetPrompt prompt, float targetX, float targetY)
    {

    }

    @Override
    public void updateRipple(float revealAmount, final float alphaModifier)
    {

    }
}
