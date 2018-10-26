/*
 * Copyright (C) 2018 Samuel Wall
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

/**
 * Initialise the tabs.
 */
function initTabs()
{
    // Get the desired language
    let url = new URL(window.location);
    let language = url.searchParams.get("language");
    let tabIndex = 0;
    // If kolin then the index should be 1
    if (language == 'kotlin')
    {
        tabIndex = 1;
    }
    selectAllTabs(tabIndex, false);
}

/**
 * Changes all tab groups to show the same tab index.
 *
 * @param index The 0 based index for the tab in the group.
 * @param pushHistory Should the page location be updated.
 */
function selectAllTabs(index, pushHistory)
{
    let tabGroups = document.getElementsByClassName('tabs');
    for (let i = 0, count = tabGroups.length; i < count; i++)
    {
        let group = tabGroups[i];
        selectTab(group, index);
    }
    if (pushHistory)
    {
        history.pushState(null, document.title, window.location.pathname
            + '?language=' + tabGroups[0].getElementsByClassName('tabs-title')[index].innerHTML.trim().toLowerCase()
            + window.location.hash);
    }
}

/**
 * Select a tab and show its contents in a tab group.
 *
 * @param group The tab group to update.
 * @param index The tab position in the group.
 */
function selectTab(group, index)
{
    let tabContents = group.getElementsByClassName('tabs-content');
    for (let i = 0, count = tabContents.length; i < count; i++)
    {
        let content = tabContents[i];
        if (i == index)
        {
            content.classList.remove('hidden');
        }
        else
        {
            content.classList.add('hidden');
        }
    }
    let titles = group.getElementsByClassName('tabs-title');
    for (let i = 0, count = titles.length; i < count; i++)
    {
        let content = titles[i];
        if (i == index)
        {
            content.classList.add('selected');
        }
        else
        {
            content.classList.remove('selected');
        }
        if (!content.classList.contains('inited'))
        {
            let pos = i;
            content.onclick = () =>
            {
                selectAllTabs(pos, true);
            };
            content.classList.add('inited');
        }
    }
}

initTabs();
