/*
 * Copyright (C) 2017 Dennis van Dalen
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

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptUtils;

/**
 * {@link PromptBackground} implementation that renders the prompt background as a rectangle.
 */
public class FullscreenPromptBackground extends PromptBackground
{
    RectF mBounds, mBaseBounds;
    Paint mPaint;
    int mBaseColourAlpha;
    float mRx, mRy;
    PointF mFocalCentre;

    /**
     * Constructor.
     */
    public FullscreenPromptBackground()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBounds = new RectF();
        mBaseBounds = new RectF();
        mFocalCentre = new PointF();
        mRx = mRy = 0;
    }

    /**
     * Set the radius for the rectangle corners.
     *
     * @param rx The x-radius of the oval used to round the corners
     * @param ry The y-radius of the oval used to round the corners
     * @return This prompt background
     */
    @NonNull
    public FullscreenPromptBackground setCornerRadius(final float rx, final float ry)
    {
        mRx = rx;
        mRy = ry;
        return this;
    }

    @Override
    public void setColour(@ColorInt int colour)
    {
        mPaint.setColor(colour);
        mBaseColourAlpha = Color.alpha(colour);
        mPaint.setAlpha(mBaseColourAlpha);
    }

    @Override
    public void prepare(@NonNull final PromptOptions options, final boolean clipToBounds, @NonNull Rect clipBounds)
    {
        final RectF focalBounds = options.getPromptFocal().getBounds();
        DisplayMetrics metrics = getDisplayMetrics();

        mBaseBounds.set(0, 0, metrics.widthPixels, metrics.heightPixels);
        mFocalCentre.x = focalBounds.centerX();
        mFocalCentre.y = focalBounds.centerY();
    }

    @NonNull
    protected DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    @Override
    public void update(@NonNull final PromptOptions prompt, float revealModifier,
                       float alphaModifier)
    {
        mPaint.setAlpha((int) (mBaseColourAlpha * alphaModifier));
        PromptUtils.scale(mFocalCentre, mBaseBounds, mBounds, revealModifier, false);
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        canvas.drawRect(mBounds, mPaint);
    }

    @Override
    public boolean contains(float x, float y)
    {
        return mBounds.contains(x, y);
    }
}
