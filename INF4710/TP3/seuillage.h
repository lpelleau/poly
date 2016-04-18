//
// Created by qdufour on 01/04/16.
//

#ifndef TP3_SEUILLAGE_H
#define TP3_SEUILLAGE_H

#include <opencv2/opencv.hpp>
#include <iostream>
#endif //TP3_SEUILLAGE_H

using namespace cv;

Mat seuillage_simple_cumule(const Mat& image) {
    Mat res = Mat(image.rows, image.cols, CV_8U);

    for (int x = 0; x < res.rows; x++) {
        for (int y = 0; y < res.cols; y++) {
            float val = 0;
            for (int i = 0; i < image.channels(); i++) {
                val += image.at<Vec3f>(x, y)[i];
            }
            if (val > 0.6f) res.at<uchar>(x,y) = 255;
            else res.at<uchar>(x,y) = 0;
        }
    }

    return res;
}

Mat seuillage_simple(const Mat& image) {
    Mat res = Mat(image.rows, image.cols, CV_8U);

    for (int x = 0; x < res.rows; x++) {
        for (int y = 0; y < res.cols; y++) {
            res.at<uchar>(x,y) = 0;
            for (int i = 0; i < image.channels(); i++) {
                if (image.at<Vec3f>(x, y)[i] > 0.2f) res.at<uchar>(x,y) = 255;
            }
        }
    }

    return res;
}

Mat seuillage_intervalle_cumule(const Mat& image) {
    Mat res = Mat(image.rows, image.cols, CV_8U);

    for (int x = 0; x < res.rows; x++) {
        for (int y = 0; y < res.cols; y++) {
            float val = 0;
            for (int i = 0; i < image.channels(); i++) {
                val += image.at<Vec3f>(x, y)[i];
            }
            if (val > 0.6f && val < 1.0f) res.at<uchar>(x,y) = 255;
            else res.at<uchar>(x,y) = 0;
        }
    }

    return res;
}

Mat seuillage_intervalle(const Mat& image) {
    Mat res = Mat(image.rows, image.cols, CV_8U);

    for (int x = 0; x < res.rows; x++) {
        for (int y = 0; y < res.cols; y++) {
            res.at<uchar>(x,y) = 0;
            for (int i = 0; i < image.channels(); i++) {
                if (image.at<Vec3f>(x, y)[i] > 0.2f && image.at<Vec3f>(x, y)[i] < 0.4f) res.at<uchar>(x,y) = 255;
            }
        }
    }

    return res;
}