/*
 * Copyright (C) 2017-2018 Samuel Wall
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import uk.co.samuelwall.materialtaptargetprompt.test.EmptyTestActivity;
import uk.co.samuelwall.materialtaptargetprompt.test.EmptyTestDialogFragment;
import uk.co.samuelwall.materialtaptargetprompt.test.EmptyTestFragment;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class BuilderUnitTest
{
    @Test
    public void testBuilder_Activity_Resource()
    {
        ActivityScenario scenario = ActivityScenario.launch(EmptyTestActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<EmptyTestActivity>()
        {
            @Override
            public void perform(EmptyTestActivity activity)
            {
                final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity, 0);
                assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
            }
        });
    }

    @Test
    public void testBuilder_Activity()
    {
        ActivityScenario scenario = ActivityScenario.launch(EmptyTestActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<EmptyTestActivity>()
        {
            @Override
            public void perform(EmptyTestActivity activity)
            {
                final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity);
                assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
            }
        });
    }

    @Test
    public void testBuilder_Fragment()
    {
        FragmentScenario scenario = FragmentScenario.launchInContainer(EmptyTestFragment.class);
        scenario.onFragment(new FragmentScenario.FragmentAction<EmptyTestFragment>()
        {
            @Override
            public void perform(@NonNull EmptyTestFragment fragment)
            {
                final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment);
                assertTrue(builder.getResourceFinder() instanceof SupportFragmentResourceFinder);
            }
        });
    }

    @Test
    public void testBuilder_Fragment_Resource()
    {
        FragmentScenario scenario = FragmentScenario.launchInContainer(EmptyTestFragment.class);
        scenario.onFragment(new FragmentScenario.FragmentAction<EmptyTestFragment>()
        {
            @Override
            public void perform(@NonNull EmptyTestFragment fragment)
            {
                final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment, 0);
                assertTrue(builder.getResourceFinder() instanceof SupportFragmentResourceFinder);
            }
        });
    }

    @Test
    public void testBuilder_DialogFragment()
    {
        FragmentScenario scenario = FragmentScenario.launchInContainer(EmptyTestDialogFragment.class);
        scenario.onFragment(new FragmentScenario.FragmentAction<EmptyTestDialogFragment>()
        {
            @Override
            public void perform(@NonNull EmptyTestDialogFragment fragment)
            {
                final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment);
                assertTrue(builder.getResourceFinder() instanceof SupportFragmentResourceFinder);
            }
        });
    }
}
