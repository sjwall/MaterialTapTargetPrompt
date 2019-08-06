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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.fragment.app.DialogFragment;
import android.view.ViewGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class SupportFragmentResourceFinderUnitTest
{
    @Test
    public void testGetString()
    {
        final String resource = "test string";
        final int resourceId = 64532;
        final DialogFragment dialogFragment = spy(new DialogFragment());
        final SupportFragmentResourceFinder resourceFinder = new SupportFragmentResourceFinder(dialogFragment);
        final Context context = mock(Context.class);
        when(dialogFragment.getContext()).thenReturn(context);
        final Resources resources = mock(Resources.class);
        when(dialogFragment.getResources()).thenReturn(resources);
        when(dialogFragment.getString(resourceId)).thenReturn(resource);
        assertEquals(resource, resourceFinder.getString(resourceId));
    }

    @Test
    public void testGetDrawable()
    {

        final int resourceId = 64532;
        final DialogFragment dialogFragment = spy(new DialogFragment());
        final SupportFragmentResourceFinder resourceFinder = new SupportFragmentResourceFinder(dialogFragment);
        final Drawable resource = mock(Drawable.class);
        final Resources resources = mock(Resources.class);
        final Context context = mock(Context.class);
        when(dialogFragment.getContext()).thenReturn(context);
        when(dialogFragment.getResources()).thenReturn(resources);
        when(resources.getDrawable(resourceId)).thenReturn(resource);
        assertEquals(resource, resourceFinder.getDrawable(resourceId));
    }

    @Test
    public void testGetDrawablePreLollipop()
    {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 20);
        final Drawable resource = mock(Drawable.class);
        final int resourceId = 64532;
        final DialogFragment dialogFragment = spy(new DialogFragment());
        final SupportFragmentResourceFinder resourceFinder = new SupportFragmentResourceFinder(dialogFragment);
        final Resources resources = mock(Resources.class);
        final Context context = mock(Context.class);
        when(dialogFragment.getContext()).thenReturn(context);
        when(dialogFragment.getResources()).thenReturn(resources);
        when(resources.getDrawable(resourceId)).thenReturn(resource);
        assertEquals(resource, resourceFinder.getDrawable(resourceId));
    }
    @Test
    public void testGetPromptParentView()
    {
        final DialogFragment dialogFragment = spy(new DialogFragment());
        final ViewGroup parent = mock(ViewGroup.class);
        final ViewGroup view = mock(ViewGroup.class);
        final SupportFragmentResourceFinder resourceFinder = new SupportFragmentResourceFinder(dialogFragment);
        when(dialogFragment.getView()).thenReturn(view);
        when(view.getParent()).thenReturn(parent);
        assertEquals(parent, resourceFinder.getPromptParentView());
        assertEquals(parent, resourceFinder.getPromptParentView());
    }

    @Test
    public void testGetContext()
    {
        final DialogFragment dialogFragment = spy(new DialogFragment());
        final Context context = mock(Context.class);
        final SupportFragmentResourceFinder resourceFinder = new SupportFragmentResourceFinder(dialogFragment);
        when(dialogFragment.getContext()).thenReturn(context);
        assertEquals(context, resourceFinder.getContext());
    }
}
