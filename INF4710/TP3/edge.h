//
// Created by lpelleau on 4/1/16.
//

#ifndef TP3_EDGE_H
#define TP3_EDGE_H

#include <opencv2/opencv.hpp>
#include <iostream>

float edge_ratio(const cv::Mat& A, const cv::Mat& B) {
    float valTop = 0.0f;
    float valBot = 0.0f;

    for (int x = 0; x < A.rows; x++) {
        for (int y = 0; y < A.cols; y++) {
            valTop += A.at<uchar>(x,y) * B.at<uchar>(x,y);
            valBot += B.at<uchar>(x,y);
        }
    }

    return 1 - (valTop / valBot);
}

#endif //TP3_EDGE_H
