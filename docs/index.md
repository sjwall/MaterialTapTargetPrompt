---
layout: home
---

Follow the [quick start guide](#quick-start) below to get started.

[![FAB Example](assets/example_FAB.png)](examples#View)

[JavaDocs](javadocs), [examples](examples) and a [sample app](https://github.com/sjwall/MaterialTapTargetPrompt/tree/master/sample/src/main/java/uk/co/samuelwall/materialtaptargetprompt/sample) with examples implemented are available.
The sample app is available to download on the Google Play Store:
<a href='https://play.google.com/store/apps/details?id=uk.co.samuelwall.materialtaptargetprompt.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' style='max-width:90px' src='assets/play_store.png'/></a>

# Quick start

## Gradle

Add the following to build.gradle:

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'uk.co.samuelwall:material-tap-target-prompt:2.5.2'
}
```
Supports minSdkVersion 14

## Usage

Basic usage is shown below:

```java
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

new MaterialTapTargetPrompt.Builder(MainActivity.this)
        .setTarget(R.id.fab)
        .setPrimaryText("Send your first email")
        .setSecondaryText("Tap the envelop to start composing your first email")
        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
        {
            @Override
            public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
            {
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                {
                    // User has pressed the prompt target
                }
            }
        })
        .show();
```

### Note

If a target is not set or the target view could not be found or both the primary and secondary text are `null` then `builder.show` and `builder.create` will return `null`.

## Customisation

The library is designed with extendable classes to allow for maximum customisation. More details and examples available [here](customisation).

# License
    Copyright (C) 2016-2018 Samuel Wall

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
