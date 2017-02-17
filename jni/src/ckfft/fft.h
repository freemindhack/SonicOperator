#pragma once
#include "ckfft/ckfft.h"


namespace ckfft
{

void fft(
        CkFftContext* context, 
        const CkFftComplex* input, 
        CkFftComplex* output, 
        int count, 
        bool inverse);

}

