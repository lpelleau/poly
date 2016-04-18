
#pragma once
#include "TP2Utils.h"

template<typename Tin>
inline float F(int x, int y, const cv::Mat_<Tin>& oBlock) {
    float sum = 0;
    //int n = 8;
    for (int v = 0; v < oBlock.rows; v++) {
        for (int u = 0; u < oBlock.cols; u++) {
            sum +=  small_c(u) * small_c(v) * oBlock(u, v) *
                    cos((CV_PI * (2 * x + 1) * u) / 16) *
                    cos((CV_PI * (2 * y + 1) * v) / 16);
        }
    }

    // Put a white pixel when we have -0.15...
    if (sum < 0) {
        return 0;
    } else if (sum > 255) {
        return 255;
    }
    return sum;
}

// idct: computes the inverse discrete cosinus tranform of a 8x8 block
template<typename Tin=float,typename Tout=uchar>
inline cv::Mat_<Tout> idct(const cv::Mat_<Tin>& oBlock_c) {
    cv::Mat_<Tout> matrix_out(WIDTH, HEIGHT);

    for (int y = 0; y < oBlock_c.rows; y++) {
        for (int x = 0; x < oBlock_c.cols; x++) {
            matrix_out(x,y) =  F(x, y, oBlock_c);
        }
    }

    return matrix_out;
}
