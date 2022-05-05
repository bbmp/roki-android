cmake_minimum_required(VERSION 3.4.1)

add_library(SmartConfig SHARED src/main/cpp/SmartConfig.cpp src/main/cpp/ConfigManager.cpp src/main/cpp/Thread.cpp
        src/main/cpp/BroadcastManager.cpp src/main/cpp/BroadcastMessage.cpp src/main/cpp/Message.cpp)

find_library(log-lib log)

target_link_libraries(SmartConfig ${log-lib})