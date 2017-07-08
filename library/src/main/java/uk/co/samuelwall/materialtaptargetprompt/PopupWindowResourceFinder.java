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
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by sam on 08/07/17.
 */

public class PopupWindowResourceFinder implements ResourceFinder
{
    private final PopupWindow mPopupWindow;
    private Window mWindow;

    public PopupWindowResourceFinder(final PopupWindow popupWindow, Window window)
    {
        mPopupWindow = popupWindow;
        mWindow = window;
    }

    @Nullable
    @Override
    public View findViewById(@IdRes int resId)
    {
        return mPopupWindow.getContentView().findViewById(resId);
    }

    @Override
    public Window getWindow()
    {
        return null;
    }

    @Override
    public ViewGroup getPromptParentView()
    {
        return (ViewGroup) mPopupWindow.getContentView().getRootView();
    }

    @Override
    public Context getContext()
    {
        return mPopupWindow.getContentView().getContext();
    }

    @Override
    public Resources getResources()
    {
        return getContext().getResources();
    }

    @Override
    public Resources.Theme getTheme()
    {
        return getContext().getTheme();
    }

    @NonNull
    @Override
    public String getString(@StringRes int resId)
    {
        return getContext().getString(resId);
    }

    @Override
    public TypedArray obtainStyledAttributes(@StyleRes int resId, @StyleableRes int[] attrs)
    {
        return getContext().obtainStyledAttributes(resId, attrs);
    }

    @Nullable
    @Override
    public Drawable getDrawable(@DrawableRes int resId)
    {
        final Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            drawable = getContext().getDrawable(resId);
        }
        else
        {
            //noinspection deprecation
            drawable = getContext().getResources().getDrawable(resId);
        }
        return drawable;
    }
}
