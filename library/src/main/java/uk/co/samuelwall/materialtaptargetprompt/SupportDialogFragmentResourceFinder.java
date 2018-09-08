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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link ResourceFinder} implementation for {@link DialogFragment}.
 */
public class SupportDialogFragmentResourceFinder implements ResourceFinder
{
    /**
     * The fragment to get the resources from.
     */
    private final DialogFragment dialogFragment;

    /**
     * Constructor.
     *
     * @param fragment Fragment to get the resources from.
     */
    public SupportDialogFragmentResourceFinder(final DialogFragment fragment)
    {
        this.dialogFragment = fragment;
    }

    @Nullable
    @Override
    public View findViewById(int resId)
    {
        return this.dialogFragment.getView().findViewById(resId);
    }

    @NonNull
    @Override
    public ViewGroup getPromptParentView()
    {
        //noinspection ConstantConditions
        return (ViewGroup) this.dialogFragment.getView();
    }

    @NonNull
    @Override
    public Context getContext()
    {
        return this.dialogFragment.requireContext();
    }

    @NonNull
    @Override
    public Resources getResources()
    {
        return this.dialogFragment.getResources();
    }

    @NonNull
    @Override
    public Resources.Theme getTheme()
    {
        return this.dialogFragment.requireActivity().getTheme();
    }

    @NonNull
    @Override
    public String getString(int resId)
    {
        return this.dialogFragment.getString(resId);
    }

    @NonNull
    @Override
    public TypedArray obtainStyledAttributes(int resId, int[] attrs)
    {
        return this.dialogFragment.requireActivity().obtainStyledAttributes(resId, attrs);
    }

    @Nullable
    @Override
    public Drawable getDrawable(int resId)
    {
        return this.dialogFragment.getResources().getDrawable(resId);
    }
}
