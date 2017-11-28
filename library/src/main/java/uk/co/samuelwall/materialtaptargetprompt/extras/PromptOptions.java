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
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.graphics.drawable.DrawableCompat;
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

/**
 * Contains all the settings for creating a prompt.
 *
 * @param <T> The subclass that extends this.
 */
@SuppressWarnings("unchecked")
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
    private @Nullable View mTargetView;

    /**
     * The left and top positioning for the focal centre point.
     */
    private PointF mTargetPosition;

    /**
     * The primary text to display.
     */
    private @Nullable CharSequence mPrimaryText;

    /**
     * The secondary text to display.
     */
    private @Nullable CharSequence mSecondaryText;

    /**
     * The colour for the primary text.
     */
    private @ColorInt int mPrimaryTextColour = Color.WHITE;

    /**
     * The colour for the secondary text.
     */
    private @ColorInt int mSecondaryTextColour = Color.argb(179, 255, 255, 255);

    /**
     * The colour for the prompt background.
     */
    private @ColorInt int mBackgroundColour = Color.argb(244, 63, 81, 181);

    /**
     * The colour for the prompt focal.
     */
    private @ColorInt int mFocalColour = Color.WHITE;

    /**
     * The circle focal implementation radius.
     */
    private float mFocalRadius;

    /**
     * The primary text font size.
     */
    private float mPrimaryTextSize;

    /**
     * The secondary text font size.
     */
    private float mSecondaryTextSize;

    /**
     * The maximum width to allow the text to be.
     */
    private float mMaxTextWidth;

    /**
     * The distance between the text and the background edge.
     */
    private float mTextPadding;

    /**
     * The distance between the focal edge and the text.
     */
    private float mFocalPadding;

    /**
     * The interpolator to use for animations.
     */
    private Interpolator mAnimationInterpolator;

    /**
     * The drawable to use to render the target.
     */
    private @Nullable Drawable mIconDrawable;

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
    private boolean mAutoDismiss = true;
    private boolean mAutoFinish = true;
    private boolean mCaptureTouchEventOutsidePrompt;
    private Typeface mPrimaryTextTypeface, mSecondaryTextTypeface;
    private int mPrimaryTextTypefaceStyle, mSecondaryTextTypefaceStyle;
    private ColorStateList mIconDrawableTintList = null;
    private PorterDuff.Mode mIconDrawableTintMode = PorterDuff.Mode.MULTIPLY;
    private boolean mHasIconDrawableTint;
    private int mIconDrawableColourFilter;
    private View mTargetRenderView;
    private boolean mIdleAnimationEnabled = true;
    private int mPrimaryTextGravity = Gravity.START, mSecondaryTextGravity = Gravity.START;
    private View mClipToView;

    /**
     * The shape to render for the prompt background.
     */
    private PromptBackground mPromptBackground = new CirclePromptBackground();

    /**
     * The shape to render for the prompt focal.
     */
    private PromptFocal mPromptFocal = new CirclePromptFocal();

    /**
     * The renderer for drawing the prompt text.
     */
    private PromptText mPromptText = new PromptText();

    /**
     * Constructor.
     *
     * @param resourceFinder The resource finder implementation to use to find resources.
     */
    public PromptOptions(@NonNull final ResourceFinder resourceFinder)
    {
        mResourceFinder = resourceFinder;
        final float density = mResourceFinder.getResources().getDisplayMetrics().density;
        mFocalRadius =       density * 44;
        mPrimaryTextSize =   density * 22;
        mSecondaryTextSize = density * 18;
        mMaxTextWidth =      density * 400;
        mTextPadding =       density * 40;
        mFocalPadding =      density * 20;
        mTextSeparation =    density * 16;
    }

    /**
     * Loads the supplied theme into the prompt overwriting any previously set values if they are set in the theme.
     *
     * @param themeResId The resource id for the theme.
     */
    public void load(@StyleRes int themeResId)
    {
        //Attempt to load the theme from the activity theme
        if (themeResId == 0)
        {
            final TypedValue outValue = new TypedValue();
            mResourceFinder.getTheme().resolveAttribute(R.attr.MaterialTapTargetPromptTheme, outValue, true);
            themeResId = outValue.resourceId;
        }

        final TypedArray a = mResourceFinder.obtainStyledAttributes(themeResId, R.styleable.PromptView);
        mPrimaryTextColour = a.getColor(R.styleable.PromptView_mttp_primaryTextColour, mPrimaryTextColour);
        mSecondaryTextColour = a.getColor(R.styleable.PromptView_mttp_secondaryTextColour, mSecondaryTextColour);
        mPrimaryText = a.getString(R.styleable.PromptView_mttp_primaryText);
        mSecondaryText = a.getString(R.styleable.PromptView_mttp_secondaryText);
        mBackgroundColour = a.getColor(R.styleable.PromptView_mttp_backgroundColour, mBackgroundColour);
        mFocalColour = a.getColor(R.styleable.PromptView_mttp_focalColour, mFocalColour);
        mFocalRadius = a.getDimension(R.styleable.PromptView_mttp_focalRadius, mFocalRadius);
        mPrimaryTextSize = a.getDimension(R.styleable.PromptView_mttp_primaryTextSize, mPrimaryTextSize);
        mSecondaryTextSize = a.getDimension(R.styleable.PromptView_mttp_secondaryTextSize, mSecondaryTextSize);
        mMaxTextWidth = a.getDimension(R.styleable.PromptView_mttp_maxTextWidth, mMaxTextWidth);
        mTextPadding = a.getDimension(R.styleable.PromptView_mttp_textPadding, mTextPadding);
        mFocalPadding = a.getDimension(R.styleable.PromptView_mttp_focalToTextPadding, mFocalPadding);
        mTextSeparation = a.getDimension(R.styleable.PromptView_mttp_textSeparation, mTextSeparation);
        mAutoDismiss = a.getBoolean(R.styleable.PromptView_mttp_autoDismiss, mAutoDismiss);
        mAutoFinish = a.getBoolean(R.styleable.PromptView_mttp_autoFinish, mAutoFinish);
        mCaptureTouchEventOutsidePrompt = a.getBoolean(R.styleable.PromptView_mttp_captureTouchEventOutsidePrompt, mCaptureTouchEventOutsidePrompt);
        mCaptureTouchEventOnFocal = a.getBoolean(R.styleable.PromptView_mttp_captureTouchEventOnFocal, mCaptureTouchEventOnFocal);
        mPrimaryTextTypefaceStyle = a.getInt(R.styleable.PromptView_mttp_primaryTextStyle, mPrimaryTextTypefaceStyle);
        mSecondaryTextTypefaceStyle = a.getInt(R.styleable.PromptView_mttp_secondaryTextStyle, mSecondaryTextTypefaceStyle);
        mPrimaryTextTypeface = PromptUtils.setTypefaceFromAttrs(a.getString(R.styleable.PromptView_mttp_primaryTextFontFamily), a.getInt(R.styleable.PromptView_mttp_primaryTextTypeface, 0), mPrimaryTextTypefaceStyle);
        mSecondaryTextTypeface = PromptUtils.setTypefaceFromAttrs(a.getString(R.styleable.PromptView_mttp_secondaryTextFontFamily), a.getInt(R.styleable.PromptView_mttp_secondaryTextTypeface, 0), mSecondaryTextTypefaceStyle);

        mIconDrawableColourFilter = a.getColor(R.styleable.PromptView_mttp_iconColourFilter, mBackgroundColour);
        mIconDrawableTintList = a.getColorStateList(R.styleable.PromptView_mttp_iconTint);
        mIconDrawableTintMode = PromptUtils.parseTintMode(a.getInt(R.styleable.PromptView_mttp_iconTintMode, -1), mIconDrawableTintMode);
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

    /**
     * Get the resource finder being used.
     *
     * @return The resource finder being used.
     */
    @NonNull
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
    @NonNull
    public T setTarget(@Nullable final View target)
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
    @NonNull
    public T setTarget(@IdRes final int target)
    {
        mTargetView = mResourceFinder.findViewById(target);
        mTargetPosition = null;
        mTargetSet = mTargetView != null;
        return (T) this;
    }

    /**
     * Gets the view that the prompt is targeting.
     *
     * @return The target view or null if not set or targeting a position.
     */
    @Nullable
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
    @NonNull
    public T setTarget(final float left, final float top)
    {
        mTargetView = null;
        mTargetPosition = new PointF(left, top);
        mTargetSet = true;
        return (T) this;
    }

    /**
     * Get the position on the screen that is being targeted.
     *
     * @return The target position or null if targeting a view.
     */
    @Nullable
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
    @NonNull
    public T setTargetRenderView(@Nullable final View view)
    {
        mTargetRenderView = view;
        return (T) this;
    }

    /**
     * Get the view that is rendered as the target.
     *
     * @return The view used to render the prompt target.
     */
    @Nullable
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
    @NonNull
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
    @NonNull
    public T setPrimaryText(@Nullable final String text)
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
    @NonNull
    public T setPrimaryText(@Nullable final CharSequence text)
    {
        mPrimaryText = text;
        return (T) this;
    }

    /**
     * Get the text to draw for the primary text.
     *
     * @return The primary text.
     */
    @Nullable
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
    @NonNull
    public T setPrimaryTextSize(@Dimension final float size)
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
    @NonNull
    public T setPrimaryTextSize(@DimenRes final int resId)
    {
        mPrimaryTextSize = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Get the primary text font size.
     *
     * @return The primary text font size.
     */
    @Dimension
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
    @NonNull
    public T setPrimaryTextColour(@ColorInt final int colour)
    {
        mPrimaryTextColour = colour;
        return (T) this;
    }

    /**
     * Gets the primary text font colour.
     *
     * @return The primary text font colour.
     */
    @ColorInt
    public int getPrimaryTextColour()
    {
        return mPrimaryTextColour;
    }

    /**
     * Sets the typeface and style used to display the primary text.
     *
     * @param typeface The primary text typeface
     */
    @NonNull
    public T setPrimaryTextTypeface(@Nullable final Typeface typeface)
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
    @NonNull
    public T setPrimaryTextTypeface(@Nullable final Typeface typeface, final int style)
    {
        mPrimaryTextTypeface = typeface;
        mPrimaryTextTypefaceStyle = style;
        return (T) this;
    }

    /**
     * Get the typeface for the primary text.
     *
     * @return The primary text typeface.
     */
    @Nullable
    public Typeface getPrimaryTextTypeface()
    {
        return mPrimaryTextTypeface;
    }

    /**
     * Get the primary text typeface style.
     *
     * @return the primary text typeface style.
     */
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
    @NonNull
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
    @NonNull
    public T setSecondaryText(@Nullable final String text)
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
    @NonNull
    public T setSecondaryText(@Nullable final CharSequence text)
    {
        mSecondaryText = text;
        return (T) this;
    }

    /**
     * Get the secondary text.
     *
     * @return The secondary text.
     */
    @Nullable
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
    @NonNull
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
    @NonNull
    public T setSecondaryTextSize(@Dimension final float size)
    {
        mSecondaryTextSize = size;
        return (T) this;
    }

    /**
     * Get the secondary text size.
     *
     * @return The secondary text size.
     */
    @Dimension
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
    @NonNull
    public T setSecondaryTextColour(@ColorInt final int colour)
    {
        mSecondaryTextColour = colour;
        return (T) this;
    }

    /**
     * Get the secondary text colour.
     *
     * @return The secondary text colour.
     */
    public int getSecondaryTextColour()
    {
        return mSecondaryTextColour;
    }

    /**
     * Sets the typeface used to display the secondary text.
     *
     * @param typeface The secondary text typeface
     */
    @NonNull
    public T setSecondaryTextTypeface(@Nullable final Typeface typeface)
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
    @NonNull
    public T setSecondaryTextTypeface(@Nullable final Typeface typeface, final int style)
    {
        mSecondaryTextTypeface = typeface;
        mSecondaryTextTypefaceStyle = style;
        return (T) this;
    }

    /**
     * Get the secondary text typeface.
     *
     * @return The secondary text typeface.
     */
    @Nullable
    public Typeface getSecondaryTextTypeface()
    {
        return mSecondaryTextTypeface;
    }

    /**
     * Get the secondary text typeface style.
     *
     * @return The secondary text typeface style.
     */
    public int getSecondaryTextTypefaceStyle()
    {
        return mSecondaryTextTypefaceStyle;
    }

    /**
     * Set the text left and right padding using the given resource id.
     *
     * @param resId The text padding dimension resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setTextPadding(@DimenRes final int resId)
    {
        mTextPadding = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set the text left and right padding.
     *
     * @param padding The padding on the text left and right
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setTextPadding(@Dimension final float padding)
    {
        mTextPadding = padding;
        return (T) this;
    }

    /**
     * Get the text left and right padding.
     *
     * @return The text left and right padding.
     */
    @Dimension
    public float getTextPadding()
    {
        return mTextPadding;
    }

    /**
     * Set the distance between the primary and secondary text using the given resource id.
     *
     * @param resId The dimension resource id for the text separation
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setTextSeparation(@DimenRes final int resId)
    {
        mTextSeparation = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set the distance between the primary and secondary text.
     *
     * @param separation The distance separation between the primary and secondary text
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setTextSeparation(@Dimension final float separation)
    {
        mTextSeparation = separation;
        return (T) this;
    }

    /**
     * Get the distance between the primary and secondary text.
     *
     * @return the distance between the primary and secondary text.
     */
    @Dimension
    public float getTextSeparation()
    {
        return mTextSeparation;
    }

    /**
     * Set the padding between the text and the focal point using the given resource id.
     *
     * @param resId The dimension resource id for the focal to text distance
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setFocalPadding(@DimenRes final int resId)
    {
        mFocalPadding = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set the padding between the text and the focal point.
     *
     * @param padding The distance between the text and focal
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setFocalPadding(@Dimension final float padding)
    {
        mFocalPadding = padding;
        return (T) this;
    }

    /**
     * Get the padding between the text and the focal.
     *
     * @return The padding between the text and the focal.
     */
    @Dimension
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
    @NonNull
    public T setAnimationInterpolator(@Nullable final Interpolator interpolator)
    {
        mAnimationInterpolator = interpolator;
        return (T) this;
    }

    /**
     * Get the animation interpolator that is used.
     *
     * @return The animation interpolator that is used.
     */
    @Nullable
    public Interpolator getAnimationInterpolator()
    {
        return mAnimationInterpolator;
    }

    /**
     * Enable/disable focal animation.
     * true by default
     *
     * @param enabled Idle animation enabled
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setIdleAnimationEnabled(final boolean enabled)
    {
        mIdleAnimationEnabled = enabled;
        return (T) this;
    }

    /**
     * Is the focal animation enabled.
     *
     * @return True if the idle animation is enabled.
     */
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
    @NonNull
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
    @NonNull
    public T setIconDrawable(@Nullable final Drawable drawable)
    {
        mIconDrawable = drawable;
        return (T) this;
    }

    /**
     * Get the icon drawn as the target.
     *
     * @return The icon drawn as the target.
     */
    @Nullable
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
    @NonNull
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
    @NonNull
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
    @NonNull
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
    @NonNull
    public T setPromptStateChangeListener(
            @Nullable final MaterialTapTargetPrompt.PromptStateChangeListener listener)
    {
        mPromptStateChangeListener = listener;
        return (T) this;
    }

    /**
     * Handles emitting the prompt state changed events.
     *
     * @param state The state that the prompt is now in.
     */
    public void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state)
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
    @NonNull
    public T setCaptureTouchEventOnFocal(final boolean captureTouchEvent)
    {
        mCaptureTouchEventOnFocal = captureTouchEvent;
        return (T) this;
    }

    /**
     * Get if the prompt should stop touch events on the focal point from passing to underlying
     * views.
     *
     * @return True to capture touch events in the prompt
     */
    public boolean getCaptureTouchEventOnFocal()
    {
        return mCaptureTouchEventOnFocal;
    }

    /**
     * Set the max width that the primary and secondary text can be using the given resource
     * id.
     *
     * @param resId The dimension resource id for the max width that the text can reach
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setMaxTextWidth(@DimenRes final int resId)
    {
        mMaxTextWidth = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set the max width that the primary and secondary text can be.
     *
     * @param width The max width that the text can reach
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setMaxTextWidth(@Dimension final float width)
    {
        mMaxTextWidth = width;
        return (T) this;
    }

    /**
     * Get the maximum width that the primary and secondary text can be.
     *
     * @return The maximum text width.
     */
    @Dimension
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
    @NonNull
    public T setBackgroundColour(@ColorInt final int colour)
    {
        mBackgroundColour = colour;
        return (T) this;
    }

    /**
     * Get the background colour.
     *
     * @return The background colour.
     */
    @ColorInt
    public int getBackgroundColour()
    {
        return mBackgroundColour;
    }

    /**
     * Set the focal point colour.
     *
     * @param colour The focal colour colour resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setFocalColour(@ColorInt final int colour)
    {
        mFocalColour = colour;
        return (T) this;
    }

    /**
     * Get the focal point colour.
     *
     * @return The focal point colour.
     */
    @ColorInt
    public int getFocalColour()
    {
        return mFocalColour;
    }

    /**
     * Set the focal point radius using the given resource id.
     *
     * @param resId The focal radius dimension resource id
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setFocalRadius(@DimenRes final int resId)
    {
        mFocalRadius = mResourceFinder.getResources().getDimension(resId);
        return (T) this;
    }

    /**
     * Set the focal point radius.
     *
     * @param radius The focal radius
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setFocalRadius(@Dimension final float radius)
    {
        mFocalRadius = radius;
        return (T) this;
    }

    /**
     * Get the focal point radius for the circle prompt focal.
     *
     * @return The radius used for the circle prompt focal.
     */
    @Dimension
    public float getFocalRadius()
    {
        return mFocalRadius;
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
    @NonNull
    public T setAutoDismiss(final boolean autoDismiss)
    {
        mAutoDismiss = autoDismiss;
        return (T) this;
    }

    /**
     * Get whether the prompt should dismiss itself when a touch event occurs outside the focal.
     *
     * @return True - prompt will dismiss when touched outside the focal, false - no
     *                    action taken.
     */
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
    @NonNull
    public T setAutoFinish(final boolean autoFinish)
    {
        mAutoFinish = autoFinish;
        return (T) this;
    }

    /**
     * Get if the prompt should finish itself when a touch event occurs inside the focal.
     *
     * @return True if the prompt should finish itself when a touch event occurs inside the focal.
     */
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
    @NonNull
    public T setCaptureTouchEventOutsidePrompt(
            final boolean captureTouchEventOutsidePrompt)
    {
        mCaptureTouchEventOutsidePrompt = captureTouchEventOutsidePrompt;
        return (T) this;
    }

    /**
     * Get if the prompt should stop touch events outside the prompt from passing to underlying views.
     *
     * @return True if touch events will not be passed to views below the prompt.
     */
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
    @NonNull
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
    @NonNull
    public T setPrimaryTextGravity(final int gravity)
    {
        mPrimaryTextGravity = gravity;
        return (T) this;
    }

    /**
     * Gets the gravity for the primary text.
     *
     * @return The primary texts gravity.
     */
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
    @NonNull
    public T setSecondaryTextGravity(final int gravity)
    {
        mSecondaryTextGravity = gravity;
        return (T) this;
    }

    /**
     * Gets the gravity for the secondary text.
     *
     * @return The secondary texts gravity.
     */
    public int getSecondaryTextGravity()
    {
        return mSecondaryTextGravity;
    }

    /**
     * Set the view to clip the prompt to.
     * The prompt won't draw outside the bounds of this view.
     * Default: {@link android.R.id#content}
     * <p>
     * Null can be used to stop the prompt being clipped to a view.
     *
     * @param view The view to clip to
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setClipToView(@Nullable final View view)
    {
        mClipToView = view;
        return (T) this;
    }

    /**
     * Get the view that the prompt canvas is clipped to.
     * The prompt won't draw outside the bounds of this view.
     *
     * @return The view that the prompt canvas is clipped to.
     */
    @Nullable
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
    @NonNull
    public T setBackButtonDismissEnabled(final boolean enabled)
    {
        mBackButtonDismissEnabled = enabled;
        return (T) this;
    }

    /**
     * Will the pressing the system back button dismiss the prompt.
     *
     * @return True if pressing the system back button will dismiss the prompt, false otherwise.
     */
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
    @NonNull
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
    @NonNull
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
    @NonNull
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
    @NonNull
    public PromptFocal getPromptFocal()
    {
        return mPromptFocal;
    }

    /**
     * Set the {@link PromptText} implementation to use to render the prompt text.
     *
     * @param promptText The prompt text implementation.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public T setPromptText(@NonNull final PromptText promptText)
    {
        mPromptText = promptText;
        return (T) this;
    }

    /**
     * Get the {@link PromptText} implementation used to render the prompt text.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
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
    @Nullable
    public MaterialTapTargetPrompt create()
    {
        if (!mTargetSet || (mPrimaryText == null && mSecondaryText == null))
        {
            return null;
        }
        final MaterialTapTargetPrompt mPrompt = MaterialTapTargetPrompt.createDefault(this);

        if (mAnimationInterpolator == null)
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
                    DrawableCompat.setTintList(mIconDrawable, mIconDrawableTintList);
                }
                else
                {
                    mIconDrawable.setColorFilter(mIconDrawableColourFilter, mIconDrawableTintMode);
                    mIconDrawable.setAlpha(Color.alpha(mIconDrawableColourFilter));
                }
            }
        }

        mPromptBackground.setColour(getBackgroundColour());

        mPromptFocal.setColour(getFocalColour());
        mPromptFocal.setRippleAlpha(150);
        mPromptFocal.setDrawRipple(getIdleAnimationEnabled());
        if (mPromptFocal instanceof CirclePromptFocal)
        {
            ((CirclePromptFocal) mPromptFocal).setRadius(getFocalRadius());
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
    @Nullable
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
