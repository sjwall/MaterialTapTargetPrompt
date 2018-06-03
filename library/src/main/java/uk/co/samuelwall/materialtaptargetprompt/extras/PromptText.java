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
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.TextPaint;

public class PromptText implements PromptUIElement
{
    RectF mTextBounds = new RectF();
    float mPrimaryTextLeft;
    float mPrimaryTextLeftChange;
    float mPrimaryTextTop;
    float mSecondaryTextLeft;
    float mSecondaryTextLeftChange;
    float mSecondaryTextOffsetTop;
    Layout mPrimaryTextLayout;
    Layout mSecondaryTextLayout;

    /**
     * The paint used to draw the primary text.
     */
    TextPaint mPaintPrimaryText;

    /**
     * The paint used to draw the secondary text.
     */
    TextPaint mPaintSecondaryText;

    /**
     * The primary text layout alignment.
     * Left, centre or right.
     */
    Layout.Alignment mPrimaryTextAlignment;

    /**
     * The secondary text layout alignment.
     * Left, centre or right.
     */
    Layout.Alignment mSecondaryTextAlignment;

    boolean mClipToBounds;

    Rect mClipBounds;

    public  PromptText() {}

    /**
     * Get the window position for the prompt text.
     *
     * @return The prompt text bounds.
     */
    @NonNull
    public RectF getBounds()
    {
        return mTextBounds;
    }

    /**
     * Recalculates the primary and secondary text positions.
     */
    public void prepare(@NonNull PromptOptions options,
                        boolean clipToBounds, @NonNull Rect clipBounds)
    {
        mClipToBounds = clipToBounds;
        mClipBounds = clipBounds;
        final CharSequence primaryText = options.getPrimaryText();
        if (primaryText != null)
        {
            mPaintPrimaryText = new TextPaint();
            @ColorInt final int primaryTextColour = options.getPrimaryTextColour();
            mPaintPrimaryText.setColor(primaryTextColour);
            mPaintPrimaryText.setAlpha(Color.alpha(primaryTextColour));
            mPaintPrimaryText.setAntiAlias(true);
            mPaintPrimaryText.setTextSize(options.getPrimaryTextSize());
            PromptUtils.setTypeface(mPaintPrimaryText, options.getPrimaryTextTypeface(), options.getPrimaryTextTypefaceStyle());
            mPrimaryTextAlignment = PromptUtils.getTextAlignment(options.getResourceFinder().getResources(),
                    options.getPrimaryTextGravity(), primaryText);
        }

        final CharSequence secondaryText = options.getSecondaryText();
        if (secondaryText != null)
        {
            mPaintSecondaryText = new TextPaint();
            @ColorInt final int secondaryTextColour = options.getSecondaryTextColour();
            mPaintSecondaryText.setColor(secondaryTextColour);
            mPaintSecondaryText.setAlpha(Color.alpha(secondaryTextColour));
            mPaintSecondaryText.setAntiAlias(true);
            mPaintSecondaryText.setTextSize(options.getSecondaryTextSize());
            PromptUtils.setTypeface(mPaintSecondaryText, options.getSecondaryTextTypeface(),
                    options.getSecondaryTextTypefaceStyle());
            mSecondaryTextAlignment = PromptUtils.getTextAlignment(options.getResourceFinder().getResources(),
                    options.getSecondaryTextGravity(), secondaryText);
        }
        final RectF focalBounds = options.getPromptFocal().getBounds();
        final float focalCentreX = focalBounds.centerX();
        final float focalCentreY = focalBounds.centerY();

        final boolean verticalTextPositionAbove = focalCentreY > clipBounds.centerY();
        final boolean horizontalTextPositionLeft = focalCentreX > clipBounds.centerX();

        final float maxWidth = PromptUtils.calculateMaxWidth(options.getMaxTextWidth(),
                clipToBounds ? clipBounds : null,
                options.getResourceFinder().getPromptParentView().getWidth(),
                options.getTextPadding());
        createTextLayout(options, maxWidth, 1);
        final float primaryTextWidth = PromptUtils.calculateMaxTextWidth(mPrimaryTextLayout);
        final float secondaryTextWidth = PromptUtils.calculateMaxTextWidth(mSecondaryTextLayout);
        final float textWidth = Math.max(primaryTextWidth, secondaryTextWidth);
        final float focalPadding = options.getFocalPadding();
        final float textPadding = options.getTextPadding();

        if (PromptUtils.containsInset(clipBounds,
                (int) (88 * options.getResourceFinder().getResources().getDisplayMetrics().density),
                (int) focalCentreX, (int) focalCentreY))
        {
            mPrimaryTextLeft = clipBounds.left;
            final float width = Math.min(textWidth, maxWidth);
            if (horizontalTextPositionLeft)
            {
                mPrimaryTextLeft = focalCentreX - width + focalPadding;
            }
            else
            {
                mPrimaryTextLeft = focalCentreX - width - focalPadding;
            }
            if (mPrimaryTextLeft < clipBounds.left + textPadding)
            {
                mPrimaryTextLeft = clipBounds.left + textPadding;
            }
            if (mPrimaryTextLeft + width > clipBounds.right - textPadding)
            {
                mPrimaryTextLeft = clipBounds.right - textPadding - width;
            }
        }
        else
        {
            if (horizontalTextPositionLeft)
            {
                mPrimaryTextLeft = (clipToBounds ? clipBounds.right :
                        options.getResourceFinder().getPromptParentView().getRight()) - textPadding - textWidth;
            }
            else
            {
                mPrimaryTextLeft = (clipToBounds ? clipBounds.left :
                        options.getResourceFinder().getPromptParentView().getLeft()) + textPadding;
            }
        }

        if (verticalTextPositionAbove)
        {
            mPrimaryTextTop = focalBounds.top - focalPadding;
            if (mPrimaryTextLayout != null)
            {
                mPrimaryTextTop -= mPrimaryTextLayout.getHeight();
            }
        }
        else
        {
            mPrimaryTextTop = focalBounds.bottom + focalPadding;
        }

        float primaryTextHeight = 0;
        if (mPrimaryTextLayout != null)
        {
            primaryTextHeight = mPrimaryTextLayout.getHeight();
        }
        float textHeight;
        if (mSecondaryTextLayout != null)
        {
            textHeight = mSecondaryTextLayout.getHeight();
            if (verticalTextPositionAbove)
            {
                mPrimaryTextTop -= textHeight;
                if (mPrimaryTextLayout != null)
                {
                    mPrimaryTextTop -= options.getTextSeparation();
                }
            }

            if (mPrimaryTextLayout != null)
            {
                mSecondaryTextOffsetTop = primaryTextHeight + options.getTextSeparation();
            }
            textHeight += mSecondaryTextOffsetTop;
        }
        else
        {
            textHeight = primaryTextHeight;
        }

        mSecondaryTextLeft = mPrimaryTextLeft;
        mPrimaryTextLeftChange = 0;
        mSecondaryTextLeftChange = 0;
        final float change = maxWidth - textWidth;
        if (PromptUtils.isRtlText(mPrimaryTextLayout, options.getResourceFinder().getResources()))
        {
            mPrimaryTextLeftChange = change;
        }
        if (PromptUtils.isRtlText(mSecondaryTextLayout, options.getResourceFinder().getResources()))
        {
            mSecondaryTextLeftChange = change;
        }
        mTextBounds.left = mPrimaryTextLeft;// - change;
        mTextBounds.top = mPrimaryTextTop;
        mTextBounds.right = mTextBounds.left + textWidth;
        mTextBounds.bottom = mTextBounds.top + textHeight;
    }

    /**
     * Creates the text layouts for the primary and secondary text.
     *
     * @param maxWidth The maximum width that the text can be.
     */
    void createTextLayout(@NonNull final PromptOptions options, final float maxWidth,
                          final float alphaModifier)
    {
        if (options.getPrimaryText() != null)
        {
            mPrimaryTextLayout = PromptUtils.createStaticTextLayout(options.getPrimaryText(),
                    mPaintPrimaryText, (int) maxWidth, mPrimaryTextAlignment, alphaModifier);
        }
        else
        {
            mPrimaryTextLayout = null;
        }
        if (options.getSecondaryText() != null)
        {
            mSecondaryTextLayout = PromptUtils.createStaticTextLayout(options.getSecondaryText(),
                    mPaintSecondaryText, (int) maxWidth, mSecondaryTextAlignment, alphaModifier);
        }
        else
        {
            mSecondaryTextLayout = null;
        }
    }

    @Override
    public void update(@NonNull final PromptOptions options, float revealModifier,
                       float alphaModifier)
    {
        final float maxWidth = PromptUtils.calculateMaxWidth(options.getMaxTextWidth(),
                mClipToBounds ? mClipBounds : null,
                options.getResourceFinder().getPromptParentView().getWidth(),
                options.getTextPadding());
        createTextLayout(options, maxWidth, alphaModifier);
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        canvas.translate(mPrimaryTextLeft - mPrimaryTextLeftChange, mPrimaryTextTop);
        if (mPrimaryTextLayout != null)
        {
            mPrimaryTextLayout.draw(canvas);
        }
        if (mSecondaryTextLayout != null)
        {
            canvas.translate(-(mPrimaryTextLeft - mPrimaryTextLeftChange)
                    + mSecondaryTextLeft - mSecondaryTextLeftChange, mSecondaryTextOffsetTop);
            mSecondaryTextLayout.draw(canvas);
        }
    }

    @Override
    public boolean contains(float x, float y)
    {
        return mTextBounds.contains(x, y);
    }
}
