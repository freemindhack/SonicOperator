#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/fft.h"
#include "ckfft/fft_real_default.h"
#include "ckfft/fft_real_neon.h"
#include "ckfft/math_util.h"
#include "ckfft/context.h"


namespace ckfft
{

void fft_real(CkFftContext* context, 
         const float* input, 
         CkFftComplex* output, 
         int count)
{
    // handle trivial cases here, so we don't have to check for them in fft_real_default
    // scale by 2 to match what fft_real_default would produce
    if (count == 1)
    {
        output->real = *input * 2.0f;
        output->imag = 0.0f;
    }
    else if (count == 2)
    {
        // radix-2
        output[0].real = (input[0] + input[1]) * 2.0f;
        output[0].imag = 0.0f;
        output[1].real = (input[0] - input[1]) * 2.0f;
        output[1].imag = 0.0f;
    }
    else if (count == 4)
    {
        // radix-4 
        float sum02 = (input[0] + input[2]) * 2.0f;
        float diff02 = (input[0] - input[2]) * 2.0f;
        float sum13 = (input[1] + input[3]) * 2.0f;
        float diff13 = (input[1] - input[3]) * 2.0f;
        output[0].real = sum02 + sum13;
        output[0].imag = 0.0f;
        output[1].real = diff02;
        output[1].imag = -diff13;
        output[2].real = sum02 - sum13;
        output[2].imag = 0.0f;
        output[3].real = diff02;
        output[3].imag = diff13;
    }
    else
    {
#if 1
        // NEON enabled
        if (context->neon)
        {
            fft_real_neon(context, input, output, count);
        }
        else
        {
            fft_real_default(context, input, output, count);
        }
#else
        fft_real_default(context, input, output, count);
#endif
    }
}

void fft_real_inverse(CkFftContext* context, 
         const CkFftComplex* input, 
         float* output, 
         int count,
         CkFftComplex* tmpBuf)
{
    // handle trivial cases here, so we don't have to check for them in fft_real_default
    if (count == 1)
    {
        *output = input->real;
    }
    else if (count == 2)
    {
        // radix-2 
        output[0] = input[0].real + input[1].real;
        output[1] = input[0].real - input[1].real;
    }
    else if (count == 4)
    {
        // radix-4
        // note that input[3] = input[1]*
        float sum02_r = input[0].real + input[2].real;
        float sum13_r = 2.0f * input[1].real;
        CkFftComplex diff02;
        subtract(input[0], input[2], diff02);
        float diff13_i = 2.0f * input[1].imag;

        output[0] = sum02_r + sum13_r;
        output[1] = diff02.real - diff13_i;
        output[2] = sum02_r - sum13_r;
        output[3] = diff02.real + diff13_i;
    }
    else
    {
#if 1
        // NEON enabled
        if (context->neon)
        {
            fft_real_inverse_neon(context, input, output, count, tmpBuf);
        }
        else
        {
            fft_real_inverse_default(context, input, output, count, tmpBuf);
        }
#else
        fft_real_inverse_default(context, input, output, count, tmpBuf);
#endif
    }
}

} // namespace ckfft

