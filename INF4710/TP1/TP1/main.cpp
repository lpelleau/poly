
// INF4710 H2016 TP1 v1.2

#include <opencv2/opencv.hpp>
#include <exception>
#include <iostream>

typedef std::pair<uchar, uchar> RLECode; // first = symbol, second = count
typedef std::pair<std::string, int> ImagePathFlag; // first = path, second = imread flag

struct FreemanChain {
	enum FreemanCode {
		eFreemanCode_North = 0, // (same order as INF4710 course notes)
		eFreemanCode_NorthEast,
		eFreemanCode_East,
		eFreemanCode_SouthEast,
		eFreemanCode_South,
		eFreemanCode_SouthWest,
		eFreemanCode_West,
		eFreemanCode_NorthWest,
	};
	std::vector<FreemanCode> vChain; // contains the chain composed of linked shape boundary pixels
	cv::Point2i oInitPos; // initial point used to start the encoding process
	int nChannelId; // channel index where the shape was found, and where it should be reconstructed
	size_t size() { // return the encoded (byte) size of this freeman-coded chain
		return vChain.size() + sizeof(oInitPos)+sizeof(nChannelId);
	}
};

// detectBinaryEdges: used as a cheap way to detect shape edges in binary images (*OPTIONAL FOR FREEMAN ENCODING/DECODING*)
cv::Mat detectBinaryEdges(const cv::Mat& oInputBinaryImage) {
	CV_Assert(!oInputBinaryImage.empty());
	cv::Mat oOutputEdgeImage;
	cv::dilate(oInputBinaryImage, oOutputEdgeImage, cv::Mat());
	oOutputEdgeImage = oOutputEdgeImage & ~oInputBinaryImage;
	return oOutputEdgeImage;
}

// deformatInput: reconstructs a 2D/3D image (depending on channel count) from a 1D byte vector.
cv::Mat deformatInput(const std::vector<uchar>& vSignal, const cv::Size& oImageSize, int nChannels) {
	CV_Assert(!vSignal.empty());
	std::cout << "DEBUG " << vSignal.size() << " " << (size_t)oImageSize.area()*nChannels << std::endl;
	CV_Assert(vSignal.size() == (size_t)oImageSize.area()*nChannels);
	// note: in opencv, rows = height, and cols = width
	cv::Mat oReconstructedImage(oImageSize, CV_8UC(nChannels));
	// note: in OpenCV, data is stored in row-major format, and channels are interlaced!
	if (nChannels == 1)
		std::copy(vSignal.begin(), vSignal.end(), oReconstructedImage.data);
	else {
		for (int c = 0; c<oReconstructedImage.channels(); ++c) {
			for (int i = 0; i<oReconstructedImage.rows; ++i) {
				for (int j = 0; j<oReconstructedImage.cols; ++j) {
					const size_t nSignalIdx = c*(oReconstructedImage.rows*oReconstructedImage.cols) + i*(oReconstructedImage.cols) + j;
					const size_t nImageIdx = i*(oReconstructedImage.cols*oReconstructedImage.channels()) + j*(oReconstructedImage.channels()) + c;
					oReconstructedImage.data[nImageIdx] = vSignal[nSignalIdx];
				}
			}
		}
	}
	return oReconstructedImage;
}

// formatInput: reformats a 2D or 3D image (i.e. may be multi-channel) to a 1D byte vector.
std::vector<uchar> formatInput(const cv::Mat& oInputImg) {
	CV_Assert(!oInputImg.empty());
	std::vector<uchar> vSignal;
	vSignal.assign(oInputImg.channels() * oInputImg.rows * oInputImg.cols, 0);

	// Following deformat source code...
	// For one chanel
	if (oInputImg.type() == 1)
		std::vector<uchar> vSignal(oInputImg.begin<uchar>(), oInputImg.end<uchar>());
	// For multiple chanels (RGBRGBRGB become RRRGGGBBB)
	else {
		for (int c = 0; c < oInputImg.channels(); ++c) {
			for (int i = 0; i < oInputImg.rows; ++i) {
				for (int j = 0; j < oInputImg.cols; ++j) {
					const size_t nSignalIdx = c * (oInputImg.rows * oInputImg.cols) + i * (oInputImg.cols) + j;
					const size_t nImageIdx = i * (oInputImg.cols * oInputImg.channels()) + j * (oInputImg.channels()) + c;
					vSignal[nSignalIdx] = oInputImg.data[nImageIdx];
				}
			}
		}
	}

	return vSignal; // decent compilers will use return-value-optimization or move constructor to avoid copies
}

// decodeRLE: reads an encoded RLE signal (1D byte vector), and decodes it into its original signal.
std::vector<uchar> decodeRLE(const std::vector<RLECode>& vInputSignal) {
	CV_Assert(!vInputSignal.empty());
	std::vector<uchar> vOutputSignal;
	vOutputSignal.reserve(640 * 480 * 3); // worse case scenario, 640x480 image with 3 channels
	for (size_t i = 0; i<vInputSignal.size(); ++i)
	for (size_t j = 0; j<vInputSignal[i].second; ++j)
		vOutputSignal.push_back(vInputSignal[i].first);
	return vOutputSignal; // decent compilers will use return-value-optimization or move constructor to avoid copies
}

// encodeRLE: encodes a signal (1D byte vector) using a run-length-encoding strategy.
std::vector<RLECode> encodeRLE(const std::vector<uchar>& vInputSignal) {
	CV_Assert(!vInputSignal.empty());
	std::vector<RLECode> vOutputSignal;
	
	// First pixel read
	vOutputSignal.push_back(RLECode(vInputSignal[0], 1));
	
	// Next pixels
	for (int i = 1; i < vInputSignal.size(); i++)  {
		RLECode current = vOutputSignal.back();

		if (current.first == vInputSignal[i] && current.second < 255) {
			vOutputSignal.pop_back();

			// Store in the list
			vOutputSignal.push_back(RLECode(current.first, current.second + 1));
		}
		// If counter is at it's maximum or color change
		else {
			// Creation of a new counter
			vOutputSignal.push_back(RLECode(vInputSignal[i], 1));
		}
	}

	return vOutputSignal; // decent compilers will use return-value-optimization or move constructor to avoid copies
}

// decodeFreeman: decodes a Freeman signal and reconstructs its original 2D/grayscale or 3D/multi-channel matrix.
// NOTE: if you do not use a dilation-based method to detect shape edges (i.e. detectBinaryEdges), you should set bErode to false
cv::Mat decodeFreeman(const std::vector<FreemanChain>& vSignal, const cv::Size& oImageSize, int nChannels, bool bErode = true) {
	cv::Mat oReconstructedImage(oImageSize, CV_8UC(nChannels), cv::Scalar_<uchar>::all(0));
	for (size_t i = 0; i<vSignal.size(); ++i) {
		std::vector<cv::Point2i> vShapeContourPts(vSignal[i].vChain.size() + 1);
		vShapeContourPts[0] = vSignal[i].oInitPos;
		for (size_t j = 0; j<vSignal[i].vChain.size(); ++j) {
			switch (vSignal[i].vChain[j]) {
			case FreemanChain::eFreemanCode_North:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(0, -1); break;
			case FreemanChain::eFreemanCode_NorthEast:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(1, -1); break;
			case FreemanChain::eFreemanCode_East:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(1, 0); break;
			case FreemanChain::eFreemanCode_SouthEast:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(1, 1); break;
			case FreemanChain::eFreemanCode_South:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(0, 1); break;
			case FreemanChain::eFreemanCode_SouthWest:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(-1, 1); break;
			case FreemanChain::eFreemanCode_West:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(-1, 0); break;
			case FreemanChain::eFreemanCode_NorthWest:
				vShapeContourPts[j + 1] = vShapeContourPts[j] + cv::Point2i(-1, -1); break;
			}
		}
		cv::Mat oReconstructedPoly(oReconstructedImage.size(), oReconstructedImage.type(), cv::Scalar_<uchar>::all(0));
		cv::Scalar_<uchar> vColor = cv::Scalar_<uchar>::all(0);
		vColor[vSignal[i].nChannelId] = 255;
		cv::fillPoly(oReconstructedPoly, std::vector<std::vector<cv::Point2i>>(1, vShapeContourPts), vColor);
		oReconstructedImage |= oReconstructedPoly;
	}
	if (bErode) // erode is only necessary if the initial edge image was obtained via dilation (i.e. using 'detectBinaryEdges')
		cv::erode(oReconstructedImage, oReconstructedImage, cv::Mat());
	return oReconstructedImage;
}

// Return the position in the image data array from channel, X and Y.
int idx(const cv::Mat& oInputImg, int c, int i, int j){
	int nSignalIdx = c*(oInputImg.rows*oInputImg.cols) + i*(oInputImg.cols) + j;
	return i*(oInputImg.cols*oInputImg.channels()) + j*(oInputImg.channels()) + c;
}

bool findNext(cv::Mat& binaries, int c, int& i, int& j, FreemanChain::FreemanCode& nextDirection) {
	int shift[8][2] = { { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };

	for (int s = 0; s < 8; s++) {
		//Check that we are not outside of the pic
		int newI = i + shift[s][1];
		int newJ = j + shift[s][0];
		if (newI < 0 || newI >= binaries.rows || newJ < 0 || newJ >= binaries.cols) continue;

		//Check that we have not a black point
		int nImageIdx = idx(binaries, c, newI, newJ);

		if (binaries.data[nImageIdx] < 250) continue;

		//Remove pixel noise when we have a corner.
		if (s % 2 == 1) {
			for (int k = 0; k < 8; k += 2) {
				binaries.data[idx(binaries, c, i + shift[k][1], j + shift[k][0])] = 0;
			}
		}

    //Save the new direction and set this pixel to black
		i = newI;
		j = newJ;
		binaries.data[nImageIdx] = 0;
		nextDirection = (FreemanChain::FreemanCode) s;
		return true;
	}
	return false;
}

// encodeFreeman: encodes a 2D/grayscale or 3D/multi-channel matrix using a Freeman encoding strategy.
std::vector<FreemanChain> encodeFreeman(const cv::Mat& oInputImg) {
  //Checks and initialization
	CV_Assert(!oInputImg.empty());
	std::vector<FreemanChain> vOutputSignal;
	cv::Mat binaries = detectBinaryEdges(oInputImg);

  //Iterate over the picture
	for (int c = 0; c < binaries.channels(); ++c) {
		for (int i = 0; i < binaries.rows; ++i) {
			for (int j = 0; j < binaries.cols; ++j) {

				int nImageIdx = idx(binaries, c, i, j);

        //If we find a colored pixel, start contour detection
				if (binaries.data[nImageIdx] > 0) {
					binaries.data[nImageIdx] = 0;
					FreemanChain shape;
					shape.oInitPos = cv::Point2i(j, i);
					shape.nChannelId = c;
					int iCursor = i, jCursor = j;
					FreemanChain::FreemanCode nextDirection;

          //While we find a neighbourg pixel, add it to the freeman chain
					while (findNext(binaries, c, iCursor, jCursor, nextDirection)) {
						shape.vChain.push_back(nextDirection);
					}
					vOutputSignal.push_back(shape);
				}
			}
		}
	}
	std::cout << std::endl << vOutputSignal.size() << "-" << vOutputSignal[0].vChain.size() << std::endl;
	return vOutputSignal;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int main(int /*argc*/, char** /*argv*/) {
	try {
		// note: by default, on most platforms, imread always returns 3-ch images unless the cv::IMREAD_GRAYSCALE flag is set (here we hardcode it based on prior knowledge)
		const std::vector<ImagePathFlag> vsTestImages = {
			//{"TP1_H2016_images/test0.png", cv::IMREAD_GRAYSCALE },
			//{"TP1_H2016_images/test02.png", cv::IMREAD_GRAYSCALE },
			{ "TP1_H2016_images/test1.png", cv::IMREAD_GRAYSCALE },
			{ "TP1_H2016_images/test2.png", cv::IMREAD_GRAYSCALE },
			{ "TP1_H2016_images/test3.png", cv::IMREAD_GRAYSCALE },
			{ "TP1_H2016_images/test4.png", cv::IMREAD_COLOR },
			{ "TP1_H2016_images/test5.png", cv::IMREAD_COLOR },
			{ "TP1_H2016_images/test6.png", cv::IMREAD_COLOR },
			{ "TP1_H2016_images/test7.png", cv::IMREAD_COLOR },
		};
		for (const ImagePathFlag& oImagePathFlag : vsTestImages) {
			cv::Mat oInputImg = cv::imread(oImagePathFlag.first, oImagePathFlag.second);
			if (oInputImg.empty())
				CV_Error(-1, "Could not load image!");

			std::vector<uchar> formattedInput = formatInput(oInputImg);
			std::vector<RLECode> encodedRLE = encodeRLE(formattedInput);
			formattedInput = decodeRLE(encodedRLE);

			cv::Mat deformattedInput = deformatInput(formattedInput, oInputImg.size(), oInputImg.channels());

			int dataLength = oInputImg.channels() * oInputImg.rows * oInputImg.cols;
			for (int i = 0; i < dataLength; i++)
				CV_Assert(oInputImg.data[i] == deformattedInput.data[i]);

			std::cout << "RLE stats || orig: " << dataLength << ", enc: " << encodedRLE.size() * 2 << ", ratio: " << dataLength / (encodedRLE.size() * 2) << std::endl;

			std::vector<FreemanChain> encodedFreeman = encodeFreeman(deformattedInput);
			int freemanSize = 0;
			for (int l = 0; l < encodedFreeman.size(); l++) {
				freemanSize += encodedFreeman[l].size();
			}

			std::cout << "Freeman stats || orig: " << dataLength << ", enc: " << freemanSize << ", ratio: " << dataLength / freemanSize << std::endl;

			cv::Mat decodedFreeman = decodeFreeman(encodedFreeman, deformattedInput.size(), deformattedInput.channels(), true);

      //Count differences between original picture and new one
      //If diff == 0, we have a lossless compression
			int diff = 0;
			for (int i = 0; i < dataLength; i++)
			if (oInputImg.data[i] != decodedFreeman.data[i]) diff++;
			std::cout << "diff: " << diff << std::endl;

			cv::imshow("original", oInputImg);
			cv::waitKey(0);
			cv::imshow("freeman", decodedFreeman);
			cv::waitKey(0);
		}
	}
	catch (const cv::Exception& e) {
		std::cerr << "Caught cv::Exceptions: " << e.what() << std::endl;
	}
	catch (const std::runtime_error& e) {
		std::cerr << "Caught std::runtime_error: " << e.what() << std::endl;
	}
	catch (...) {
		std::cerr << "Caught unhandled exception." << std::endl;
	}
	int a;
	std::cin >> a;
	return 0;
}
