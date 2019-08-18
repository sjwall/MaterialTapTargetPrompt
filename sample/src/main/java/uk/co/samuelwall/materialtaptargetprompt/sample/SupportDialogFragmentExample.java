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

package uk.co.samuelwall.materialtaptargetprompt.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.viewpager.widget.ViewPager;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Prompt example for {@link DialogFragment}.
 */
public class SupportDialogFragmentExample extends DialogFragment
{
    private ViewPager viewPager;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.support_fragment_dialog, container);

        getDialog().setTitle("DialogFragment");
        getDialog().setOnShowListener(dialog -> showFragmentFabPrompt());
        rootView.findViewById(R.id.button_fab_prompt)
                .setOnClickListener(view -> showFragmentFabPrompt());

        rootView.findViewById(R.id.button_view_pager_prompt)
            .setOnClickListener(v -> showViewPagerTab());

        viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), this));
        return rootView;
    }

    private void showFragmentFabPrompt()
    {
        new MaterialTapTargetPrompt.Builder((Fragment) this)
                .setTarget(R.id.fab)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setPrimaryText("Clipped to dialog bounds")
                .setSecondaryText("The prompt does not draw outside the dialog")
                .show();
    }

    private void showViewPagerTab()
    {
        new MaterialTapTargetPrompt.Builder((Fragment) this)
            .setTarget(this.viewPager.getChildAt(0))
            .setPromptFocal(new RectanglePromptFocal())
            .setAnimationInterpolator(new FastOutSlowInInterpolator())
            .setPrimaryText("View Pager Tab")
            .setSecondaryText("Change tab")
            .setFocalRadius(R.dimen.dp60)
            .show();
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter
    {
        private static int ITEM_COUNT = 6;
        private DialogFragment dialogFragment;

        @SuppressLint("WrongConstant")
        MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull DialogFragment fragment)
        {
            super(fragmentManager, 1);//FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            dialogFragment = fragment;
        }

        @Override
        public int getCount()
        {
            return ITEM_COUNT;
        }

        @NotNull
        @Override
        public Fragment getItem(int position)
        {
            return ViewPagerSupportFragment.newInstance(dialogFragment);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return "Page " + position;
        }
    }
}
