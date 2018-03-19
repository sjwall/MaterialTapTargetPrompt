## Recycler View Card

![Card Example](assets/example_card.png)

First get the card view.
In the example below a <a href="https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager.html" target="_blank">LinearLayoutManager</a> is being used on a <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html" target="_blank">RecyclerView</a>:
```java
LinearLayout card = (LinearLayout) mLinearLayoutManager.findViewByPosition(0);
```

Once the card view has been obtained, find the associated <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html" target="_blank">ViewHolder</a>. Replace the `CardAdapter.ViewHolder` class with your own.

```java
CardAdapter.ViewHolder viewHolder = (CardAdapter.ViewHolder) mRecyclerView.getChildViewHolder(card);
```

With the ViewHolder it's child views can now be targeted:

```java
builder.setTarget(viewHolder.mImageView);
```

An additional step is to clip the prompt to the card views bounds so that it is not drawn outside it:

```java
builder.setClipToView(card.getChildAt(0));
```

Full example:
```java
LinearLayout card = (LinearLayout) mLinearLayoutManager.findViewByPosition(0);
// Check that the view exists for the item
if (card != null)
{
    CardAdapter.ViewHolder viewHolder = (CardAdapter.ViewHolder) mRecyclerView.getChildViewHolder(card);
    new MaterialTapTargetPrompt.Builder(this)
            .setTarget(viewHolder.mImageView)
            .setClipToView(card.getChildAt(0))
            .setPrimaryText(R.string.example_card_card_title)
            .setSecondaryText(R.string.example_card_card_description)
            .show();
}
```
