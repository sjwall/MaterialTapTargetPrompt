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

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

/**
 * {@link CharacterStyle} class the modifies the painted foreground and background alpha values.
 */
class AlphaSpan extends CharacterStyle
{
    /**
     * The alpha modification value between 1 and 0.
     */
    private final float mValue;

    /**
     * Constructor.
     *
     * @param value The alpha modification value between 1 and 0.
     */
    AlphaSpan(final float value)
    {
        mValue = value;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint paint)
    {
        paint.setAlpha((int) (paint.getAlpha() * mValue));
        paint.bgColor = Color.argb((int) (Color.alpha(paint.bgColor) * mValue),
                Color.red(paint.bgColor), Color.green(paint.bgColor), Color.blue(paint.bgColor));
    }
}
