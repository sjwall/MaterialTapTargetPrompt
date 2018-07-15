---
layout: home
---

# FAQ

## The prompt is too wide

Use [setMaxTextWidth](javadocs/uk/co/samuelwall/materialtaptargetprompt/extras/PromptOptions.html#setMaxTextWidth-float-) to set the maximum width that the primary and secondary text can be.
The background is size is calculated based on the text size.
Setting the maximum width for the text will then alter the background calculated size

## Show from a life-cycle event

If a view has not yet been created the library won't be able to find it. To get around this post a runnable which creates the prompt:

```java
new android.os.Handler(Looper.getMainLooper()).post(new Runnable()
{
    @Override
    public void run()
    {
        // Create the prompt here
    }
})
```
