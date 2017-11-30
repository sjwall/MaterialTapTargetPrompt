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
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DialogResourceFinderUnitTest
{
    @Test
    public void testGetPromptParentView()
    {
        final Dialog dialog = mock(Dialog.class);
        final Activity activity = mock(Activity.class);
        final Window window = mock(Window.class);
        final ViewGroup decorView = mock(ViewGroup.class);
        final DialogResourceFinder resourceFinder = new DialogResourceFinder(dialog);
        when(window.getDecorView()).thenReturn(decorView);
        when(dialog.getWindow()).thenReturn(window);
        when(dialog.getOwnerActivity()).thenReturn(activity);
        assertEquals(decorView, resourceFinder.getPromptParentView());
    }

    @Test
    public void testGetContext()
    {
        final Dialog dialog = mock(Dialog.class);
        final Activity activity = mock(Activity.class);
        final Context context = mock(Context.class);
        final DialogResourceFinder resourceFinder = new DialogResourceFinder(dialog);
        when(dialog.getOwnerActivity()).thenReturn(activity);
        when(dialog.getContext()).thenReturn(context);
        assertEquals(context, resourceFinder.getContext());
    }
}
