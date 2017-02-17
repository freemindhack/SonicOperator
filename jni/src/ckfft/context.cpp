#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/context.h"

#if CKFFT_PLATFORM_WIN
#  define _USE_MATH_DEFINES
#endif

#include <math.h>
#include <new>

#if CKFFT_PLATFORM_ANDROID
#  include <cpu-features.h>
#endif

_CkFftContext::_CkFftContext() :
    neon(false),
    maxCount(0),
    fwdExpTable(NULL),
    invExpTable(NULL),
    ownBuf(false)
{}

_CkFftContext* _CkFftContext::create(int maxCount, CkFftDirection direction, void* userBuf, size_t* userBufSize)
{
    // size of context object
    int contextSize = sizeof(_CkFftContext);
    if (contextSize % sizeof(CkFftComplex))
    {
        // alignment XXX
        contextSize += sizeof(CkFftComplex) - (contextSize % sizeof(CkFftComplex));
    }

    int reqBufSize = contextSize;

    // size of lookup table(s)
    int expTableSize = maxCount * sizeof(CkFftComplex);
    if (direction & kCkFftDirection_Forward)
    {
        reqBufSize += expTableSize;
    }
    if (direction & kCkFftDirection_Inverse)
    {
        reqBufSize += expTableSize;
    }

    if (userBufSize && (!userBuf || (int) *userBufSize < reqBufSize))
    {
        *userBufSize = reqBufSize;
        return NULL;
    }

    // allocate buffer if needed
    void* buf = NULL;
    if (userBuf)
    {
        buf = userBuf;
    }
    else
    {
        // allocate buffer
        buf = malloc(reqBufSize);
        if (!buf)
        {
            return NULL;
        }
    }

    // initialize
    _CkFftContext* context = new (buf) _CkFftContext();

    // lookup table(s)
    CkFftComplex* fwdExpBuf = NULL;
    CkFftComplex* invExpBuf = NULL;
    CkFftComplex* expBuf = (CkFftComplex*) ((char*) buf + contextSize);
    if (direction == kCkFftDirection_Forward)
    {
        fwdExpBuf = expBuf;
    }
    else if (direction == kCkFftDirection_Inverse)
    {
        invExpBuf = expBuf;
    }
    else if (direction == kCkFftDirection_Both)
    {
        fwdExpBuf = expBuf;
        invExpBuf = expBuf + maxCount;
    }

    for (int i = 0; i < maxCount; ++i)
    {
        float theta = -2.0f * (float) M_PI * i / maxCount;
        float c = cosf(theta);
        float s = sinf(theta);
        if (fwdExpBuf)
        {
            fwdExpBuf[i].real = c;
            fwdExpBuf[i].imag = s;
        }
        if (invExpBuf)
        {
            invExpBuf[i].real = c;
            invExpBuf[i].imag = -s;
        }
    }

    context->neon = isNeonSupported();
    context->maxCount = maxCount;
    context->fwdExpTable = fwdExpBuf;
    context->invExpTable = invExpBuf;
    context->ownBuf = (userBuf == NULL);

    return context;
}

void _CkFftContext::destroy(_CkFftContext* context)
{
    if (context && context->ownBuf)
    {
        free(context);
    }
}

bool _CkFftContext::isNeonSupported()
{
    bool neon = false;
#if CKFFT_PLATFORM_ANDROID
    // on Android, need to check for NEON support at runtime
    neon = ((android_getCpuFeatures() & ANDROID_CPU_ARM_FEATURE_NEON) == ANDROID_CPU_ARM_FEATURE_NEON);
#elif CKFFT_PLATFORM_IOS
#  if CKFFT_ARM_NEON
    // on iOS, all armv7(s) devices support NEON
    neon = true;
#  endif
#endif

    return neon;
}
