/*
 * Copyright (C) 2016 Samuel Wall
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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * A Material Design tap target onboarding implementation.
 *
 * <div class="special reference">
 * <h3>Onboarding</h3>
 * <p>For more information about onboarding and tap targets, read the
 * <a href="https://www.google.com/design/spec/growth-communications/onboarding.html">Onboarding</a>
 * Material Design guidelines.</p>
 * </div>
 */
public class MaterialTapTargetPrompt
{
    private Activity mActivity;
    private PromptView mView;
    private View mTargetView;
    private float mBaseLeft, mBaseTop;
    private float mBaseFocalRadius, mBaseBackgroundRadius;
    private float mFocalRadius10Percent;
    private float mRevealedAmount;
    private String mPrimaryText, mSecondaryText;
    private float mMaxTextWidth;
    private float mTextPadding;
    private boolean mTextPositionRight, mTextPositionAbove;
    private float mFocalToTextPadding;
    private int mPrimaryTextColourAlpha, mSecondaryTextColourAlpha;
    private ValueAnimator mAnimationCurrent, mAnimationFocalRipple;
    private Interpolator mAnimationInterpolator;
    private float mFocalRippleProgress;
    private int mBaseFocalRippleAlpha;
    private TextPaint mPaintPrimaryText, mPaintSecondaryText;
    private OnHidePromptListener mOnHidePromptListener;
    private boolean mDismissing;
    private ViewGroup mParentView;
    private boolean mParentViewIsDecor;
    private ViewGroup mClipToView;
    private final float mStatusBarHeight;
    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private boolean mAutoDismiss, mAutoFinish;

    MaterialTapTargetPrompt(final Activity activity)
    {
        mActivity = activity;
        mView = new PromptView(activity);
        mView.mOnPromptTouchedListener = new PromptView.OnPromptTouchedListener()
            {
                @Override
                public void onPromptTouched(MotionEvent event, boolean tappedTarget)
                {
                    if (!mDismissing)
                    {
                        MaterialTapTargetPrompt.this.onHidePrompt(event, tappedTarget);
                        if (tappedTarget)
                        {
                            if (mAutoFinish)
                            {
                                finish();
                            }
                        }
                        else
                        {
                            if (mAutoDismiss)
                            {
                                dismiss();
                            }
                        }
                    }
                }
            };

        int resourceId = mView.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            mStatusBarHeight = mView.getResources().getDimensionPixelSize(resourceId);
        }
        else
        {
            mStatusBarHeight = 0;
        }

        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    updateFocalCentrePosition();
                }
            };
    }

    /**
     * Returns {@link #mParentView}.
     *
     * If the {@link #mParentView} is {@link null} it determines what view it should be.
     *
     * @return The view to add the prompt view to.
     */
    private ViewGroup getParentView()
    {
        if (mParentView == null)
        {
            final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            final ViewGroup contentView = (ViewGroup) ((ViewGroup) decorView.findViewById(android.R.id.content)).getChildAt(0);
            // If the content view is a drawer layout then that is the parent so
            // that the prompt can be added behind the navigation drawer
            if (contentView.getClass().getName().equals("android.support.v4.widget.DrawerLayout"))
            {
                mParentView = contentView;
                mParentViewIsDecor = false;
            }
            else
            {
                mParentView = decorView;
                mParentViewIsDecor = true;
            }
            mView.mClipBounds = mParentViewIsDecor;
        }

        return mParentView;
    }

    /**
     * Displays the prompt.
     */
    public void show()
    {
        final ViewGroup parent = getParentView();
        // If the content view is a drawer layout then that is the parent so
        // that the prompt can be added behind the navigation drawer
        if (parent.getClass().getName().equals("android.support.v4.widget.DrawerLayout"))
        {
            parent.addView(mView, 1);
        }
        else
        {
            parent.addView(mView);
        }

        addGlobalLayoutListener();

        updateFocalCentrePosition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            startRevealAnimation();
        }
        else
        {
            mView.mBackgroundRadius = mBaseBackgroundRadius;
            mView.mFocalRadius = mBaseFocalRadius;
            mView.mPaintFocal.setAlpha(255);
            mView.mPaintBackground.setAlpha(244);
            mPaintSecondaryText.setAlpha(mSecondaryTextColourAlpha);
            mPaintPrimaryText.setAlpha(mPrimaryTextColourAlpha);
        }
    }

    /**
     * Adds layout listener to view parent to capture layout changes.
     */
    private void addGlobalLayoutListener()
    {
        final ViewTreeObserver viewTreeObserver = getParentView().getViewTreeObserver();
        if (viewTreeObserver.isAlive())
        {
            viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    /**
     * Removes global layout listener added in {@link #addGlobalLayoutListener()}.
     */
    private void removeGlobalLayoutListener()
    {
        final ViewTreeObserver viewTreeObserver = getParentView().getViewTreeObserver();
        if (viewTreeObserver.isAlive())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                viewTreeObserver.removeOnGlobalLayoutListener(mGlobalLayoutListener);
            }
            else
            {
                //noinspection deprecation
                viewTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
            }
        }
    }

    /**
     * Removes the prompt from view, using a expand and fade animation.
     *
     * This is treated as if the user has touched the target focal point.
     */
    public void finish()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if (mDismissing)
            {
                return;
            }
            mDismissing = true;
            if (mAnimationCurrent != null)
            {
                mAnimationCurrent.removeAllListeners();
                mAnimationCurrent.cancel();
                mAnimationCurrent = null;
            }
            mAnimationCurrent = ValueAnimator.ofFloat(1f, 0f);
            mAnimationCurrent.setDuration(225);
            mAnimationCurrent.setInterpolator(mAnimationInterpolator);
            mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    final float value = (float) animation.getAnimatedValue();
                    mRevealedAmount = 1f + ((1f - value) / 4);
                    mView.mBackgroundRadius = mBaseBackgroundRadius * mRevealedAmount;
                    mView.mFocalRadius = mBaseFocalRadius * mRevealedAmount;
                    mView.mPaintFocal.setAlpha((int) (255 * value));
                    mView.mPaintBackground.setAlpha((int) (244 * value));
                    mPaintSecondaryText.setAlpha((int) (mSecondaryTextColourAlpha * value));
                    mPaintPrimaryText.setAlpha((int) (mPrimaryTextColourAlpha * value));
                    if (mView.mIconDrawable != null)
                    {
                        mView.mIconDrawable.setAlpha(mView.mPaintBackground.getAlpha());
                    }
                    mView.invalidate();
                }
            });
            mAnimationCurrent.addListener(new AnimatorListener()
            {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    removeGlobalLayoutListener();
                    getParentView().removeView(mView);
                    mAnimationCurrent.removeAllListeners();
                    mAnimationCurrent = null;
                    mDismissing = false;
                    onHidePromptComplete();
                    mParentView = null;
                }

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onAnimationCancel(Animator animation)
                {
                    removeGlobalLayoutListener();
                    getParentView().removeView(mView);
                    mAnimationCurrent.removeAllListeners();
                    mAnimationCurrent = null;
                    mDismissing = false;
                    onHidePromptComplete();
                    mParentView = null;
                }
            });
            mAnimationCurrent.start();
        }
        else
        {
            removeGlobalLayoutListener();
            getParentView().removeView(mView);
            onHidePromptComplete();
            mParentView = null;
        }
    }

    /**
     * Removes the prompt from view, using a contract and fade animation.
     *
     * This is treated as if the user has touched outside the target focal point.
     */
    public void dismiss()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if (mDismissing)
            {
                return;
            }
            mDismissing = true;
            if (mAnimationCurrent != null)
            {
                mAnimationCurrent.removeAllListeners();
                mAnimationCurrent.cancel();
                mAnimationCurrent = null;
            }
            mAnimationCurrent = ValueAnimator.ofFloat(1f, 0f);
            mAnimationCurrent.setDuration(225);
            mAnimationCurrent.setInterpolator(mAnimationInterpolator);
            mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    mRevealedAmount = (float) animation.getAnimatedValue();
                    mView.mBackgroundRadius = mBaseBackgroundRadius * mRevealedAmount;
                    mView.mFocalRadius = mBaseFocalRadius * mRevealedAmount;
                    mView.mPaintBackground.setAlpha((int) (244 * mRevealedAmount));
                    mPaintSecondaryText.setAlpha((int) (mSecondaryTextColourAlpha * mRevealedAmount));
                    mPaintPrimaryText.setAlpha((int) (mPrimaryTextColourAlpha * mRevealedAmount));
                    if (mView.mIconDrawable != null)
                    {
                        mView.mIconDrawable.setAlpha(mView.mPaintBackground.getAlpha());
                    }
                    mView.invalidate();
                }
            });
            mAnimationCurrent.addListener(new AnimatorListener()
            {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    removeGlobalLayoutListener();
                    getParentView().removeView(mView);
                    mAnimationCurrent.removeAllListeners();
                    mAnimationCurrent = null;
                    mDismissing = false;
                    onHidePromptComplete();
                    mParentView = null;
                }

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onAnimationCancel(Animator animation)
                {
                    removeGlobalLayoutListener();
                    getParentView().removeView(mView);
                    mAnimationCurrent.removeAllListeners();
                    mAnimationCurrent = null;
                    mDismissing = false;
                    onHidePromptComplete();
                    mParentView = null;
                }
            });
            mAnimationCurrent.start();
        }
        else
        {
            removeGlobalLayoutListener();
            getParentView().removeView(mView);
            onHidePromptComplete();
            mParentView = null;
        }
    }

    @TargetApi(11)
    private void startRevealAnimation()
    {
        mPaintSecondaryText.setAlpha(0);
        mPaintPrimaryText.setAlpha(0);
        mView.mPaintBackground.setAlpha(0);
        mView.mPaintFocal.setAlpha(0);
        mView.mFocalRadius = 0;
        mView.mBackgroundRadius = 0;
        if (mView.mIconDrawable != null)
        {
            mView.mIconDrawable.setAlpha(0);
        }
        mRevealedAmount = 0f;
        mAnimationCurrent = ValueAnimator.ofFloat(0f, 1f);
        mAnimationCurrent.setInterpolator(mAnimationInterpolator);
        mAnimationCurrent.setDuration(225);
        mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mRevealedAmount = (float) animation.getAnimatedValue();
                mView.mBackgroundRadius = mBaseBackgroundRadius * mRevealedAmount;
                mView.mFocalRadius = mBaseFocalRadius * mRevealedAmount;
                mView.mPaintFocal.setAlpha((int) (255 * mRevealedAmount));
                mView.mPaintBackground.setAlpha((int) (244 * mRevealedAmount));
                mPaintSecondaryText.setAlpha((int) (mSecondaryTextColourAlpha * mRevealedAmount));
                mPaintPrimaryText.setAlpha((int) (mPrimaryTextColourAlpha * mRevealedAmount));
                if (mView.mIconDrawable != null)
                {
                    mView.mIconDrawable.setAlpha(mView.mPaintBackground.getAlpha());
                }
                mView.invalidate();
            }
        });
        mAnimationCurrent.addListener(new AnimatorListener()
        {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationEnd(Animator animation)
            {
                animation.removeAllListeners();
                mAnimationCurrent = null;
                mRevealedAmount = 1;
                startIdleAnimations();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationCancel(Animator animation)
            {
                animation.removeAllListeners();
                mRevealedAmount = 1;
                mAnimationCurrent = null;
            }
        });
        mAnimationCurrent.start();
    }

    @TargetApi(11)
    private void startIdleAnimations()
    {
        if (mAnimationCurrent != null)
        {
            mAnimationCurrent.removeAllUpdateListeners();
            mAnimationCurrent.cancel();
            mAnimationCurrent = null;
        }
        mAnimationCurrent = ValueAnimator.ofFloat(0, mFocalRadius10Percent, 0);
        mAnimationCurrent.setInterpolator(mAnimationInterpolator);
        mAnimationCurrent.setDuration(1000);
        mAnimationCurrent.setStartDelay(225);
        mAnimationCurrent.setRepeatCount(ValueAnimator.INFINITE);
        mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            boolean direction = true;
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                final float newFocalFraction = (Float) animation.getAnimatedValue();
                boolean newDirection = direction;
                if (newFocalFraction < mFocalRippleProgress && direction)
                {
                    newDirection = false;
                }
                else if (newFocalFraction > mFocalRippleProgress && !direction)
                {
                    newDirection = true;
                }
                if (newDirection != direction && !newDirection)
                {
                    mAnimationFocalRipple.start();
                }
                direction = newDirection;
                mFocalRippleProgress = newFocalFraction;
                mView.mFocalRadius = mBaseFocalRadius + mFocalRippleProgress;
                mView.invalidate();
            }
        });
        mAnimationCurrent.start();
        if (mAnimationFocalRipple != null)
        {
            mAnimationFocalRipple.removeAllUpdateListeners();
            mAnimationFocalRipple.cancel();
            mAnimationFocalRipple = null;
        }
        final float baseRadius = mBaseFocalRadius + mFocalRadius10Percent;
        mAnimationFocalRipple = ValueAnimator.ofFloat(baseRadius, baseRadius + (mFocalRadius10Percent * 6));
        mAnimationFocalRipple.setInterpolator(mAnimationInterpolator);
        mAnimationFocalRipple.setDuration(500);
        mAnimationFocalRipple.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mView.mFocalRippleSize = (float) animation.getAnimatedValue();
                final float fraction;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
                {
                    fraction = animation.getAnimatedFraction();
                }
                else
                {
                    fraction = (mFocalRadius10Percent * 6) / (mView.mFocalRippleSize - mBaseFocalRadius - mFocalRadius10Percent);
                }
                mView.mFocalRippleAlpha = (int) (mBaseFocalRippleAlpha * (1f - fraction));
            }
        });
    }

    private void updateFocalCentrePosition()
    {
        updateClipBounds();
        if (mTargetView != null)
        {
            final int[] viewPosition = new int[2];
            mView.getLocationInWindow(viewPosition);
            final int[] targetPosition = new int[2];
            mTargetView.getLocationInWindow(targetPosition);

            mView.mCentreLeft = mBaseLeft + targetPosition[0] - viewPosition[0] + (mTargetView.getWidth() / 2);
            mView.mCentreTop = mBaseTop + targetPosition[1] - viewPosition[1] + (mTargetView.getHeight() / 2);
        }
        else
        {
            mView.mCentreLeft = mBaseLeft;
            mView.mCentreTop = mBaseTop;
        }

        final ViewGroup parent = getParentView();
        mTextPositionAbove = mView.mCentreTop > parent.getHeight() / 2;
        mTextPositionRight = mView.mCentreLeft > parent.getWidth() / 2;

        updateTextPositioning();
    }

    private void updateTextPositioning()
    {
        final float primaryTextWidth = mPaintPrimaryText.measureText(mPrimaryText);
        final float secondaryTextWidth = mSecondaryText != null ? mPaintSecondaryText.measureText(mSecondaryText) : 0;
        final float textWidth;
        final float maxWidth = Math.max(80, (mView.mClipBounds ? mView.mClipBoundsRight - mView.mClipBoundsLeft : getParentView().getWidth()) - (mTextPadding * 2));
        final float textWidthCalculation = Math.min(mMaxTextWidth, Math.max(primaryTextWidth, secondaryTextWidth));
        if (textWidthCalculation > maxWidth)
        {
            mView.mTextLeft = (mView.mClipBounds ? mView.mClipBoundsLeft : 0) + mTextPadding;
            textWidth = maxWidth;
        }
        else
        {
            if (mTextPositionRight)
            {
                mView.mTextLeft = (mView.mClipBounds ? mView.mClipBoundsRight : getParentView().getRight()) - mTextPadding - textWidthCalculation;
            }
            else
            {
                mView.mTextLeft = mTextPadding + (mView.mClipBounds ? mView.mClipBoundsLeft : 0);
            }
            textWidth = textWidthCalculation;
        }

        mView.mPrimaryTextLayout = new StaticLayout(mPrimaryText, mPaintPrimaryText, (int) textWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        mView.mPrimaryTextTop = mView.mCentreTop;
        if (mTextPositionAbove)
        {
            mView.mPrimaryTextTop = mView.mPrimaryTextTop - mBaseFocalRadius - mFocalToTextPadding - mView.mPrimaryTextLayout.getHeight();
        }
        else
        {
            mView.mPrimaryTextTop += mBaseFocalRadius + mFocalToTextPadding;
        }

        if (mSecondaryText != null)
        {
            mView.mSecondaryTextLayout = new StaticLayout(mSecondaryText, mPaintSecondaryText, (int) textWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
            if (mTextPositionAbove)
            {
                mView.mPrimaryTextTop =  mView.mPrimaryTextTop - mView.mTextSeparation - mView.mSecondaryTextLayout.getHeight();
            }

            mView.mSecondaryTextOffsetTop = mView.mPrimaryTextLayout.getHeight() + mView.mTextSeparation;
        }
        else
        {
            mView.mSecondaryTextLayout = null;
        }

        updateBackgroundRadius();
        updateIconPosition();
    }

    private void updateBackgroundRadius()
    {
        final float height;
        if (mTextPositionAbove)
        {
            height = mView.mCentreTop - mView.mPrimaryTextTop;
        }
        else
        {
            height = mView.mPrimaryTextTop + mView.mPrimaryTextLayout.getHeight() + (mView.mSecondaryTextLayout != null ? mView.mSecondaryTextLayout.getHeight() : 0) - mView.mCentreTop + mView.mTextSeparation;
        }

        final float length;
        if (mTextPositionRight)
        {
            length = mView.mCentreLeft - mView.mTextLeft + mTextPadding;
        }
        else
        {
            length = mView.mTextLeft + Math.max(mView.mPrimaryTextLayout.getWidth(), mView.mSecondaryTextLayout != null ? mView.mSecondaryTextLayout.getWidth() : 0)
                    + mTextPadding - mView.mCentreLeft;
        }
        //noinspection SuspiciousNameCombination
        mBaseBackgroundRadius = Double.valueOf(Math.sqrt(Math.pow(length, 2) + Math.pow(height, 2))).floatValue();
    }

    private void updateIconPosition()
    {
        if (mView.mIconDrawable != null)
        {
            mView.mIconDrawableLeft = mView.mCentreLeft - (mView.mIconDrawable.getIntrinsicWidth() / 2);
            mView.mIconDrawableTop = mView.mCentreTop - (mView.mIconDrawable.getIntrinsicHeight() / 2);
        }
        else if (mView.mTargetView != null)
        {
            mView.mIconDrawableLeft = mView.mCentreLeft - (mView.mTargetView.getWidth() / 2);
            mView.mIconDrawableTop = mView.mCentreTop - (mView.mTargetView.getHeight() / 2);
        }
    }

    private void updateClipBounds()
    {
        if (mClipToView != null)
        {
            mView.mClipBounds = true;
            mView.mClipBoundsLeft = mClipToView.getLeft();
            mView.mClipBoundsBottom = mClipToView.getBottom();
            mView.mClipBoundsTop = mClipToView.getTop();
            mView.mClipBoundsRight = mClipToView.getRight();
            if (mParentViewIsDecor)
            {
                mView.mClipBoundsTop += mStatusBarHeight;
                mView.mClipBoundsBottom += mStatusBarHeight;
            }
        }
        else if (mParentViewIsDecor)
        {
            mView.mClipBounds = true;
            //Stop the canvas drawing over the status bar
            mView.mClipBoundsTop = mStatusBarHeight;
            mView.mClipBoundsLeft = 0f;
            mView.mClipBoundsBottom = mActivity.getResources().getDisplayMetrics().heightPixels - mStatusBarHeight;
            mView.mClipBoundsRight = mActivity.getResources().getDisplayMetrics().widthPixels;
        }
        else
        {
            mView.mClipBounds = false;
        }
    }

    protected void onHidePrompt(final MotionEvent event, final boolean targetTapped)
    {
        if (mOnHidePromptListener != null)
        {
            mOnHidePromptListener.onHidePrompt(event, targetTapped);
        }
    }

    protected void onHidePromptComplete()
    {
        if (mOnHidePromptListener != null)
        {
            mOnHidePromptListener.onHidePromptComplete();
        }
    }

    /**
     * View used to render the tap target.
     */
    static class PromptView extends View
    {
        private float mCentreLeft, mCentreTop;
        private Paint mPaintBackground, mPaintFocal;
        private float mFocalRadius, mBackgroundRadius;
        private float mFocalRippleSize;
        private int mFocalRippleAlpha;
        private Drawable mIconDrawable;
        private float mIconDrawableLeft;
        private float mIconDrawableTop;
        private float mTextLeft;
        private float mPrimaryTextTop;
        private float mSecondaryTextOffsetTop;
        private Layout mPrimaryTextLayout;
        private Layout mSecondaryTextLayout;
        private boolean mDrawRipple = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        private OnPromptTouchedListener mOnPromptTouchedListener;
        private boolean mCaptureTouchEventOnFocal;
        private float mClipBoundsTop, mClipBoundsLeft, mClipBoundsBottom, mClipBoundsRight;
        private View mTargetView;
        private float mTextSeparation;
        private boolean mClipBounds;
        private boolean mCaptureTouchEventOutsidePrompt;

        public PromptView(final Context context)
        {
            super(context);
        }

        @Override
        public void onDraw(final Canvas canvas)
        {
            if (mClipBounds)
            {
                canvas.clipRect(mClipBoundsLeft, mClipBoundsTop, mClipBoundsRight, mClipBoundsBottom);
            }

            //Draw the backgrounds
            canvas.drawCircle(mCentreLeft, mCentreTop, mBackgroundRadius, mPaintBackground);
            //Draw the ripple
            if (mDrawRipple)
            {
                final int oldAlpha = mPaintFocal.getAlpha();
                mPaintFocal.setAlpha(mFocalRippleAlpha);
                canvas.drawCircle(mCentreLeft, mCentreTop, mFocalRippleSize, mPaintFocal);
                mPaintFocal.setAlpha(oldAlpha);
            }
            //Draw the focal
            canvas.drawCircle(mCentreLeft, mCentreTop, mFocalRadius, mPaintFocal);

            //Draw the icon
            if (mIconDrawable != null)
            {
                canvas.translate(mIconDrawableLeft, mIconDrawableTop);
                mIconDrawable.draw(canvas);
                canvas.translate(-mIconDrawableLeft, -mIconDrawableTop);
            }
            else if (mTargetView != null)
            {
                canvas.translate(mIconDrawableLeft, mIconDrawableTop);
                mTargetView.draw(canvas);
                canvas.translate(-mIconDrawableLeft, -mIconDrawableTop);
            }

            //Draw the text
            canvas.translate(mTextLeft, mPrimaryTextTop);
            mPrimaryTextLayout.draw(canvas);
            if (mSecondaryTextLayout != null)
            {
                canvas.translate(0f, mSecondaryTextOffsetTop);
                mSecondaryTextLayout.draw(canvas);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            final float x = event.getX();
            final float y = event.getY();
            //If the touch point is within the prompt background stop the event from passing through it
            boolean captureEvent = pointInCircle(x, y, mBackgroundRadius);
            //If the touch event was at least in the background and in the focal
            if (captureEvent && pointInCircle(x, y, mFocalRadius))
            {
                //Override allowing the touch event to pass through the view with the user defined value
                captureEvent = mCaptureTouchEventOnFocal;
                onPromptTouched(event, true);
            }
            else
            {
                // If the prompt background was not touched
                if (!captureEvent)
                {
                    captureEvent = mCaptureTouchEventOutsidePrompt;
                }
                onPromptTouched(event, false);
            }
            return captureEvent;
        }

        /**
         * Determines if a point is in the centre of a circle with a radius from the point ({@link #mCentreLeft, {@link #mCentreTop}}.
         *
         * @param x The x position in the view.
         * @param y The y position in the view.
         * @param radius The radius of the circle.
         * @return True if the point (x, y) is in the circle.
         */
        private boolean pointInCircle(final float x, final float y, final float radius)
        {
            return Math.pow(x - mCentreLeft, 2) + Math.pow(y - mCentreTop, 2) < Math.pow(radius, 2);
        }

        protected void onPromptTouched(final MotionEvent event, final boolean targetTapped)
        {
            if (mOnPromptTouchedListener != null)
            {
                mOnPromptTouchedListener.onPromptTouched(event, targetTapped);
            }
        }

        /**
         * Interface definition for a callback to be invoked when a {@link PromptView} is touched.
         */
        public interface OnPromptTouchedListener
        {
            /**
             * Called when a touch event occurs in the prompt view.
             *
             * @param event The touch event that triggered the dismiss or finish.
             * @param tappedTarget True if the prompt focal point was touched.
             */
            void onPromptTouched(final MotionEvent event, final boolean tappedTarget);
        }
    }

    /**
     * A builder to create a {@link MaterialTapTargetPrompt} instance.
     */
    public static class Builder
    {
        /**
         * The containing activity.
         */
        private Activity mActivity;

        private boolean mTargetSet;

        /**
         * The view to place the prompt around.
         */
        private View mTargetView;

        /**
         * The left and top positioning for the focal centre point.
         */
        private float mCentreLeft, mCentreTop;

        /**
         * The text to display.
         */
        private String mPrimaryText, mSecondaryText;
        private int mPrimaryTextColour, mSecondaryTextColour, mBackgroundColour, mFocalColour;
        private float mFocalRadius;
        private float mPrimaryTextSize, mSecondaryTextSize;
        private float mMaxTextWidth;
        private float mTextPadding;
        private float mFocalToTextPadding;
        private Interpolator mAnimationInterpolator;
        private Drawable mIconDrawable;
        private OnHidePromptListener mOnHidePromptListener;
        private boolean mCaptureTouchEventOnFocal;
        private float mTextSeparation;
        private boolean mAutoDismiss, mAutoFinish;
        private boolean mCaptureTouchEventOutsidePrompt;

        /**
         * Creates a builder for a tap target prompt that uses the default
         * tap target prompt theme.
         *
         * @param activity the activity to show the prompt within.
         */
        public Builder(final Activity activity)
        {
            this(activity, 0);
        }

        /**
         * Creates a builder for a material tap target prompt that uses an explicit theme
         * resource.
         *
         * The {@code themeResId} may be specified as {@code 0}
         * to use the parent {@code context}'s resolved value for
         * {@link R.attr#MaterialTapTargetPromptTheme}.
         *
         * @param activity the activity to show the prompt within.
         * @param themeResId the resource ID of the theme against which to inflate
         *                   this dialog, or {@code 0} to use the parent
         *                   {@code context}'s default material tap target prompt theme
         */
        public Builder(final Activity activity, int themeResId)
        {
            mActivity = activity;
            //Attempt to load the theme from the activity theme
            if (themeResId == 0)
            {
                final TypedValue outValue = new TypedValue();
                activity.getTheme().resolveAttribute(R.attr.MaterialTapTargetPromptTheme, outValue, true);
                themeResId = outValue.resourceId;
            }

            final float density = activity.getResources().getDisplayMetrics().density;
            final TypedArray a = mActivity.obtainStyledAttributes(themeResId, R.styleable.PromptView);
            mPrimaryTextColour = a.getColor(R.styleable.PromptView_primaryTextColour, Color.WHITE);
            mSecondaryTextColour = a.getColor(R.styleable.PromptView_secondaryTextColour, Color.argb(179, 255, 255, 255));
            mPrimaryText = a.getString(R.styleable.PromptView_primaryText);
            mSecondaryText = a.getString(R.styleable.PromptView_secondaryText);
            mBackgroundColour = a.getColor(R.styleable.PromptView_backgroundColour, Color.argb(244, 63, 81, 181));
            mFocalColour = a.getColor(R.styleable.PromptView_focalColour, Color.WHITE);
            mFocalRadius = a.getDimension(R.styleable.PromptView_focalRadius, density * 44);
            mPrimaryTextSize = a.getDimension(R.styleable.PromptView_primaryTextSize, 22 * density);
            mSecondaryTextSize = a.getDimension(R.styleable.PromptView_secondaryTextSize, 18 * density);
            mMaxTextWidth = a.getDimension(R.styleable.PromptView_maxTextWidth, 400 * density);
            mTextPadding = a.getDimension(R.styleable.PromptView_textPadding, 40 * density);
            mFocalToTextPadding = a.getDimension(R.styleable.PromptView_focalToTextPadding, 20 * density);
            mTextSeparation = a.getDimension(R.styleable.PromptView_textSeparation, 16 * density);
            mAutoDismiss = a.getBoolean(R.styleable.PromptView_autoDismiss, true);
            mAutoFinish = a.getBoolean(R.styleable.PromptView_autoFinish, true);
            mCaptureTouchEventOutsidePrompt = a.getBoolean(R.styleable.PromptView_captureTouchEventOutsidePrompt, false);
            mCaptureTouchEventOnFocal = a.getBoolean(R.styleable.PromptView_captureTouchEventOnFocal, false);
            final int targetId = a.getResourceId(R.styleable.PromptView_target, 0);
            a.recycle();

            if (targetId != 0)
            {
                mTargetView = mActivity.findViewById(targetId);
                if (mTargetView != null)
                {
                    mTargetSet = true;
                }
            }
        }

        /**
         * Set the view for the prompt to focus on.
         * @param target The view that the prompt will highlight.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTarget(final View target)
        {
            mTargetView = target;
            mTargetSet = true;
            return this;
        }

        /**
         * Set the view for the prompt to focus on using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTarget(@IdRes final int target)
        {
            mTargetView = mActivity.findViewById(target);
            mTargetSet = mTargetView != null;
            return this;
        }

        /**
         * Set the centre point as a screen position
         * @param left Centre point from screen left
         * @param top Centre point from screen top
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTarget(final float left, final float top)
        {
            mTargetView = null;
            mCentreLeft = left;
            mCentreTop = top;
            mTargetSet = true;
            return this;
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
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrimaryText(@StringRes final int resId)
        {
            mPrimaryText = mActivity.getString(resId);
            return this;
        }

        /**
         * Set the primary text to the given string
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrimaryText(final String text)
        {
            mPrimaryText = text;
            return this;
        }

        /**
         * Set the primary text font size using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrimaryTextSize(@DimenRes final int resId)
        {
            mPrimaryTextSize = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set the primary text font size.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrimaryTextSize(final float size)
        {
            mPrimaryTextSize = size;
            return this;
        }

        /**
         * Set the primary text colour.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrimaryTextColour(@ColorInt final int colour)
        {
            mPrimaryTextColour = colour;
            return this;
        }

        /**
         * Set the primary text colour using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrimaryTextColourFromRes(@ColorRes final int resId)
        {
            mPrimaryTextColour = getColour(resId);
            return this;
        }

        /**
         * Set the secondary text using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSecondaryText(@StringRes final int resId)
        {
            mSecondaryText = mActivity.getString(resId);
            return this;
        }

        /**
         * Set the secondary text.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSecondaryText(final String text)
        {
            mSecondaryText = text;
            return this;
        }

        /**
         * Set the secondary text font size using the give resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSecondaryTextSize(@DimenRes final int resId)
        {
            mSecondaryTextSize = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set the secondary text font size.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSecondaryTextSize(final float size)
        {
            mSecondaryTextSize = size;
            return this;
        }

        /**
         * Set the secondary text colour.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSecondaryTextColour(@ColorInt final int colour)
        {
            mSecondaryTextColour = colour;
            return this;
        }

        /**
         * Set the secondary text colour using the give resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSecondaryTextColourFromRes(@ColorRes final int resId)
        {
            mSecondaryTextColour = getColour(resId);
            return this;
        }

        /**
         * Set the text left and right padding.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextPadding(final float padding)
        {
            mTextPadding = padding;
            return this;
        }

        /**
         * Set the text left and right padding using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextPadding(@DimenRes final int resId)
        {
            mTextPadding = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set the distance between the primary and secondary text.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextSeparation(final float separation)
        {
            mTextSeparation = separation;
            return this;
        }

        /**
         * Set the distance between the primary and secondary text using the given
         * resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextSeparation(@DimenRes final int resId)
        {
            mTextSeparation = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set the padding between the text and the focal point.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setFocalToTextPadding(final float padding)
        {
            mFocalToTextPadding = padding;
            return this;
        }

        /**
         * Set the padding between the text and the focal point using the given
         * resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setFocalToTextPadding(@DimenRes final int resId)
        {
            mFocalToTextPadding = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set the interpolator to use in animations.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setAnimationInterpolator(final Interpolator interpolator)
        {
            mAnimationInterpolator = interpolator;
            return this;
        }

        /**
         * Set the icon to draw in the focal point using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIcon(@DrawableRes final int resId)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                mIconDrawable = mActivity.getDrawable(resId);
            }
            else
            {
                //noinspection deprecation
                mIconDrawable = mActivity.getResources().getDrawable(resId);
            }
            if (mIconDrawable != null)
            {
                mIconDrawable.setBounds(0, 0, mIconDrawable.getIntrinsicWidth(), mIconDrawable.getIntrinsicHeight());
                mIconDrawable.setColorFilter(mBackgroundColour, PorterDuff.Mode.MULTIPLY);
                mIconDrawable.setAlpha(Color.alpha(mBackgroundColour));
            }
            return this;
        }

        /**
         * Set the icon to draw in the focal point.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIconDrawable(final Drawable drawable)
        {
            mIconDrawable = drawable;
            return this;
        }

        /**
         * Set the listener to listen for when the prompt is touched.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnHidePromptListener(final OnHidePromptListener listener)
        {
            mOnHidePromptListener = listener;
            return this;
        }

        /**
         * Set if the prompt should stop touch events on the focal point from passing
         * to underlying views. Default is false.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCaptureTouchEventOnFocal(final boolean captureTouchEvent)
        {
            mCaptureTouchEventOnFocal = captureTouchEvent;
            return this;
        }

        /**
         * Set the max width that the primary and secondary text can be.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMaxTextWidth(final float width)
        {
            mMaxTextWidth = width;
            return this;
        }

        /**
         * Set the max width that the primary and secondary text can be using the given
         * resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMaxTextWidth(@DimenRes final int resId)
        {
            mMaxTextWidth = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set the background colour.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackgroundColour(@ColorInt final int colour)
        {
            mBackgroundColour = colour;
            return this;
        }

        /**
         * Set the background colour using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackgroundColourFromRes(@ColorRes final int resId)
        {
            mBackgroundColour = getColour(resId);
            return this;
        }

        /**
         * Set the focal point colour.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setFocalColour(@ColorInt final int colour)
        {
            mFocalColour = colour;
            return this;
        }

        /**
         * Set the focal point colour using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setFocalColourFromRes(@ColorRes final int resId)
        {
            mFocalColour = getColour(resId);
            return this;
        }

        /**
         * Set the focal point radius.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setFocalRadius(final float radius)
        {
            mFocalRadius = radius;
            return this;
        }

        /**
         * Set the focal point radius using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setFocalRadius(@DimenRes final int resId)
        {
            mFocalRadius = mActivity.getResources().getDimension(resId);
            return this;
        }

        /**
         * Set whether the prompt should dismiss itself when a touch event occurs outside the focal.
         * Default is true.
         *
         * @param autoDismiss True - prompt will dismiss when touched outside the focal, false - no action taken.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setAutoDismiss(final boolean autoDismiss)
        {
            mAutoDismiss = autoDismiss;
            return this;
        }

        /**
         * Set whether the prompt should finish itself when a touch event occurs inside the focal.
         * Default is true.
         *
         * @param autoFinish True - prompt will finish when touched inside the focal, false - no action taken.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setAutoFinish(final boolean autoFinish)
        {
            mAutoFinish = autoFinish;
            return this;
        }

        /**
         * Set if the prompt should stop touch events outside the prompt from passing
         * to underlying views. Default is false.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCaptureTouchEventOutsidePrompt(final boolean captureTouchEventOutsidePrompt)
        {
            mCaptureTouchEventOutsidePrompt = captureTouchEventOutsidePrompt;
            return this;
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
         * Will return {@link null} if a valid target has not been set or the primary text is {@link null}.
         * To check that a valid target has been set call {@link #isTargetSet()}.
         * </p>
         */
        public MaterialTapTargetPrompt create()
        {
            if (!mTargetSet || mPrimaryText == null)
            {
                return null;
            }
            final MaterialTapTargetPrompt mPrompt = new MaterialTapTargetPrompt(mActivity);
            if (mTargetView != null)
            {
                mPrompt.mTargetView = mTargetView;
                mPrompt.mView.mTargetView = mTargetView;
            }
            else
            {
                mPrompt.mBaseLeft = mCentreLeft;
                mPrompt.mBaseTop = mCentreTop;
            }
            mPrompt.mClipToView = (ViewGroup) ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);

            mPrompt.mPrimaryText = mPrimaryText;
            mPrompt.mPrimaryTextColourAlpha = Color.alpha(mPrimaryTextColour);
            mPrompt.mSecondaryText = mSecondaryText;
            mPrompt.mSecondaryTextColourAlpha = Color.alpha(mSecondaryTextColour);
            mPrompt.mMaxTextWidth = mMaxTextWidth;
            mPrompt.mTextPadding = mTextPadding;
            mPrompt.mFocalToTextPadding = mFocalToTextPadding;
            mPrompt.mBaseFocalRippleAlpha = 150;

            mPrompt.mView.mTextSeparation = mTextSeparation;

            mPrompt.mOnHidePromptListener = mOnHidePromptListener;
            mPrompt.mView.mCaptureTouchEventOnFocal = mCaptureTouchEventOnFocal;

            if (mAnimationInterpolator != null)
            {
                mPrompt.mAnimationInterpolator = mAnimationInterpolator;
            }
            else
            {
                mPrompt.mAnimationInterpolator = new AccelerateDecelerateInterpolator();
            }

            mPrompt.mBaseFocalRadius = mFocalRadius;
            //Calculate 10% of the focal radius
            mPrompt.mFocalRadius10Percent = mFocalRadius / 100 * 10;

            mPrompt.mView.mIconDrawable = mIconDrawable;

            mPrompt.mView.mPaintFocal = new Paint();
            mPrompt.mView.mPaintFocal.setColor(mFocalColour);
            mPrompt.mView.mPaintFocal.setAlpha(Color.alpha(mFocalColour));
            mPrompt.mView.mPaintFocal.setAntiAlias(true);

            mPrompt.mView.mPaintBackground = new Paint();
            mPrompt.mView.mPaintBackground.setColor(mBackgroundColour);
            mPrompt.mView.mPaintBackground.setAlpha(Color.alpha(mBackgroundColour));
            mPrompt.mView.mPaintBackground.setAntiAlias(true);

            mPrompt.mPaintPrimaryText = new TextPaint();
            mPrompt.mPaintPrimaryText.setColor(mPrimaryTextColour);
            mPrompt.mPaintPrimaryText.setAlpha(Color.alpha(mPrimaryTextColour));
            mPrompt.mPaintPrimaryText.setAntiAlias(true);
            mPrompt.mPaintPrimaryText.setTextSize(mPrimaryTextSize);

            mPrompt.mPaintSecondaryText = new TextPaint();
            mPrompt.mPaintSecondaryText.setColor(mSecondaryTextColour);
            mPrompt.mPaintSecondaryText.setAlpha(Color.alpha(mSecondaryTextColour));
            mPrompt.mPaintSecondaryText.setAntiAlias(true);
            mPrompt.mPaintSecondaryText.setTextSize(mSecondaryTextSize);

            mPrompt.mAutoDismiss = mAutoDismiss;
            mPrompt.mAutoFinish = mAutoFinish;

            mPrompt.mView.mCaptureTouchEventOutsidePrompt = mCaptureTouchEventOutsidePrompt;

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
         * Will return {@link null} if a valid target has not been set or the primary text is {@link null}.
         * To check that a valid target has been set call {@link #isTargetSet()}.
         * </p>
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

        private int getColour(final int resId)
        {
            final int colour;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                colour = mActivity.getColor(resId);
            }
            else
            {
                //noinspection deprecation
                colour = mActivity.getResources().getColor(resId);
            }
            return colour;
        }
    }

    /**
     * Interface definition for a callback to be invoked when a {@link MaterialTapTargetPrompt} is removed from view.
     */
    public interface OnHidePromptListener
    {
        /**
         * Called when the use touches the prompt view,
         * but before the prompt is removed from view.
         *
         * @param event The touch event that triggered the dismiss or finish.
         * @param tappedTarget True if the prompt focal point was touched.
         */
        void onHidePrompt(final MotionEvent event, final boolean tappedTarget);

        /**
         * Called after the prompt has been removed from view.
         */
        void onHidePromptComplete();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static class AnimatorListener implements Animator.AnimatorListener
    {

        @Override
        public void onAnimationStart(Animator animation)
        {

        }

        @Override
        public void onAnimationEnd(Animator animation)
        {

        }

        @Override
        public void onAnimationCancel(Animator animation)
        {

        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {

        }
    }
}
