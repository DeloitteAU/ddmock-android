# DD Mock

An API mocking library for Android.

## Pre-requisite

App must be using OkHttp for networking

## Getting started

[![](https://jitpack.io/v/DeloitteDigitalAPAC/ddmock-android.svg)](https://jitpack.io/#DeloitteDigitalAPAC/ddmock-android)

1. In your project `build.gradle` file:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. In your `build.gradle` file:

If the project is using AndroidX:

```groovy
debugImplementation 'com.github.DeloitteDigitalAPAC.ddmock-android:ddmock:[version]'
releaseImplementation 'com.github.DeloitteDigitalAPAC.ddmock-android:ddmock-no-op:[version]'
```

If the project is using Support libraries:

```groovy
debugImplementation 'com.github.DeloitteDigitalAPAC.ddmock-android:ddmock-support:[version]'
releaseImplementation 'com.github.DeloitteDigitalAPAC.ddmock-android:ddmock-no-op-support:[version]'
```

3. In your `Application` class:

```java
class ExampleApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    DDMock.install(this)
    // Other app init code...
  }
}
```

4. Add `MockInterceptor` to OkHttpClient

```java
val clientBuilder = OkHttpClient().newBuilder()
  .addInterceptor(MockInterceptor())
  // Other configurations...
  .build()
```


## Mock API files

* All API mock files are stored under __/assets/mockfiles__ and are mapped based on the __endpoint path__ and __HTTP method__.
  * e.g. login mock response file for endpoint __POST__ BASE_URL/__mobile-api/v1/auth/login__ should be stored under __mobile-api/v1/auth/login/post__
* For dynamic endpoint url, create directories with __{__ and __}__ for every replacement blocks and parameters
  * e.g. mock files for __GET__ BASE_URL/__mobile-api/v1/users/{usersId}__ should be stored under __mobile-api/v1/users/{usersId}/get__
  * see `sample`
* All mock files need to be JSON files
* There can be more than one mock file stored under each endpoint path
  * By default, the first file listed (alphabetically ordered) under each endpoint path is selected as the mock response

## FAQs

* Can we only mock some endpoints, while the rest still calls the actual API?
  * Yes. If there are no mock files to return as a response, the app will call the actual API configured in RetrofitBuilder
