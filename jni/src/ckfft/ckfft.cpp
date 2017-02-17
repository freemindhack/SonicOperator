#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/ckfft.h"
#include "ckfft/fft.h"
#include "ckfft/fft_real.h"
#include "ckfft/context.h"
#include "ckfft/math_util.h"

using namespace ckfft;

extern "C"
{

CkFftContext* CkFftInit(int maxCount, CkFftDirection direction, void* userBuf, size_t* userBufSize) 
{
    if (maxCount <= 0)
    {
        return NULL;
    }
    if (!isPowerOfTwo(maxCount))
    {
        return NULL;
    }
    if (direction != kCkFftDirection_Forward && direction != kCkFftDirection_Inverse && direction != kCkFftDirection_Both)
    {
        return NULL;
    }
    if (userBuf && !userBufSize)
    {
        return NULL;
    }

    return (CkFftContext*) CkFftContext::create(maxCount, direction, userBuf, userBufSize);
}

int CkFftRealForward(CkFftContext* context, int count, const float* input, CkFftComplex* output)
{
    if (!context || !context->fwdExpTable)
    {
        return 0;
    }
    if (!isPowerOfTwo(count) || count > context->maxCount)
    {
        return 0;
    }
    if (!input || !output || (void*) input == (void*) output)
    {
        return 0;
    }

    fft_real(context, input, output, count);
    return 1;
}

int CkFftRealInverse(CkFftContext* context, int count, const CkFftComplex* input, float* output, CkFftComplex* tmpBuf)
{
    if (!tmpBuf)
    {
        return 0;
    }
    if (!context || !context->invExpTable)
    {
        return 0;
    }
    if (!isPowerOfTwo(count) || count > context->maxCount)
    {
        return 0;
    }
    if (!input || !output || (void*) input == (void*) output)
    {
        return 0;
    }

    fft_real_inverse(context, input, output, count, tmpBuf);
    return 1;
}

int CkFftComplexForward(CkFftContext* context, int count, const CkFftComplex* input, CkFftComplex* output)
{
    if (!context || !context->fwdExpTable)
    {
        return 0;
    }
    if (!isPowerOfTwo(count) || count > context->maxCount)
    {
        return 0;
    }
    if (!input || !output || input == output)
    {
        return 0;
    }

    fft(context, input, output, count, false);
    return 1;
}

int CkFftComplexInverse(CkFftContext* context, int count, const CkFftComplex* input, CkFftComplex* output)
{
    if (!context || !context->invExpTable)
    {
        return 0;
    }
    if (!isPowerOfTwo(count) || count > context->maxCount)
    {
        return 0;
    }
    if (!input || !output || input == output)
    {
        return 0;
    }

    fft(context, input, output, count, true);
    return 1;
}

void CkFftShutdown(CkFftContext* context)  
{
    CkFftContext::destroy(context);
}

} // extern "C"
