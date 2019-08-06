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
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import uk.co.samuelwall.materialtaptargetprompt.ActivityResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.R;
import uk.co.samuelwall.materialtaptargetprompt.ResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.TestResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.UnitTestUtils;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
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
    public void testPromptOptions_TargetView_Resource()
    {
        @IdRes final int resourceId = 325436;
        final View view = mock(View.class);
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        when(options.getResourceFinder().findViewById(resourceId)).thenReturn(view);
        assertEquals(options, options.setTarget(56, 24));
        assertEquals(options, options.setTarget(resourceId));
        assertEquals(view, options.getTargetView());
        assertEquals(null, options.getTargetRenderView());
        assertNull(options.getTargetPosition());
        assertTrue(options.isTargetSet());
    }

    @Test
    public void testPromptOptions_TargetView_Resource_Null()
    {
        @IdRes final int resourceId = 325436;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        when(options.getResourceFinder().findViewById(resourceId)).thenReturn(null);
        assertEquals(options, options.setTarget(56, 24));
        assertEquals(options, options.setTarget(resourceId));
        assertEquals(null, options.getTargetView());
        assertEquals(null, options.getTargetRenderView());
        assertNull(options.getTargetPosition());
        assertFalse(options.isTargetSet());
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
    public void testPromptOptions_TargetView_Load()
    {
        final @IdRes int resourceId = 325436;
        final @StyleRes int styleId = 436547;
        final View view = mock(View.class);
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        TestResourceFinder testResourceFinder = (TestResourceFinder) options.getResourceFinder();
        testResourceFinder.addFindByView(resourceId, view);
        final TypedArray typedArray = options.getResourceFinder().obtainStyledAttributes(styleId, new int[0]);
        when(typedArray.getResourceId(R.styleable.PromptView_mttp_target, 0)).thenReturn(resourceId);
        options.load(styleId);
    }

    @Test
    public void testPromptOptions_TargetView_Load_Null()
    {
        final @IdRes int resourceId = 325436;
        final @StyleRes int styleId = 436547;
        final PromptOptions options = UnitTestUtils.createPromptOptionsWithTestResourceFinder();
        TestResourceFinder testResourceFinder = (TestResourceFinder) options.getResourceFinder();
        testResourceFinder.addFindByView(resourceId, null);
        final TypedArray typedArray = options.getResourceFinder().obtainStyledAttributes(styleId, new int[0]);
        when(typedArray.getResourceId(R.styleable.PromptView_mttp_target, 0)).thenReturn(resourceId);
        options.load(styleId);
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

    @Test
    public void testPromptOptions_MaxTextWidth()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setMaxTextWidth(452f));
        assertEquals(452f, options.getMaxTextWidth(), 0);
    }

    @Test
    public void testPromptOptions_MaxTextWidth_Resource()
    {
        @DimenRes final int resourceId = 325436;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(235f);
        assertEquals(options, options.setMaxTextWidth(resourceId));
        assertEquals(235f, options.getMaxTextWidth(), 0);
    }

    @Test
    public void testPromptOptions_AutoDismiss_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertTrue(options.getAutoDismiss());
    }

    @Test
    public void testPromptOptions_AutoDismiss_Disabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setAutoDismiss(false));
        assertFalse(options.getAutoDismiss());
    }

    @Test
    public void testPromptOptions_AutoDismiss_Enabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setAutoDismiss(true));
        assertTrue(options.getAutoDismiss());
    }

    @Test
    public void testPromptOptions_AutoFinish_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertTrue(options.getAutoFinish());
    }

    @Test
    public void testPromptOptions_AutoFinish_Disabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setAutoFinish(false));
        assertFalse(options.getAutoFinish());
    }

    @Test
    public void testPromptOptions_AutoFinish_Enabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setAutoFinish(true));
        assertTrue(options.getAutoFinish());
    }

    @Test
    public void testPromptOptions_CaptureTouchEventOnFocal_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertFalse(options.getCaptureTouchEventOnFocal());
    }

    @Test
    public void testPromptOptions_CaptureTouchEventOnFocal_Disabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setCaptureTouchEventOnFocal(false));
        assertFalse(options.getCaptureTouchEventOnFocal());
    }

    @Test
    public void testPromptOptions_CaptureTouchEventOnFocal_Enabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setCaptureTouchEventOnFocal(true));
        assertTrue(options.getCaptureTouchEventOnFocal());
    }

    @Test
    public void testPromptOptions_CaptureTouchEventOutsidePrompt_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertFalse(options.getCaptureTouchEventOutsidePrompt());
    }

    @Test
    public void testPromptOptions_CaptureTouchEventOutsidePrompt_Disabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setCaptureTouchEventOutsidePrompt(false));
        assertFalse(options.getCaptureTouchEventOutsidePrompt());
    }

    @Test
    public void testPromptOptions_CaptureTouchEventOutsidePrompt_Enabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setCaptureTouchEventOutsidePrompt(true));
        assertTrue(options.getCaptureTouchEventOutsidePrompt());
    }

    @Test
    public void testPromptOptions_PromptText_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertNotNull(options.getPromptText());
    }

    @Test
    public void testPromptOptions_PromptText_Custom()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final PromptText text = new PromptText();
        assertEquals(options, options.setPromptText(text));
        assertEquals(text, options.getPromptText());
    }

    @Test
    public void testPromptOptions_PromptFocal_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertNotNull(options.getPromptFocal());
    }

    @Test
    public void testPromptOptions_PromptFocal_Custom()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final PromptFocal promptFocal = new RectanglePromptFocal();
        assertEquals(options, options.setPromptFocal(promptFocal));
        assertEquals(promptFocal, options.getPromptFocal());
        options.setPrimaryText("Primary Text");
        options.setTarget(mock(View.class));
        options.create();
    }

    @Test
    public void testPromptOptions_PromptBackground_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertNotNull(options.getPromptBackground());
    }

    @Test
    public void testPromptOptions_PromptBackground_Custom()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final PromptBackground promptBackground = new RectanglePromptBackground();
        assertEquals(options, options.setPromptBackground(promptBackground));
        assertEquals(promptBackground, options.getPromptBackground());
    }

    @Test
    public void testPromptOptions_FocalRadius_Default()
    {
        final PromptOptions options  = UnitTestUtils.createPromptOptions();
        assertEquals(44, options.getFocalRadius(), 0);
    }

    @Test
    public void testPromptOptions_FocalRadius()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setFocalRadius(436f));
        assertEquals(436f, options.getFocalRadius(), 0);
    }

    @Test
    public void testPromptOptions_FocalRadius_Resource()
    {
        @DimenRes final int resourceId = 254346;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(223f);
        assertEquals(options, options.setFocalRadius(resourceId));
        assertEquals(223f, options.getFocalRadius(), 0);
    }

    @Test
    public void testPromptOptions_ClipToView()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final View view = mock(View.class);
        assertEquals(options, options.setClipToView(view));
        assertEquals(view, options.getClipToView());
    }

    @Test
    public void testPromptOptions_TargetRenderView()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final View view = mock(View.class);
        assertEquals(options, options.setTargetRenderView(view));
        assertEquals(view, options.getTargetRenderView());
    }

    @Test
    public void testPromptOptions_PrimaryTextGravity_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(Gravity.START, options.getPrimaryTextGravity());
    }

    @Test
    public void testPromptOptions_PrimaryTextGravity()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setPrimaryTextGravity(Gravity.END));
        assertEquals(Gravity.END, options.getPrimaryTextGravity());
    }

    @Test
    public void testPromptOptions_SecondaryTextGravity_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(Gravity.START, options.getSecondaryTextGravity());
    }

    @Test
    public void testPromptOptions_SecondaryTextGravity()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setSecondaryTextGravity(Gravity.END));
        assertEquals(Gravity.END, options.getSecondaryTextGravity());
    }

    @Test
    public void testPromptOptions_TextGravity()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTextGravity(Gravity.END));
        assertEquals(Gravity.END, options.getPrimaryTextGravity());
        assertEquals(Gravity.END, options.getSecondaryTextGravity());
    }

    @Test
    public void testPromptOptions_PrimaryTextColour()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setPrimaryTextColour(Color.DKGRAY));
        assertEquals(Color.DKGRAY, options.getPrimaryTextColour());
    }

    @Test
    public void testPromptOptions_PrimaryTextColour_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(Color.WHITE, options.getPrimaryTextColour());
    }

    @Test
    public void testPromptOptions_SecondaryTextColour()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setSecondaryTextColour(Color.YELLOW));
        assertEquals(Color.YELLOW, options.getSecondaryTextColour());
    }

    @Test
    public void testPromptOptions_SecondaryTextColour_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(Color.argb(179, 255, 255, 255), options.getSecondaryTextColour());
    }

    @Test
    public void testPromptOptions_PrimaryTextTypefaceStyle()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final Typeface typeface = mock(Typeface.class);
        assertEquals(options, options.setPrimaryTextTypeface(typeface, Typeface.BOLD));
        assertEquals(typeface, options.getPrimaryTextTypeface());
        assertEquals(Typeface.BOLD, options.getPrimaryTextTypefaceStyle(), 0);
    }

    @Test
    public void testPromptOptions_PrimaryTextTypeface()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final Typeface typeface = mock(Typeface.class);
        assertEquals(options, options.setPrimaryTextTypeface(typeface));
        assertEquals(typeface, options.getPrimaryTextTypeface());
        assertEquals(Typeface.NORMAL, options.getPrimaryTextTypefaceStyle(), 0);
    }


    @Test
    public void testPromptOptions_SecondaryTextTypefaceStyle()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final Typeface typeface = mock(Typeface.class);
        assertEquals(options, options.setSecondaryTextTypeface(typeface, Typeface.BOLD));
        assertEquals(typeface, options.getSecondaryTextTypeface());
        assertEquals(Typeface.BOLD, options.getSecondaryTextTypefaceStyle(), 0);
    }

    @Test
    public void testPromptOptions_SecondaryTextTypeface()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        final Typeface typeface = mock(Typeface.class);
        assertEquals(options, options.setSecondaryTextTypeface(typeface));
        assertEquals(typeface, options.getSecondaryTextTypeface());
        assertEquals(Typeface.NORMAL, options.getSecondaryTextTypefaceStyle(), 0);
    }

    @Test
    public void testPromptOptions_TextPadding_Resource()
    {
        @DimenRes final int resourceId = 436455;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(12f);
        assertEquals(options, options.setTextPadding(resourceId));
        assertEquals(12f, options.getTextPadding(), 0);
    }

    @Test
    public void testPromptOptions_TextPadding_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(40f, options.getTextPadding(), 0);
    }

    @Test
    public void testPromptOptions_TextPadding()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTextPadding(23f));
        assertEquals(23f, options.getTextPadding(), 0);
    }

    @Test
    public void testPromptOptions_TextSeparation_Resource()
    {
        @DimenRes final int resourceId = 426547;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(15f);
        assertEquals(options, options.setTextSeparation(resourceId));
        assertEquals(15f, options.getTextSeparation(), 0);
    }

    @Test
    public void testPromptOptions_TextSeparation_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(16f, options.getTextSeparation(), 0);
    }

    @Test
    public void testPromptOptions_TextSeparation()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        assertEquals(options, options.setTextSeparation(23f));
        assertEquals(23f, options.getTextSeparation(), 0);
    }

    @Test
    public void testPromptOptions_FocalPadding_Resource()
    {
        @DimenRes final int resourceId = 243365;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        final Resources resources = mock(Resources.class);
        when(options.getResourceFinder().getResources()).thenReturn(resources);
        when(resources.getDimension(resourceId)).thenReturn(18f);
        assertEquals(options, options.setFocalPadding(resourceId));
        assertEquals(18f, options.getFocalPadding(), 0);
    }

    @Test
    public void testPromptOptions_FocalPadding_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(20f, options.getFocalPadding(), 0);
    }

    @Test
    public void testPromptOptions_FocalPadding()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        assertEquals(options, options.setFocalPadding(65f));
        assertEquals(65f, options.getFocalPadding(), 0);
    }

    @Test
    public void testPromptOptions_AnimationInterpolator()
    {
        final Interpolator animationInterpolator = mock(Interpolator.class);
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setAnimationInterpolator(animationInterpolator));
        assertEquals(animationInterpolator, options.getAnimationInterpolator());
    }

    @Test
    public void testPromptOptions_AnimationInterpolator_Null()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertNull(options.getAnimationInterpolator());
        options.setPrimaryText("Primary Text");
        options.setTarget(mock(View.class));
        options.create();
        assertNotNull(options.getAnimationInterpolator());
    }

    @Test
    public void testPromptOptions_IdleAnimation_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        assertTrue(options.getIdleAnimationEnabled());
    }

    @Test
    public void testPromptOptions_IdleAnimation_Disabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setIdleAnimationEnabled(false));
        assertFalse(options.getIdleAnimationEnabled());
    }

    @Test
    public void testPromptOptions_IdleAnimation_Enabled()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setIdleAnimationEnabled(true));
        assertTrue(options.getIdleAnimationEnabled());
    }

    @Test
    public void testPromptOptions_FocalColour_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(Color.WHITE, options.getFocalColour());
    }

    @Test
    public void testPromptOptions_FocalColour()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setFocalColour(Color.CYAN));
        assertEquals(Color.CYAN, options.getFocalColour());
    }

    @Test
    public void testPromptOptions_BackgroundColour_Default()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(Color.argb(244, 63, 81, 181), options.getBackgroundColour());
    }

    @Test
    public void testPromptOptions_BackgroundColour()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setBackgroundColour(Color.GREEN));
        assertEquals(Color.GREEN, options.getBackgroundColour());
    }

    @Test
    public void testPromptOptions_StateListener_Null()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setPromptStateChangeListener(null));
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_REVEALING);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_REVEALED);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_FOCAL_PRESSED);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_DISMISSED);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_DISMISSED);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_FINISHING);
        options.onPromptStateChanged(null, MaterialTapTargetPrompt.STATE_FINISHED);
    }

    @Test
    public void testPromptOptions_StateListener()
    {
        final MaterialTapTargetPrompt thePrompt = mock(MaterialTapTargetPrompt.class);
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
            @Override
            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state)
            {
                assertEquals(thePrompt, prompt);
                assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state, 0);
            }
        }));
        options.onPromptStateChanged(thePrompt, MaterialTapTargetPrompt.STATE_FINISHED);
    }

    @Test
    public void testPromptOptions_Icon()
    {
        final Drawable drawable = mock(Drawable.class);
        @DrawableRes final int resourceId = 235436;
        final PromptOptions options = UnitTestUtils.createPromptOptions(true);
        when(options.getResourceFinder().getDrawable(resourceId)).thenReturn(drawable);
        assertEquals(options, options.setIcon(resourceId));
        assertEquals(drawable, options.getIconDrawable());
    }

    @Test
    public void testPromptOptions_IconDrawable()
    {
        final Drawable drawable = mock(Drawable.class);
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        options.setPrimaryText("Primary Text");
        options.setTarget(mock(View.class));
        options.create();
        assertEquals(options, options.setIconDrawable(drawable));
        assertEquals(drawable, options.getIconDrawable());
        options.create();
        assertEquals(options, options.setIconDrawableTintMode(PorterDuff.Mode.ADD));
        assertEquals(options, options.setIconDrawableColourFilter(Color.argb(21,235,23,43)));
        options.create();
        assertEquals(options, options.setIconDrawableTintMode(null));
    }

    @Test
    public void testPromptOptions_IconDrawable_TintList()
    {
        final Drawable drawable = mock(Drawable.class);
        final ColorStateList colourStateList = mock(ColorStateList.class);
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setIconDrawable(drawable));
        assertEquals(drawable, options.getIconDrawable());
        assertEquals(options, options.setIconDrawableTintList(colourStateList));
        options.setPrimaryText("Primary Text");
        options.setTarget(mock(View.class));
        options.create();
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 16);
        options.create();
        assertEquals(options, options.setIconDrawableTintList(null));
    }

    @Test
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
    }

    @Test
    public void testPromptOptions_ShowFor()
    {
        final PromptOptions options = UnitTestUtils.createPromptOptions();
        assertEquals(options, options.setTarget(mock(View.class)));
        assertEquals(options, options.setPrimaryText("text"));
        assertEquals(options, options.setSecondaryText("text"));
        final MaterialTapTargetPrompt prompt = options.showFor(5000);
        assertNotNull(prompt);
    }

    @Test
    public void testPromptOptions_ShowFor_Empty()
    {
        assertNull(UnitTestUtils.createPromptOptions().showFor(6000));
    }
}
