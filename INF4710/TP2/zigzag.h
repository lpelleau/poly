
#pragma once
#include "TP2Utils.h"

// zigzag: returns a 64-element array created by zig-zagging through a 8x8 block
template<int nBlockSize=8,typename T=int16_t>
inline std::vector<T> zigzag(const cv::Mat_<T>& mat) {
    CV_Assert(!mat.empty());
    CV_Assert(mat.rows==mat.cols && mat.rows==nBlockSize);

	std::vector<T> res;
	int x = 0, y = 0;
	bool topRight = true, halfWay = false;
	do {
		res.push_back(mat(y, x));

		if (topRight) {
			x++;
			y--;
			if (y < 0) {
				y++;
				topRight = false;
			}
			if (x >= mat.cols) {
				if (halfWay) {
					y += 2;
				}
				x--;
				topRight = false;
			}
		}
		else {
			y++;
			x--;
			if (x < 0 && y >= mat.cols) {
				x += 2;
				y--;
				topRight = true;
				halfWay = true;
			}
			if (x < 0) {
				x++;
				topRight = true;
			}
			if (y >= mat.cols) {
				if (halfWay) {
					x += 2;
				}
				y--;
				topRight = true;
			}
		}
	} while (res.size() != mat.cols * mat.cols);

	return res;	
}