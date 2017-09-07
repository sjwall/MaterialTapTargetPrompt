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

import android.graphics.RectF;
import android.view.View;

public abstract class PromptFocal implements PromptUIElement
{
    protected boolean mDrawRipple;
    protected int mBaseRippleAlpha;
    protected float mPadding;

    public void setDrawRipple(final boolean drawRipple)
    {
        mDrawRipple = drawRipple;
    }

    public void setRippleAlpha(final int rippleAlpha)
    {
        mBaseRippleAlpha = rippleAlpha;
    }

    public void setPadding(final float padding)
    {
        mPadding = padding;
    }

    public float getPadding()
    {
        return mPadding;
    }

    public abstract RectF getBounds();
    public abstract void prepare(MaterialTapTargetPrompt prompt, View target);
    public abstract void prepare(MaterialTapTargetPrompt prompt, float targetX, float targetY);
    public abstract void updateRipple(float revealAmount, float alphaModifier);
}
