
#pragma once
#include "TP2Utils.h"

// match_block: finds the best match for a 8x8 block in a 16x16 region by minimizing mean squared error (EQM)
template<typename Tin=uchar>
 cv::Point2i match_block(const cv::Mat_<Tin>& oBlock, const cv::Point2i& oBlockPos, const cv::Mat& oSearchImage, double& dEQM_min) {
    // return value : 2d displacement vector between oBlockPos and the newly found 8x8 region's top-left corner
    // oBlock : 8x8 image block taken from the source image
    // oBlockPos : 2d position vector of the top-left corner of 'oBlock'
    // oSearchImage : full image to be searched for local correspondances around block_pos
    // dEQM_min : minimal mean squared error found (output only)
    CV_Assert(oSearchImage.depth()==CV_8U);
    // @@@@ TODO (hint: make sure border checks wont crash your implementation!)
	double eqm = -1;
	cv::Point2i pos;
	int delta = oBlock.cols / 2;
	// Centré sur le block
	for (int i = delta * -1; i <= delta; i++) {
		for (int j = delta * -1; j <= delta; j++) {
			// Posiiton de haut-gauche pour le bloc pour lequel calculer EQM
			int sX = oBlockPos.x + i, sY = oBlockPos.y + j;

			// Si on est dans l'image
			if (sX >= 0 && sY >= 0 && sX < oSearchImage.rows && sY < oSearchImage.cols) {
				// Calcul de l'EQM pour ce block
				double ieqm = 0;
				for (int k = 0; k < oBlock.cols; k++) {
					for (int l = 0; l < oBlock.cols; l++) {
						auto n = (oBlock(k, l) - oSearchImage.at<uchar>(cv::Point2i(sX + k, sY + l)));
						ieqm += n * n;
					}
				}
				ieqm *= (1. / (oBlock.cols * oBlock.cols));

				// Si le block a un EQM minimum
				if (eqm == -1 || ieqm < eqm) {
					eqm = ieqm;
					pos = cv::Point2i(i * -1, j * -1);
				}
			}
		}
	}
	dEQM_min = eqm;
	return pos;
}
