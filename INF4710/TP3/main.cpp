
// INF4710 H2016 TP3 v1.0

#include <opencv2/opencv.hpp>
#include <fstream>
#include "convo.h"
#include "seuillage.h"
#include "dilatation.h"
#include "edge.h"
#include "detect.h"

using namespace std;
using namespace cv;

void readCSV(const string &file, list<float> &pinTab, list<float> &poutTab, list<float> &pTab) {
    FILE* fichier = NULL;
    fichier = fopen(file.c_str(), "r");

    if (fichier == NULL)
        return;

    float pin, pout, p;

    while(fscanf(fichier,"%f, %f, %f", &pin, &pout, &p) != EOF){
        pinTab.push_back(pin);
        poutTab.push_back(pout);
        pTab.push_back(p);
    }

    fclose(fichier);
}

int main(int /*argc*/, char** /*argv*/) {
    try {
        ofstream logs;
        logs.open ("logs.csv");

        static VideoCapture oCap("INF4710_TP3_Fichiers/INF4710_TP3_video_xvid.avi");
        CV_Assert(oCap.isOpened());

        bool prevInit = false;
        Mat prevImgE;
        Mat prevImgD;
        Mat currImgE;
        Mat currImgD;
        list<float> pinTab;
        list<float> poutTab;
        list<float> pTab;

        readCSV("logs.bak.csv", pinTab, poutTab, pTab);
        detect(pinTab, poutTab, pTab);
        logs.close();
        return 0;

        for(int i=0; i<(int)oCap.get(CV_CAP_PROP_FRAME_COUNT); ++i) {
            Mat oImg;
			oCap >> oImg;

            //imshow("oImg", oImg);

            currImgE = seuillage_simple(fg_norm_sobel(oImg));
            currImgD = dilatation(currImgE, 1);

            if (!prevInit) {
                prevInit = true;

                logs << "pin, pout, max(pin, pout)" << endl;
            } else {
                float pin = edge_ratio(prevImgD, currImgE);
                float pout = edge_ratio(currImgD, prevImgE);

                pinTab.push_back(pin);
                poutTab.push_back(pout);
                pTab.push_back(max(pin, pout));

                logs << pin << ", " << pout << ", " << max(pin, pout) << endl;
            }

            prevImgE = currImgE;
            prevImgD = currImgD;

            cout << "Frame n°" << i << endl;

            waitKey(1);
        }

        detect(pinTab, poutTab, pTab);

        logs.close();
    }
    catch(const cv::Exception& e) {
        std::cerr << "Caught cv::Exceptions: " << e.what() << std::endl;
    }
    catch(const std::runtime_error& e) {
        std::cerr << "Caught std::runtime_error: " << e.what() << std::endl;
    }
    catch(...) {
        std::cerr << "Caught unhandled exception." << std::endl;
    }
    return 0;
}
