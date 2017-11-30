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

import android.support.annotation.NonNull;
import android.text.Layout;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestPromptText extends PromptText
{
    boolean mRtl;
    float mMaxTextWidth;

    public TestPromptText(float maxTextWidth)
    {
        mMaxTextWidth = maxTextWidth;
    }

    public TestPromptText(float maxTextWidth, boolean rtl)
    {
        mMaxTextWidth = maxTextWidth;
        mRtl = rtl;
    }

    @Override
    void createTextLayout(@NonNull final PromptOptions options, final float maxWidth, final float alphaModifier)
    {
        super.createTextLayout(options, maxWidth, alphaModifier);
        if (mPrimaryTextLayout != null)
        {
            mPrimaryTextLayout = spy(mPrimaryTextLayout);
            when(mPrimaryTextLayout.getLineWidth(0)).thenReturn(mMaxTextWidth);
            when(mPrimaryTextLayout.getHeight()).thenReturn(200);
            if (mRtl)
            {
                when(mPrimaryTextLayout.getAlignment()).thenReturn(Layout.Alignment.ALIGN_OPPOSITE);
            }
        }
        if (mSecondaryTextLayout != null)
        {
            mSecondaryTextLayout = spy(mSecondaryTextLayout);
            when(mSecondaryTextLayout.getLineWidth(0)).thenReturn(mMaxTextWidth);
            when(mSecondaryTextLayout.getHeight()).thenReturn(200);
            if (mRtl)
            {
                when(mSecondaryTextLayout.getAlignment()).thenReturn(Layout.Alignment.ALIGN_OPPOSITE);
            }
        }
    }
}
