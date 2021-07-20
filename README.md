# Feeling-And-Healing_Mental-health-Application
An Android application that helps people who suffers from mental illness by taking quiz, talking to them via chat bot and suggesting them exercices.

## Technologies used
* Firebase
* DialogFlow
* SQlite Database

## Highlights
* Material design
* Streaming Music
* lottie animations
* Graph implementation
* Permission handling with Dexter
* Splash screen with motion layout
* Blogs/Articles about mental health
* Music stops when audio focus changes
* in-app web browser with chrome custom tabs
* Storing data of every users in SQlite databse
* Three different tests for different mental health problems
* Firebase for authentication of user with email id or google
* Implementation of chat bot user can talk to chat bot about mental health

## Chat-bot
* Created chat bot with dialogFlow to provide better treatment to patient, you have to put your credential.json file into raw folder.

## News and Blogs API
* Fetching news from NEWSAPI with thier own library.
* you can add their library by
```
implementation 'com.github.KwabenBerko:News-API-Java:1.0.0'
```
adding this line in app level gradle file and following line in project level gradle file
```
allprojects {
    repositories {
        ......
        maven { url 'https://jitpack.io' }
    }
}
```

## GraphView
```
implementation 'com.jjoe64:graphview:4.2.2'
```

## Audio Visulizer
```
implementation 'com.gauravk.audiovisualizer:audiovisualizer:0.9.2'
```

## Chrome custom tabs
```
implementation "androidx.browser:browser:1.3.0"
```

## Demo
https://youtu.be/k_BsoPZHNuQ
