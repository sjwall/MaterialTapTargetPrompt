# Material Tap Target Prompt
A Tap Target implementation in Android based on Material Design Onboarding guidelines. For more information on tap targets check out the [guidelines][1].

![Example](http://i.imgur.com/Ei7iAcn.png?1)

# Gradle
To use the gradle dependency, add this to build.gradle:
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'uk.co.samuelwall:material-tap-target-prompt:1.1.2'
}
```

# Usage
Basic usage is shown below with more examples in the sample app:

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
