# Change Log

This project adheres to [Semantic Versioning](http://semver.org/).

## v3.3.2 (2021-11-08

### Bug Fixes

* Clip text using background path instead of via Xfermode to fix weird UI issues ([#202](https://github.com/sjwall/MaterialTapTargetPrompt/pull/202) [#201](https://github.com/sjwall/MaterialTapTargetPrompt/issues/201)) ([@peterdk](https://github.com/peterdk)))

## v3.3.1 (2021-11-06)

### Bug Fixes

* Add Xfer mode to paint, so the text pixels are only drawn when the background reaches them ([#200](https://github.com/sjwall/MaterialTapTargetPrompt/pull/200) [#146](https://github.com/sjwall/MaterialTapTargetPrompt/issues/146)) ([@danilodanicomendes](https://github.com/danilodanicomendes)))

## v3.3.0 (2021-07-02)

### Bug Fixes

* Fix NPE when setting Typeface with style 0 ([#188](https://github.com/sjwall/MaterialTapTargetPrompt/issues/188)) ([@akwizgran](https://github.com/akwizgran)))

### Other

* Target Android SDK 30

## v3.2.0 (2021-05-18)

### Testing

* PromptView and PromptView.mPromptOptions are visible for testing ([#193](https://github.com/sjwall/MaterialTapTargetPrompt/pull/193)) ([@hdani9307](https://github.com/hdani9307))

## v3.1.1 (2021-05-03)

### Bug Fixes

* Fix NPE on animation start ([#164](https://github.com/sjwall/MaterialTapTargetPrompt/issues/186))
* Fix NPE on `CircleFocal` path ([#164](https://github.com/sjwall/MaterialTapTargetPrompt/issues/190))
* Fix `PromptView` content description not being set

## v3.1.0 (2020-12-17)

### Features

* Add `setIgnoreStatusBar` to `PromptOptions` to allow configuring if the prompt displays over the status bar. Default is false. ([#183](https://github.com/sjwall/MaterialTapTargetPrompt/pull/183)) ([@alexeyosminin](https://github.com/alexeyosminin))

### Bug Fixes

* Fix `FullscreenPromptBackground` not displaying correctly on displays with cutouts ([#182](https://github.com/sjwall/MaterialTapTargetPrompt/pull/182)) ([@Iljo](https://github.com/Iljo))

## v3.0.1 (2020-10-23)

* Upgraded dependencies to be not version specific

### Bug Fixes

*  Fix FragmentContainerView as parent ([#180](https://github.com/sjwall/MaterialTapTargetPrompt/pull/180)) ([@JMavrelos](https://github.com/JMavrelos))

## v3.0.0 (2019-08-18)

* Migrate to [AndroidX](https://developer.android.com/jetpack/androidx) ([#164](https://github.com/sjwall/MaterialTapTargetPrompt/issues/pull/164)) ([@virengujariya](https://github.com/virengujariya), [@liaowng](https://github.com/liaowng))

### Removed

* Remove android.app.Fragment and android.app.DialogFragment builder constructors.
These Fragments can still be used by passing `getActivity()` and `getDialog()` respectively.

## v2.15.0 (2019-06-23)

### Features

* Added `setContentDescription` to override content description for accessibility ([#157](https://github.com/sjwall/MaterialTapTargetPrompt/issues/pull/157)) ([@mudar](https://github.com/mudar))

### Bug Fixes

* Fix index out of bounds exception in sequence showFromIndex ([#161](https://github.com/sjwall/MaterialTapTargetPrompt/issues/pull/161)) ([@radgospodinov](https://github.com/radgospodinov))

## v2.14.0 (2018-10-26)

### Features

* Added `ImmersiveModeCompatPromptBackground` ([#135](https://github.com/sjwall/MaterialTapTargetPrompt/issues/pull/135))

### Bug Fixes

* Fix null pointer exception in `SupportFragmentResourceFinder` ([#137](https://github.com/sjwall/MaterialTapTargetPrompt/issues/137))

## v2.13.0 (2018-10-03)

### Features

* Added `STATE_BACK_BUTTON_PRESSED` state which is emitted before `STATE_NON_FOCAL_PRESSED` when the system back button is pressed ([#138](https://github.com/sjwall/MaterialTapTargetPrompt/issues/138))

## v2.12.4 (2018-09-24)

### Bug Fixes

* Correct target rendered position with scroll ([#136](https://github.com/sjwall/MaterialTapTargetPrompt/issues/136))
* Fix animation trying to continue after view removed

## v2.12.3 (2018-09-10)

### Bug Fixes

* Fixed prompt being added to wrong view for fragments
* Fixed prompt text being laid out incorrectly when clipping bounds are empty

## v2.12.2 (2018-09-09)

### Bug Fixes

* Fixed prompt clipping incorrectly in support dialog fragment
* Fixed prompt increasing parent view size

## v2.12.1 (2018-07-25)

### Bug Fixes

* Fixed NPE in TalkBack accessibility ([#131](https://github.com/sjwall/MaterialTapTargetPrompt/issues/131))

## v2.12.0 (2018-07-14)

### Features

* Added TalkBack support ([#127](https://github.com/sjwall/MaterialTapTargetPrompt/issues/127))

## v2.11.0 (2018-07-08)

### Features

* Added `finish` to `MaterialTapTargetSequence` to cancel a sequence mid way with a finish action
* Added `dismiss` to `MaterialTapTargetSequence` to cancel a sequence mid way with a dismiss action
* Added `showFromIndex` to `MaterialTapTargetSequence` to start or continue a sequence from any prompt in it

## v2.10.0 (2018-06-30)

### Features

* Added `showFor` support to `MaterialTapTargetSequence`
* Added support for passing `MaterialTapTargetPrompt.Builder` or `PromptOptions` to `MaterialTapTargetSequence`

### Improvements

* Improved circle background position calculations

### Other

* Added examples written in Kotlin

## v2.9.0 (2018-05-19)

### Features

* Added `MaterialTapTargetSequence` for showing prompts one after another ([#118](https://github.com/sjwall/MaterialTapTargetPrompt/issues/118))

### Bug Fixes

* Fixed incorrect prompt size calculation ([#116](https://github.com/sjwall/MaterialTapTargetPrompt/issues/116))
* Fixed incorrect prompt size calculation when target is wider than the text

## v2.8.0 (2018-05-06)

### Features

* Added support library as a `CompileOnly` dependency ([#114](https://github.com/sjwall/MaterialTapTargetPrompt/issues/114))
* Added support library fragment constructors to `Builder` ([#114](https://github.com/sjwall/MaterialTapTargetPrompt/issues/114))

### Bug Fixes

* Fixed memory leak caused by animation maintaining view reference ([#113](https://github.com/sjwall/MaterialTapTargetPrompt/issues/113))

### Deprecated

* Deprecated Android system fragment constructors in `Builder` ([#114](https://github.com/sjwall/MaterialTapTargetPrompt/issues/114))

## v2.7.0 (2018-02-17)

### Features

* Added showFor to show prompt for a time limit

## v2.6.1 (2018-02-04)

### Bug Fixes

* Fixed UnsupportedOperationException caused by clipping to path in Android SDK < 18 ([#101](https://github.com/sjwall/MaterialTapTargetPrompt/issues/101))

## v2.6.0 (2018-01-11)

### Features

* Added `getPath` to `PromptFocal` to get the clip path for the prompt focal ([#99](https://github.com/sjwall/MaterialTapTargetPrompt/pull/95))
* Transparent focal colour is now supported and no longer renders black ([#99](https://github.com/sjwall/MaterialTapTargetPrompt/pull/95))

## v2.5.2 (2017-12-20)

### Bug Fixes

* Fixed prompt not updating correctly when target view moves ([#95](https://github.com/sjwall/MaterialTapTargetPrompt/pull/95))

## v2.5.1 (2017-12-12)

### Bug Fixes

* Reverted changes that fixed prompt positioning not updating correctly when the target moves but caused the background to not be shown

## v2.5.0 (2017-11-30)

### Features

* Added nullable annotations ([#92](https://github.com/sjwall/MaterialTapTargetPrompt/pull/92))
* Added Android SDK 27 support ([#91](https://github.com/sjwall/MaterialTapTargetPrompt/pull/91))

### Bug Fixes

* Fixed prompt not updating correctly when target view moves

## v2.4.1 (2017-11-14)

### Bug Fixes

* Fixed invalid state when attempting to show prompt that is dismissing ([#89](https://github.com/sjwall/MaterialTapTargetPrompt/pull/89))

## v2.4.0 (2017-11-05)

### Features

* Added Prompt.getState() to get the current prompt state. ([#87](https://github.com/sjwall/MaterialTapTargetPrompt/pull/87))
* Added prompt state STATE_NOT_SHOWN for the base state before prompt is shown. ([#87](https://github.com/sjwall/MaterialTapTargetPrompt/pull/87))

### Bug Fixes

* Fixed null pointer exception when attempting to dismiss prompt before it has been shown. ([#87](https://github.com/sjwall/MaterialTapTargetPrompt/pull/87))

## v2.3.0 (2017-10-24)

### Features

* Added fullscreen prompt background. ([#85](https://github.com/sjwall/MaterialTapTargetPrompt/pull/85))

## v2.2.0 (2017-10-14)

### Features

* Added PromptBackground, PromptFocal and PromptText to enable overriding the default implementation.
* Added RectanglePromptBackground, RectanglePromptFocal, CirclePromptBackground and CirclePromptFocal background and focal implementations.
* Added PromptOptions class as base for Builder class.

### Other

* Expanded unit testing.

## v2.1.0 (2017-09-06)

### Features

* Added support for primary and secondary text being CharSequence. ([#82](https://github.com/sjwall/MaterialTapTargetPrompt/pull/82))
* Added STATE_FINISHING for state change after the finish method has been called.
* Added STATE_NON_FOCAL_PRESSED for state change after the user has pressed the prompt somewhere other than the target or the system back button has been pressed.

### Bug Fixes

* Fixed STATE_DISMISSING state change incorrectly occurring when auto dismiss is disabled. Fixes null pointer exception in sample. ([#82](https://github.com/sjwall/MaterialTapTargetPrompt/pull/82))

## v2.0.1 (2017-08-08)

### Bug Fixes

* Fix user being unable to leave Activity with back button if setAutoDismiss(false) ([#81](https://github.com/sjwall/MaterialTapTargetPrompt/pull/81))

## v2.0.0 (2017-08-07)

* Minimum sdk is now 14.
* Back button dismiss is now enabled by default.

### Bug Fixes

* Attributes are prefixed with mttp to be compatible with API 26.

### Removed

* `ResourceFinder.getWindow()`
* `OnHidePromptListener` - Replaced with `PromptStateChangeListener`.
* `OnHidePromptListener.onHidePrompt` - Replaced with `PromptStateChangeListener(MaterialTapTargetPrompt prompt, boolean state)` where the state is either `MaterialTapTargetPrompt.STATE_DISMISSING` or `MaterialTapTargetPrompt.STATE_FOCAL_PRESSED`.
* `OnHidePromptListener.onHidePromptComplete` - Replaced with `PromptStateChangeListener(MaterialTapTargetPrompt prompt, boolean state)` where the state is either `MaterialTapTargetPrompt.STATE_DISMISSED` or `MaterialTapTargetPrompt.STATE_FINISHED`.
*
* `builder.setBackgroundColourAlpha` - Alpha is taken from `builder.setBackgroundColour(int)`
* `builder.setBackgroundColourFromRes(int)` - Use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setFocalColourAlpha(int)` - Alpha value is taken from `builder.setFocalColour(int)`
* `builder.setFocalColourFromRes(int)` - Use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setFocalToTextPadding(float)` - Renamed to `setFocalPadding(float)`
* `builder.setFocalToTextPadding(int)` - Rename to `setFocalPadding(int)`
* `builder.setIconDrawableColourFilterFromRes(int)` - Use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setOnHidePromptListener(MaterialTapTargetPrompt.OnHidePromptListener` - Replaced with `builder.setPromptStateChangeListener(PromptStateChangeListener)`
* `builder.setPrimaryTextColourFromRes(int)` - Use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setSecondaryTextColourFromRes(int)` - Use a constructor with a theme for example `Builder(Activity, int)`

## v1.12.1 (2017-08-05)

### Bug Fixes

* Fixed prompt not working with window app bar

## v1.12.0 (2017-07-28)

### Bug Fixes

* Fixed incorrect positioning of prompt when target view gets removed from window.

### Deprecated

* `ResourceFinder.getWindow()` no longer required, will be removed in v2.0.0

## v1.11.0 (2017-07-02)

### Features

* Added `setBackButtonDismissEnabled(boolean)` to allow the system back button to dismiss the prompt.
* Added bottom sheet dialog fragment example.
* Added `PromptStateChangeListener` to listen for prompt being shown and hidden. Possible states are:
 * `STATE_REVEALING` - Prompt reveal animation is running.
 * `STATE_REVEALED` - Prompt reveal animation has finished and the prompt is displayed.
 * `STATE_FOCAL_PRESSED` - The prompt target has been pressed.
 * `STATE_FINISHED` - The prompt has been removed from view after the prompt target has been pressed.
 * `STATE_DISMISSING` - The prompt has been pressed somewhere other than the prompt target or the system back button has been pressed.
 * `STATE_DISMISSED` - The prompt has been removed from view after the prompt has either been pressed somewhere other than the prompt target or the system back button has been pressed.
* Replaced dependency on `Activity` with `ResourceFinder` interface. Now uses `ActivityResourceFinder` or `DialogResourceFinder`.
* Primary text is no longer required, either primary or secondary text must be set.

### Bug Fixes

* Fixed prompt not working correctly with dialogs by using `DialogResourceFinder`.

### Deprecated

* `OnHidePromptListener` - Replaced with `PromptStateChangeListener`.
* `OnHidePromptListener.onHidePrompt` - Replaced with `PromptStateChangeListener(MaterialTapTargetPrompt prompt, boolean state)` where the state is either `MaterialTapTargetPrompt.STATE_DISMISSING` or `MaterialTapTargetPrompt.STATE_FOCAL_PRESSED`.
* `OnHidePromptListener.onHidePromptComplete` - Replaced with `PromptStateChangeListener(MaterialTapTargetPrompt prompt, boolean state)` where the state is either `MaterialTapTargetPrompt.STATE_DISMISSED` or `MaterialTapTargetPrompt.STATE_FINISHED`.
*
* `builder.setBackgroundColourAlpha` - Alpha value will be taken from `builder.setBackgroundColour(int)` in v2.0.0
* `builder.setBackgroundColourFromRes(int)` - Will be removed in v2.0.0, use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setFocalColourAlpha(int)` - Alpha value will be taken from `builder.setFocalColour(int)` in v2.0.0
* `builder.setFocalColourFromRes(int)` - Will be removed in v2.0.0, use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setFocalToTextPadding(float)` - Renamed to `setFocalPadding(float)`
* `builder.setFocalToTextPadding(int)` - Rename to `setFocalPadding(int)`
* `builder.setIconDrawableColourFilterFromRes(int)` - Will be removed in v2.0.0, use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setOnHidePromptListener(MaterialTapTargetPrompt.OnHidePromptListener` - Replaced with `builder.setPromptStateChangeListener(PromptStateChangeListener)`
* `builder.setPrimaryTextColourFromRes(int)` - Will be removed in v2.0.0, use a constructor with a theme for example `Builder(Activity, int)`
* `builder.setSecondaryTextColourFromRes(int)` - Will be removed in v2.0.0, use a constructor with a theme for example `Builder(Activity, int)`

## v1.10.0 (2017-04-15)

### Features

* Added id to prompt view

## v1.9.5 (2017-04-03)

### Bug Fixes

* Fixed null text causing crash when layout direction is right to left

## v1.9.4 (2017-04-02)

### Bug Fixes

* Fixed right to left text positioning

### Other

* Improved text and background positioning calculations
* Added side nav target example


## v1.9.3 (2017-02-26)

### Bug Fixes

* Fixed right to left text drawing outside the background

## v1.9.2 (2017-02-19)

### Bug Fixes

* Added check for primary text layout being created before drawing

## v1.9.1 (2017-02-14)

### Bug Fixes

* Fixed background not covering text in certain scenarios

## v1.9 (2017-02-12)

### Features

* Prompts greater than 88dp from the top or bottom now use an optimized background

### Bug Fixes

* Fixed onHidePromptComplete event being triggered twice
* Fixed background being too small in certain use cases

## v1.8.3 (2017-01-31)

### Bug Fixes

* Fixed null target view not being handled gracefully

## v1.8.2 (2017-01-27)

### Bug Fixes

* Fixed point in background calculation

### Other

* Added card example

## v1.8.1 (2017-01-24)

### Bug Fixes

* Fixed background not showing when animations are disabled

## v1.8.0 (2017-01-21)

### Features

* Added following guidelines on centre positioned targets
* Added setting background colour alpha
* Added setting focal colour alpha

### Bug Fixes

* Fixed incorrect text positioning on centre positioned targets

## v1.7.0 (2017-01-07)

### Dialog style breaking changes

The fix for the target activity not having a view group as its first child view changed the default view that is clipped to.
This means that for the dialog activity sample the view that is clipped to needs to be set.

```java
builder.setClipToView(findViewById(R.id.dialog_view))
```

### Features

* Added ability to set the view that the prompt is clipped to
* Added app bar action icon examples

### Bug Fixes

* Fixed crash that occurred when target activity did not have a view group as its first child view

### Other

* Improved text position calculations
* Improved background radius calculations

## v1.6.3 (2016-11-29)

### Bug Fixes

* Fixed prompt icon sharing its state with other drawables
* Improved ActionMode example

## v1.6.2 (2016-11-21)

### Bug Fixes

* Fixed crash when running on Gingerbread and below that was introduced in v1.6.1

## v1.6.1 (2016-11-18)

### Bug Fixes

* Fixed prompt being shown behind ActionMode (removed drawing behind DrawerLayout)
* Added ActionMode example

## v1.6.0 (2016-11-06)

### Features

* Added RTL layout support
* Added methods to change text gravity
* Added disabling idle animation

## v1.5.0 (2016-10-25)

### Features

* Added method to change the view that is rendered as the target
* Improved dialog style sample

### Bug Fixes

* Fixed prompt text positioning not taking clip bounds into account

## v1.4.3 (2016-10-05)

### Bug Fixes

* Fixed clip bounds being incorrectly calculated on KitKat and lower

## v1.4.2 (2016-10-03)

### Bug Fixes

* Fixed status bar height being incorrectly applied to clip bounds

## v1.4.1 (2016-10-01)

### Bug Fixes

* Changed how status bar height is obtained
* Fixed clip bounds being incorrectly calculated

## v1.4.0 (2016-09-23)

### Features

* Added tinting icon drawable

### Bug Fixes

* Fixed icon drawable being incorrectly tinted

## v1.3.0 (2016-09-21)

### Features

* Added changing primary and secondary text typeface
* Reduced number of generated methods

## v1.2.1 (2016-09-03)

### Bug Fixes

* Removed incorrect delay in starting the prompt display animation

## v1.2.0 (2016-08-25)

### Features

* Added style attribute for controlling capturing focal touch events
* Added option to capture touch events outside prompt
* Added options to disable auto dismiss and auto finish prompt

## v1.1.4 (2016-08-21)

### Bug Fixes

* Fixed prompt view position not changing on target view position change
* Fixed null pointer exception in Builder.show when create returns null

## v1.1.3 (2016-06-26)

### Bug Fixes

* Fixed crash if secondary text not set
* Fixed incorrectly named method getTextSeparation, renamed to setTextSeparation

## v1.1.2 (2016-06-20)

### Bug Fixes

* Fixed incorrect text positioning when view is clipped

## v1.1.1 (2016-06-13)

### Bug Fixes

* Fixed incorrect margin use

## v1.1.0 (2016-06-12)

### Features

* Added loading prompt theme from style

### Bug Fixes

* Fixed prompt being drawn outside activity bounds

## v1.0.0 (2016-06-05)

* Initial release
