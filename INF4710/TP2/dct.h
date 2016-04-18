
#pragma once
#include "TP2Utils.h"

inline float small_c(int w) {
	return (float) (w == 0 ? sqrt(1.0f / 8.0f) : sqrt(2.0f / 8.0f));
}

//Seems to be a good reference. Same formula with a better form : https://unix4lyfe.org/dct/

template<typename Tin>
inline float big_C(int u, int v, const cv::Mat_<Tin>& oBlock) {
	float sum = 0;
	for (int y = 0; y < oBlock.rows; y++) {
		for (int x = 0; x < oBlock.cols; x++) {
			sum +=
				oBlock(x, y) *
				cos((CV_PI * (2 * x + 1) * u) / 16 ) *
				cos((CV_PI * (2 * y + 1) * v) / 16 );
		}
	}

	return small_c(u) * small_c(v) * sum;
}

// dct: computes the discrete cosinus tranform of a 8x8 block
template<typename Tin=uchar,typename Tout=float>
inline cv::Mat_<Tout> dct(const cv::Mat_<Tin>& oBlock) {
	cv::Mat_<Tout> matrix_out(WIDTH, HEIGHT);

	for (int v = 0; v < oBlock.rows; v++) {
		for (int u = 0; u < oBlock.cols; u++) {
            auto r = big_C(u, v, oBlock);
			matrix_out(u,v) = r;
		}
	}

	return matrix_out;
}