---
layout: home
---

# Text

The text that the prompt displays is rendered using the [PromptText](javadocs/uk/co/samuelwall/materialtaptargetprompt/extras/PromptText.html) class. 
For most use cases it should not be necessary to extend this calls as it supports [SpannableStringBuilder](https://developer.android.com/reference/android/text/SpannableStringBuilder) which allows for text content and markup to be changed:

```java
SpannableStringBuilder secondaryText = new SpannableStringBuilder(
    "Tap the envelope to start composing your first email");
secondaryText.setSpan(
    new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)),
    8, 15, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

SpannableStringBuilder primaryText = new SpannableStringBuilder(
    "Send your first email");
primaryText.setSpan(
    new BackgroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 
    0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

builder.setPrimaryText(primaryText)
    .setSecondaryText(secondaryText);
```

To customise the text placement extend the [PromptText](javadocs/uk/co/samuelwall/materialtaptargetprompt/extras/PromptText.html) class and set it to be used in the builder by calling `builder.setPromptText()`
