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

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;

import java.text.Bidi;

/**
 * Useful methods for prompts that don't fit else where.
 */
public class PromptUtils
{

    private PromptUtils() {}

    /**
     * Determines if a point is in the centre of a circle with a radius from the point.
     *
     * @param x            The x position in the view.
     * @param y            The y position in the view.
     * @param circleCentre The circle centre position
     * @param radius       The radius of the circle.
     * @return True if the point (x, y) is in the circle.
     */
    public static boolean isPointInCircle(final float x, final float y,
                                          @NonNull final PointF circleCentre,
                                          final float radius)
    {
        return Math.pow(x - circleCentre.x, 2) + Math.pow(y - circleCentre.y, 2) < Math.pow(radius, 2);
    }

    /**
     * Based on setTypeface in android TextView, Copyright (C) 2006 The Android Open Source
     * Project. https://android.googlesource.com/platform/frameworks/base.git/+/master/core/java/android/widget/TextView.java
     */
    public static void setTypeface(@NonNull TextPaint textPaint, @Nullable Typeface typeface, int style)
    {
        if (style > 0)
        {
            if (typeface == null)
            {
                typeface = Typeface.defaultFromStyle(style);
            }
            else
            {
                typeface = Typeface.create(typeface, style);
            }

            textPaint.setTypeface(typeface);

            int typefaceStyle = typeface != null ? typeface.getStyle() : 0;
            int need = style & ~typefaceStyle;
            textPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
            textPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
        }
        else if (typeface != null)
        {
            textPaint.setTypeface(typeface);
        }
        else
        {
            textPaint.setTypeface(Typeface.defaultFromStyle(style));
        }
    }

    /**
     * Based on setTypefaceFromAttrs in android TextView, Copyright (C) 2006 The Android Open
     * Source Project. https://android.googlesource.com/platform/frameworks/base.git/+/master/core/java/android/widget/TextView.java
     */
    @NonNull
    public static Typeface setTypefaceFromAttrs(@Nullable String familyName, int typefaceIndex, int styleIndex)
    {
        Typeface tf = null;
        if (familyName != null)
        {
            tf = Typeface.create(familyName, styleIndex);
            if (tf != null)
            {
                return tf;
            }
        }
        switch (typefaceIndex)
        {
            case 1:
                tf = Typeface.SANS_SERIF;
                break;

            case 2:
                tf = Typeface.SERIF;
                break;

            case 3:
                tf = Typeface.MONOSPACE;
                break;
        }
        return Typeface.create(tf, styleIndex);
    }

    /**
     * Based on parseTintMode in android appcompat v7 DrawableUtils, Copyright (C) 2014 The
     * Android Open Source Project. https://android.googlesource.com/platform/frameworks/support.git/+/master/v7/appcompat/src/android/support/v7/widget/DrawableUtils.java
     */
    @Nullable
    public static PorterDuff.Mode parseTintMode(int value, @Nullable PorterDuff.Mode defaultMode)
    {
        switch (value)
        {
            case 3: return PorterDuff.Mode.SRC_OVER;
            case 5: return PorterDuff.Mode.SRC_IN;
            case 9: return PorterDuff.Mode.SRC_ATOP;
            case 14: return PorterDuff.Mode.MULTIPLY;
            case 15: return PorterDuff.Mode.SCREEN;
            case 16: return PorterDuff.Mode.valueOf("ADD");
            default: return defaultMode;
        }
    }

    /**
     * Gets the absolute text alignment value based on the supplied gravity and the activities
     * layout direction.
     *
     * @param gravity The gravity to convert to absolute values
     * @return absolute layout direction
     */
    @SuppressLint("RtlHardcoded")
    @NonNull
    public static Layout.Alignment getTextAlignment(@NonNull final Resources resources,
                                                    final int gravity,
                                                    @Nullable final CharSequence text)
    {
        final int absoluteGravity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            int realGravity = gravity;
            final int layoutDirection = resources.getConfiguration().getLayoutDirection();
            if (text != null && layoutDirection == View.LAYOUT_DIRECTION_RTL
                    && new Bidi(text.toString(), Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isRightToLeft())
            {
                if (gravity == Gravity.START)
                {
                    realGravity = Gravity.END;
                }
                else if (gravity == Gravity.END)
                {
                    realGravity = Gravity.START;
                }
            }
            absoluteGravity = Gravity.getAbsoluteGravity(realGravity, layoutDirection);
        }
        else
        {
            if ((gravity & Gravity.START) == Gravity.START)
            {
                absoluteGravity = Gravity.LEFT;
            }
            else if ((gravity & Gravity.END) == Gravity.END)
            {
                absoluteGravity = Gravity.RIGHT;
            }
            else
            {
                absoluteGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            }
        }
        final Layout.Alignment alignment;
        switch (absoluteGravity)
        {
            case Gravity.RIGHT:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case Gravity.CENTER_HORIZONTAL:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            default:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
        }
        return alignment;
    }

    /**
     * Creates a static text layout. Uses the {@link android.text.StaticLayout.Builder} if
     * available.
     *
     * @param text          The text to be laid out, optionally with spans
     * @param paint         The base paint used for layout
     * @param maxTextWidth  The width in pixels
     * @param textAlignment Alignment for the resulting {@link StaticLayout}
     * @param alphaModifier The modification to apply to the alpha value between 0 and 1.
     * @return the newly constructed {@link StaticLayout} object
     */
    @NonNull
    public static StaticLayout createStaticTextLayout(@NonNull final CharSequence text,
                                                      @NonNull final TextPaint paint,
                                                      final int maxTextWidth,
                                                      @NonNull final Layout.Alignment textAlignment,
                                                      final float alphaModifier)
    {
        final SpannableStringBuilder wrappedText = new SpannableStringBuilder(text);
        wrappedText.setSpan(new AlphaSpan(alphaModifier), 0, wrappedText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        final StaticLayout layout;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            final StaticLayout.Builder builder = StaticLayout.Builder.obtain(wrappedText, 0, text.length(), paint, maxTextWidth);
            builder.setAlignment(textAlignment);
            layout = builder.build();
        }
        else
        {
            layout = new StaticLayout(wrappedText, paint, maxTextWidth, textAlignment, 1f, 0f, false);
        }
        return layout;
    }

    /**
     * Scales a rectangle.
     *
     * @param origin The point to scale from.
     * @param base The rectangle at scale 1.0.
     * @param out The rectangle to put the scaled size in.
     * @param scale The amount to scale the rectangle by.
     * @param even Should the rectangle be scaled evenly in both directions.
     */
    public static void scale(@NonNull final PointF origin, @NonNull final RectF base,
                             @NonNull final RectF out,
                             final float scale, final boolean even)
    {
        if (scale == 1)
        {
            out.set(base);
            return;
        }

        final float horizontalFromCentre = base.centerX() - base.left;
        final float verticalFromCentre = base.centerY() - base.top;

        if (even && scale > 1)
        {
            final float minChange = Math.min(horizontalFromCentre * scale - horizontalFromCentre,
                    verticalFromCentre * scale - verticalFromCentre);
            out.left = base.left - minChange;
            out.top = base.top - minChange;
            out.right = base.right + minChange;
            out.bottom = base.bottom + minChange;
        }
        else
        {
            out.left = origin.x - horizontalFromCentre * scale * ((origin.x - base.left) / horizontalFromCentre);
            out.top = origin.y - verticalFromCentre * scale * ((origin.y - base.top) / verticalFromCentre);
            out.right = origin.x + horizontalFromCentre * scale * ((base.right - origin.x) / horizontalFromCentre);
            out.bottom = origin.y + verticalFromCentre * scale * ((base.bottom - origin.y) / verticalFromCentre);
        }
    }

    /**
     * Determines if the text in the supplied layout is displayed right to left.
     *
     * @param layout The layout to check.
     * @return True if the text in the supplied layout is displayed right to left. False otherwise.
     */
    public static boolean isRtlText(@Nullable final Layout layout, @NonNull final Resources resources)
    {
        boolean result = false;
        if (layout != null)
        {
            // Treat align opposite as right to left by default
            result = layout.getAlignment() == Layout.Alignment.ALIGN_OPPOSITE;
            // If the first character is a right to left character
            final boolean textIsRtl = layout.isRtlCharAt(0);
            // If the text and result are right to left then false otherwise use the textIsRtl value
            result = (!(result && textIsRtl) && !(!result && !textIsRtl)) || textIsRtl;
            if (!result && layout.getAlignment() == Layout.Alignment.ALIGN_NORMAL
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                // If the layout and text are right to left and the alignment is normal then rtl
                result = resources.getConfiguration()
                            .getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
            }
            else if (layout.getAlignment() == Layout.Alignment.ALIGN_OPPOSITE && textIsRtl)
            {
                result = false;
            }
        }
        return result;
    }


    /**
     * Calculates the maximum width that the prompt can be.
     *
     * @return Maximum width in pixels that the prompt can be.
     */
    public static float calculateMaxWidth(final float maxTextWidth, @Nullable final Rect clipBounds, final int parentWidth, final float textPadding)
    {
        return Math.max(80, Math.min(maxTextWidth, (clipBounds != null ? clipBounds.right - clipBounds.left : parentWidth) - (textPadding * 2)));
    }

    /**
     * Calculates the maximum width line in a text layout.
     *
     * @param textLayout The text layout
     * @return The maximum length line
     */
    public static float calculateMaxTextWidth(@Nullable final Layout textLayout)
    {
        float maxTextWidth = 0f;
        if (textLayout != null)
        {
            for (int i = 0, count = textLayout.getLineCount(); i < count; i++)
            {
                maxTextWidth = Math.max(maxTextWidth, textLayout.getLineWidth(i));
            }
        }
        return maxTextWidth;
    }

    /**
     * Determines if a point is within a rectangle that has been inset.
     *
     * @param bounds The rectangle bounds.
     * @param inset The amount that the rectangle is inset by.
     * @param x The point x coordinate.
     * @param y The point y coordinate.
     * @return True if the point is within the inset rectangle, false otherwise.
     */
    public static boolean containsInset(@NonNull final Rect bounds,
                                        final int inset, final int x, final int y)
    {
        return x > bounds.left + inset
                && x < bounds.right - inset
                && y > bounds.top + inset
                && y < bounds.bottom - inset;
    }

    /**
     * Determines if Android is on or after Jelly Bean MR1.
     *
     * @return True if running on Android on or after Jelly Bean MR1.
     * @deprecated use Build directly: {@code Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1}
     */
    @Deprecated
    public static boolean isVersionAfterJellyBeanMR1()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
