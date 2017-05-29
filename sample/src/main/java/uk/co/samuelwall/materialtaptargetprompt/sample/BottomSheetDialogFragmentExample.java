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

package uk.co.samuelwall.materialtaptargetprompt.sample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Example {@link BottomSheetDialogFragment} that shows a prompt when displayed.
 */
public class BottomSheetDialogFragmentExample extends BottomSheetDialogFragment
{
    @Override
    public Dialog onCreateDialog(@NonNull final Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);

        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                new MaterialTapTargetPrompt.Builder(BottomSheetDialogFragmentExample.this)
                        .setPrimaryText(R.string.search_prompt_title)
                        .setSecondaryText(R.string.search_prompt_description)
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                        .setIcon(R.drawable.ic_search)
                        .setTarget(R.id.bs_search)
                        .show();
            }
        });
        return dialog;
    }
}
