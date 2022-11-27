#pragma once

#include <jni.h>
#include <string>

class mp3_player {
public:
    mp3_player(JNIEnv *env, const jobject &instance, const std::string &path);

    int play(const float &pos = 0.0);

private:
    JNIEnv *env;
    jobject instance;
    // audio file path
    std::string fp;
    // 输出采样率
    int audio_sample_rate = 44100;
    int app_exit(int error);
};

