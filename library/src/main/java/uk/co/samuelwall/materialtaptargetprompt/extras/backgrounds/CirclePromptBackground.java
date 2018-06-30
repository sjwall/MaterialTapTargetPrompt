/*
 * Copyright (C) 2017-2018 Samuel Wall
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
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptText;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptUtils;

/**
 * {@link PromptBackground} implementation that renders the prompt background as a circle.
 */
public class CirclePromptBackground extends PromptBackground
{
    /**
     * The current circle centre position.
     */
    PointF mPosition;

    /**
     * The current radius for the circle.
     */
    float mRadius;

    /**
     * The position for circle centre at 1.0 scale.
     */
    PointF mBasePosition;

    /**
     * The radius for the circle at 1.0 scale.
     */
    float mBaseRadius;

    /**
     * The paint to use to render the circle.
     */
    Paint mPaint;

    /**
     * The alpha value to use at 1.0 scale.
     */
    @IntRange(from=0, to=255)
    int mBaseColourAlpha;
    /*PointF point1 = new PointF();
    PointF point2 = new PointF();
    PointF point3 = new PointF();
    Paint pointPaint = new Paint();*/

    /**
     * Constructor.
     */
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
    public void setColour(@ColorInt int colour)
    {
        mPaint.setColor(colour);
        mBaseColourAlpha = Color.alpha(colour);
        mPaint.setAlpha(mBaseColourAlpha);
    }

    @Override
    public void prepare(@NonNull final PromptOptions options, final boolean clipToBounds,
                        @NonNull final Rect clipBounds)
    {
        // Obtain values from the prompt options.
        final PromptText promptText = options.getPromptText();
        final RectF focalBounds = options.getPromptFocal().getBounds();
        final float focalCentreX = focalBounds.centerX();
        final float focalCentreY = focalBounds.centerY();
        final float focalPadding = options.getFocalPadding();
        final RectF textBounds = promptText.getBounds();
        final float textPadding = options.getTextPadding();
        final RectF clipBoundsInset88dp = new RectF(clipBounds);
        // Default material design offset prompt when more than 88dp inset
        final float inset88dp = 88f * options.getResourceFinder().getResources().getDisplayMetrics().density;
        clipBoundsInset88dp.inset(inset88dp, inset88dp);

        // Is the focal centre more than 88dp from the clip bounds edge
        if ((focalCentreX > clipBoundsInset88dp.left
                && focalCentreX < clipBoundsInset88dp.right)
                || (focalCentreY > clipBoundsInset88dp.top
                && focalCentreY < clipBoundsInset88dp.bottom))
        {
            // The circle position and radius is calculated based on three points placed around the
            // prompt: XY1, XY2 and XY3.
            // XY2: the text left side
            // XY3: the text right side
            // XY1: the furthest point on the focal target from the text centre x point

            // XY1
            float textWidth = textBounds.width();
            // Calculate the X distance from the text centre x to focal centre x
            float distanceX = focalCentreX - textBounds.left + (textWidth / 2);
            // Calculate how much percentage wise the focal centre x is from the text centre x to
            // the nearest text edge
            float percentageOffset = 100 / textWidth * distanceX;
            // Angle is the above percentage of 90 degrees
            float angle = 90 * (percentageOffset / 100);
            // 0 degrees is right side middle
            // If text above target
            if (textBounds.top < focalBounds.top)
            {
                angle = 180 - angle;
            }
            else
            {
                angle = 180 + angle;
            }
            final PointF furthestPoint = options.getPromptFocal().calculateAngleEdgePoint(angle,
                focalPadding);
            final float x1 = furthestPoint.x;
            final float y1 = furthestPoint.y;

            // XY2
            final float x2 = textBounds.left - textPadding;
            final float y2;
            // If text is above the target
            if (textBounds.top < focalBounds.top)
            {
                y2 = textBounds.top;
            }
            else
            {
                y2 = textBounds.bottom;
            }

            // XY3
            //noinspection UnnecessaryLocalVariable
            final float y3 = y2;
            float x3 = textBounds.right + textPadding;

            // If the focal width is greater than the text width
            if (focalBounds.right > x3)
            {
                x3 = focalBounds.right + focalPadding;
            }

            // Calculate the position and radius
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
           // Calculate the furthest distance from the center based on the text size.
            final float length = Math.max(
                    Math.abs(textBounds.right - focalCentreX),
                    Math.abs(textBounds.left - focalCentreX)
                ) + textPadding;
            // Calculate the height based on the distance from the focal centre to the furthest text y position.
            float height = (focalBounds.height() / 2) + focalPadding + textBounds.height();
            // Calculate the radius based on the calculated width and height
            mBaseRadius = (float) Math.sqrt(Math.pow(length, 2) + Math.pow(height, 2));
            /*point1.set(focalCentreX + (prompt.mHorizontalTextPositionLeft ? -length : length),
                            focalCentreY + (prompt.mVerticalTextPositionAbove ? - height : height));*/
        }
        mPosition.set(mBasePosition);
    }

    @Override
    public void update(@NonNull final PromptOptions options, float revealModifier, float alphaModifier)
    {
        final RectF focalBounds = options.getPromptFocal().getBounds();
        final float focalCentreX = focalBounds.centerX();
        final float focalCentreY = focalBounds.centerY();
        mRadius = mBaseRadius * revealModifier;
        mPaint.setAlpha((int) (mBaseColourAlpha * alphaModifier));
        // Change the current centre position to be a position scaled from the focal to the base.
        mPosition.set(focalCentreX + ((mBasePosition.x - focalCentreX) * revealModifier),
                focalCentreY + ((mBasePosition.y - focalCentreY) * revealModifier));
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        canvas.drawCircle(mPosition.x, mPosition.y, mRadius, mPaint);

        /*pointPaint.setColor(Color.YELLOW);
        pointPaint.setAlpha(100);
        canvas.drawCircle(point1.x, point1.y, 100, pointPaint);
        pointPaint.setColor(Color.GREEN);
        pointPaint.setAlpha(100);
        canvas.drawCircle(point2.x, point2.y, 100, pointPaint);
        pointPaint.setColor(Color.RED);
        pointPaint.setAlpha(100);
        canvas.drawCircle(point3.x, point3.y, 100, pointPaint);*/
    }

    @Override
    public boolean contains(float x, float y)
    {
        return PromptUtils.isPointInCircle(x, y, mPosition, mRadius);
    }
}
