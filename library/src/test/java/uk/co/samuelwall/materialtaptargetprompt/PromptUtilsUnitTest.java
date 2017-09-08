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

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.TextPaint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class PromptUtilsUnitTest
{
    @Test
    public void testParseTintMode()
    {
        assertEquals(PromptUtils.parseTintMode(-1, null), null);
        assertEquals(PromptUtils.parseTintMode(3, null), PorterDuff.Mode.SRC_OVER);
        assertEquals(PromptUtils.parseTintMode(5, null), PorterDuff.Mode.SRC_IN);
        assertEquals(PromptUtils.parseTintMode(9, null), PorterDuff.Mode.SRC_ATOP);
        assertEquals(PromptUtils.parseTintMode(14, null), PorterDuff.Mode.MULTIPLY);
        assertEquals(PromptUtils.parseTintMode(15, null), PorterDuff.Mode.SCREEN);
        assertEquals(PromptUtils.parseTintMode(16, null), PorterDuff.Mode.ADD);
    }

    @Test
    public void testSetTypeface()
    {
        TextPaint textPaint = new TextPaint();

        PromptUtils.setTypeface(textPaint, Typeface.MONOSPACE, 0);
        assertEquals(Typeface.MONOSPACE, textPaint.getTypeface());

        PromptUtils.setTypeface(textPaint, null, Typeface.NORMAL);
        assertEquals(Typeface.DEFAULT, textPaint.getTypeface());

        PromptUtils.setTypeface(textPaint, Typeface.MONOSPACE, Typeface.NORMAL);
        assertEquals(Typeface.NORMAL, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, null, Typeface.BOLD);
        assertEquals(Typeface.BOLD, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.MONOSPACE, Typeface.BOLD);
        assertEquals(Typeface.BOLD, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, null, Typeface.ITALIC);
        assertEquals(Typeface.ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.MONOSPACE, Typeface.ITALIC);
        assertEquals(Typeface.ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, null, Typeface.BOLD_ITALIC);
        assertEquals(Typeface.BOLD_ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.MONOSPACE, Typeface.BOLD_ITALIC);
        assertEquals(Typeface.BOLD_ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD_ITALIC);
        assertEquals(Typeface.BOLD_ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.defaultFromStyle(Typeface.BOLD), Typeface.ITALIC);
        assertEquals(Typeface.ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);
        assertEquals(Typeface.BOLD, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
        assertEquals(Typeface.ITALIC, textPaint.getTypeface().getStyle());

        PromptUtils.setTypeface(textPaint, Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.BOLD);
        assertEquals(Typeface.BOLD, textPaint.getTypeface().getStyle());
    }

    @Test
    public void testSetTypefaceFromAttrs()
    {
        Typeface typeface = PromptUtils.setTypefaceFromAttrs("Arial", 0, Typeface.NORMAL);
        assertNotNull(typeface);
        assertEquals(Typeface.NORMAL, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs("Arial", 0, Typeface.BOLD);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs("Arial", 0, Typeface.ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs("Arial", 0, Typeface.BOLD_ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD_ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 1, Typeface.NORMAL);
        assertNotNull(typeface);
        assertEquals(Typeface.NORMAL, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 1, Typeface.BOLD);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 1, Typeface.ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 1, Typeface.BOLD_ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD_ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 2, Typeface.NORMAL);
        assertNotNull(typeface);
        assertEquals(Typeface.NORMAL, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 2, Typeface.BOLD);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 2, Typeface.ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 2, Typeface.BOLD_ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD_ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 3, Typeface.NORMAL);
        assertNotNull(typeface);
        assertEquals(Typeface.NORMAL, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 3, Typeface.BOLD);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 3, Typeface.ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.ITALIC, typeface.getStyle());

        typeface = PromptUtils.setTypefaceFromAttrs(null, 3, Typeface.BOLD_ITALIC);
        assertNotNull(typeface);
        assertEquals(Typeface.BOLD_ITALIC, typeface.getStyle());
    }

    /*@Test
    @TargetApi(Build.VERSION_CODES.M)
    public void testCreateStaticTextLayout()
    {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", Build.VERSION_CODES.M);
        final TextPaint textPaint = new TextPaint();
        final StaticLayout layout = PromptUtils.createStaticTextLayout("test", textPaint, 100, Layout.Alignment.ALIGN_CENTER, 1);
        assertNotNull(layout);
        assertEquals(Layout.Alignment.ALIGN_CENTER, layout.getAlignment());
        assertEquals("test", layout.getText());
        assertEquals(textPaint, layout.getPaint());
    }*/
}
