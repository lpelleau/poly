
#pragma once
#include "TP2Utils.h"

inline void test_match(size_t nFrameIdx, size_t nBlockIdx) {
    CV_Assert(nFrameIdx > 0 && nFrameIdx < 38); // 38 frames total in heart sequence, and need previous too
    CV_Assert(nBlockIdx < 1024); // 256x256 grayscale frames => 1024 blocks total
    static cv::VideoCapture oCap("./INF4710_TP2_Fichiers/Heart/heart.%03d.png");
    CV_Assert(oCap.isOpened());
    //Ne passe pas la compilation sous CLion..........
    oCap.set(cv::CAP_PROP_POS_FRAMES,(double) (nFrameIdx - 1));
    cv::Mat oOrigImage, oSearchImage;
    CV_Assert(oCap.read(oSearchImage) && !oSearchImage.empty());
    CV_Assert(oCap.read(oOrigImage) && !oOrigImage.empty());
    std::vector<cv::Mat_<uchar>> vBlocks = decoupage(oOrigImage);
    CV_Assert(vBlocks.size() == 1024);
	const cv::Mat_<uchar>& oBlock = vBlocks[nBlockIdx];

	int x = (nBlockIdx * 8) % oSearchImage.cols;
	int y = (nBlockIdx * 8) / oSearchImage.cols;
	const cv::Point2i oBlockPos(x, y); /* = @@@@ TODO: FILL COORDS BASED ON nBlockIdx @@@@*/
	std::cout << "Previous position: " << oBlockPos << std::endl;

    double dEQM;
    cv::Point oRelPosDiff = match_block(oBlock, oBlockPos, oSearchImage, dEQM);
    std::cout << " test @ nFrameIdx=" << nFrameIdx << ", nBlockIdx=" << nBlockIdx << " => dEQM=" << dEQM << ", oRelPosDiff=" << oRelPosDiff << std::endl;
}
