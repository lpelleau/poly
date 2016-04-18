
#pragma once
#include "TP2Utils.h"

// idecoupage: reconstructs a 2D/3D image (depending on channel count) from a uint8 block array (where blocks are always 8x8).
template<typename T=uchar>
inline cv::Mat idecoupage(const std::vector<cv::Mat_<T>>& vBlocks, const cv::Size& oImageSize, int nChannels) {
	cv::Mat oImage(oImageSize, CV_8UC(nChannels));

	int posBlocks = 0;
	for (int c = 0; c < oImage.channels(); ++c) {
		for (int i = 0; i < oImage.rows; i += 8) {
			for (int j = 0; j < oImage.cols; j += 8) {
				cv::Mat_<T> block = vBlocks[posBlocks++];

				for (int y = 0; y < HEIGHT; y++) {
					for (int x = 0; x < WIDTH; x++) {
						int dataPos = (j + x + (i + y) * oImage.cols) * oImage.channels() + c;
						oImage.data[dataPos] = block.data[x + y*WIDTH];
					}
				}
			}
		}
	}
	return oImage;
}
