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
}
