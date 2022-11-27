cmake_minimum_required(VERSION 3.22.1)

project("player")

include_directories(${CMAKE_SOURCE_DIR}/include/)

aux_source_directory(./player/ PLAYER_SOURCES)

add_library(player
        SHARED
        ${PLAYER_SOURCES})

# ffmpeg library
add_library(libavcodec SHARED IMPORTED)
add_library(libavfilter SHARED IMPORTED)
add_library(libavformat SHARED IMPORTED)
add_library(libavutil SHARED IMPORTED)
add_library(libpostproc SHARED IMPORTED)
add_library(libswresample SHARED IMPORTED)
add_library(libswscale SHARED IMPORTED)
add_library(libavdevice SHARED IMPORTED)
set_target_properties(libavcodec PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libavcodec-58.so)
set_target_properties(libavfilter PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libavfilter-7.so)
set_target_properties(libavformat PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libavformat-58.so)
set_target_properties(libavutil PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libavutil-56.so)
set_target_properties(libpostproc PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libpostproc-55.so)
set_target_properties(libswresample PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libswresample-3.so)
set_target_properties(libswscale PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libswscale-5.so)
set_target_properties(libavdevice PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/libavdevice-58.so)

find_library(log-lib log)

target_link_libraries(player
        libavfilter
        libavformat
        libavcodec
        libswscale
        libswresample
        libpostproc
        libavutil
        libavdevice
        ${log-lib}
        android)
