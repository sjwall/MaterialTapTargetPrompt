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

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;

import java.text.Bidi;

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
    public static boolean isPointInCircle(final float x, final float y, final PointF circleCentre,
                                          final float radius)
    {
        return Math.pow(x - circleCentre.x, 2) + Math.pow(y - circleCentre.y, 2) < Math.pow(radius, 2);
    }

    /**
     * Based on setTypeface in android TextView, Copyright (C) 2006 The Android Open Source
     * Project. https://android.googlesource.com/platform/frameworks/base.git/+/master/core/java/android/widget/TextView.java
     */
    public static void setTypeface(TextPaint textPaint, Typeface typeface, int style)
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
    public static Typeface setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex)
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
        return tf;
    }

    /**
     * Based on parseTintMode in android appcompat v7 DrawableUtils, Copyright (C) 2014 The
     * Android Open Source Project. https://android.googlesource.com/platform/frameworks/support.git/+/master/v7/appcompat/src/android/support/v7/widget/DrawableUtils.java
     */
    public static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode)
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
    public static Layout.Alignment getTextAlignment(final ResourceFinder resourceFinder, final int gravity,
                                             final CharSequence text)
    {
        final int absoluteGravity;
        if (isVersionAfterJellyBeanMR1())
        {
            int realGravity = gravity;
            final int layoutDirection = resourceFinder.getResources().getConfiguration().getLayoutDirection();
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
     * Determines if Android is after Jelly Bean MR1.
     *
     * @return True if running on Android after Jelly Bean MR1.
     */
    public static boolean isVersionAfterJellyBeanMR1()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
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
    public static StaticLayout createStaticTextLayout(final CharSequence text,
                                                      final TextPaint paint,
                                                      final int maxTextWidth,
                                                      final Layout.Alignment textAlignment,
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
}
