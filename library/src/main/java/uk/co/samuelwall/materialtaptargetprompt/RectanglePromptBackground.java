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
import android.graphics.PointF;
import android.graphics.RectF;

public class RectanglePromptBackground extends PromptBackground
{
    private RectF mBounds, mBaseBounds;
    private Paint mPaint;
    private int mBaseColourAlpha;
    private float rx = 40, ry = 40;

    public RectanglePromptBackground()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBounds = new RectF();
        mBaseBounds = new RectF();
    }

    @Override
    public void setBackgroundColour(int colour)
    {
        mPaint.setColor(colour);
        mBaseColourAlpha = Color.alpha(colour);
        mPaint.setAlpha(mBaseColourAlpha);
    }

    @Override
    public void prepare(MaterialTapTargetPrompt prompt, float maxTextWidth)
    {
        final PointF focalCentre = prompt.getFocalCentre();
        float x1, x2, y1, y2;
        final float focalRadius = prompt.mBaseFocalRadius + prompt.mTextPadding;
        if (prompt.mVerticalTextPositionAbove)
        {
            y1 = prompt.mView.mPrimaryTextTop - prompt.mTextPadding;
            y2 = focalCentre.y + focalRadius;
        }
        else
        {
            y1 = focalCentre.y - focalRadius;
            y2 = prompt.mView.mPrimaryTextTop + prompt.mView.mPrimaryTextLayout.getHeight();
            if (prompt.mView.mSecondaryTextLayout != null)
            {
                y2 += prompt.mView.mSecondaryTextLayout.getHeight() + prompt.mView.mTextSeparation;
            }
            y2 += prompt.mTextPadding;
        }
        x1 = Math.min(prompt.mView.mPrimaryTextLeft - prompt.mTextPadding,
                focalCentre.x - focalRadius);
        x2 = Math.max(prompt.mView.mPrimaryTextLeft + maxTextWidth + prompt.mTextPadding,
                focalCentre.x + focalRadius);
        mBaseBounds.set(x1, y1, x2, y2);
    }

    @Override
    public void update(MaterialTapTargetPrompt prompt, float revealAmount,
                       float alphaModifier)
    {
        mPaint.setAlpha((int) (mBaseColourAlpha * alphaModifier));
        mBounds.set(mBaseBounds);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawRoundRect(mBounds, rx, ry, mPaint);
    }

    @Override
    public boolean isPointInShape(float x, float y)
    {
        return mBounds.contains(x, y);
    }
}
