cmake_minimum_required(VERSION 3.22.1)

project("player")

include_directories(${CMAKE_SOURCE_DIR}/include/)

aux_source_directory(./ PLAYER_SOURCES)

add_library(player
        SHARED
        ${PLAYER_SOURCES})

find_library(log-lib log)

target_link_libraries(player
        ${log-lib}
        android)
