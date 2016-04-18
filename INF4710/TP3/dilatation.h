//
// Created by qdufour on 01/04/16.
//

#ifndef TP3_DILATATION_H
#define TP3_DILATATION_H
#include <opencv2/opencv.hpp>
#include <iostream>
#endif //TP3_DILATATION_H

using namespace cv;

Mat dilatation(const Mat& image, int range) {
    Mat res = Mat(image.rows, image.cols, CV_8U);
    for (int x = 0; x < res.rows; x++) {
        for (int y = 0; y < res.cols; y++) {
            res.at<uchar>(x,y) = 0;
            if (image.at<uchar>(x,y) > 0) {
                for (int i = -range; i <= range; i++) {
                    if (x+i < 0 || x+i >= res.rows) continue;
                    for (int j = -range; j <= range; j++) {
                        if (y+j < 0 || y+j >= res.cols) continue;
                        res.at<uchar>(x+i,y+j) = 255;
                    }
                }
            }
        }
    }

    return res;
}