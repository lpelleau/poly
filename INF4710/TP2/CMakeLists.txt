cmake_minimum_required(VERSION 3.3)
project(tp2)

find_package( OpenCV REQUIRED )

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
set(SOURCE_FILES main.cpp dct.h decoupage.h idct.h idecoupage.h izigzag.h mat2huff.h match_block.h quantif.h test_match.h TP2Utils.h zigzag.h)

add_executable(tp2 ${SOURCE_FILES})
target_link_libraries(tp2 ${OpenCV_LIBS})