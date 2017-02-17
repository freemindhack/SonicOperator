#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/fft_default.h"
#include "ckfft/context.h"
#include "ckfft/math_util.h"
#include <assert.h>

namespace ckfft
{

// see http://www.engineeringproductivitytools.com/stuff/T0001/PT10.HTM

void fft_real_default(
        CkFftContext* context, 
        const float* input, 
        CkFftComplex* output, 
        int count)
{
    int countDiv2 = count / 2;

    fft_default(context, (const CkFftComplex*) input, output, countDiv2, false, 1, context->fwdExpTable, context->maxCount / countDiv2);

    output[countDiv2] = output[0];

    int expTableStride = context->maxCount/count;
    const CkFftComplex* exp0 = context->fwdExpTable;
    const CkFftComplex* exp1 = context->fwdExpTable + countDiv2 * expTableStride;

    int countDiv4 = count / 4;
    for (int i = 0; i < countDiv4; ++i)
    {
        CkFftComplex z0 = output[i];
        CkFftComplex z1 = output[countDiv2 - i];

        CkFftComplex sum;
        CkFftComplex diff;
        CkFftComplex f;
        CkFftComplex c;

        sum.real = z0.real + z1.real;
        sum.imag = z0.imag - z1.imag;
        diff.real = z0.real - z1.real;
        diff.imag = z0.imag + z1.imag;
        f.real = -(exp0->imag);
        f.imag = exp0->real;
        multiply(f, diff, c);
        subtract(sum, c, output[i]);

        diff.real = -diff.real;
        sum.imag = -sum.imag;
        f.real = -(exp1->imag);
        f.imag = exp1->real;
        multiply(f, diff, c);
        subtract(sum, c, output[countDiv2 - i]);

        exp0 += expTableStride;
        exp1 -= expTableStride;
    }

    // middle:
    output[countDiv4].real = output[countDiv4].real * 2.0f;
    output[countDiv4].imag = -output[countDiv4].imag * 2.0f;
}

void fft_real_inverse_default(
        CkFftContext* context, 
        const CkFftComplex* input, 
        float* output, 
        int count,
        CkFftComplex* tmpBuf)
{
    int countDiv2 = count / 2;

    int expTableStride = context->maxCount/count;
    const CkFftComplex* exp0 = context->invExpTable;
    const CkFftComplex* exp1 = context->invExpTable + countDiv2 * expTableStride;

    int countDiv4 = count / 4;
    for (int i = 0; i < countDiv4; ++i)
    {
        CkFftComplex z0 = input[i];
        CkFftComplex z1 = input[countDiv2 - i];

        CkFftComplex sum;
        CkFftComplex diff;
        CkFftComplex f;
        CkFftComplex c;

        sum.real = z0.real + z1.real;
        sum.imag = z0.imag - z1.imag;
        diff.real = z0.real - z1.real;
        diff.imag = z0.imag + z1.imag;
        f.real = -(exp0->imag);
        f.imag = exp0->real;
        multiply(f, diff, c);
        add(sum, c, tmpBuf[i]);

        diff.real = -diff.real;
        sum.imag = -sum.imag;
        f.real = -(exp1->imag);
        f.imag = exp1->real;
        multiply(f, diff, c);
        add(sum, c, tmpBuf[countDiv2 - i]);

        exp0 += expTableStride;
        exp1 -= expTableStride;
    }

    // middle:
    tmpBuf[countDiv4].real = input[countDiv4].real * 2.0f;
    tmpBuf[countDiv4].imag = -input[countDiv4].imag * 2.0f;

    fft_default(context, tmpBuf, (CkFftComplex*) output, countDiv2, true, 1, context->invExpTable, context->maxCount / countDiv2);
}

} // namespace ckfft
