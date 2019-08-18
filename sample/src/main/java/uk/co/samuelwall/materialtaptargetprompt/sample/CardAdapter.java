/*
 * Copyright (C) 2017, 2019 Samuel Wall
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

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>
{
    private String[] mDataSet = {"foo", "bar", "baz"};

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTextView;
        ImageView mImageView;
        ViewHolder(View v)
        {
            super(v);
            mTextView = v.findViewById(R.id.info_text);
            mImageView = v.findViewById(R.id.info_icon);
        }
    }

    CardAdapter()
    {
    }

    @NotNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position)
    {
        holder.mTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.length;
    }
}
