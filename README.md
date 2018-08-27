# Material Tap Target Prompt

[![Build Status](https://travis-ci.org/sjwall/MaterialTapTargetPrompt.svg?branch=master)](https://travis-ci.org/sjwall/MaterialTapTargetPrompt)
[![codecov](https://codecov.io/gh/sjwall/MaterialTapTargetPrompt/branch/master/graph/badge.svg)](https://codecov.io/gh/sjwall/MaterialTapTargetPrompt)
[![Maintainability](https://api.codeclimate.com/v1/badges/242b94ecd2d181b8298e/maintainability)](https://codeclimate.com/github/sjwall/MaterialTapTargetPrompt/maintainability)
[![Download](https://api.bintray.com/packages/sjwall/maven/material-tap-target-prompt/images/download.svg)](https://bintray.com/sjwall/maven/material-tap-target-prompt/_latestVersion)
[![JavaDoc](https://img.shields.io/badge/JavaDocs-2.12.1-brightgreen.svg)][4]
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat)](http://makeapullrequest.com)

A Tap Target implementation in Android based on Material Design Onboarding guidelines. For more information on tap targets check out the [guidelines][1].

[JavaDocs][4], [examples][3] and a [sample app][2] with examples implemented are available.

![FAB Example](docs/assets/example_FAB.png) ![App Bar Example](docs/assets/example_appbar.png)

![Card Example](docs/assets/example_card.png) ![Centre Example](docs/assets/example_centre.png)

The sample app is available to download on the [Google Play Store][5]:
<a href='https://play.google.com/store/apps/details?id=uk.co.samuelwall.materialtaptargetprompt.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' style='max-width:90px' src='docs/assets/play_store.png'/></a>

# Quick start

## Gradle

Add the following to `build.gradle`:

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'uk.co.samuelwall:material-tap-target-prompt:2.12.1'
}
```
Supports Android minSdkVersion 14

## Usage
Basic usage is shown below with more examples in the [sample app][2] and [documentation][3]:

```java
new MaterialTapTargetPrompt.Builder(MainActivity.this)
        .setTarget(R.id.fab)
        .setPrimaryText("Send your first email")
        .setSecondaryText("Tap the envelope to start composing your first email")
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

## Other shapes

The default shape is a circle but any other shape can be rendered by extending the [PromptBackground][6] and [PromptFocal][7] classes.
Custom shapes such as a rectangle can be set by calling `setPromptBackground` and/or `setPromptFocal`.
Documentation and examples are available [here][8].

![Rectangle Example](docs/assets/example_rectangle.png)

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

[1]: https://material.io/design/communication/onboarding.html#quickstart-model
[2]: https://github.com/sjwall/MaterialTapTargetPrompt/tree/master/sample/src/main/java/uk/co/samuelwall/materialtaptargetprompt/sample
[3]: https://sjwall.github.io/MaterialTapTargetPrompt/examples
[4]: https://sjwall.github.io/MaterialTapTargetPrompt/javadocs
[5]: https://play.google.com/store/apps/details?id=uk.co.samuelwall.materialtaptargetprompt.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1
[6]: https://github.com/sjwall/MaterialTapTargetPrompt/blob/master/library/src/main/java/uk/co/samuelwall/materialtaptargetprompt/extras/PromptBackground.java
[7]: https://github.com/sjwall/MaterialTapTargetPrompt/blob/master/library/src/main/java/uk/co/samuelwall/materialtaptargetprompt/extras/PromptFocal.java
[8]: https://sjwall.github.io/MaterialTapTargetPrompt/shapes
