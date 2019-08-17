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

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class CardActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // The example below uses a RecyclerView with LinearLayoutManager
                final LinearLayout card = (LinearLayout) mLinearLayoutManager.findViewByPosition(0);
                // Check that the view exists for the item
                if (card != null)
                {
                    final CardAdapter.ViewHolder viewHolder = (CardAdapter.ViewHolder) mRecyclerView.getChildViewHolder(card);
                    new MaterialTapTargetPrompt.Builder(CardActivity.this)
                            .setTarget(viewHolder.mImageView)
                            .setClipToView(card.getChildAt(0))
                            .setPrimaryText(R.string.example_card_card_title)
                            .setSecondaryText(R.string.example_card_card_description)
                            .show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(fab)
                .setPrimaryText(R.string.example_card_fab_title)
                .setSecondaryText(R.string.example_card_fab_description)
                .setBackgroundColour(getResources().getColor(R.color.colorAccent))
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .show();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(new CardAdapter());
    }
}
