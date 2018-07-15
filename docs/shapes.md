---
layout: home
---

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

![Rectangle Example](assets/example_rectangle.png)

# Dimmed background

The background could be dimmed like so:

```java
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground;

/**
 * Prompt background implementation that darkens behind the circle background.
 */
public class DimmedPromptBackground extends CirclePromptBackground
{
    @NonNull private RectF dimBounds = new RectF();
    @NonNull private Paint dimPaint;

    public DimmedPromptBackground()
    {
        dimPaint = new Paint();
        dimPaint.setColor(Color.BLACK);
    }

    @Override
    public void prepare(@NonNull final PromptOptions options, final boolean clipToBounds, @NonNull Rect clipBounds)
    {
        super.prepare(options, clipToBounds, clipBounds);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        // Set the bounds to display as dimmed to the screen bounds
        dimBounds.set(0, 0, metrics.widthPixels, metrics.heightPixels);
    }

    @Override
    public void update(@NonNull final PromptOptions options, float revealModifier, float alphaModifier)
    {
        super.update(options, revealModifier, alphaModifier);
        // Allow for the dimmed background to fade in and out
        this.dimPaint.setAlpha((int) (200 * alphaModifier));
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        // Draw the dimmed background
        canvas.drawRect(this.dimBounds, this.dimPaint);
        // Draw the background
        super.draw(canvas);
    }
}
```
