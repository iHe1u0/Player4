#pragma once

#include <android/log.h>

#define TAG "native_ffmpeg"

#define LOG_V(...) __android_log_print(ANDROID_LOG_VERBOSE,TAG, __VA_ARGS__)
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG , TAG, __VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO  , TAG, __VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN  , TAG, __VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR  ,TAG, __VA_ARGS__)

#define LOGV  LOG_V
#define LOGD  LOG_D
#define LOGI  LOG_I
#define LOGW  LOG_W
#define LOGE  LOG_E