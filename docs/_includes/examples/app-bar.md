## App Bar

![App Bar Example](assets/example_appbar.png)

An item that is showing in the app bar with an id and not in the overflow drop down menu can be targeted.

In the menu xml file the item will look something like below with the id set:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/action_search"
        android:icon="@drawable/ic_search"
        android:title="Search"
        app:showAsAction="always" />
    <item
        android:id="@+id/action_settings"
        android:orderInCategory="100"
        android:title="@string/action_settings"
        app:showAsAction="never" />
</menu>
```

The settings item can not be targeted (as it is in the overflow).
To target the search item supply the id to the `setTarget` method:

```java
builder.setTarget(R.id.action_search);
```

To display the target correctly set the displayed icon or set the background to transparent:

```java
builder.setIcon(R.drawable.ic_search);
```

Full example:
```java
new MaterialTapTargetPrompt.Builder(this)
    .setTarget(R.id.action_search)
    .setPrimaryText("Primary text")
    .setIcon(R.drawable.ic_search)
    .show();
```
