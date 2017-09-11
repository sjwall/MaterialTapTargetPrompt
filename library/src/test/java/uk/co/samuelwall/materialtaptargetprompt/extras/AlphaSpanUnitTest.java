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
import android.text.TextPaint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class AlphaSpanUnitTest
{
    @Test
    public void testAlphaSpan()
    {
        final AlphaSpan alphaSpan = new AlphaSpan(0.5f);
        final TextPaint textPaint = new TextPaint();
        textPaint.setAlpha(200);
        textPaint.bgColor = Color.argb(100, 100, 100, 100);
        alphaSpan.updateDrawState(textPaint);
        assertEquals(100, textPaint.getAlpha());
        assertEquals(Color.argb(50, 100, 100, 100), textPaint.bgColor);
    }
}
