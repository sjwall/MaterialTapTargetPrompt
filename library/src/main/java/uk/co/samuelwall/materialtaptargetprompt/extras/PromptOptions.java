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

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.R;
import uk.co.samuelwall.materialtaptargetprompt.ResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.CirclePromptFocal;

public class PromptOptions<T extends PromptOptions>
{
    /**
     * The {@link ResourceFinder} used to find views and resources.
     */
    private ResourceFinder mResourceFinder;

    /**
     * Has the target been set successfully.
     */
    private boolean mTargetSet;

    /**
     * The view to place the prompt around.
     */
    private View mTargetView;

    /**
     * The left and top positioning for the focal centre point.
     */
    private PointF mTargetPosition;

    /**
     * The text to display.
     */
    private CharSequence mPrimaryText, mSecondaryText;
    private @ColorInt int mPrimaryTextColour, mSecondaryTextColour, mBackgroundColour, mFocalColour;

    private float mFocalRadius;
    private float mPrimaryTextSize, mSecondaryTextSize;
    private float mMaxTextWidth;
    private float mTextPadding;
    private float mFocalPadding;
    private Interpolator mAnimationInterpolator;
    private Drawable mIconDrawable;

    /**
     * Should the back button press dismiss the prompt.
     */
    private boolean mBackButtonDismissEnabled = true;

    /**
     * Listener for when the prompt state changes.
     */
    private MaterialTapTargetPrompt.PromptStateChangeListener mPromptStateChangeListener;

    private boolean mCaptureTouchEventOnFocal;
    private float mTextSeparation;
    private boolean mAutoDismiss, mAutoFinish;
    private boolean mCaptureTouchEventOutsidePrompt;
    private Typeface mPrimaryTextTypeface, mSecondaryTextTypeface;
    private int mPrimaryTextTypefaceStyle, mSecondaryTextTypefaceStyle;
    private ColorStateList mIconDrawableTintList = null;
    private PorterDuff.Mode mIconDrawableTintMode = null;
    private boolean mHasIconDrawableTint;
    private int mIconDrawableColourFilter;
    private View mTargetRenderView;
    private boolean mIdleAnimationEnabled = true;
    private int mPrimaryTextGravity = Gravity.START, mSecondaryTextGravity = Gravity.START;
    private View mClipToView;
    private float m88dp;

    /**
     * The shape to render for the prompt background.
     */
    private PromptBackground mPromptBackground = new CirclePromptBackground();

    /**
     * The shape to render for the prompt focal.
     */
    private PromptFocal mPromptFocal = new CirclePromptFocal();

    private PromptText mPromptText = new PromptText();

    public PromptOptions(final ResourceFinder resourceFinder)
    {
        mResourceFinder = resourceFinder;
    }

    public void load(int themeResId)
    {
        //Attempt to load the theme from the activity theme
        if (themeResId == 0)
        {
            final TypedValue outValue = new TypedValue();
            mResourceFinder.getTheme().resolveAttribute(R.attr.MaterialTapTargetPromptTheme, outValue, true);
            themeResId = outValue.resourceId;
        }

        final float density = mResourceFinder.getResources().getDisplayMetrics().density;
        m88dp = 88 * density;
        final TypedArray a = mResourceFinder.obtainStyledAttributes(themeResId, R.styleable.PromptView);
        mPrimaryTextColour = a.getColor(R.styleable.PromptView_mttp_primaryTextColour, Color.WHITE);
        mSecondaryTextColour = a.getColor(R.styleable.PromptView_mttp_secondaryTextColour, Color.argb(179, 255, 255, 255));
        mPrimaryText = a.getString(R.styleable.PromptView_mttp_primaryText);
        mSecondaryText = a.getString(R.styleable.PromptView_mttp_secondaryText);
        mBackgroundColour = a.getColor(R.styleable.PromptView_mttp_backgroundColour, Color.argb(244, 63, 81, 181));
        mFocalColour = a.getColor(R.styleable.PromptView_mttp_focalColour, Color.WHITE);
        mFocalRadius = a.getDimension(R.styleable.PromptView_mttp_focalRadius, density * 44);
        mPrimaryTextSize = a.getDimension(R.styleable.PromptView_mttp_primaryTextSize, 22 * density);
        mSecondaryTextSize = a.getDimension(R.styleable.PromptView_mttp_secondaryTextSize, 18 * density);
        mMaxTextWidth = a.getDimension(R.styleable.PromptView_mttp_maxTextWidth, 400 * density);
        mTextPadding = a.getDimension(R.styleable.PromptView_mttp_textPadding, 40 * density);
        mFocalPadding = a.getDimension(R.styleable.PromptView_mttp_focalToTextPadding, 20 * density);
        mTextSeparation = a.getDimension(R.styleable.PromptView_mttp_textSeparation, 16 * density);
        mAutoDismiss = a.getBoolean(R.styleable.PromptView_mttp_autoDismiss, true);
        mAutoFinish = a.getBoolean(R.styleable.PromptView_mttp_autoFinish, true);
        mCaptureTouchEventOutsidePrompt = a.getBoolean(R.styleable.PromptView_mttp_captureTouchEventOutsidePrompt, false);
        mCaptureTouchEventOnFocal = a.getBoolean(R.styleable.PromptView_mttp_captureTouchEventOnFocal, false);
        mPrimaryTextTypefaceStyle = a.getInt(R.styleable.PromptView_mttp_primaryTextStyle, 0);
        mSecondaryTextTypefaceStyle = a.getInt(R.styleable.PromptView_mttp_secondaryTextStyle, 0);
        mPrimaryTextTypeface = PromptUtils.setTypefaceFromAttrs(a.getString(R.styleable.PromptView_mttp_primaryTextFontFamily), a.getInt(R.styleable.PromptView_mttp_primaryTextTypeface, 0), mPrimaryTextTypefaceStyle);
        mSecondaryTextTypeface = PromptUtils.setTypefaceFromAttrs(a.getString(R.styleable.PromptView_mttp_secondaryTextFontFamily), a.getInt(R.styleable.PromptView_mttp_secondaryTextTypeface, 0), mSecondaryTextTypefaceStyle);

        mIconDrawableColourFilter = a.getColor(R.styleable.PromptView_mttp_iconColourFilter, mBackgroundColour);
        mIconDrawableTintList = a.getColorStateList(R.styleable.PromptView_mttp_iconTint);
        mIconDrawableTintMode = PromptUtils.parseTintMode(a.getInt(R.styleable.PromptView_mttp_iconTintMode, -1), PorterDuff.Mode.MULTIPLY);
        mHasIconDrawableTint = true;

        final int targetId = a.getResourceId(R.styleable.PromptView_mttp_target, 0);
        a.recycle();

        if (targetId != 0)
        {
            mTargetView = mResourceFinder.findViewById(targetId);
            if (mTargetView != null)
            {
                mTargetSet = true;
            }
        }

        mClipToView = (View) mResourceFinder.findViewById(android.R.id.content).getParent();
    }

    public ResourceFinder getResourceFinder()
    {
        return mResourceFinder;
    }

    /**
     * Set the view for the prompt to focus on.
     *
     * @param target The view that the prompt will highlight.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTarget(final View target)
    {
        mTargetView = target;
        mTargetPosition = null;
        mTargetSet = mTargetView != null;
        return (T) this;
    }

    /**
     * Set the view for the prompt to focus on using the given resource id.
     *
     * @param target The view that the prompt will highlight.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTarget(@IdRes final int target)
    {
        mTargetView = mResourceFinder.findViewById(target);
        mTargetPosition = null;
        mTargetSet = mTargetView != null;
        return (T) this;
    }

    public View getTargetView()
    {
        return mTargetView;
    }

    /**
     * Set the centre point as a screen position
     *
     * @param left Centre point from screen left
     * @param top  Centre point from screen top
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTarget(final float left, final float top)
    {
        mTargetView = null;
        mTargetPosition = new PointF(left, top);
        mTargetSet = true;
        return (T) this;
    }

    public PointF getTargetPosition()
    {
        return mTargetPosition;
    }

    /**
     * Change the view that is rendered as the target.
     * By default the view from {@link #setTarget(View)} is rendered as the target.
     *
     * @param view The view to use to render the prompt target
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTargetRenderView(final View view)
    {
        mTargetRenderView = view;
        return (T) this;
    }

    public View getTargetRenderView()
    {
        return mTargetRenderView;
    }

    /**
     * Has the target been set successfully?
     *
     * @return True if set successfully.
     */
    public boolean isTargetSet()
    {
        return mTargetSet;
    }

    /**
     * Set the primary text using the given resource id.
     *
     * @param resId The string resource id for the primary text
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryText(@StringRes final int resId)
    {
        mPrimaryText = mResourceFinder.getString(resId);
        return (T) this;
    }

    /**
     * Set the primary text to the given string
     *
     * @param text The primary text
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryText(final String text)
    {
        mPrimaryText = text;
        return (T) this;
    }

    /**
     * Set the primary text to the given CharSequence.
     * It is recommended that you don't go crazy with custom Spannables.
     *
     * @param text The primary text as CharSequence
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryText(final CharSequence text)
    {
        mPrimaryText = text;
        return (T) this;
    }

    public CharSequence getPrimaryText()
    {
        return mPrimaryText;
    }

    /**
     * Set the primary text font size.
     *
     * @param size The primary text font size
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryTextSize(final float size)
    {
        mPrimaryTextSize = size;
        return (T) this;
    }

    /**
     * Set the primary text font size using the given resource id.
     *
     * @param resId The resource id for the primary text size
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryTextSize(@DimenRes final int resId)
    {
        mPrimaryTextSize = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    public float getPrimaryTextSize()
    {
        return mPrimaryTextSize;
    }

    /**
     * Set the primary text colour.
     *
     * @param colour The primary text colour resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryTextColour(@ColorInt final int colour)
    {
        mPrimaryTextColour = colour;
        return (T) this;
    }

    public @ColorInt int getPrimaryTextColour()
    {
        return mPrimaryTextColour;
    }

    /**
     * Sets the typeface and style used to display the primary text.
     *
     * @param typeface The primary text typeface
     */
    public T setPrimaryTextTypeface(final Typeface typeface)
    {
        return setPrimaryTextTypeface(typeface, 0);
    }

    /**
     * Sets the typeface used to display the primary text.
     *
     * @param typeface The primary text typeface
     * @param style    The typeface style
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryTextTypeface(final Typeface typeface, final int style)
    {
        mPrimaryTextTypeface = typeface;
        mPrimaryTextTypefaceStyle = style;
        return (T) this;
    }

    public Typeface getPrimaryTextTypeface()
    {
        return mPrimaryTextTypeface;
    }

    public int getPrimaryTextTypefaceStyle()
    {
        return mPrimaryTextTypefaceStyle;
    }

    /**
     * Set the secondary text using the given resource id.
     *
     * @param resId The secondary text resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryText(@StringRes final int resId)
    {
        mSecondaryText = mResourceFinder.getString(resId);
        return (T) this;
    }

    /**
     * Set the secondary text.
     *
     * @param text The secondary text
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryText(final String text)
    {
        mSecondaryText = text;
        return (T) this;
    }

    /**
     * Set the secondary text.
     * It is recommended that you don't go crazy with custom Spannables.
     *
     * @param text The secondary text as a CharSequence
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryText(final CharSequence text)
    {
        mSecondaryText = text;
        return (T) this;
    }

    public CharSequence getSecondaryText()
    {
        return mSecondaryText;
    }

    /**
     * Set the secondary text font size using the give resource id.
     *
     * @param resId The secondary text string resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryTextSize(@DimenRes final int resId)
    {
        mSecondaryTextSize = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set the secondary text font size.
     *
     * @param size The secondary text font size
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryTextSize(final float size)
    {
        mSecondaryTextSize = size;
        return (T) this;
    }

    public float getSecondaryTextSize()
    {
        return mSecondaryTextSize;
    }

    /**
     * Set the secondary text colour.
     *
     * @param colour The secondary text colour resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryTextColour(@ColorInt final int colour)
    {
        mSecondaryTextColour = colour;
        return (T) this;
    }

    public int getSecondaryTextColour()
    {
        return mSecondaryTextColour;
    }

    /**
     * Sets the typeface used to display the secondary text.
     *
     * @param typeface The secondary text typeface
     */
    public T setSecondaryTextTypeface(final Typeface typeface)
    {
        return setSecondaryTextTypeface(typeface, 0);
    }

    /**
     * Sets the typeface and style used to display the secondary text.
     *
     * @param typeface The secondary text typeface
     * @param style    The typeface style
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryTextTypeface(final Typeface typeface, final int style)
    {
        mSecondaryTextTypeface = typeface;
        mSecondaryTextTypefaceStyle = style;
        return (T) this;
    }

    public Typeface getSecondaryTextTypeface()
    {
        return mSecondaryTextTypeface;
    }

    public int getSecondaryTextTypefaceStyle()
    {
        return mSecondaryTextTypefaceStyle;
    }

    /**
     * Set the text left and right padding.
     *
     * @param padding The padding on the text left and right
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTextPadding(final float padding)
    {
        mTextPadding = padding;
        return (T) this;
    }

    /**
     * Set the text left and right padding using the given resource id.
     *
     * @param resId The text padding dimension resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTextPadding(@DimenRes final int resId)
    {
        mTextPadding = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    public float getTextPadding()
    {
        return mTextPadding;
    }

    /**
     * Set the distance between the primary and secondary text.
     *
     * @param separation The distance separation between the primary and secondary text
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTextSeparation(final float separation)
    {
        mTextSeparation = separation;
        return (T) this;
    }

    /**
     * Set the distance between the primary and secondary text using the given resource id.
     *
     * @param resId The dimension resource id for the text separation
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTextSeparation(@DimenRes final int resId)
    {
        mTextSeparation = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    public float getTextSeparation()
    {
        return mTextSeparation;
    }

    /**
     * Set the padding between the text and the focal point.
     *
     * @param padding The distance between the text and focal
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setFocalPadding(final float padding)
    {
        mFocalPadding = padding;
        return (T) this;
    }

    /**
     * Set the padding between the text and the focal point using the given resource id.
     *
     * @param resId The dimension resource id for the focal to text distance
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setFocalPadding(@DimenRes final int resId)
    {
        mFocalPadding = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    public float getFocalPadding()
    {
        return mFocalPadding;
    }

    /**
     * Set the interpolator to use in animations.
     *
     * @param interpolator The animation interpolator to use
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setAnimationInterpolator(final Interpolator interpolator)
    {
        mAnimationInterpolator = interpolator;
        return (T) this;
    }

    public Interpolator getAnimationInterpolator()
    {
        return mAnimationInterpolator;
    }

    /**
     * Enable/disable animation above target.
     * true by default
     *
     * @param enabled Idle animation enabled
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setIdleAnimationEnabled(final boolean enabled)
    {
        mIdleAnimationEnabled = enabled;
        return (T) this;
    }

    public boolean getIdleAnimationEnabled()
    {
        return mIdleAnimationEnabled;
    }

    /**
     * Set the icon to draw in the focal point using the given resource id.
     *
     * @param resId The drawable resource id for the icon
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setIcon(@DrawableRes final int resId)
    {
        mIconDrawable = mResourceFinder.getDrawable(resId);
        return (T) this;
    }

    /**
     * Set the icon to draw in the focal point.
     *
     * @param drawable The drawable for the icon
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setIconDrawable(final Drawable drawable)
    {
        mIconDrawable = drawable;
        return (T) this;
    }

    public Drawable getIconDrawable()
    {
        return mIconDrawable;
    }

    /**
     * Applies a tint to the icon drawable
     *
     * @param tint the tint to apply to the icon drawable, {@code null} will remove the tint.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setIconDrawableTintList(@Nullable ColorStateList tint)
    {
        mIconDrawableTintList = tint;
        mHasIconDrawableTint = tint != null;
        return (T) this;
    }

    /**
     * Sets the PorterDuff mode to use to apply the tint.
     *
     * @param tintMode the tint mode to use on the icon drawable, {@code null} will remove the
     *                 tint.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setIconDrawableTintMode(@Nullable PorterDuff.Mode tintMode)
    {
        mIconDrawableTintMode = tintMode;
        if (tintMode == null)
        {
            mIconDrawableTintList = null;
            mHasIconDrawableTint = false;
        }
        return (T) this;
    }

    /**
     * Sets the colour to use to tint the icon drawable.
     *
     * @param colour The colour to use to tint the icon drawable, call {@link
     *               #setIconDrawableTintList(ColorStateList)} or {@link
     *               #setIconDrawableTintMode(PorterDuff.Mode)} with {@code null} to remove the
     *               tint.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setIconDrawableColourFilter(@ColorInt final int colour)
    {
        mIconDrawableColourFilter = colour;
        mIconDrawableTintList = null;
        mHasIconDrawableTint = true;
        return (T) this;
    }

    /**
     * Set the listener to listen for when the prompt state changes.
     *
     * @param listener The listener to use
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPromptStateChangeListener(final MaterialTapTargetPrompt.PromptStateChangeListener listener)
    {
        mPromptStateChangeListener = listener;
        return (T) this;
    }

    /**
     * Handles emitting the prompt state changed events.
     *
     * @param state The state that the prompt is now in.
     */
    public void onPromptStateChanged(final MaterialTapTargetPrompt prompt, final int state)
    {
        if (mPromptStateChangeListener != null)
        {
            mPromptStateChangeListener.onPromptStateChanged(prompt, state);
        }
    }

    /**
     * Set if the prompt should stop touch events on the focal point from passing to underlying
     * views. Default is false.
     *
     * @param captureTouchEvent True to capture touch events in the prompt
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setCaptureTouchEventOnFocal(final boolean captureTouchEvent)
    {
        mCaptureTouchEventOnFocal = captureTouchEvent;
        return (T) this;
    }

    public boolean getCaptureTouchEventOnFocal()
    {
        return mCaptureTouchEventOnFocal;
    }

    /**
     * Set the max width that the primary and secondary text can be.
     *
     * @param width The max width that the text can reach
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setMaxTextWidth(final float width)
    {
        mMaxTextWidth = width;
        return (T) this;
    }

    /**
     * Set the max width that the primary and secondary text can be using the given resource
     * id.
     *
     * @param resId The dimension resource id for the max width that the text can reach
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setMaxTextWidth(@DimenRes final int resId)
    {
        mMaxTextWidth = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    public float getMaxTextWidth()
    {
        return mMaxTextWidth;
    }

    /**
     * Set the background colour.
     * The Material Design Guidelines specify that this should be 244 or hex F4.
     *
     * @param colour The background colour colour resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setBackgroundColour(@ColorInt final int colour)
    {
        mBackgroundColour = colour;
        return (T) this;
    }

    /**
     * Set the focal point colour.
     *
     * @param colour The focal colour colour resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setFocalColour(@ColorInt final int colour)
    {
        mFocalColour = colour;
        return (T) this;
    }

    /**
     * Set the focal point radius.
     *
     * @param radius The focal radius
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setFocalRadius(final float radius)
    {
        mFocalRadius = radius;
        return (T) this;
    }

    /**
     * Set the focal point radius using the given resource id.
     *
     * @param resId The focal radius dimension resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setFocalRadius(@DimenRes final int resId)
    {
        mFocalRadius = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set whether the prompt should dismiss itself when a touch event occurs outside the focal.
     * Default is true.
     *
     * Listen for the {@link MaterialTapTargetPrompt#STATE_NON_FOCAL_PRESSED} event in the
     * {@link #setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener)} to handle the prompt
     * being pressed outside the focal area.
     *
     * @param autoDismiss True - prompt will dismiss when touched outside the focal, false - no
     *                    action taken.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setAutoDismiss(final boolean autoDismiss)
    {
        mAutoDismiss = autoDismiss;
        return (T) this;
    }

    public boolean getAutoDismiss()
    {
        return mAutoDismiss;
    }

    /**
     * Set whether the prompt should finish itself when a touch event occurs inside the focal.
     * Default is true.
     *
     * Listen for the {@link MaterialTapTargetPrompt#STATE_FOCAL_PRESSED} event in the
     * {@link #setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener)} to handle the prompt
     * target being pressed.
     *
     * @param autoFinish True - prompt will finish when touched inside the focal, false - no
     *                   action taken.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setAutoFinish(final boolean autoFinish)
    {
        mAutoFinish = autoFinish;
        return (T) this;
    }

    public boolean getAutoFinish()
    {
        return mAutoFinish;
    }

    /**
     * Set if the prompt should stop touch events outside the prompt from passing to underlying
     * views. Default is false.
     *
     * @param captureTouchEventOutsidePrompt True to capture touch events out side the prompt
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setCaptureTouchEventOutsidePrompt(
            final boolean captureTouchEventOutsidePrompt)
    {
        mCaptureTouchEventOutsidePrompt = captureTouchEventOutsidePrompt;
        return (T) this;
    }

    public boolean getCaptureTouchEventOutsidePrompt()
    {
        return mCaptureTouchEventOutsidePrompt;
    }

    /**
     * Set the primary and secondary text horizontal layout gravity.
     * Default: {@link Gravity#START}
     *
     * @param gravity The horizontal gravity
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setTextGravity(final int gravity)
    {
        mPrimaryTextGravity = gravity;
        mSecondaryTextGravity = gravity;
        return (T) this;
    }

    /**
     * Set the primary text horizontal layout gravity.
     * Default: {@link Gravity#START}
     *
     * @param gravity The horizontal gravity
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPrimaryTextGravity(final int gravity)
    {
        mPrimaryTextGravity = gravity;
        return (T) this;
    }

    public int getPrimaryTextGravity()
    {
        return mPrimaryTextGravity;
    }

    /**
     * Set the secondary text horizontal layout gravity.
     * Default: {@link Gravity#START}
     *
     * @param gravity The horizontal gravity
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setSecondaryTextGravity(final int gravity)
    {
        mSecondaryTextGravity = gravity;
        return (T) this;
    }

    public int getSecondaryTextGravity()
    {
        return mSecondaryTextGravity;
    }

    /**
     * Set the view to clip the prompt to.
     * Default: {@link android.R.id#content}
     * <p>
     * Null can be used to stop the prompt being clipped to a view.
     *
     * @param view The view to clip to
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setClipToView(final View view)
    {
        mClipToView = view;
        return (T) this;
    }

    public View getClipToView()
    {
        return mClipToView;
    }

    /**
     * Back button can be used to dismiss the prompt.
     * Default: {@link true}
     *
     * @param enabled True for back button dismiss enabled
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setBackButtonDismissEnabled(final boolean enabled)
    {
        mBackButtonDismissEnabled = enabled;
        return (T) this;
    }

    public boolean getBackButtonDismissEnabled()
    {
        return  mBackButtonDismissEnabled;
    }

    /**
     * Sets the renderer for the prompt background.
     *
     * @param promptBackground The background shape to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPromptBackground(@NonNull final PromptBackground promptBackground)
    {
        mPromptBackground = promptBackground;
        return (T) this;
    }

    /**
     * Get the prompt focal renderer.
     *
     * @return The prompt focal instance.
     */
    public PromptBackground getPromptBackground()
    {
        return mPromptBackground;
    }

    /**
     * Sets the renderer for the prompt focal.
     *
     * @param promptFocal The focal shape to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public T setPromptFocal(@NonNull final PromptFocal promptFocal)
    {
        mPromptFocal = promptFocal;
        return (T) this;
    }

    /**
     * Get the prompt focal renderer.
     *
     * @return The prompt focal instance.
     */
    public PromptFocal getPromptFocal()
    {
        return mPromptFocal;
    }

    public T setPromptText(@NonNull final PromptText promptText)
    {
        mPromptText = promptText;
        return (T) this;
    }

    public PromptText getPromptText()
    {
        return mPromptText;
    }

    /**
     * Creates an {@link MaterialTapTargetPrompt} with the arguments supplied to this
     * builder.
     * <p>
     * Calling this method does not display the prompt. If no additional
     * processing is needed, {@link #show()} may be called instead to both
     * create and display the prompt.
     * </p>
     * <p>
     * Will return {@link null} if a valid target has not been set or the primary text is {@link
     * null}.
     * To check that a valid target has been set call {@link #isTargetSet()}.
     * </p>
     *
     * @return The created builder or null if no target
     */
    public MaterialTapTargetPrompt create()
    {
        if (!mTargetSet || (mPrimaryText == null && mSecondaryText == null))
        {
            return null;
        }
        final MaterialTapTargetPrompt mPrompt = MaterialTapTargetPrompt.createDefault(this);

        if (mAnimationInterpolator != null)
        {
            mAnimationInterpolator = new AccelerateDecelerateInterpolator();
        }

        if (mIconDrawable != null)
        {
            mIconDrawable.mutate();
            mIconDrawable.setBounds(0, 0, mIconDrawable.getIntrinsicWidth(), mIconDrawable.getIntrinsicHeight());
            if (mHasIconDrawableTint)
            {
                if (mIconDrawableTintList != null)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        mIconDrawable.setTintList(mIconDrawableTintList);
                    }
                }
                else
                {
                    mIconDrawable.setColorFilter(mIconDrawableColourFilter, mIconDrawableTintMode);
                    mIconDrawable.setAlpha(Color.alpha(mIconDrawableColourFilter));
                }
            }
        }

        mPromptBackground.setColour(mBackgroundColour);

        mPromptFocal.setColour(mFocalColour);
        mPromptFocal.setRippleAlpha(150);
        mPromptFocal.setDrawRipple(mIdleAnimationEnabled);
        mPromptFocal.setPadding(mFocalPadding);
        if (mPromptFocal instanceof CirclePromptFocal)
        {
            ((CirclePromptFocal) mPromptFocal).setRadius(mFocalRadius);
        }

        return mPrompt;
    }

    /**
     * Creates an {@link MaterialTapTargetPrompt} with the arguments supplied to this
     * builder and immediately displays the prompt.
     * <p>
     * Calling this method is functionally identical to:
     * </p>
     * <pre>
     *     MaterialTapTargetPrompt prompt = builder.create();
     *     prompt.show();
     * </pre>
     * <p>
     * Will return {@link null} if a valid target has not been set or the primary text is {@link
     * null}.
     * To check that a valid target has been set call {@link #isTargetSet()}.
     * </p>
     *
     * @return The created builder or null if no target
     */
    public MaterialTapTargetPrompt show()
    {
        final MaterialTapTargetPrompt mPrompt = create();
        if (mPrompt != null)
        {
            mPrompt.show();
        }
        return mPrompt;
    }
}
