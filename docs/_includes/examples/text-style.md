## Primary & secondary text style

A <a href="https://developer.android.com/reference/android/text/SpannableStringBuilder.html" target="_blank">SpannableStringBuilder</a> can be used to customise how text is rendered.

```java
SpannableStringBuilder primaryText = new SpannableStringBuilder(
    "Send your first email");
BackgroundColorSpan backgroundColour = BackgroundColorSpan(
    ContextCompat.getColor(this, R.color.colorAccent));
primaryText.setSpan(backgroundColour, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

SpannableStringBuilder secondaryText = new SpannableStringBuilder(
    "Tap the envelope to start composing your first email");
ForegroundColorSpan foregroundColour = new ForegroundColorSpan(
    ContextCompat.getColor(this, R.color.colorAccent));
secondaryText.setSpan(foregroundColour, 8, 15, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

new MaterialTapTargetPrompt.Builder(this)
    .setTarget(target)
    .setPrimaryText(primaryText)
    .setSecondaryText(secondaryText)
    .show();
```
