## View

![FAB Example](assets/example_FAB.png)

Any view can be targeted by either supplying its id or the <a href="https://developer.android.com/reference/android/view/View.html" target="blank">view</a> object.

Id:

```java
new MaterialTapTargetPrompt.Builder(this)
        .setTarget(R.id.fab)
        .setPrimaryText("primaryText")
        .show();
```

View object:

```java
new MaterialTapTargetPrompt.Builder(this)
        .setTarget(findViewById(R.id.fab))
        .setPrimaryText("primaryText")
        .show();
```
