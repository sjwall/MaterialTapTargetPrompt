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

package uk.co.samuelwall.materialtaptargetprompt;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

/**
 * Base test class that provides easy state testing with listeners.
 */
public class BaseTestStateProgress
{
    /**
     * The current state.
     * Change this value through the listeners.
     */
    protected int actualStateProgress;

    /**
     * The expected state progress value.
     */
    protected int expectedStateProgress;

    @Before
    public void setup()
    {
        // Reset the state progress
        actualStateProgress = 0;
        expectedStateProgress = -1;
    }

    @After
    public void after()
    {
        // If there is an expected state progress
        if (expectedStateProgress > -1)
        {
            Assert.assertEquals(expectedStateProgress, actualStateProgress);
        }
    }
}
