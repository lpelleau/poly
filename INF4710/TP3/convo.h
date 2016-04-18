
#include <opencv2/opencv.hpp>
#include <iostream>

using namespace cv;

Mat convolution(const Mat& image_1ch, const Mat& kernel) {
	Mat ret = Mat(image_1ch.rows, image_1ch.cols, CV_32F);

	//Iterate over cols
	for (int x = 0; x < image_1ch.rows; x++) {
		//Iterate over rows
		for (int y = 0; y < image_1ch.cols; y++) {
			ret.at<float>(x, y) = 0;

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
                    int deltax = x + i - 1; int deltay = y + j - 1;
					if (deltax < 0 || deltax >= image_1ch.rows || deltay < 0 || deltay >= image_1ch.cols) continue;
					ret.at<float>(x, y) += (float)kernel.at<int>(i, j) * (float)image_1ch.at<uchar>(deltax, deltay);
                    /*std::cout << "pos(" << x << "," << y << ")=" << (int)image_1ch.at<uchar>(deltax, deltay) <<
                            ", ker(" << i-1 << "," << j-1 << ")=" << (int)kernel.at<int>(i, j) <<
                            ", val=" << (float)kernel.at<int>(i, j) * (float)image_1ch.at<uchar>(deltax, deltay) <<
                            ", total=" << ret.at<float>(x, y) <<
                            std::endl;*/
				}
			}
		}
	}

	return ret;
}

float norme(const Mat& convox, const Mat& convoy, int x, int y) {
    return sqrt(pow(convox.at<float>(x, y), 2) + pow(convoy.at<float>(x, y), 2));
}

Mat fg_norm_sobel(const Mat& image)  {
	int data_sobelx[3][3] = { {-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1} };
	int data_sobely[3][3] = { { 1, 2, 1 }, { 0, 0, 0}, { -1, -2, -1 } };
	Mat sobelx = Mat(3, 3, CV_32S, data_sobelx);
	Mat sobely = Mat(3, 3, CV_32S, data_sobely);
	Mat force = Mat(image.rows, image.cols, CV_32FC3);

	// Pour chaque canaux
	for (int i = 0; i < image.channels(); i++){
		Mat channel;
		extractChannel(image, channel, i);
		Mat convox = convolution(channel, sobelx);
		Mat convoy = convolution(channel, sobely);
        float max = norme(convox,convoy,0,0);
        float min = norme(convox,convoy,0,0);

		// On obtient la norme du gradient pour chaque point
		for (int x = 0; x < channel.rows; x++) {
			for (int y = 0; y < channel.cols; y++) {
				force.at<Vec3f>(x, y)[i] = norme(convox, convoy, x, y);
                if (force.at<Vec3f>(x, y)[i] > max) max = force.at<Vec3f>(x, y)[i];
                else if (force.at<Vec3f>(x, y)[i] < min) min = force.at<Vec3f>(x, y)[i];
			}
		}

        // On fait la normalisation 0-1
        for (int x = 0; x < channel.rows; x++) {
            for (int y = 0; y < channel.cols; y++) {
                force.at<Vec3f>(x, y)[i] = (force.at<Vec3f>(x, y)[i] - min) / (max - min);
            }
        }
	}

	return force;
}

Mat displayable(const Mat& image) {
    Mat result = Mat(image.rows, image.cols, CV_8UC3);

    for (int i = 0; i < result.channels(); i++) {
        for (int x = 0; x < result.rows; x++) {
            for (int y = 0; y < result.cols; y++) {
                result.at<Vec3b>(x,y)[i] = (uchar) (image.at<Vec3f>(x,y)[i] * 255);
            }
        }
    }

    return result;
}