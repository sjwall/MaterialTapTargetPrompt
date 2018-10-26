/*
 * Copyright (C) 2016-2018 Samuel Wall
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

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * {@link ImmersiveModeCompatPromptBackground} implementation that renders the prompt background as a rectangle for supporting immersive mode.
 */
public class ImmersiveModeCompatPromptBackground extends FullscreenPromptBackground
{
    @NonNull
    private final DisplayMetrics mBaseMetrics;
    @NonNull
    private final WindowManager mWindowManager;

    public ImmersiveModeCompatPromptBackground(@NonNull WindowManager windowManager) {
        mWindowManager = windowManager;
        mBaseMetrics = new DisplayMetrics();
    }

    @NonNull
    @Override
    protected DisplayMetrics getDisplayMetrics() {
        Display defaultDisplay = mWindowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(mBaseMetrics);
        } else {
            defaultDisplay.getMetrics(mBaseMetrics);
        }
        return mBaseMetrics;
    }
}
