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

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link ResourceFinder} implementation for {@link Dialog}.
 */
public class DialogResourceFinder extends ActivityResourceFinder
{
    @NonNull private final Dialog mDialog;

    public DialogResourceFinder(@NonNull final Dialog dialog)
    {
        super(dialog.getOwnerActivity());
        mDialog = dialog;
    }

    @Nullable
    @Override
    public View findViewById(@IdRes int resId)
    {
        return mDialog.findViewById(resId);
    }

    @NonNull
    @Override
    public ViewGroup getPromptParentView()
    {
        return (ViewGroup) mDialog.getWindow().getDecorView();
    }

    @NonNull
    @Override
    public Context getContext()
    {
        return mDialog.getContext();
    }
}
