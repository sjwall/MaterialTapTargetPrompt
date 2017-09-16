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

package uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptText;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptUtils;

public class CirclePromptBackground extends PromptBackground
{
    PointF mPosition;
    float mRadius;
    PointF mBasePosition;
    float mBaseRadius;
    Paint mPaint;
    int mBaseColourAlpha;
    /*PointF point1 = new PointF();
    PointF point2 = new PointF();
    PointF point3 = new PointF();
    Paint pointPaint = new Paint();*/

    public CirclePromptBackground()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPosition = new PointF();
        mBasePosition = new PointF();
        /*pointPaint.setColor(Color.RED);
        pointPaint.setAlpha(100);*/
    }

    @Override
    public void setColour(int colour)
    {
        mPaint.setColor(colour);
        mBaseColourAlpha = Color.alpha(colour);
        mPaint.setAlpha(mBaseColourAlpha);
    }

    @Override
    public void prepare(final PromptOptions options, final boolean clipToBounds,
                        final Rect clipBounds)
    {
        final PromptText promptText = options.getPromptText();
        final RectF focalBounds = options.getPromptFocal().getBounds();
        final float focalCentreX = focalBounds.centerX();
        final float focalCentreY = focalBounds.centerY();
        final float focalPadding = options.getFocalPadding();
        final RectF textBounds = promptText.getBounds();
        final float textPadding = options.getTextPadding();
        final RectF clipBoundsInset88dp = new RectF(clipBounds);
        final float inset88dp = 88f * options.getResourceFinder().getResources().getDisplayMetrics().density;
        clipBoundsInset88dp.inset(inset88dp, inset88dp);
        if ((focalCentreX > clipBoundsInset88dp.left
                && focalCentreX < clipBoundsInset88dp.right)
                || (focalCentreY > clipBoundsInset88dp.top
                && focalCentreY < clipBoundsInset88dp.bottom))
        {
            final boolean isTextAboveTarget = textBounds.top < focalBounds.top;
            float x1 = focalCentreX;
            float x2 = textBounds.left - textPadding;
            float y1, y2;
            if (isTextAboveTarget)
            {
                y1 = focalBounds.bottom + textPadding;
                y2 = textBounds.top;
            }
            else
            {
                y1 = focalBounds.top - (focalPadding + textPadding);
                y2 = textBounds.bottom;
            }

            final float y3 = y2;
            float x3 = promptText.getBounds().right + textPadding;

            final float focalLeft = focalBounds.left - focalPadding;
            final float focalRight = focalBounds.right + focalPadding;
            if (x2 > focalLeft && x2 < focalRight)
            {
                if (isTextAboveTarget)
                {
                    x1 = focalBounds.left - focalPadding;
                }
                else
                {
                    x2 -= (focalBounds.width() / 2) - focalPadding;
                }
            }
            else if (x3 > focalLeft && x3 < focalRight)
            {
                if (isTextAboveTarget)
                {
                    x1 = focalBounds.right + focalPadding;
                }
                else
                {
                    x3 += (focalBounds.width() / 2) + focalPadding;
                }
            }

            final double offset = Math.pow(x2, 2) + Math.pow(y2, 2);
            final double bc = (Math.pow(x1, 2) + Math.pow(y1, 2) - offset) / 2.0;
            final double cd = (offset - Math.pow(x3, 2) - Math.pow(y3, 2)) / 2.0;
            final double det = (x1 - x2) * (y2 - y3) - (x2 - x3) * (y1 - y2);
            final double idet = 1 / det;
            mBasePosition.set((float) ((bc * (y2 - y3) - cd * (y1 - y2)) * idet),
                    (float) ((cd * (x1 - x2) - bc * (x2 - x3)) * idet));
            mBaseRadius = (float) Math.sqrt(Math.pow(x2 - mBasePosition.x, 2)
                    + Math.pow(y2 - mBasePosition.y, 2));
            /*point1.set(x1, y1);
            point2.set(x2, y2);
            point3.set(x3, y3);*/
        }
        else
        {
            mBasePosition.set(focalCentreX, focalCentreY);
            final float length = Math.abs(
                    (textBounds.left < focalBounds.left ?
                            textBounds.left - textPadding
                            : textBounds.right + textPadding)
                    - focalCentreX);
            float height = (focalBounds.height() / 2) + focalPadding + textBounds.height();
            mBaseRadius = (float) Math.sqrt(Math.pow(length, 2) + Math.pow(height, 2));
            /*point1.set(focalCentreX + (prompt.mHorizontalTextPositionLeft ? -length : length),
                            focalCentreY + (prompt.mVerticalTextPositionAbove ? - height : height));*/
        }
        mPosition.set(mBasePosition);
    }

    @Override
    public void update(final PromptOptions options, float revealModifier, float alphaModifier)
    {
        final RectF focalBounds = options.getPromptFocal().getBounds();
        final float focalCentreX = focalBounds.centerX();
        final float focalCentreY = focalBounds.centerY();
        mRadius = mBaseRadius * revealModifier;
        mPaint.setAlpha((int) (mBaseColourAlpha * alphaModifier));
        mPosition.set(focalCentreX + ((mBasePosition.x - focalCentreX) * revealModifier),
                focalCentreY + ((mBasePosition.y - focalCentreY) * revealModifier));
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawCircle(mPosition.x, mPosition.y, mRadius, mPaint);

        /*canvas.drawCircle(point1.x, point1.y, 100, pointPaint);
        canvas.drawCircle(point2.x, point2.y, 100, pointPaint);
        canvas.drawCircle(point3.x, point3.y, 100, pointPaint);*/
    }

    @Override
    public boolean contains(float x, float y)
    {
        return PromptUtils.isPointInCircle(x, y, mPosition, mRadius);
    }
}
