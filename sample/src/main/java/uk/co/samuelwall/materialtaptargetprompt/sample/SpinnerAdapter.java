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

package uk.co.samuelwall.materialtaptargetprompt.sample;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Based on https://stackoverflow.com/a/25137740/8345775
 *
 * @param <T>
 */
public class SpinnerAdapter<T> extends ArrayAdapter<T>
{
    private final Collection<SpinnerListener> spinnerListeners = new ArrayList<SpinnerListener>();
    private ViewGroup itemParent;

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
            @IdRes int textViewResourceId, @NonNull T[] objects)
    {
        super(context, resource, textViewResourceId, objects);
    }

    // Add the rest of the constructors here ...


    // Just grab the spinner view (parent of the spinner item view) and add a listener to it.
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        if (isParentTheListView(parent))
        {
            itemParent = parent;
            addFocusListenerAsExpansionListener();
        }

        return super.getDropDownView(position, convertView, parent);
    }

    // Assumes the item view parent is a ListView (which it is when a Spinner class is used)
    private boolean isParentTheListView(ViewGroup parent)
    {
        return (parent != itemParent && parent != null && ListView.class.isAssignableFrom(parent.getClass()));
    }

    // Add a focus listener to listen to spinner expansion and collapse events.
    private void addFocusListenerAsExpansionListener()
    {
        final View.OnFocusChangeListener listenerWrapper =
                new OnFocusChangeListenerWrapper(itemParent, spinnerListeners);
        itemParent.setOnFocusChangeListener(listenerWrapper);
    }

    // Utility method.
    public boolean isExpanded()
    {
        return (itemParent != null && itemParent.hasFocus());
    }

    public void addSpinnerListener(SpinnerListener spinnerListener)
    {
        spinnerListeners.add(spinnerListener);
    }

    public boolean removeSpinnerListener(SpinnerListener spinnerListener)
    {
        return spinnerListeners.remove(spinnerListener);
    }

    public interface SpinnerListener
    {

        void onSpinnerExpanded(View viewParent);

        void onSpinnerCollapsed();
    }

    // Listener that listens for 'expand' and 'collapse' events.
    private static class OnFocusChangeListenerWrapper implements View.OnFocusChangeListener
    {
        private final Collection<SpinnerListener> spinnerListeners;
        private final View.OnFocusChangeListener originalFocusListener;
        private final View viewParent;

        private OnFocusChangeListenerWrapper(View parent, Collection<SpinnerListener> spinnerListeners)
        {
            this.viewParent = parent;
            this.spinnerListeners = spinnerListeners;
            this.originalFocusListener = parent.getOnFocusChangeListener();
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus)
        {
            if (originalFocusListener != null)
            {
                originalFocusListener.onFocusChange(view,
                        hasFocus); // Preserve the pre-existing focus listener (if any).
            }
            callSpinnerListeners(hasFocus);
        }

        private void callSpinnerListeners(boolean hasFocus)
        {
            for (SpinnerListener spinnerListener : spinnerListeners)
            {
                if (spinnerListener != null)
                {
                    callSpinnerListener(hasFocus, spinnerListener);
                }
            }
        }

        private void callSpinnerListener(boolean hasFocus, SpinnerListener spinnerListener)
        {
            if (hasFocus)
            {
                spinnerListener.onSpinnerExpanded(viewParent);
            }
            else
            {
                spinnerListener.onSpinnerCollapsed();
            }
        }
    }
}
