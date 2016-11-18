# Change Log

This project adheres to [Semantic Versioning](http://semver.org/).

## v1.6.1 (2016-11-18)

### Bug Fixes

* Fixed prompt being shown behind ActionMode (removed drawing behind DrawerLayout)

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
