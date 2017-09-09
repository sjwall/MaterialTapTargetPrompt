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

package uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds;

import android.graphics.PointF;

/**
 * Created by sam on 09/09/17.
 */

public class CirclePromptBackgroundUtils
{
    private CirclePromptBackgroundUtils() {}

    public static int getColour(final CirclePromptBackground promptBackground)
    {
        return promptBackground.mPaint.getColor();
    }

    public static float getBaseRadius(final CirclePromptBackground promptBackground)
    {
        return promptBackground.mBaseRadius;
    }

    public static PointF getBasePosition(final CirclePromptBackground promptBackground)
    {
        return promptBackground.mBasePosition;
    }
}
