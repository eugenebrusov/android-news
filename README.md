# Native News Android app
News is a native Android app that brings together news, popular stories and conversations around any interest or passion. It's designed and built in strict compliance with Material Design.

All the data provided by [The Guardian - Open Platform](https://open-platform.theguardian.com)

Introduction
------------
This sample demonstrates native Android app implementation in Kotlin using Android Architecture Components. It also includes following components:
- [The Paging Library](https://developer.android.com/topic/libraries/architecture/paging/) that helps to load news list gradually and gracefully by fetching combined data from on-device database and `The Guardian API`
- [Room](https://developer.android.com/topic/libraries/architecture/room) that helps to create a cache of news data on a local database
- [Retrofit](http://square.github.io/retrofit/) that consumes remote `The Guardian API` 
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) that updates UI in lifecycle-aware way
- [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/) that binds news list and news details to LiveData using a declarative format rather than programmatically

Pre-requisites
--------------

- Android Studio 3.+
- Gradle 3.+
- API key can be taken from https://bonobo.capi.gutools.co.uk/register/developer

Getting started
---------------

Check out branch master and then open the `News` directory in Android Studio. 

It's required to obtain `The Guardian API` key. It can be obtained from https://bonobo.capi.gutools.co.uk/register/developer. Once received put it to `API_KEY` property in Constants.kt file.

Support
-------

If you've found an error in this sample, please file an issue: https://github.com/eugenebrusov/android-news/issues

License
-------
```
Copyright (c) 2018 Eugene Brusov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```