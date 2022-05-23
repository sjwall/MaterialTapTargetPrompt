/*
 * Copyright (C) 2016-2022 Samuel Wall
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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import uk.co.samuelwall.materialtaptargetprompt.compose.TapTargetPromptBox

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello world!")
            examplePrompt()
        }
    }

    @Composable
    fun examplePrompt() {
        val openPrompt = remember { mutableStateOf(false) }

        TapTargetPromptBox(prompt = if (openPrompt.value) ( { Text(text = "cake") } ) else null,
                           onDismiss = { openPrompt.value = false}) {
            FloatingActionButton(onClick = { openPrompt.value = true } ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}