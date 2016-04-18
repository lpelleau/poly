cmake_minimum_required(VERSION 3.3)
project(tp3)

find_package(OpenCV REQUIRED)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
set(SOURCE_FILES main.cpp convo.h seuillage.h dilatation.h edge.h)

add_executable(tp3 ${SOURCE_FILES})
target_link_libraries(tp3 ${OpenCV_LIBS})
