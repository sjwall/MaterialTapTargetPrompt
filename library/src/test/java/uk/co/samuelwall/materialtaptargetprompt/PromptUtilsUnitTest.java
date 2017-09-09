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

import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
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

    @Test
    public void testScaleOriginIsNotCentre()
    {
        final RectF base = new RectF(0, 672, 841, 1508);
        final PointF origin = new PointF(540, 924);
        final RectF[] evenExpectedResults = {
                new RectF(540f, 924f, 540f, 924f), // 0
                new RectF(486f, 898.8f, 570.1f, 982.4f), // 0.1
                new RectF(432f, 873.6f, 600.2f, 1040.8f), // 0.2
                new RectF(378f, 848.4f, 630.3f, 1099.2f), // 0.3
                new RectF(324f, 823.2f, 660.4f, 1157.6f), // 0.4
                new RectF(270f, 798f, 690.5f, 1216f), // 0.5
                new RectF(216f, 772.8f, 720.6f, 1274.4f), // 0.6
                new RectF(162f, 747.6f, 750.7f, 1332.8f), // 0.7
                new RectF(108f, 722.4f, 780.8f, 1391.2f), // 0.8
                new RectF(54f, 697.2f, 810.9f, 1449.6f), // 0.9
                new RectF(0f, 672f, 841f, 1508f), // 1
                new RectF(-41.8f, 630.2f, 882.8f, 1549.8f), // 1.1
                new RectF(-83.6f, 588.4f, 924.6f, 1591.6f), // 1.2
                new RectF(-125.4f, 546.6f, 966.4f, 1633.4f), // 1.3
                new RectF(-167.2f, 504.8f, 1008.2f, 1675.2f), // 1.4
                new RectF(-209f, 463f, 1050f, 1717f), // 1.5
                new RectF(-250.8f, 421.2f, 1091.8f, 1758.8f), // 1.6
                new RectF(-292.6f, 379.4f, 1133.6f, 1800.6f), // 1.7
                new RectF(-334.4f, 337.6f, 1175.4f, 1842.4f), // 1.8
                new RectF(-376.2f, 295.8f, 1217.2f, 1884.2f), // 1.9
                new RectF(-418f, 254f, 1259f, 1926f) // 2
        };
        for (int i = 0, count = evenExpectedResults.length;  i < count; i++)
        {
            doScaleTest(origin, base, (float) i / 10, true, evenExpectedResults[i]);
        }
        final RectF[] expectedResults = {
                new RectF(540f, 924f, 540f, 924f), // 0
                new RectF(486f, 898.8f, 570.1f, 982.4f), // 0.1
                new RectF(432f, 873.6f, 600.2f, 1040.8f), // 0.2
                new RectF(378f, 848.4f, 630.3f, 1099.2f), // 0.3
                new RectF(324f, 823.2f, 660.4f, 1157.6f), // 0.4
                new RectF(270f, 798f, 690.5f, 1216f), // 0.5
                new RectF(216f, 772.8f, 720.6f, 1274.4f), // 0.6
                new RectF(162f, 747.6f, 750.7f, 1332.8f), // 0.7
                new RectF(108f, 722.4f, 780.8f, 1391.2f), // 0.8
                new RectF(54f, 697.2f, 810.9f, 1449.6f), // 0.9
                new RectF(0f, 672f, 841f, 1508f), // 1
                new RectF(-54f, 646.8f, 871.1f, 1566.4f), // 1.1
                new RectF(-108f, 621.6f, 901.2f, 1624.8f), // 1.2
                new RectF(-162f, 596.4f, 931.3f, 1683.2f), // 1.3
                new RectF(-216f, 571.2f, 961.4f, 1741.6f), // 1.4
                new RectF(-270f, 546f, 991.5f, 1800f), // 1.5
                new RectF(-324f, 520.8f, 1021.6f, 1858.4f), // 1.6
                new RectF(-378f, 495.6f, 1051.7f, 1916.8f), // 1.7
                new RectF(-432f, 470.4f, 1081.8f, 1975.2f), // 1.8
                new RectF(-486f, 445.2f, 1111.9f, 2033.6f), // 1.9
                new RectF(-540f, 420f, 1142f, 2092f), // 2
        };
        for (int i = 0, count = expectedResults.length;  i < count; i++)
        {
            doScaleTest(origin, base, (float) i / 10, false, expectedResults[i]);
        }
    }

    @Test
    public void testScaleOriginIsCentre()
    {
        final RectF base = new RectF(8, 1528, 508, 1752);
        final PointF origin = new PointF(258, 1640);
        final RectF[] evenExpectedResults = {
                new RectF(258f, 1640f, 258f, 1640f), // 0
                new RectF(233f, 1628.8f, 283f, 1651.2f), // 0.1
                new RectF(208f, 1617.6f, 308f, 1662.4f), // 0.2
                new RectF(183f, 1606.4f, 333f, 1673.6f), // 0.3
                new RectF(158f, 1595.2f, 358f, 1684.8f), // 0.4
                new RectF(133f, 1584f, 383f, 1696f), // 0.5
                new RectF(108f, 1572.8f, 408f, 1707.2f), // 0.6
                new RectF(83f, 1561.6f, 433f, 1718.4f), // 0.7
                new RectF(58f, 1550.4f, 458f, 1729.6f), // 0.8
                new RectF(33f, 1539.2f, 483f, 1740.8f), // 0.9
                new RectF(8, 1528, 508, 1752), // 1
                new RectF(-3.2f, 1516.8f, 519.2f, 1763.2f), // 1.1
                new RectF(-14.4f, 1505.6f, 530.4f, 1774.4f), // 1.2
                new RectF(-25.6f, 1494.4f, 541.6f, 1785.6f), // 1.3
                new RectF(-36.8f, 1483.2f, 552.8f, 1796.8f), // 1.4
                new RectF(-48f, 1472f, 564f, 1808f), // 1.5
                new RectF(-59.2f, 1460.8f, 575.2f, 1819.2f), // 1.6
                new RectF(-70.4f, 1449.6f, 586.4f, 1830.4f), // 1.7
                new RectF(-81.6f, 1438.4f, 597.6f, 1841.6f), // 1.8
                new RectF(-92.8f, 1427.2f, 608.8f, 1852.8f), // 1.9
                new RectF(-104f, 1416f, 620f, 1864f) // 2
            };
        for (int i = 0, count = evenExpectedResults.length;  i < count; i++)
        {
            doScaleTest(origin, base, (float) i / 10, true, evenExpectedResults[i]);
        }
        final RectF[] expectedResults = {
                new RectF(258f, 1640f, 258f, 1640f), // 0
                new RectF(233f, 1628.8f, 283f, 1651.2f), // 0.1
                new RectF(208f, 1617.6f, 308f, 1662.4f), // 0.2
                new RectF(183f, 1606.4f, 333f, 1673.6f), // 0.3
                new RectF(158f, 1595.2f, 358f, 1684.8f), // 0.4
                new RectF(133f, 1584f, 383f, 1696f), // 0.5
                new RectF(108f, 1572.8f, 408f, 1707.2f), // 0.6
                new RectF(83f, 1561.6f, 433f, 1718.4f), // 0.7
                new RectF(58f, 1550.4f, 458f, 1729.6f), // 0.8
                new RectF(33f, 1539.2f, 483f, 1740.8f), // 0.9
                new RectF(8, 1528, 508, 1752), // 1
                new RectF(-17f, 1516.8f, 533f, 1763.2f), // 1.1
                new RectF(-42f, 1505.6f, 558f, 1774.4f), // 1.2
                new RectF(-67f, 1494.4f, 583f, 1785.6f), // 1.3
                new RectF(-92f, 1483.2f, 608f, 1796.8f), // 1.4
                new RectF(-117f, 1472f, 633f, 1808f), // 1.5
                new RectF(-142f, 1460.8f, 658f, 1819.2f), // 1.6
                new RectF(-167f, 1449.6f, 683f, 1830.4f), // 1.7
                new RectF(-192f, 1438.4f, 708f, 1841.6f), // 1.8
                new RectF(-217f, 1427.2f, 733f, 1852.8f), // 1.9
                new RectF(-242f, 1416f, 758f, 1864f), // 2
        };
        for (int i = 0, count = expectedResults.length;  i < count; i++)
        {
            doScaleTest(origin, base, (float) i / 10, false, expectedResults[i]);
        }
    }

    private void doScaleTest(final PointF origin, final RectF base, final float scale, final boolean even, final RectF expected)
    {
        final RectF out = new RectF();
        PromptUtils.scale(origin, base, out, scale, even);
        assertEquals(expected.left, out.left, 0.1);
        assertEquals(expected.top, out.top, 0.1);
        assertEquals(expected.right, out.right, 0.1);
        assertEquals(expected.bottom, out.bottom, 0.1);
    }
}
