// INF4710 H2016 TP2 v1.2

#include "TP2Utils.h"

typedef std::pair<std::string, int> ImagePathFlag; // first = path, second = imread flag

int main(int /*argc*/, char ** /*argv*/) {
	try {
        const std::vector<ImagePathFlag> vsTestImages = {
                {"INF4710_TP2_Fichiers/car.bmp",        cv::IMREAD_GRAYSCALE},
                {"INF4710_TP2_Fichiers/fleurs.bmp",     cv::IMREAD_GRAYSCALE},
                {"INF4710_TP2_Fichiers/lena.png",       cv::IMREAD_COLOR},
                {"INF4710_TP2_Fichiers/lion.bmp",       cv::IMREAD_COLOR},
                {"INF4710_TP2_Fichiers/logo.tif",       cv::IMREAD_COLOR},
                {"INF4710_TP2_Fichiers/logo_noise.tif", cv::IMREAD_COLOR},
        };
        for (const ImagePathFlag &oImagePathFlag : vsTestImages) {
            cv::Mat oInputImg = cv::imread(oImagePathFlag.first, oImagePathFlag.second);

            auto decoupe = decoupage(oInputImg);

            std::vector<cv::Mat_<uchar>> dct_idct;
            std::vector<HuffOutput<int16_t>> huff;
            std::vector<std::vector<int16_t >> zzz;
            for (int i = 0; i < decoupe.size(); i++) {
                auto convert_to_frequency = dct(decoupe[i]);

                auto quantify = do_quantification(convert_to_frequency, 10);

                auto zz = zigzag(quantify);
                zzz.push_back(zz);
                huff.push_back(mat2huff(zz));
                auto izz = izigzag(zz);

                auto unquantify = undo_quantification(izz, 10);

                auto convert_to_luminosity = idct(unquantify);
                dct_idct.push_back(convert_to_luminosity);
            }

            cv::Mat coupe = idecoupage(dct_idct, oInputImg.size(), oInputImg.channels());
            cv::Mat diff;
            cv::absdiff(coupe, oInputImg, diff);

            /*int diff_count = 0;
            for (int i = 0; i < diff.channels() + diff.rows + diff.cols; i++)
                diff_count += diff.data[i];
            std::cout << diff_count << std::endl;*/

            double origSize = oInputImg.channels() * oInputImg.rows * oInputImg.cols * 8;
            double newSize = 0;
            for (int i = 0; i < huff.size(); i++) {
                newSize += huff[i].code.size() * 1;
                for (auto const& x : huff[i].map) {
                    newSize += sizeof(int16_t); //For the key
                    newSize += x.second.size() * 1;
                }
            }

            std::cout << origSize << ", " << newSize << ", " << origSize / newSize << std::endl;

            cv::imshow("original", oInputImg);
            cv::imshow("encodee", coupe);
            cv::imshow("diff", diff);
            cv::waitKey(0);
        }
    }
    catch (const cv::Exception &e) {
        std::cerr << "Caught cv::Exceptions: " << e.what() << std::endl;
    }
    catch (const std::runtime_error &e) {
        std::cerr << "Caught std::runtime_error: " << e.what() << std::endl;
    }
    catch (...) {
        std::cerr << "Caught unhandled exception." << std::endl;
    }

    /*int a;
    std::cin >> a;*/

    return 0;
}
