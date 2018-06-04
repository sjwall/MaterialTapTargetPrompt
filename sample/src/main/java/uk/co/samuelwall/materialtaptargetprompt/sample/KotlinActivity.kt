/*
 * Copyright (C) 2018 Samuel Wall
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

package uk.co.samuelwall.materialtaptargetprompt.sample

import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.ActionMenuView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal

class KotlinActivity : AppCompatActivity() {

    private var mFabPrompt: MaterialTapTargetPrompt? = null

    private var mActionMode: ActionMode? = null
    private val mActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.actionmode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            mActionMode!!.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            mActionMode = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_empty)
        findViewById<View>(R.id.kotlin_examples_activity).visibility = View.GONE
        findViewById<View>(R.id.other_examples_dialog).visibility = View.GONE
        findViewById<View>(R.id.other_examples_centre).visibility = View.GONE
        findViewById<View>(R.id.other_examples_card).visibility = View.GONE
        findViewById<View>(R.id.other_examples_list).visibility = View.GONE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun showFabPrompt(view: View) {
        if (mFabPrompt != null) {
            return
        }
        mFabPrompt = MaterialTapTargetPrompt.Builder(this@KotlinActivity)
                .setTarget(findViewById<View>(R.id.fab))
                .setPrimaryText("Send your first email")
                .setSecondaryText("Tap the envelop to start composing your first email")
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_DISMISSING) {
                        mFabPrompt = null
                        //Do something such as storing a value so that this prompt is never shown again
                    }
                }
                .create()
        mFabPrompt!!.show()
    }

    fun showFabPromptFor(view: View) {
        MaterialTapTargetPrompt.Builder(this@KotlinActivity)
                .setTarget(findViewById<View>(R.id.fab))
                .setFocalPadding(R.dimen.dp40)
                .setPrimaryText("showFor(7000)")
                .setSecondaryText("This prompt will show for 7 seconds")
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_SHOW_FOR_TIMEOUT) {

                        Toast.makeText(this@KotlinActivity,
                                       "Prompt timedout after 7 seconds", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                .showFor(7000)
    }

    fun showSideNavigationPrompt(view: View) {
        val tapTargetPromptBuilder = MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.menu_prompt_title)
                .setSecondaryText(R.string.menu_prompt_description)
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_back)
        val tb = this.findViewById<Toolbar>(android.support.v7.appcompat.R.id.action_bar)
        tapTargetPromptBuilder.setTarget(tb.getChildAt(1))

        tapTargetPromptBuilder.setPromptStateChangeListener { prompt, state ->
            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                //Do something such as storing a value so that this prompt is never shown again
            }
        }
        tapTargetPromptBuilder.show()
    }

    fun showOverflowPrompt(view: View) {
        val tapTargetPromptBuilder = MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.overflow_prompt_title)
                .setSecondaryText(R.string.overflow_prompt_description)
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_more_vert)
        val tb = this.findViewById<Toolbar>(android.support.v7.appcompat.R.id.action_bar)
        val child = tb.getChildAt(2)
        if (child is ActionMenuView) {
            tapTargetPromptBuilder.setTarget(child.getChildAt(child.childCount - 1))
        } else {
            Toast.makeText(this, R.string.overflow_unavailable, Toast.LENGTH_SHORT)
        }
        tapTargetPromptBuilder.show()
    }

    fun showSearchPrompt(view: View) {
        MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.search_prompt_title)
                .setSecondaryText(R.string.search_prompt_description)
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_search)
                .setTarget(R.id.action_search)
                .show()
    }

    fun showBottomSheetDialogPrompt(view: View) {
        val bottomSheetDialogFragmentExample = BottomSheetDialogFragmentExample()

        bottomSheetDialogFragmentExample.show(supportFragmentManager,
                                              bottomSheetDialogFragmentExample.tag)
    }

    fun showStylePrompt(view: View) {
        val builder = MaterialTapTargetPrompt.Builder(this,
                                                      R.style.MaterialTapTargetPromptTheme_FabTarget)
        val tb = this.findViewById<Toolbar>(android.support.v7.appcompat.R.id.action_bar)
        val child = tb.getChildAt(2)
        if (child is ActionMenuView) {
            builder.setTarget(child.getChildAt(child.childCount - 1))
        } else {
            Toast.makeText(this, R.string.overflow_unavailable, Toast.LENGTH_SHORT)
        }
        builder.setIcon(R.drawable.ic_more_vert)
                .show()
    }

    fun showNoAutoDismiss(view: View) {
        if (mFabPrompt != null) {
            return
        }
        mFabPrompt = MaterialTapTargetPrompt.Builder(this@KotlinActivity)
                .setTarget(findViewById<View>(R.id.fab))
                .setPrimaryText("No Auto Dismiss")
                .setSecondaryText("This prompt will only be removed after tapping the envelop")
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        mFabPrompt!!.finish()
                        mFabPrompt = null
                    } else if (state == MaterialTapTargetPrompt.STATE_DISMISSING) {

                    }
                }
                .show()
    }

    fun showActionModePrompt(view: View) {
        mActionMode = this.startSupportActionMode(mActionModeCallback)
        MaterialTapTargetPrompt.Builder(this@KotlinActivity)
                .setPrimaryText(R.string.action_mode_tick_prompt_title)
                .setSecondaryText(R.string.action_mode_tick_prompt_description)
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setTarget(findViewById<View>(R.id.action_tick))
                .setIcon(R.drawable.ic_check)
                .show()
    }

    fun showRectPrompt(view: View) {
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(view)
                .setPrimaryText("Different shapes")
                .setSecondaryText("Extend PromptFocal or PromptBackground to change the shapes")
                .setPromptBackground(RectanglePromptBackground())
                .setPromptFocal(RectanglePromptFocal())
                .show()
    }

    fun showFullscreenRectPrompt(view: View) {
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(view)
                .setPrimaryText("Different shapes")
                .setSecondaryText("Extend PromptFocal or PromptBackground to change the shapes")
                .setPromptBackground(FullscreenPromptBackground())
                .setPromptFocal(RectanglePromptFocal())
                .show()
    }

    fun showSequence(view: View) {
        MaterialTapTargetSequence()
                .addPrompt(MaterialTapTargetPrompt.Builder(this)
                                   .setTarget(findViewById<View>(R.id.fab))
                                   .setPrimaryText("Step 1")
                                   .setSecondaryText("This will show for 4 seconds")
                                   .setFocalPadding(R.dimen.dp40)
                                   .create(), 4000)
                .addPrompt(MaterialTapTargetPrompt.Builder(this)
                                   .setTarget(findViewById<View>(R.id.action_search))
                                   .setPrimaryText("Step 2")
                                   .setSecondaryText("This will show till you press it")
                                   .setAnimationInterpolator(LinearOutSlowInInterpolator())
                                   .setFocalPadding(R.dimen.dp40)
                                   .setIcon(R.drawable.ic_search))
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        } else if (id == android.R.id.home) {
            this.onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
