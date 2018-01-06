# Shapes

The default shape is a circle but any other shape can be rendered by extending the [PromptBackground](https://github.com/sjwall/MaterialTapTargetPrompt/blob/master/library/src/main/java/uk/co/samuelwall/materialtaptargetprompt/extras/PromptBackground.java) and [PromptFocal](https://github.com/sjwall/MaterialTapTargetPrompt/blob/master/library/src/main/java/uk/co/samuelwall/materialtaptargetprompt/extras/PromptFocal.java) classes.
A rectangle implementation is provided through the [RectanglePromptBackground](https://github.com/sjwall/MaterialTapTargetPrompt/blob/master/library/src/main/java/uk/co/samuelwall/materialtaptargetprompt/extras/backgrounds/RectanglePromptBackground.java) and [RectanglePromptFocal](https://github.com/sjwall/MaterialTapTargetPrompt/blob/master/library/src/main/java/uk/co/samuelwall/materialtaptargetprompt/extras/focals/RectanglePromptFocal.java) implementations.


```java
new MaterialTapTargetPrompt.Builder(this)
                .setTarget(view)
                .setPrimaryText("Different shapes")
                .setSecondaryText("Extend PromptFocal or PromptBackground to change the shapes")
                .setPromptBackground(new RectanglePromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .show();
```

![Rectangle Example](docs/assets/example_rectangle.png)
