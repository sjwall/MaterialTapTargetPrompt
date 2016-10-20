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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.OnHidePromptListener;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.OnViewFoundListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    MaterialTapTargetPrompt mFabPrompt;
    Handler handler = new Handler();

    public void showFabPrompt(View view)
    {
        if (mFabPrompt != null)
        {
            return;
        }
        mFabPrompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(findViewById(R.id.fab))
                .setPrimaryText("Send your first email")
                .setSecondaryText("Tap the envelop to start composing your first email")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setOnHidePromptListener(new OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                    {
                        mFabPrompt = null;
                        //Do something such as storing a value so that this prompt is never shown again
                    }

                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .create();
        mFabPrompt.show();
    }

    public void showSideNavigationPrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.menu_prompt_title)
                .setSecondaryText(R.string.menu_prompt_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_menu);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        tapTargetPromptBuilder.setTarget(tb.getChildAt(1));

        tapTargetPromptBuilder.setOnHidePromptListener(new OnHidePromptListener()
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
        tapTargetPromptBuilder.setTarget(tb.getChildAt(2));

        tapTargetPromptBuilder.setOnHidePromptListener(new OnHidePromptListener()
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

    public void showStylePrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(this, R.style.MaterialTapTargetPromptTheme_FabTarget);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        builder.setIcon(R.drawable.ic_more_vert)
                .setTarget(tb.getChildAt(2))
                .show();
    }

    public void showDialog(View view)
    {
        startActivity(new Intent(this, DialogStyleActivity.class));
    }

    public void showFindDelayedPrompt(View view)
    {
        showFindDelayedPrompt(view, getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false), R.id.extra_target_fab,
            "Delayed Find Prompt", "This prompt found it's view once the view was added to the screen");
    }

    public void showCenterPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Target Prompt", "This is a prompt of a target in the center of the screen");
    }

    public void showCenterLeftPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        View mainLayout = findViewById(R.id.drawer_layout);
        newView.setPadding(0, 0, mainLayout.getWidth()/2, 0);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Left Target Prompt", "This is a prompt of a target to the left of the center of the screen");
    }

    public void showCenterRightPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        View mainLayout = findViewById(R.id.drawer_layout);
        newView.setPadding(mainLayout.getWidth()/2, 0, 0, 0);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Right Target Prompt", "This is a prompt of a target to the right of the center of the screen");
    }

    public void showCenterTopPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        View mainLayout = findViewById(R.id.drawer_layout);
        newView.setPadding(0, 0, 0, mainLayout.getHeight()/2);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Top Target Prompt", "This is a prompt of a target above of the center of the screen");
    }

    public void showCenterBottomPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        View mainLayout = findViewById(R.id.drawer_layout);
        newView.setPadding(0, mainLayout.getHeight()/2, 0, 0);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Bottom Target Prompt", "This is a prompt of a target below the center of the screen");
    }

    public void showCenterRightLongPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        View mainLayout = findViewById(R.id.drawer_layout);
        newView.setPadding(mainLayout.getWidth()/2, 0, 0, 0);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Right Target Prompt but with much longer text", "This is a prompt of a target to the right of the center of the screen but with really long text to see how it behaves.");
    }

    public void showCenterPrimaryOnlyPrompt(View view)
    {
        View newView = getLayoutInflater().inflate(R.layout.extra_fab_for_tageting_center,
            (ViewGroup) findViewById(R.id.content_main), false);
        View mainLayout = findViewById(R.id.drawer_layout);
        newView.setPadding(mainLayout.getWidth()/2, 0, 0, 0);
        showFindDelayedPrompt(view, newView, R.id.extra_target_fab,
            "Center Primary Only Text", null);
    }

    public void showFindDelayedPrompt(View view, final View viewToAdd, int idToFind, String primary, String secondary)
    {
        abstract class CombinedPromptListener implements OnHidePromptListener, OnViewFoundListener
        {
            View targetView;

            abstract void cleanup();

            @Override
            public void onHidePrompt(MotionEvent event, boolean tappedTarget)
            {
                cleanup();
            }

            @Override
            public void onHidePromptComplete()
            {
                cleanup();
            }

            @Override
            public void onViewFound(View view)
            {
                targetView = view;
            }
        }

        CombinedPromptListener listener = new CombinedPromptListener()
        {
            @Override
            void cleanup()
            {
                if (targetView != null)
                {
                    if (targetView.getParent() != null) {
                        ((ViewGroup) targetView.getParent()).removeView(targetView);
                    }
                    targetView = null;
                }
            }
        };

        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                ((ViewGroup) findViewById(R.id.content_main)).addView(viewToAdd);
            }
        }, 500);

        new MaterialTapTargetPrompt.Builder(MainActivity.this)
            .setTarget(idToFind)
            .setPrimaryText(primary)
            .setSecondaryText(secondary)
            .setAnimationInterpolator(new FastOutSlowInInterpolator())
            .setCaptureTouchEventOutsidePrompt(true)
            .setOnViewFroundListener(listener)
            .setOnHidePromptListener(listener)
            .show();
    }

    public void showNoAutoDismiss(View view)
    {
        if (mFabPrompt != null)
        {
            return;
        }
        mFabPrompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
            .setTarget(findViewById(R.id.fab))
            .setPrimaryText("No Auto Dismiss")
            .setSecondaryText("This prompt will only be removed after tapping the envelop")
            .setAnimationInterpolator(new FastOutSlowInInterpolator())
            .setAutoDismiss(false)
            .setAutoFinish(false)
            .setCaptureTouchEventOutsidePrompt(true)
            .setOnHidePromptListener(new OnHidePromptListener()
            {
                @Override
                public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                {
                    if (tappedTarget)
                    {
                        mFabPrompt.finish();
                        mFabPrompt = null;
                    }
                }

                @Override
                public void onHidePromptComplete()
                {

                }
            })
            .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mFabPrompt != null)
                {
                    mFabPrompt.finish();
                    mFabPrompt = null;
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
