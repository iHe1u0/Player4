//
// Created by iMorning on 2022/10/27.
//
#include "mp3_player.h"
#include <logger.h>

#ifdef __cplusplus
extern "C" {
#endif
#include <libavutil/avutil.h>
#include <libavutil/avutil.h>
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>
#ifdef __cplusplus
};
#endif

mp3_player::mp3_player(JNIEnv *env, const jobject &instance, const std::string &path) {
    if (path.empty()) {
        return;
    }
    this->env = env;
    this->instance = instance;
    this->fp = path;
}

int mp3_player::play(const float &pos) {
    // 记录结果
    int result;
    // R1 Get file path
    const char *path = fp.c_str();
    // 注册组件
    // av_register_all();
    // R2 创建 AVFormatContext 上下文
    AVFormatContext *format_context = avformat_alloc_context();
    // R3 打开视频文件
    avformat_open_input(&format_context, path, nullptr, nullptr);
    // 查找视频文件的流信息
    result = avformat_find_stream_info(format_context, nullptr);
    if (result < 0) {
        return app_exit(result);
    }
    // 查找音频编码器
    int audio_stream_index = -1;
    for (int i = 0; i < format_context->nb_streams; i++) {
        // 匹配音频流
        if (format_context->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_index = i;
        }
    }
    // 没找到音频流
    if (audio_stream_index == -1) {
        return app_exit(result);
    }
    // 初始化音频编码器上下文
    AVCodecContext *audio_codec_context = avcodec_alloc_context3(nullptr);
    result = avcodec_parameters_to_context(audio_codec_context,
                                           format_context->streams[audio_stream_index]->codecpar);
    // 初始化音频编码器
    auto audio_codec = avcodec_find_decoder(audio_codec_context->codec_id);
    if (audio_codec == nullptr) {
        return app_exit(result);
    }
    // R4 打开视频解码器
    result = avcodec_open2(audio_codec_context, audio_codec, nullptr);
    if (result < 0) {
        return app_exit(result);
    }
    audio_sample_rate = audio_codec_context->sample_rate;
    LOG_I("采样率：%d", audio_codec_context->sample_rate);
    LOG_I("通道数: %d", audio_codec_context->channels);
    LOG_I("format: %d", audio_codec_context->get_format);
    // 音频重采样准备
    // R5 重采样上下文
    struct SwrContext *swr_context = swr_alloc();
    // 缓冲区
    auto resample_out_buffer = (uint8_t *) av_malloc(audio_codec_context->frame_size * 2 * 2);
    // 输出的声道布局 (双通道 立体音)
    auto out_channel_layout = AV_CH_LAYOUT_STEREO;
    // 输出采样位数 16位
    enum AVSampleFormat out_format = AVSampleFormat::AV_SAMPLE_FMT_S16;
    //swr_alloc_set_opts 将PCM源文件的采样格式转换为自己希望的采样格式
    swr_alloc_set_opts(swr_context,
                       out_channel_layout,
                       out_format,
                       audio_sample_rate,
                       audio_codec_context->channel_layout,
                       audio_codec_context->sample_fmt,
                       audio_codec_context->sample_rate,
                       0, nullptr);
    swr_init(swr_context);
    // 调用 Java 层创建 AudioTrack
    int out_channels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);
    jclass player_class = env->GetObjectClass(instance);
    jmethodID create_audio_track_method_id = env->GetMethodID(player_class,
                                                              "createAudioTrack",
                                                              "(II)V");
    env->CallVoidMethod(instance, create_audio_track_method_id, audio_sample_rate, out_channels);
    // 播放音频准备
    jmethodID play_audio_track_method_id = env->GetMethodID(player_class, "playAudioTrack",
                                                            "([BI)V");
    // 声明数据容器 有2个
    // R6 解码前数据容器 Packet 编码数据
    AVPacket *packet = av_packet_alloc();
    // R7 解码后数据容器 Frame MPC数据 还不能直接播放 还要进行重采样
    AVFrame *frame = av_frame_alloc();
    // 开始读取帧
    while (av_read_frame(format_context, packet) >= 0) {
        // 匹配音频流
        if (packet->stream_index == audio_stream_index) {
            // 解码
            result = avcodec_send_packet(audio_codec_context, packet);
            if (result < 0 && result != AVERROR(EAGAIN) && result != AVERROR_EOF) {
                return app_exit(result);
            }
            result = avcodec_receive_frame(audio_codec_context, frame);
            if (result < 0 && result != AVERROR_EOF) {
                LOGW("something is not right: [%d] %s", result, av_err2str(result));
                continue;
            }
            // 重采样
            swr_convert(swr_context, &resample_out_buffer, 44100 * 2,
                        (const uint8_t **) frame->data,
                        frame->nb_samples);
            // 播放音频
            // 调用 Java 层播放 AudioTrack
            int size = av_samples_get_buffer_size(nullptr, out_channels, frame->nb_samples,
                                                  AV_SAMPLE_FMT_S16, 1);
            jbyteArray audio_sample_array = env->NewByteArray(size);
            env->SetByteArrayRegion(audio_sample_array, 0, size,
                                    (const jbyte *) resample_out_buffer);
            env->CallVoidMethod(instance, play_audio_track_method_id, audio_sample_array, size);
            env->DeleteLocalRef(audio_sample_array);
        }
        // 释放 packet 引用
        av_packet_unref(packet);
    }
    // 调用 Java 层释放 AudioTrack
    jmethodID release_audio_track_method_id = env->GetMethodID(player_class, "releaseAudioTrack",
                                                               "()V");
    env->CallVoidMethod(instance, release_audio_track_method_id);
    // 释放 R7
    av_frame_free(&frame);
    // 释放 R6
    av_packet_free(&packet);
    // 释放 R5
    swr_free(&swr_context);
    // 关闭 R4
    avcodec_close(audio_codec_context);
    // 关闭 R3
    avformat_close_input(&format_context);
    // 释放 R2
    avformat_free_context(format_context);
    // 释放 R1
    // env->ReleaseStringUTFChars(path_, path);
    return app_exit(0);
}

int mp3_player::app_exit(int error) {
//    if (pAvFormatContext != nullptr) {
//        avformat_close_input(&pAvFormatContext);
//        avformat_free_context(pAvFormatContext);
//        pAvFormatContext = nullptr;
//    }
    if (error != 0) {
        LOG_E("error code:%d msg:%s", error, av_err2str(error));
    }
    return error;
}

extern "C"
JNIEXPORT jint JNICALL
Java_cc_imorning_ffmpeg_player_audio_mp3_Mp3Player_playAudio(JNIEnv *env, jobject instance,
                                                             jstring path) {
    const char *fp = env->GetStringUTFChars(path, nullptr);
    auto mp3Player = new mp3_player(env, instance, fp);
    return mp3Player->play();
}