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
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.StyleableRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link ResourceFinder} implementation for {@link Activity}.
 */
public class ActivityResourceFinder implements ResourceFinder
{
    @NonNull private final Activity mActivity;

    public ActivityResourceFinder(@NonNull final Activity activity)
    {
        mActivity = activity;
    }

    @Nullable
    @Override
    public View findViewById(@IdRes int resId)
    {
        return mActivity.findViewById(resId);
    }

    @NonNull
    @Override
    public ViewGroup getPromptParentView()
    {
        return (ViewGroup) mActivity.getWindow().getDecorView();
    }

    @NonNull
    @Override
    public Context getContext()
    {
        return mActivity;
    }

    @NonNull
    @Override
    public Resources getResources()
    {
        return mActivity.getResources();
    }

    @NonNull
    @Override
    public Resources.Theme getTheme()
    {
        return mActivity.getTheme();
    }

    @NonNull
    @Override
    public String getString(@StringRes int resId)
    {
        return mActivity.getString(resId);
    }

    @NonNull
    @Override
    public TypedArray obtainStyledAttributes(@StyleRes int resId, @StyleableRes int[] attrs)
    {
        return mActivity.obtainStyledAttributes(resId, attrs);
    }

    @Nullable
    @Override
    public Drawable getDrawable(@DrawableRes int resId)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            return mActivity.getDrawable(resId);
        }
        else
        {
            //noinspection deprecation
            return mActivity.getResources().getDrawable(resId);
        }
    }
}
