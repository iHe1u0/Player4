#include <jni.h>
#include <string>
#include "mp3_player.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_cc_imorning_player_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_cc_imorning_player_player_audio_mp3_Mp3Player_playAudio(JNIEnv *env, jobject instance,
                                                             jstring path) {
    const char *fp = env->GetStringUTFChars(path, nullptr);
    auto mp3Player = new mp3_player(env, instance, fp);
    return mp3Player->play();
}