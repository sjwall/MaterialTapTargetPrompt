/*
 * Copyright (C) 2017-2019 Samuel Wall
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

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BuilderUnitTest
{
    @Test
    public void testBuilder_Activity_Resource()
    {
        ActivityScenario<EmptyTestActivity> scenario = ActivityScenario.launch(EmptyTestActivity.class);
        scenario.onActivity(activity -> {
            final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity, 0);
            assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
        });
    }

    @Test
    public void testBuilder_Activity()
    {
        ActivityScenario<EmptyTestActivity> scenario = ActivityScenario.launch(EmptyTestActivity.class);
        scenario.onActivity(activity -> {
            final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity);
            assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
        });
    }

    @Test
    public void testBuilder_Fragment()
    {
        FragmentScenario<EmptyTestFragment> scenario = FragmentScenario.launchInContainer(EmptyTestFragment.class);
        scenario.onFragment(fragment -> {
            final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment);
            assertTrue(builder.getResourceFinder() instanceof SupportFragmentResourceFinder);
        });
    }

    @Test
    public void testBuilder_Fragment_Resource()
    {
        FragmentScenario<EmptyTestFragment> scenario = FragmentScenario.launchInContainer(EmptyTestFragment.class);
        scenario.onFragment(fragment -> {
            final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment, 0);
            assertTrue(builder.getResourceFinder() instanceof SupportFragmentResourceFinder);
        });
    }

    @Test
    public void testBuilder_DialogFragment()
    {
        FragmentScenario<EmptyTestDialogFragment> scenario = FragmentScenario.launchInContainer(EmptyTestDialogFragment.class);
        scenario.onFragment(fragment -> {
            final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment);
            assertTrue(builder.getResourceFinder() instanceof SupportFragmentResourceFinder);
        });
    }
}
