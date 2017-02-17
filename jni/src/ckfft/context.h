#pragma once
#include "ckfft/ckfft.h"

struct _CkFftContext
{
    bool neon;
    int maxCount;
    const CkFftComplex* fwdExpTable;
    const CkFftComplex* invExpTable;
    bool ownBuf; // true if memory was allocated by us, rather than user

    static _CkFftContext* create(int maxCount, CkFftDirection, void* buf, size_t* bufSize);
    static void destroy(_CkFftContext*);

    static bool isNeonSupported();

private:
    _CkFftContext();
};


