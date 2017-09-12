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

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.co.samuelwall.materialtaptargetprompt.ActivityResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.ResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class PromptOptionsUnitTest
{
    @Test
    public void testPromptOptions_ResourceFinder()
    {
        final ResourceFinder resourceFinder = new ActivityResourceFinder(Robolectric.buildActivity(Activity.class).create().get());
        final PromptOptions options = new PromptOptions(resourceFinder);
        assertEquals(resourceFinder, options.getResourceFinder());
    }


    @Test
    public void testPromptOptions_TargetView()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final View view = mock(View.class);
        assertEquals(options, options.setTarget(56, 24));
        assertEquals(options, options.setTarget(view));
        assertEquals(view, options.getTargetView());
        assertEquals(null, options.getTargetRenderView());
        assertNull(options.getTargetPosition());
        assertTrue(options.isTargetSet());
    }

    @Test
    public void testPromptOptions_TargetView_Null()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(56, 24));
        assertEquals(options, options.setTarget(null));
        assertEquals(null, options.getTargetView());
        assertEquals(null, options.getTargetRenderView());
        assertNull(options.getTargetPosition());
        assertFalse(options.isTargetSet());
    }

    @Test
    public void testPromptOptions_TargetPosition()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final View view = mock(View.class);
        assertEquals(options, options.setTarget(view));
        assertEquals(options, options.setTarget(56, 24));
        assertEquals(56, options.getTargetPosition().x, 0);
        assertEquals(24, options.getTargetPosition().y, 0);
        assertNull(options.getTargetView());
        assertNull(options.getTargetRenderView());
        assertTrue(options.isTargetSet());
    }

    @Test
    public void testPromptOptions_PrimaryText_String()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final String text = "text";
        assertEquals(options, options.setPrimaryText(text));
        assertEquals(text, options.getPrimaryText());
    }

    @Test
    public void testPromptOptions_PrimaryText_CharSequence()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final CharSequence text = "text";
        assertEquals(options, options.setPrimaryText(text));
        assertEquals(text, options.getPrimaryText());
    }

    @Test
    public void testPromptOptions_PrimaryText_Resource()
    {
        final String text = "test";
        @StringRes final int resourceId = 235435;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        when(options.getResourceFinder().getString(resourceId)).thenReturn(text);
        assertEquals(options, options.setPrimaryText(resourceId));
        assertEquals(text, options.getPrimaryText());
    }

    @Test
    public void testPromptOptions_PrimaryTextSize()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setPrimaryTextSize(15f));
        assertEquals(15f, options.getPrimaryTextSize(), 1);
    }

    @Test
    public void testPromptOptions_PrimaryTextSize_Resource()
    {
        @DimenRes final int resourceId = 324356;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(14f);
        assertEquals(options, options.setPrimaryTextSize(resourceId));
        assertEquals(14f, options.getPrimaryTextSize(), 1);
    }

    @Test
    public void testPromptOptions_SecondaryText_String()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final String text = "text";
        assertEquals(options, options.setSecondaryText(text));
        assertEquals(text, options.getSecondaryText());
    }

    @Test
    public void testPromptOptions_SecondaryText_CharSequence()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final CharSequence text = "text";
        assertEquals(options, options.setSecondaryText(text));
        assertEquals(text, options.getSecondaryText());
    }

    @Test
    public void testPromptOptions_SecondaryText_Resource()
    {
        final String text = "test";
        @StringRes final int resourceId = 245686;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        when(options.getResourceFinder().getString(resourceId)).thenReturn(text);
        assertEquals(options, options.setSecondaryText(resourceId));
        assertEquals(text, options.getSecondaryText());
    }

    @Test
    public void testPromptOptions_SecondaryTextSize()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setSecondaryTextSize(15f));
        assertEquals(15f, options.getSecondaryTextSize(), 1);
    }

    @Test
    public void testPromptOptions_SecondaryTextSize_Resource()
    {
        @DimenRes final int resourceId = 325436;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(14f);
        assertEquals(options, options.setSecondaryTextSize(resourceId));
        assertEquals(14f, options.getSecondaryTextSize(), 1);
    }

    @Test
    public void testPromptOptions_BackButtonDismiss_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertTrue(options.getBackButtonDismissEnabled());
    }

    @Test
    public void testPromptOptions_BackButtonDismiss_Enabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setBackButtonDismissEnabled(true));
        assertTrue(options.getBackButtonDismissEnabled());
    }

    @Test
    public void testPromptOptions_BackButtonDismiss_Disabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setBackButtonDismissEnabled(false));
        assertFalse(options.getBackButtonDismissEnabled());
    }

    /*@Test
    public void testPromptOptions_CreateEmpty()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertNull(options.create());
    }

    @Test
    public void testPromptOptions_ShowEmpty()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertNull(options.show());
    }

    @Test
    public void testPromptOptions_Create_NullTarget()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(null));
        assertNull(options.create());
    }

    @Test
    public void testPromptOptions_Create_NullText()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(mock(View.class)));
        assertNull(options.create());
    }

    @Test
    public void testPromptOptions_Create_NullPrimaryText()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(mock(View.class)));
        assertEquals(options, options.setSecondaryText("text"));
        assertNotNull(options.create());
    }

    @Test
    public void testPromptOptions_Create_NullSecondaryText()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(mock(View.class)));
        assertEquals(options, options.setPrimaryText("text"));
        assertNotNull(options.create());
    }

    @Test
    public void testPromptOptions_Create()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(mock(View.class)));
        assertEquals(options, options.setPrimaryText("text"));
        assertEquals(options, options.setSecondaryText("text"));
        assertNotNull(options.create());
    }

    @Test
    public void testPromptOptions_Show()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(mock(View.class)));
        assertEquals(options, options.setPrimaryText("text"));
        assertEquals(options, options.setSecondaryText("text"));
        assertNotNull(options.show());
    }*/
}
