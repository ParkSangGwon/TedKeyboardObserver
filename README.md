 
# What is TedKeyboardObserver?
- Sometime we want to know keyboard's visibility.
(When keyboard shown, change some layout or do something. etc..)
- But Android SDK doesn't have any observer.
- `TedKeyboardObserver` provide keyboard visibility

<br/><br/>



## Demo
- You can observe keyboard status like this
<br/><br/>![Screenshot](https://github.com/ParkSangGwon/TedKeyboardObserver/blob/master/art/demo.gif?raw=true)
<br/><br/><br/><br/>- Keyboard show / hide
<br/><br/>![Screenshot](https://github.com/ParkSangGwon/TedKeyboardObserver/blob/master/art/screenshot1.jpg?raw=true)  ![Screenshot](https://github.com/ParkSangGwon/TedKeyboardObserver/blob/master/art/screenshot2.jpg?raw=true)    

           
## Setup


### Gradle
[ ![Download](https://api.bintray.com/packages/tkdrnjs0912/maven/tedkeyboardobserver/images/download.svg) ](https://bintray.com/tkdrnjs0912/maven/tedkeyboardobserver/_latestVersion)
```gradle
dependencies {
    implementation 'gun0912.ted:tedkeyboardobserver:x.y.z'
    //implementation 'gun0912.ted:tedkeyboardobserver:1.0.0-alpha2'
}

```

If you think this library is useful, please press star button at upside. 
<br/>
<img src="https://phaser.io/content/news/2015/09/10000-stars.png" width="200">
<br/><br/>



## How to use
`TedKeyboardObserver` support Listener and RxJava style


### RxJava
- You don't need dispose this observable. When activity destroy, `TedRxKeyboardObserver` will call `onComplete()`
#### Java
```java
       new TedRxKeyboardObserver(this)
                .listen()
                .subscribe(isShow -> {
                            // do something
                        }, Throwable::printStackTrace);
```
#### Kotlin
```kotlin
       
        TedRxKeyboardObserver(this)
            .listen()
            .subscribe({ isShow -> // do something }
                , { throwable -> throwable.printStackTrace() })
```
<br/><br/>
### Listener
#### Java
```java
      new TedKeyboardObserver(this)
                .listen(isShow -> {
                    // do something
                });

```
#### Kotlin
```kotlin
      TedKeyboardObserver(this)
               .listen { isShow ->
                    // do something
               }
```


<br/>


<br/><br/>


## License 
 ```code
Copyright 2019 Ted Park

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.```
