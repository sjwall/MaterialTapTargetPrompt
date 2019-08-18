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

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class ListActivity extends AppCompatActivity
{
    ListView listView;

    int itemIndex = 0;

    final String[] values = new String[] { "Banana",
            "Apple",
            "Pear",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.firstLine, values);

        listView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showPrompt());
        new Handler().post(this::showPrompt);
    }

    private void showPrompt()
    {
        new MaterialTapTargetPrompt.Builder(ListActivity.this)
                .setTarget(listView.getChildAt(itemIndex++))
                .setPrimaryText("List item")
                .setSecondaryText("This is targeting a list item")
                .setPromptFocal(new RectanglePromptFocal())
                .show();
        if (itemIndex == values.length)
        {
            itemIndex = 0;
        }
    }
}
