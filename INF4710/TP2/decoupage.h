
#pragma once
#include "TP2Utils.h"

// decoupage: reformats a 2D or 3D image (i.e. may be multi-channel) to a uint8 block array (where blocks are always 8x8)
template<typename T = uchar>
std::vector<cv::Mat_<T>> decoupage(const cv::Mat& oImage) {
	CV_Assert(oImage.depth() == CV_8U);
	std::vector<cv::Mat_<T>> signal;
	cv::Size blockSize(WIDTH, HEIGHT);

	for (int c = 0; c < oImage.channels(); ++c) {
		for (int i = 0; i < oImage.rows; i += 8) {
			for (int j = 0; j < oImage.cols; j += 8) {
				//We jump from one block to another here

				cv::Mat_<T> block(blockSize);

				for (int y = 0; y < HEIGHT; y++) {
					for (int x = 0; x < WIDTH; x++) {
						int blockPos = x + y*WIDTH;
						int dataPos = (j + x + (i + y) * oImage.cols) * oImage.channels() + c;
						block.data[blockPos] = oImage.data[dataPos];
					}
				}

				signal.push_back(block);
			}
		}
	}

	return signal;
}