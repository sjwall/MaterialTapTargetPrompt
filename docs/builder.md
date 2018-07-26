---
layout: home
---

# Builder

The default [Builder](javadocs/uk/co/samuelwall/materialtaptargetprompt/MaterialTapTargetPrompt.Builder.html) implementation in MaterialTapTargetPrompt extends [PromptOptions](javadocs/uk/co/samuelwall/materialtaptargetprompt/extras/PromptOptions.html) which provides the common functionality for creating a tap target.
The [PromptOptions](javadocs/uk/co/samuelwall/materialtaptargetprompt/extras/PromptOptions.html) class can be extended to provide custom functionality. The custom builder below stores `true` for the supplied key in the shared preferences when the prompt is shown and will only show the prompt if that key value in the shared preferences is false.

```java
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import uk.co.samuelwall.materialtaptargetprompt.ActivityResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.ResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;

/**
 * {@link PromptOptions} implementation that only shows the 
 * prompt if it hasn't been shown before.
 */
public class CustomPromptBuilder extends PromptOptions<CustomPromptBuilder>
{
    /**
     * The key to use in the shared preferences to check if the
     * prompt has already been shown.
     */
    @Nullable private String key;

    /**
     * Constructor.
     *
     * @param activity The activity to use to find resources.
     */
    public CustomPromptBuilder(final @NonNull Activity activity)
    {
        this(new ActivityResourceFinder(activity));
    }

    /**
     * Constructor.
     *
     * @param resourceFinder The resource finder implementation 
     *  to use to find resources.
     */
    public CustomPromptBuilder(final @NonNull ResourceFinder resourceFinder)
    {
        super(resourceFinder);
    }

    /**
     * Set the key to use in the shared preferences.
     * 
     * @param key Preferences key.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @NonNull
    public CustomPromptBuilder setPreferenceKey(@Nullable final String key)
    {
        this.key = key;
        return this;
    }

    @Nullable
    @Override
    public MaterialTapTargetPrompt create()
    {
        final SharedPreferences sharedPreferences = this.getResourceFinder()
            .getContext()
            .getSharedPreferences("preferences", 0);
        MaterialTapTargetPrompt prompt = null;
        // Create the prompt if key is not set or prompt hasn't already been shown
        if (this.key == null || !sharedPreferences.getBoolean(this.key, false))
        {
            prompt = super.create();
            // Set the prompt as shown if the prompt has been created and key has been set
            if (prompt != null && this.key != null)
            {
                sharedPreferences.edit().putBoolean(this.key, true).apply();
            }
        }
        return prompt;
    }
}
``` 

The custom builder can then be used to create the prompt:

```java
new CustomPromptBuilder(getActivity())
    .setPreferenceKey("prompt_key")
    .setTarget(R.id.target_view)
    .setPrimaryText("primary text")
    .show()
```
