#pragma once
#include "ckfft/ckfft.h"


namespace ckfft
{

void fft_default(
        CkFftContext* context, 
        const CkFftComplex* input, 
        CkFftComplex* output, 
        int count, 
        bool inverse,
        int stride, 
        const CkFftComplex* expTable,
        int expTableStride);

}



