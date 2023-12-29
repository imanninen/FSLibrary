# File system library on kotlin
I used `kotlinc lib/src/main/kotlin/fslibrary/Library.kt  lib/src/main/kotlin/fslibrary/Utils.kt -d FileSysytemLib.jar` command to compile my code to kotlin library.

[Here](https://github.com/imanninen/FSLibrary/blob/main/FileSysytemLib.jar) is a library file. To use this library in
your project you need to add .jar file into `resources` directory or some other, and also add to
`gradle.build.kts` file into dependencies this:
```kotlin
implementation(fileTree("src/main/resources/"))
```
And now you can use this library.


All tests are implemented using jetBrains plugin for JUnit tests. 
All tests I run on spesific directory, to don't delete something important accidentally. 
