WhoThat - Android Wear
===================================

https://github.com/orenyk/WhoThat

Modify this sample https://github.com/googlesamples/android-WearSpeakerSample to:
1. Record audio (currently 8,000 KHz PAM file)
2. Send the file to Migrosoft API using https://github.com/Microsoft/Cognitive-SpeakerRecognition-Android
    - https://www.microsoft.com/cognitive-services/en-us/Sdk-Sample
    - API Key: 41c331eb960d4c5599f599b2dc00e4c1 (10,000 transactions per month, 20 per minute.) Speaker Recognition - Preview, login: lec_maj@yahoo.com
    - https://www.microsoft.com/cognitive-services/en-us/speaker-recognition-api/documentation
    - Speaker verification: https://dev.projectoxford.ai/docs/services/563309b6778daf02acc0a508/operations/563309b7778daf06340c9652
3.

API:
- Create Enrolment or Create Profile?
- Get List of IDProfiles
- ID and provide list of profiles and sample audio to verify against
-

PCM 16bit mono, wrap in WAV -- http://stackoverflow.com/questions/27826593/androidcreating-wave-file-using-raw-pcm-the-wave-file-does-not-play
https://developer.android.com/guide/appendix/media-formats.html

Another app: https://github.com/icechen1/BlackBox, works in background on phone

A sample that shows how you can record voice using the microphone on a wearable and
play the recorded voice or an mp3 file, if the wearable device has a built-in speaker.

This app is standalone and doesn't have any companion phone app so you need to install this directly on your watch (using "adb").

Based on: https://github.com/googlesamples/android-WearSpeakerSample


Pre-requisites
--------------

- Android SDK v24
- Android Build Tools v24.0.1
- Android Support Repository

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

