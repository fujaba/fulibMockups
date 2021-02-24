# fulibMockups v0.0.1

## New Features

+ Added the `Element`, `Content`, `Page` and `WebApp` classes.

# fulibMockups v0.1.0

## New Features

+ Added the `MockupTools` class from fulibScenarios. #1
+ Added separate dump methods for different types. #6

## Improvements

* Improve file render performance. #2
* Mockups are now generated in a thread-safe manner. #3
* Mockups are now generated based on the file name and other files in the directory. #4
* `null` values in tables are now rendered as `N/A`. #5

# fulibMockups v0.2.0

## New Features

+ Added the `MockupTools.dumpToString` method for use by `![<objects...>](<file-name>.txt)`.
+ Added the `Service` class.
+ Added support for `action`s.
+ Added support for `icards`.
+ Added support for `tables`.

# fulibMockups v0.3.0

## New Features

+ Added the new `UI` model and rendering functionality. #10 #11
+ Overhauled the old `Element` model with better type safety via the `Node` class. #11
+ Added `Line` and `Section` as aliases for `Node`. #11

# fulibMockups v0.3.1

## Bugfixes

* Rendering no longer ignores description and children of some `UI` elements. #12

# fulibMockups v0.4.0

## General

* Updated external dependencies.

## New Features

+ Added the `listeners()` method to all model classes.
