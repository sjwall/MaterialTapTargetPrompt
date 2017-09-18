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


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class BuilderUnitTest
{
    @Test
    public void testBuilder_Fragment()
    {
        final Fragment fragment = Robolectric.buildFragment(Fragment.class).create().get();
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment);
        assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
    }

    @Test
    public void testBuilder_Fragment_Resource()
    {
        final Fragment fragment = Robolectric.buildFragment(Fragment.class).create().get();
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(fragment, 0);
        assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
    }

    @Test
    public void testBuilder_DialogFragment()
    {
        final DialogFragment dialogFragment = spy(Robolectric.buildFragment(DialogFragment.class).create().get());
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        final Dialog dialog = mock(Dialog.class);
        when(dialogFragment.getDialog()).thenReturn(dialog);
        when(dialog.getOwnerActivity()).thenReturn(activity);
        when(dialog.findViewById(android.R.id.content)).thenReturn(activity.findViewById(android.R.id.content));
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(dialogFragment);
        assertTrue(builder.getResourceFinder() instanceof DialogResourceFinder);
    }

    @Test
    public void testBuilder_Dialog()
    {
        final Dialog dialog = mock(Dialog.class);
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        when(dialog.getOwnerActivity()).thenReturn(activity);
        when(dialog.findViewById(android.R.id.content)).thenReturn(activity.findViewById(android.R.id.content));
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(dialog);
        assertTrue(builder.getResourceFinder() instanceof DialogResourceFinder);
    }

    @Test
    public void testBuilder_Activity()
    {
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity);
        assertTrue(builder.getResourceFinder() instanceof ActivityResourceFinder);
    }
}
