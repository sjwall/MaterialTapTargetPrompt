# Material Tap Target Prompt
[![Build Status](https://travis-ci.org/sjwall/MaterialTapTargetPrompt.svg?branch=master)](https://travis-ci.org/sjwall/MaterialTapTargetPrompt)
[![codecov](https://codecov.io/gh/sjwall/MaterialTapTargetPrompt/branch/master/graph/badge.svg)](https://codecov.io/gh/sjwall/MaterialTapTargetPrompt)
[![Download](https://api.bintray.com/packages/sjwall/maven/material-tap-target-prompt/images/download.svg)](https://bintray.com/sjwall/maven/material-tap-target-prompt/_latestVersion)

A Tap Target implementation in Android based on Material Design Onboarding guidelines. For more information on tap targets check out the [guidelines][1].

![FAB Example](art/example_FAB.png) ![App Bar Example](art/example_appbar.png)

![Card Example](art/example_card.png) ![Centre Example](art/example_centre.png)

# Sample App
The sample app in the repository is available on Google Play:

<a href='https://play.google.com/store/apps/details?id=uk.co.samuelwall.materialtaptargetprompt.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' style='max-width:250px' src='art/play_store.png'/></a>

# Gradle
To use the gradle dependency, add this to build.gradle:
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'uk.co.samuelwall:material-tap-target-prompt:1.9.3'
}
```
Supports minSdkVersion 7
# Usage
Basic usage is shown below with more [examples](https://github.com/sjwall/MaterialTapTargetPrompt/tree/master/sample/src/main/java/uk/co/samuelwall/materialtaptargetprompt/sample) in the sample app:

```java
new MaterialTapTargetPrompt.Builder(MainActivity.this)
        .setTarget(findViewById(R.id.fab))
        .setPrimaryText("Send your first email")
        .setSecondaryText("Tap the envelop to start composing your first email")
        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
        {
            @Override
            public void onHidePrompt(MotionEvent event, boolean tappedTarget)
            {
                //Do something such as storing a value so that this prompt is never shown again
            }

            @Override
            public void onHidePromptComplete()
            {

            }
        })
        .show();
```

# License
    Copyright (C) 2016 Samuel Wall

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




[1]: https://www.google.com/design/spec/growth-communications/feature-discovery.html#feature-discovery-design
