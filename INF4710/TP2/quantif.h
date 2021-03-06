
#pragma once
#include "TP2Utils.h"
#include <stdint.h>

// quantif: returns a 8x8 quantification matrix associated with a quality level (F=10, 50, or 90)
template<typename T=uchar>
inline cv::Mat_<T> quantif(int F) {
    if(F==10)
        return (cv::Mat_<T>(8,8) << 80,60,50,80,120,200,255,255,55,60,70,95,130,255,255,255,70,65,80,120,200,255,255,255,70,85,110,145,255,255,255,255,90,110,185,255,255,255,255,255,120,175,255,255,255,255,255,255,245,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255);
    else if(F==90)
        return (cv::Mat_<T>(8,8) << 3,2,2,3,5,8,10,12,2,2,3,4,5,12,12,11,3,3,3,5,8,11,14,11,3,3,4,6,10,17,16,12,4,4,7,11,14,22,21,15,5,7,11,13,16,12,23,18,10,13,16,17,21,24,24,21,14,18,19,20,22,20,20,20);
    else if(F==50)
        return (cv::Mat_<T>(8,8) << 16,11,10,16,24,40,51,61,12,12,14,19,26,58,60,55,14,13,16,24,40,57,69,56,14,17,22,29,51,87,80,62,18,22,37,56,68,109,103,77,24,35,55,64,81,104,113,92,49,64,78,87,103,121,120,101,72,92,95,98,112,100,103,99);
    std::cerr << "Facteur inconnu, matrice de quantification vide retournee." << std::endl;
    return cv::Mat_<T>::zeros(8,8);
}

template<typename Tout=int16_t, typename Tin=float>
inline cv::Mat_<Tout> do_quantification(cv::Mat_<Tin> input, int F) {
    cv::Mat_<Tout> result(8,8);

    for (int y = 0; y < input.rows; y++) {
        for (int x = 0; x < input.cols; x++) {
            result(x,y) = (int16_t) (input(x,y) / quantif(F)(x,y));
        }
    }

    return result;
}

template<typename Tin=int16_t, typename Tout=float>
inline cv::Mat_<Tout> undo_quantification(cv::Mat_<Tin> input, int F) {
    cv::Mat_<Tout> result(8,8);

    for (int y = 0; y < input.rows; y++) {
        for (int x = 0; x < input.cols; x++) {
            result(x, y) = (input(x, y) * quantif(F)(x, y));
        }
    }

    return result;
}