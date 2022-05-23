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

package uk.co.samuelwall.materialtaptargetprompt.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

@Composable
fun TapTargetPromptBox(
    modifier: Modifier = Modifier,
    prompt: @Composable (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val targetBounds = remember { mutableStateOf(Rect.Zero) }
    Layout(
        {
            Box(
                modifier = Modifier.layoutId("anchor"),
                contentAlignment = Alignment.Center,
                content = content
            )
            Box(
                modifier = Modifier.layoutId("prompt"),
                content = { if (prompt != null) TapTargetPrompt(content = prompt, target = content, onDismiss = onDismiss) else null }
            )
        },
        modifier = modifier
    ) { measurables, constraints ->
        val promptPlaceable = measurables.first { it.layoutId == "prompt" }.measure(
            // Measure with loose constraints for height as we don't want the text to take up more
            // space than it needs.
            constraints.copy(minHeight = 0)
        )
        val anchorPlaceable = measurables.first { it.layoutId == "anchor" }.measure(constraints)
        val firstBaseline = anchorPlaceable[FirstBaseline]
        val lastBaseline = anchorPlaceable[LastBaseline]
        val totalWidth = anchorPlaceable.width
        val totalHeight = anchorPlaceable.height
        targetBounds.value = Rect(0f, 0f, anchorPlaceable.width.toFloat(), anchorPlaceable.height.toFloat())
        layout(
            totalWidth,
            totalHeight,
            mapOf(
                FirstBaseline to firstBaseline,
                LastBaseline to lastBaseline
            )
        ) {
            val promptX = promptPlaceable.width / 2
            val promptY = promptPlaceable.height / 2
            anchorPlaceable.placeRelative(0, 0)
            promptPlaceable.placeRelative(promptX, promptY)
        }
    }
}

@Composable
fun TapTargetPrompt(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.error,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (() -> Unit)? = null,
    target: @Composable BoxScope.() -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    Popup (onDismissRequest = onDismiss) {
        val shape = CircleShape
        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = modifier
                    .wrapContentSize()
                    .drawBehind {
                        this.drawCircle(Color.Blue, radius = (this.size.width / 2) + 16.dp.toPx())
                    }
                    .clip(shape),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(content = target)
                if (content != null) {
                    CompositionLocalProvider(
                        LocalContentColor provides contentColor
                    ) {
                        val style =
                            MaterialTheme.typography.button.copy(fontSize = PromptContentFontSize)
                        ProvideTextStyle(
                            value = style,
                            content = { content() }
                        )
                    }
                }
            }
        }
    }
}

/*@VisibleForTesting*/
private val PromptContentFontSize = 10.sp