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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class RectanglePromptBackground extends PromptBackground
{
    private RectF mBounds, mBaseBounds;
    private Paint mPaint;
    private int mBaseColourAlpha;
    private float mRx, mRy;

    public RectanglePromptBackground()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBounds = new RectF();
        mBaseBounds = new RectF();
        mRx = mRy = 40;
    }

    public void setCornerRadius(final float rx, final float ry)
    {
        mRx = rx;
        mRy = ry;
    }

    @Override
    public void setColour(int colour)
    {
        mPaint.setColor(colour);
        mBaseColourAlpha = Color.alpha(colour);
        mPaint.setAlpha(mBaseColourAlpha);
    }

    @Override
    public void prepare(MaterialTapTargetPrompt prompt, float maxTextWidth)
    {
        final RectF focalBounds = prompt.getPromptFocal().getBounds();
        final RectF textBounds = prompt.getTextBounds();
        float x1, x2, y1, y2;
        if (prompt.mVerticalTextPositionAbove)
        {
            y1 = textBounds.top - prompt.mTextPadding;
            y2 = focalBounds.bottom + prompt.mTextPadding;
        }
        else
        {
            y1 = focalBounds.top - prompt.mTextPadding;
            y2 = textBounds.bottom + prompt.mTextPadding;
        }
        x1 = Math.min(textBounds.left - prompt.mTextPadding,
                focalBounds.left - prompt.mTextPadding);
        x2 = Math.max(textBounds.right + prompt.mTextPadding,
                focalBounds.right + prompt.mTextPadding);
        mBaseBounds.set(x1, y1, x2, y2);
    }

    @Override
    public void update(MaterialTapTargetPrompt prompt, float revealModifier,
                       float alphaModifier)
    {
        mPaint.setAlpha((int) (mBaseColourAlpha * alphaModifier));
        mBounds.set(mBaseBounds);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawRoundRect(mBounds, mRx, mRy, mPaint);
    }

    @Override
    public boolean contains(float x, float y)
    {
        return mBounds.contains(x, y);
    }
}
