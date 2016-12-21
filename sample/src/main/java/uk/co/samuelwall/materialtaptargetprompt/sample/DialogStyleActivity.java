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

package uk.co.samuelwall.materialtaptargetprompt.sample;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class DialogStyleActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_style);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showFabPrompt(View view)
    {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setPrimaryText("Clipped to activity bounds")
                .setSecondaryText("The prompt does not draw outside the activity")
                .show();
    }

    public void showSideNavigationPrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.menu_prompt_title)
                .setSecondaryText(R.string.menu_prompt_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_back);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        tapTargetPromptBuilder.setTarget(tb.getChildAt(1));

        tapTargetPromptBuilder.setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
        {
            @Override
            public void onHidePrompt(MotionEvent event, boolean tappedTarget)
            {
                //Do something such as storing a value so that this prompt is never shown again
            }

            @Override
            public void onHidePromptComplete()
            {

            }
        });
        tapTargetPromptBuilder.show();
    }

    public void showOverflowPrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.overflow_prompt_title)
                .setSecondaryText(R.string.overflow_prompt_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_more_vert);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        final View child = tb.getChildAt(2);
        if (child instanceof ActionMenuView)
        {
            final ActionMenuView actionMenuView = ((ActionMenuView) child);
            tapTargetPromptBuilder.setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 1));
        }
        else
        {
            Toast.makeText(this, R.string.overflow_unavailable, Toast.LENGTH_SHORT);
        }
        tapTargetPromptBuilder.show();
    }

    public void showSearchPrompt(View view)
    {
        new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.search_prompt_title)
                .setSecondaryText(R.string.search_prompt_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_search)
                .setTarget(R.id.action_search)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.onBackPressed();
            return true;
        }
        else if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
