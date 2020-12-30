# UP-lib

This is a library that can be used by an end user application to receive notifications from any unified push provider.

# Install
![Release](https://jitpack.io/v/UnifiedPush/UP-lib.svg)

Artifacts can be retrieved from the jitpack repository.

Add the jitpack repo to the **project level** build.gradle:
```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to the **app** build.gradle. Replace {VERSION} with the release you wish to use
```
dependencies {
    // ...
    implementation 'com.github.UnifiedPush:UP-lib:{VERSION}'
}
```

# For FCM to work
* Add `classpath 'com.google.gms:google-services:4.3.4'` to you project level build.gradle.
* Add `id 'com.google.gms.google-services'` to your app level build.gradle.
* Add the google-services.json file from firebase to your app directory.
* Add the actions `org.unifiedpush.android.distributor.REGISTER` and `org.unifiedpush.android.distributor.UNREGISTER` to your receiver on the manifest.

For instance, [here](https://github.com/UnifiedPush/UP-example/commit/ab076ba450f9c90c9024c19cf9ed4ab6d52ca409) is the commit doing the migration from the main version to the fcm-added version on the example application.
