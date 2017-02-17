#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/fft.h"
#include "ckfft/fft_neon.h"
#include "ckfft/fft_default.h"
#include "ckfft/math_util.h"
#include "ckfft/context.h"


namespace ckfft
{

void fft(CkFftContext* context, 
         const CkFftComplex* input, 
         CkFftComplex* output, 
         int count,
         bool inverse)
{
    // handle trivial cases here, so we don't have to check for them in fft_default
    if (count == 1)
    {
        *output = *input;
    }
    else if (count == 2)
    {
        // radix-2 
        // output[0] = input[0] + input[stride];
        // output[1] = input[0] - input[stride];
        add(input[0], input[1], output[0]);
        subtract(input[0], input[1], output[1]);
    }
    else
    {
        const CkFftComplex* expTable = (inverse ? context->invExpTable : context->fwdExpTable);
        int expTableStride = context->maxCount / count;
#if 1
        // NEON enabled
        if (context->neon)
        {
            fft_neon(context, input, output, count, inverse, 1, expTable, expTableStride);
        }
        else
        {
            fft_default(context, input, output, count, inverse, 1, expTable, expTableStride);
        }
#else
        fft_default(context, input, output, count, inverse, 1, expTable, expTableStride);
#endif
    }
}

} // namespace ckfft

