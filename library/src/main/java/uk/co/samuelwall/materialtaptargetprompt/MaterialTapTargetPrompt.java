/*
 * Copyright (C) 2016-2019 Samuel Wall
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
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;

/**
 * A Material Design tap target onboarding implementation.
 * <p>
 * <div class="special reference">
 * <h3>Onboarding</h3>
 * <p>For more information about onboarding and tap targets, read the
 * <a href="https://www.google.com/design/spec/growth-communications/onboarding.html">Onboarding</a>
 * Material Design guidelines.</p>
 * </div>
 */
public class MaterialTapTargetPrompt
{

    /**
     * Prompt has yet to be shown.
     */
    public static final int STATE_NOT_SHOWN = 0;

    /**
     * Prompt is reveal animation is running.
     */
    public static final int STATE_REVEALING = 1;

    /**
     * Prompt reveal animation has finished and the prompt is displayed.
     */
    public static final int STATE_REVEALED = 2;

    /**
     * The prompt target has been pressed in the focal area.
     */
    public static final int STATE_FOCAL_PRESSED = 3;

    /**
     * The prompt has been removed from view after the prompt has been pressed in the focal area.
     */
    public static final int STATE_FINISHED = 4;

    /**
     * The {@link #dismiss()} method has been called and the prompt is being removed from view.
     */
    public static final int STATE_DISMISSING = 5;

    /**
     * The prompt has been removed from view after the prompt has either been pressed somewhere
     * other than the prompt target or the system back button has been pressed.
     */
    public static final int STATE_DISMISSED = 6;

    /**
     * The {@link #finish()} method has been called and the prompt is being removed from view.
     */
    public static final int STATE_FINISHING = 7;

    /**
     * The prompt has been pressed outside the focal area.
     */
    public static final int STATE_NON_FOCAL_PRESSED = 8;

    /**
     * The prompt has been dismissed by the show for timeout.
     */
    public static final int STATE_SHOW_FOR_TIMEOUT = 9;

    /**
     * The prompt has been dismissed by the system back button being pressed.
     */
    public static final int STATE_BACK_BUTTON_PRESSED = 10;

    /**
     * The view that renders the prompt.
     */
    PromptView mView;

    /**
     * Used to calculate the animation progress for the reveal and dismiss animations.
     */
    @Nullable ValueAnimator mAnimationCurrent;

    /**
     * Used to calculate the animation progress for the idle breathing focal animation.
     */
    @Nullable ValueAnimator mAnimationFocalBreathing;

    /**
     * Used to calculate the animation progress for the idle white flash animation.
     */
    @Nullable ValueAnimator mAnimationFocalRipple;

    /**
     * The last percentage progress for idle animation.
     * Value between 1 to 0 inclusive.
     * Used in the idle animation to track when the animation should change direction.
     */
    float mFocalRippleProgress;

    /**
     * The prompt's current state.
     */
    int mState;

    /**
     * The system status bar height.
     */
    final float mStatusBarHeight;

    /**
     * Task used for triggering the prompt timeout.
     */
    final Runnable mTimeoutRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            // Emit the state change and dismiss the prompt
            onPromptStateChanged(STATE_SHOW_FOR_TIMEOUT);
            dismiss();
        }
    };

    /**
     * Listener for the view layout changing.
     */
    @Nullable final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    /**
     * Default constructor.
     *
     * @param promptOptions The options used to create the prompt.
     */
    MaterialTapTargetPrompt(final PromptOptions promptOptions)
    {
        final ResourceFinder resourceFinder = promptOptions.getResourceFinder();
        mView = new PromptView(resourceFinder.getContext());
        mView.mPrompt = this;
        mView.mPromptOptions = promptOptions;
        mView.mPromptTouchedListener = new PromptView.PromptTouchedListener()
        {
            @Override
            public void onFocalPressed()
            {
                if (!isDismissing())
                {
                    onPromptStateChanged(STATE_FOCAL_PRESSED);
                    if (mView.mPromptOptions.getAutoFinish())
                    {
                        finish();
                    }
                }
            }

            @Override
            public void onNonFocalPressed()
            {
                if (!isDismissing())
                {
                    onPromptStateChanged(STATE_NON_FOCAL_PRESSED);
                    if (mView.mPromptOptions.getAutoDismiss())
                    {
                        dismiss();
                    }
                }
            }

            @Override
            public void onBackButtonPressed()
            {
                if (!isDismissing())
                {
                    onPromptStateChanged(STATE_BACK_BUTTON_PRESSED);
                    onPromptStateChanged(STATE_NON_FOCAL_PRESSED);
                    if (mView.mPromptOptions.getAutoDismiss())
                    {
                        dismiss();
                    }
                }
            }
        };

        Rect rect = new Rect();
        resourceFinder.getPromptParentView().getWindowVisibleDisplayFrame(rect);
        mStatusBarHeight = rect.top;

        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                final View targetView = mView.mPromptOptions.getTargetView();
                if (targetView != null)
                {
                    final boolean isTargetAttachedToWindow;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                        isTargetAttachedToWindow = targetView.isAttachedToWindow();
                    }
                    else
                    {
                        isTargetAttachedToWindow = targetView.getWindowToken() != null;
                    }

                    if (!isTargetAttachedToWindow)
                    {
                        return;
                    }
                }
                prepare();

                if (mAnimationCurrent == null)
                {
                    // Force a relayout to update the view's location
                    updateAnimation(1, 1);
                }
            }
        };
    }

    /**
     * Displays the prompt.
     */
    public void show()
    {
        if (isStarting())
        {
            return;
        }

        final ViewGroup parent = mView.mPromptOptions.getResourceFinder().getPromptParentView();

        // If dismissing or the prompt already exists in the parent view
        if (isDismissing() || parent.findViewById(R.id.material_target_prompt_view) != null)
        {
            cleanUpPrompt(mState);
        }

        parent.addView(mView);
        addGlobalLayoutListener();
        onPromptStateChanged(STATE_REVEALING);
        prepare();
        startRevealAnimation();
    }

    /**
     * Displays the prompt for a maximum amount of time.
     *
     * @param millis The number of milliseconds to show the prompt for.
     */
    public void showFor(long millis)
    {
        mView.postDelayed(mTimeoutRunnable, millis);
        show();
    }

    /**
     * Cancel the show for timer if it has been created.
     */
    public void cancelShowForTimer()
    {
        mView.removeCallbacks(mTimeoutRunnable);
    }

    /**
     * Get the current state of the prompt.
     *
     * @see #STATE_NOT_SHOWN
     * @see #STATE_REVEALING
     * @see #STATE_REVEALED
     * @see #STATE_FOCAL_PRESSED
     * @see #STATE_NON_FOCAL_PRESSED
     * @see #STATE_BACK_BUTTON_PRESSED
     * @see #STATE_FINISHING
     * @see #STATE_FINISHED
     * @see #STATE_DISMISSING
     * @see #STATE_DISMISSED
     */
    public int getState()
    {
        return mState;
    }

    /**
     * Is the current state {@link #STATE_REVEALING} or {@link #STATE_REVEALED}.
     *
     * @return True if revealing or revealed.
     */
    boolean isStarting()
    {
        return mState == STATE_REVEALING || mState == STATE_REVEALED;
    }

    /**
     * Is the current state {@link #STATE_DISMISSING} or {@link #STATE_FINISHING}.
     *
     * @return True if dismissing or finishing.
     */
    boolean isDismissing()
    {
        return mState == STATE_DISMISSING || mState == STATE_FINISHING;
    }

    /**
     * Is the current state {@link #STATE_DISMISSED} or {@link #STATE_FINISHED}.
     *
     * @return True if dismissed or finished.
     */
    boolean isDismissed()
    {
        return mState == STATE_DISMISSED || mState == STATE_FINISHED;
    }

    /**
     * Is the current state neither {@link #STATE_REVEALED} or {@link #STATE_REVEALING}.
     *
     * @return True if not revealed or revealing.
     */
    boolean isComplete()
    {
        return mState == STATE_NOT_SHOWN || isDismissing() || isDismissed();
    }

    /**
     * Adds layout listener to view parent to capture layout changes.
     */
    void addGlobalLayoutListener()
    {
        final ViewTreeObserver viewTreeObserver = ((ViewGroup) mView.getParent()).getViewTreeObserver();
        if (viewTreeObserver.isAlive())
        {
            viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    /**
     * Removes global layout listener added in {@link #addGlobalLayoutListener()}.
     */
    void removeGlobalLayoutListener()
    {
        final ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent == null)
        {
            return;
        }
        final ViewTreeObserver viewTreeObserver = ((ViewGroup) mView.getParent()).getViewTreeObserver();
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
     * <p>
     * This is treated as if the user has touched the target focal point.
     */
    public void finish()
    {
        if (isComplete())
        {
            return;
        }
        cancelShowForTimer();
        cleanUpAnimation();
        mAnimationCurrent = ValueAnimator.ofFloat(1f, 0f);
        mAnimationCurrent.setDuration(225);
        mAnimationCurrent.setInterpolator(mView.mPromptOptions.getAnimationInterpolator());
        mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                final float value = (float) animation.getAnimatedValue();
                updateAnimation(1f + ((1f - value) / 4), value);
            }
        });
        mAnimationCurrent.addListener(new AnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                cleanUpPrompt(STATE_FINISHED);
                mView.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            }
        });
        onPromptStateChanged(STATE_FINISHING);
        mAnimationCurrent.start();
    }

    /**
     * Removes the prompt from view, using a contract and fade animation.
     * <p>
     * This is treated as if the user has touched outside the target focal point.
     */
    public void dismiss()
    {
        if (isComplete())
        {
            return;
        }
        cancelShowForTimer();
        cleanUpAnimation();
        mAnimationCurrent = ValueAnimator.ofFloat(1f, 0f);
        mAnimationCurrent.setDuration(225);
        mAnimationCurrent.setInterpolator(mView.mPromptOptions.getAnimationInterpolator());
        mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                final float value = (float) animation.getAnimatedValue();
                updateAnimation(value, value);
            }
        });
        mAnimationCurrent.addListener(new AnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                cleanUpPrompt(STATE_DISMISSED);
                mView.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            }
        });
        onPromptStateChanged(STATE_DISMISSING);
        mAnimationCurrent.start();
    }

    /**
     * Removes the prompt from view and triggers the {@link #onPromptStateChanged(int)} event.
     */
    void cleanUpPrompt(final int state)
    {
        cleanUpAnimation();
        removeGlobalLayoutListener();
        final ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null)
        {
            parent.removeView(mView);
        }
        if (isDismissing())
        {
            onPromptStateChanged(state);
        }
    }

    /**
     * Stops any current animation and removes references to it.
     */
    void cleanUpAnimation()
    {
        if (mAnimationCurrent != null)
        {
            mAnimationCurrent.removeAllUpdateListeners();
            mAnimationCurrent.removeAllListeners();
            mAnimationCurrent.cancel();
            mAnimationCurrent = null;
        }
        if (mAnimationFocalRipple != null)
        {
            mAnimationFocalRipple.removeAllUpdateListeners();
            mAnimationFocalRipple.cancel();
            mAnimationFocalRipple = null;
        }
        if (mAnimationFocalBreathing != null)
        {
            mAnimationFocalBreathing.removeAllUpdateListeners();
            mAnimationFocalBreathing.cancel();
            mAnimationFocalBreathing = null;
        }
    }

    /**
     * Starts the animation to reveal the prompt.
     */
    void startRevealAnimation()
    {
        updateAnimation(0, 0);
        cleanUpAnimation();
        mAnimationCurrent = ValueAnimator.ofFloat(0f, 1f);
        mAnimationCurrent.setInterpolator(mView.mPromptOptions.getAnimationInterpolator());
        mAnimationCurrent.setDuration(225);
        mAnimationCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                final float value = (float) animation.getAnimatedValue();
                updateAnimation(value, value);
            }
        });
        mAnimationCurrent.addListener(new AnimatorListener()
        {
            @Override
            public void onAnimationEnd(@NonNull Animator animation)
            {
                animation.removeAllListeners();
                updateAnimation(1, 1);
                cleanUpAnimation();
                if (mView.mPromptOptions.getIdleAnimationEnabled())
                {
                    startIdleAnimations();
                }
                onPromptStateChanged(STATE_REVEALED);

                mView.requestFocus();
                mView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
            }
        });
        mAnimationCurrent.start();
    }

    /**
     * Starts the prompt idle animations.
     */
    void startIdleAnimations()
    {
        cleanUpAnimation();
        mAnimationFocalBreathing = ValueAnimator.ofFloat(1, 1.1f, 1);
        mAnimationFocalBreathing.setInterpolator(mView.mPromptOptions.getAnimationInterpolator());
        mAnimationFocalBreathing.setDuration(1000);
        mAnimationFocalBreathing.setStartDelay(225);
        mAnimationFocalBreathing.setRepeatCount(ValueAnimator.INFINITE);
        mAnimationFocalBreathing.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            boolean direction = true;

            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
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
                mView.mPromptOptions.getPromptFocal().update(mView.mPromptOptions, newFocalFraction, 1);
                mView.invalidate();
            }
        });
        mAnimationFocalBreathing.start();

        mAnimationFocalRipple = ValueAnimator.ofFloat(1.1f, 1.6f);
        mAnimationFocalRipple.setInterpolator(mView.mPromptOptions.getAnimationInterpolator());
        mAnimationFocalRipple.setDuration(500);
        mAnimationFocalRipple.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                final float value = (float) animation.getAnimatedValue();
                mView.mPromptOptions.getPromptFocal().updateRipple(value, (1.6f - value) * 2);
            }
        });
    }

    /**
     * Updates the positioning and alpha values using the animation values.
     *
     * @param revealModifier The amount to modify the reveal size, between 0 and 1.
     * @param alphaModifier  The amount to modify the alpha value, between 0 and 1.
     */
    void updateAnimation(final float revealModifier, final float alphaModifier)
    {
        if (mView.getParent() == null)
        {
            return;
        }
        mView.mPromptOptions.getPromptText().update(mView.mPromptOptions, revealModifier, alphaModifier);
        if (mView.mIconDrawable != null)
        {
            mView.mIconDrawable.setAlpha((int) (255f * alphaModifier));
        }
        mView.mPromptOptions.getPromptFocal().update(mView.mPromptOptions, revealModifier, alphaModifier);
        mView.mPromptOptions.getPromptBackground().update(mView.mPromptOptions, revealModifier, alphaModifier);
        mView.invalidate();
    }

    /**
     * Update the focal and text positioning.
     */
    void prepare()
    {
        final View targetRenderView = mView.mPromptOptions.getTargetRenderView();
        if (targetRenderView == null)
        {
            mView.mTargetRenderView = mView.mPromptOptions.getTargetView();
        }
        else
        {
            mView.mTargetRenderView = targetRenderView;
        }
        updateClipBounds();
        final View targetView = mView.mPromptOptions.getTargetView();
        if (targetView != null)
        {
            final int[] viewPosition = new int[2];
            mView.getLocationInWindow(viewPosition);
            mView.mPromptOptions.getPromptFocal().prepare(mView.mPromptOptions, targetView, viewPosition);
        }
        else
        {
            final PointF targetPosition = mView.mPromptOptions.getTargetPosition();
            mView.mPromptOptions.getPromptFocal().prepare(mView.mPromptOptions, targetPosition.x, targetPosition.y);
        }
        mView.mPromptOptions.getPromptText().prepare(mView.mPromptOptions, mView.mClipToBounds, mView.mClipBounds);
        mView.mPromptOptions.getPromptBackground().prepare(mView.mPromptOptions, mView.mClipToBounds, mView.mClipBounds);
        updateIconPosition();
    }

    /**
     * Update the icon drawable position or target render view position.
     */
    void updateIconPosition()
    {
        mView.mIconDrawable = mView.mPromptOptions.getIconDrawable();
        if (mView.mIconDrawable != null)
        {
            final RectF mFocalBounds = mView.mPromptOptions.getPromptFocal().getBounds();
            mView.mIconDrawableLeft = mFocalBounds.centerX()
                    - (mView.mIconDrawable.getIntrinsicWidth() / 2);
            mView.mIconDrawableTop = mFocalBounds.centerY()
                    - (mView.mIconDrawable.getIntrinsicHeight() / 2);
        }
        else if (mView.mTargetRenderView != null)
        {
            final int[] viewPosition = new int[2];
            mView.getLocationInWindow(viewPosition);
            final int[] targetPosition = new int[2];
            mView.mTargetRenderView.getLocationInWindow(targetPosition);

            mView.mIconDrawableLeft = targetPosition[0] - viewPosition[0] - mView.mTargetRenderView.getScrollX();
            mView.mIconDrawableTop = targetPosition[1] - viewPosition[1] - mView.mTargetRenderView.getScrollY();
        }
    }

    /**
     * Update the bounds that the prompt is clip to.
     */
    void updateClipBounds()
    {
        final View clipToView = mView.mPromptOptions.getClipToView();
        if (clipToView != null)
        {
            mView.mClipToBounds = true;

            //Reset the top to 0
            mView.mClipBounds.set(0, 0, 0, 0);

            //Find the location of the clip view on the screen
            final Point offset = new Point();
            clipToView.getGlobalVisibleRect(mView.mClipBounds, offset);

            if (offset.y == 0)
            {
                mView.mClipBounds.top += mStatusBarHeight;
            }
        }
        else
        {
            mView.mPromptOptions.getResourceFinder().getPromptParentView().getGlobalVisibleRect(mView.mClipBounds, new Point());
            mView.mClipToBounds = false;
        }
    }

    /**
     * Handles emitting the prompt state changed events.
     *
     * @param state The state that the prompt is now in.
     */
    protected void onPromptStateChanged(final int state)
    {
        mState = state;
        mView.mPromptOptions.onPromptStateChanged(this, state);
        mView.mPromptOptions.onExtraPromptStateChanged(this, state);
    }

    /**
     * Creates a prompt with the supplied options.
     *
     * @param promptOptions The options to use to create the prompt.
     * @return The created prompt.
     */
    @NonNull
    public static MaterialTapTargetPrompt createDefault(@NonNull final PromptOptions promptOptions)
    {
        return new MaterialTapTargetPrompt(promptOptions);
    }

    /**
     * View used to render the tap target.
     */
    static class PromptView extends View
    {
        /*int padding;
        Paint paddingPaint = new Paint();
        Paint itemPaint = new Paint();*/
        Drawable mIconDrawable;
        float mIconDrawableLeft;
        float mIconDrawableTop;
        PromptTouchedListener mPromptTouchedListener;
        Rect mClipBounds = new Rect();
        View mTargetRenderView;
        MaterialTapTargetPrompt mPrompt;
        PromptOptions mPromptOptions;
        boolean mClipToBounds;
        AccessibilityManager mAccessibilityManager;

        /**
         * Create a new prompt view.
         *
         * @param context The context that the view is created in.
         */
        public PromptView(final Context context)
        {
            super(context);
            setId(R.id.material_target_prompt_view);
            setFocusableInTouchMode(true);
            requestFocus();
            // Hardware acceleration for clipping to a path is not supported on SDK < 18
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                // Disable hardware acceleration
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            /*paddingPaint.setColor(Color.GREEN);
            paddingPaint.setAlpha(100);
            itemPaint.setColor(Color.BLUE);
            itemPaint.setAlpha(100);*/

            setAccessibilityDelegate(new AccessibilityDelegate());
            mAccessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);

            if (mAccessibilityManager.isEnabled()) {
                setupAccessibilityClickListener();
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            // Stop the prompt increasing the parent size by setting it to the parents size.
            final View parent = (View) this.getParent();
            this.setMeasuredDimension(parent.getMeasuredWidth(), parent.getMeasuredHeight());
        }

        @Override
        public void onDraw(final Canvas canvas)
        {
            if (mClipToBounds)
            {
                canvas.clipRect(mClipBounds);
            }

            //Draw the backgrounds, clipping the focal path so we don't draw over it.
            final Path focalPath = mPromptOptions.getPromptFocal().getPath();
            if (focalPath != null)
            {
                canvas.save();
                canvas.clipPath(focalPath, Region.Op.DIFFERENCE);
            }

            mPromptOptions.getPromptBackground().draw(canvas);

            if (focalPath != null)
            {
                canvas.restore();
            }

            //Draw the focal
            mPromptOptions.getPromptFocal().draw(canvas);

            /*canvas.drawRect(mPrimaryTextLeft - padding, mPrimaryTextTop, mPrimaryTextLeft, mPrimaryTextTop + mSecondaryTextOffsetTop + mSecondaryTextLayout.getHeight(), paddingPaint);
            canvas.drawRect(mTextBounds, itemPaint);
            canvas.drawRect(mTextBounds.right, mPrimaryTextTop, mTextBounds.right + padding, mPrimaryTextTop + mSecondaryTextOffsetTop + mSecondaryTextLayout.getHeight(), paddingPaint);*/

            //Draw the icon
            if (mIconDrawable != null)
            {
                canvas.translate(mIconDrawableLeft, mIconDrawableTop);
                mIconDrawable.draw(canvas);
                canvas.translate(-mIconDrawableLeft, -mIconDrawableTop);
            }
            else if (mTargetRenderView != null)
            {
                canvas.translate(mIconDrawableLeft, mIconDrawableTop);
                mTargetRenderView.draw(canvas);
                canvas.translate(-mIconDrawableLeft, -mIconDrawableTop);
            }

            //Draw the text
            mPromptOptions.getPromptText().draw(canvas);
        }

        @Override
        public boolean onHoverEvent(MotionEvent event) {
            if (mAccessibilityManager.isTouchExplorationEnabled() && event.getPointerCount() == 1) {
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_HOVER_ENTER: {
                        event.setAction(MotionEvent.ACTION_DOWN);
                    } break;
                    case MotionEvent.ACTION_HOVER_MOVE: {
                        event.setAction(MotionEvent.ACTION_MOVE);
                    } break;
                    case MotionEvent.ACTION_HOVER_EXIT: {
                        event.setAction(MotionEvent.ACTION_UP);
                    } break;
                }
                return onTouchEvent(event);
            }
            return super.onHoverEvent(event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            final float x = event.getX();
            final float y = event.getY();
            //If the touch point is within the prompt background stop the event from passing through it
            boolean captureEvent = (!mClipToBounds || mClipBounds.contains((int) x, (int) y))
                    && mPromptOptions.getPromptBackground().contains(x, y);
            //If the touch event was at least in the background and in the focal
            if (captureEvent && mPromptOptions.getPromptFocal().contains(x, y))
            {
                //Override allowing the touch event to pass through the view with the user defined value
                captureEvent = mPromptOptions.getCaptureTouchEventOnFocal();
                if (mPromptTouchedListener != null)
                {
                    mPromptTouchedListener.onFocalPressed();
                }
            }
            else
            {
                // If the prompt background was not touched
                if (!captureEvent)
                {
                    captureEvent = mPromptOptions.getCaptureTouchEventOutsidePrompt();
                }
                if (mPromptTouchedListener != null)
                {
                    mPromptTouchedListener.onNonFocalPressed();
                }
            }
            return captureEvent;
        }

        @Override
        public boolean dispatchKeyEventPreIme(KeyEvent event)
        {
            if (mPromptOptions.getBackButtonDismissEnabled()
                    && event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null)
                {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && event.getRepeatCount() == 0)
                    {
                        state.startTracking(event, this);
                        return true;
                    }
                    else if (event.getAction() == KeyEvent.ACTION_UP
                            && !event.isCanceled() && state.isTracking(event))
                    {
                        if (mPromptTouchedListener != null)
                        {
                            mPromptTouchedListener.onBackButtonPressed();
                        }
                        return mPromptOptions.getAutoDismiss()
                                || super.dispatchKeyEventPreIme(event);
                    }
                }
            }

            return super.dispatchKeyEventPreIme(event);
        }

        @Override
        protected void onDetachedFromWindow()
        {
            super.onDetachedFromWindow();
            mPrompt.cleanUpAnimation();
        }

        @Override
        public CharSequence getAccessibilityClassName()
        {
            return PromptView.class.getName();
        }

        /**
         * When AccessibilityManager is enabled, the prompt view can be dismissed by double-tap.
         * The event is also passed as onClick() to the target view, when available.
         */
        private void setupAccessibilityClickListener()
        {
            setClickable(true);
            setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                    {
                        final View targetView = mPromptOptions.getTargetView();
                        if (targetView != null)
                        {
                            targetView.callOnClick();
                        }
                    }

                    mPrompt.finish();
                }
            });
        }

        /**
         * Interface definition for a callback to be invoked when a {@link PromptView} is touched.
         */
        public interface PromptTouchedListener
        {
            /**
             * Called when the focal is pressed.
             */
            void onFocalPressed();

            /**
             * Called when anywhere outside the focal is pressed or the system back button is
             * pressed.
             */
            void onNonFocalPressed();

            /**
             * Called when the system back button is pressed.
             */
            void onBackButtonPressed();
        }

        class AccessibilityDelegate extends View.AccessibilityDelegate {

            @Override
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info)
            {
                super.onInitializeAccessibilityNodeInfo(host, info);

                @Nullable final Package viewPackage = PromptView.this.getClass().getPackage();
                if (viewPackage != null)
                {
                    info.setPackageName(viewPackage.getName());
                }
                info.setSource(host);
                info.setClickable(true);
                info.setEnabled(true);
                info.setChecked(false);
                info.setFocusable(true);
                info.setFocused(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                {
                    info.setLabelFor(mPromptOptions.getTargetView());
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    info.setDismissable(true);
                }

                info.setContentDescription(mPromptOptions.getContentDescription());
                info.setText(mPromptOptions.getContentDescription());
            }

            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event)
            {
                super.onPopulateAccessibilityEvent(host, event);

                final CharSequence contentDescription = mPromptOptions.getContentDescription();
                if (!TextUtils.isEmpty(contentDescription))
                {
                    event.getText().add(contentDescription);
                }
            }
        }
    }

    /**
     * A builder to create a {@link MaterialTapTargetPrompt} instance.
     */
    public static class Builder extends PromptOptions<Builder>
    {
        /**
         * Creates a builder for a tap target prompt that uses the default tap target prompt theme.
         *
         * @param fragment the fragment to show the prompt within.
         * @see #Builder(Fragment, int)
         */
        public Builder(@NonNull final Fragment fragment)
        {
            this(fragment, 0);
        }

        /**
         * Creates a builder for a material tap target prompt that uses an explicit theme resource.
         * <p>
         * The {@code themeResId} may be specified as {@code 0} to use the parent {@code context}'s
         * resolved value for {@link R.attr#MaterialTapTargetPromptTheme}.
         *
         * @param fragment   the fragment to show the prompt within.
         * @param themeResId the resource ID of the theme against which to inflate this dialog, or
         *                   {@code 0} to use the parent {@code context}'s default material tap
         *                   target prompt theme
         */
        public Builder(@NonNull final Fragment fragment, int themeResId)
        {
            this(new SupportFragmentResourceFinder(fragment), themeResId);
        }

        /**
         * Creates a builder for a tap target prompt that uses the default tap target prompt theme.
         *
         * @param dialogFragment the dialog fragment to show the prompt within.
         * @see #Builder(DialogFragment, int)
         */
        public Builder(@NonNull final DialogFragment dialogFragment)
        {
            this(dialogFragment, 0);
        }

        /**
         * Creates a builder for a material tap target prompt that uses an explicit theme resource.
         * <p>
         * The {@code themeResId} may be specified as {@code 0} to use the parent {@code context}'s
         * resolved value for {@link R.attr#MaterialTapTargetPromptTheme}.
         *
         * @param dialogFragment the dialog fragment to show the prompt within.
         * @param themeResId     the resource ID of the theme against which to inflate this dialog,
         *                       or {@code 0} to use the parent {@code context}'s default material
         *                       tap target prompt theme
         */
        public Builder(@NonNull final DialogFragment dialogFragment, int themeResId)
        {
            this(new SupportFragmentResourceFinder(dialogFragment), themeResId);
        }

        /**
         * Creates a builder for a tap target prompt that uses the default tap target prompt theme.
         *
         * @param dialog the dialog to show the prompt within.
         */
        public Builder(@NonNull final Dialog dialog)
        {
            this(dialog, 0);
        }

        /**
         * Creates a builder for a material tap target prompt that uses an explicit theme resource.
         * <p>
         * The {@code themeResId} may be specified as {@code 0} to use the parent {@code context}'s
         * resolved value for {@link R.attr#MaterialTapTargetPromptTheme}.
         *
         * @param dialog     the dialog to show the prompt within.
         * @param themeResId the resource ID of the theme against which to inflate this dialog, or
         *                   {@code 0} to use the parent {@code context}'s default material tap
         *                   target prompt theme
         */
        public Builder(@NonNull final Dialog dialog, int themeResId)
        {
            this(new DialogResourceFinder(dialog), themeResId);
        }

        /**
         * Creates a builder for a tap target prompt that uses the default tap target prompt theme.
         *
         * @param activity the activity to show the prompt within.
         */
        public Builder(@NonNull final Activity activity)
        {
            this(activity, 0);
        }

        /**
         * Creates a builder for a material tap target prompt that uses an explicit theme resource.
         * <p>
         * The {@code themeResId} may be specified as {@code 0} to use the parent {@code context}'s
         * resolved value for {@link R.attr#MaterialTapTargetPromptTheme}.
         *
         * @param activity   the activity to show the prompt within.
         * @param themeResId the resource ID of the theme against which to inflate this dialog, or
         *                   {@code 0} to use the parent {@code context}'s default material tap
         *                   target prompt theme
         */
        public Builder(@NonNull final Activity activity, int themeResId)
        {
            this(new ActivityResourceFinder(activity), themeResId);
        }

        /**
         * Creates a builder for a material tap target prompt that uses an explicit theme resource.
         * <p>
         * The {@code themeResId} may be specified as {@code 0} to use the parent {@code context}'s
         * resolved value for {@link R.attr#MaterialTapTargetPromptTheme}.
         *
         * @param resourceFinder The {@link ResourceFinder} used to find views and resources.
         * @param themeResId     the resource ID of the theme against which to inflate this dialog,
         *                       or {@code 0} to use the parent {@code context}'s default material
         *                       tap target prompt theme
         */
        public Builder(@NonNull final ResourceFinder resourceFinder, int themeResId)
        {
            super(resourceFinder);
            load(themeResId);
        }
    }

    /**
     * Interface definition for a callback to be invoked when a prompts state changes.
     */
    public interface PromptStateChangeListener
    {
        /**
         * Called when the prompts state changes.
         *
         * @param prompt The prompt which state has changed.
         * @param state  can be either {@link #STATE_REVEALING}, {@link #STATE_REVEALED}, {@link
         *               #STATE_FOCAL_PRESSED}, {@link #STATE_FINISHED}, {@link #STATE_DISMISSING},
         *               {@link #STATE_DISMISSED}
         */
        void onPromptStateChanged(@NonNull final MaterialTapTargetPrompt prompt, final int state);
    }

    static class AnimatorListener implements Animator.AnimatorListener
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
