
using namespace std;

void detect(const list<float> &pin, const list<float> &pout, const list<float> &p) {
    if (pin.size() != pout.size() || pout.size() != p.size())
        return;

    list<float>::const_iterator pinIt;
    list<float>::const_iterator poutIt;
    list<float>::const_iterator pIt;

    float avPin = 0;
    float avPout = 0;
    float avP = 0;
    float ecPin = 0;
    float ecPout = 0;
    float ecP = 0;

    pinIt = list<float>::const_iterator(pin.begin());
    poutIt = list<float>::const_iterator(pout.begin());
    pIt = list<float>::const_iterator(p.begin());
    for(int i = 0 ; i < p.size() ; i++, ++pinIt, ++poutIt, ++pIt) {
        avPin += *pinIt;
        avPout += *poutIt;
        avP += *pIt;
    }
    avPin /= pin.size();
    avPout /= pout.size();
    avP /= p.size();

    cout << "Pin moyenne : " << avPin << endl;
    cout << "Pout moyenne : " << avPout << endl;
    cout << "P moyenne : " << avP << endl;

    pinIt = list<float>::const_iterator(pin.begin());
    poutIt = list<float>::const_iterator(pout.begin());
    pIt = list<float>::const_iterator(p.begin());
    for(int i = 0 ; i < p.size() ; i++, ++pinIt, ++poutIt, ++pIt) {
        ecPin += (*pinIt - avPin) * (*pinIt - avPin);
        ecPout += (*poutIt - avPout) * (*poutIt - avPout);
        ecP += (*pIt - avP) * (*pIt - avP);
    }
    ecPin /= pin.size();
    ecPin = sqrt(ecPin);

    ecPout /= pin.size();
    ecPout = sqrt(ecPout);

    ecP /= pin.size();
    ecP = sqrt(ecP);

    cout << "Ecart type Pin : " << ecPin << endl;
    cout << "Ecart type Pout : " << ecPout << endl;
    cout << "Ecart type P : " << ecP << endl;

    int nG = 0;
    int nF = 0;

    pinIt = list<float>::const_iterator(pin.begin());
    poutIt = list<float>::const_iterator(pout.begin());
    pIt = list<float>::const_iterator(p.begin());
    int startGrad = -1;
    int endGrad = -1;
    float prevPin;
    float prevPout;
    bool prevPb = false;
    for(int i = 0 ; i < p.size() ; i++, ++pinIt, ++poutIt, ++pIt) {
        if (*pIt >= avP / 2)
            cout << "Coupure dans l'image à la frame n°" << i << endl;

        // P est élevé, gradation ou fondu
        if (*pIt > (avP + ecP / 2)) {
            // Gradation
            if (*pinIt > *poutIt || *pinIt < *poutIt) {
                //cout << "Gradation à la frame n°" << i << endl;
                nG++;
                if(startGrad == -1) {
                    startGrad = i;
                }
            } else if (startGrad != -1) {
                endGrad = i;
                cout << "Gradation de la frame " << startGrad << " à la frame " << endGrad << endl;
                startGrad = -1;
            }

            // Fondu
            if (prevPb && prevPin > prevPout && *pinIt < *poutIt) {
                cout << "Fondu à la frame n°" << i << endl;
                nF++;
            }
        } else if (startGrad != -1) {
            endGrad = i;
            cout << "Gradation de la frame " << startGrad << " à la frame " << endGrad << endl;
            startGrad = -1;
        }

        prevPin = *pinIt;
        prevPout = *poutIt;
        prevPb = true;
    }

    cout << "Nb Gradation : " << nG << endl;
    cout << "Nb Fondu : " << nF << endl;
}
