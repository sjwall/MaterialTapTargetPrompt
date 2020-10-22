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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * {@link ResourceFinder} implementation for {@link Fragment}.
 */
public class SupportFragmentResourceFinder implements ResourceFinder
{
    /**
     * The fragment to get the resources from.
     */
    @NonNull
    private final Fragment fragment;

    /**
     * The fragment view parent.
     */
    private ViewGroup parent;

    /**
     * Constructor.
     *
     * @param fragment Fragment to get the resources from.
     */
    public SupportFragmentResourceFinder(@NonNull final Fragment fragment)
    {
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View findViewById(int resId)
    {
        return this.fragment.getView().findViewById(resId);
    }

    @NonNull
    @Override
    public ViewGroup getPromptParentView()
    {
        if (this.parent == null)
        {
            //noinspection ConstantConditions
            ViewParent parent = this.fragment.getView().getParent();
            while (parent.getClass().getName().contains("FragmentContainerView"))
                parent = parent.getParent();
            this.parent = (ViewGroup) parent;
        }
        return this.parent;
    }

    @NonNull
    @Override
    public Context getContext()
    {
        return this.fragment.requireContext();
    }

    @NonNull
    @Override
    public Resources getResources()
    {
        return this.fragment.getResources();
    }

    @NonNull
    @Override
    public Resources.Theme getTheme()
    {
        return this.fragment.requireActivity().getTheme();
    }

    @NonNull
    @Override
    public String getString(int resId)
    {
        return this.fragment.getString(resId);
    }

    @NonNull
    @Override
    public TypedArray obtainStyledAttributes(int resId, int[] attrs)
    {
        return this.fragment.requireActivity().obtainStyledAttributes(resId, attrs);
    }

    @Nullable
    @Override
    public Drawable getDrawable(int resId)
    {
        return this.fragment.getResources().getDrawable(resId);
    }
}
