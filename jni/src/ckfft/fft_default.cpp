#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/fft_default.h"
#include "ckfft/context.h"
#include "ckfft/math_util.h"
#include <assert.h>

namespace ckfft
{

// see http://www.cmlab.csie.ntu.edu.tw/cml/dsp/training/coding/transform/fft.html
void fft_default(
        CkFftContext* context, 
        const CkFftComplex* input, 
        CkFftComplex* output, 
        int count, 
        bool inverse, 
        int stride, 
        const CkFftComplex* expTable, 
        int expTableStride)
{
    if (count == 4)
    {
        // radix-4 recursion step for count == 4

        // this code will be called at the deepest recursion level for FFT sizes
        // that are a power of 4.

        const CkFftComplex* in = input;
        CkFftComplex* out = output;
        CkFftComplex* outEnd = out + 4;
        while (out < outEnd)
        {
            *out = *in; // inlined recursion step for count == 1
            in += stride;
            ++out;
        }

        CkFftComplex sum02, diff02, sum13, diff13;

        CkFftComplex* out0 = output;
        CkFftComplex* out1 = out0 + 1;
        CkFftComplex* out2 = out1 + 1;
        CkFftComplex* out3 = out2 + 1;

        add(*out0, *out2, sum02);
        subtract(*out0, *out2, diff02);
        add(*out1, *out3, sum13);
        subtract(*out1, *out3, diff13);

        add(sum02, sum13, *out0);
        subtract(sum02, sum13, *out2);
        if (inverse)
        {
            out1->real = diff02.real - diff13.imag;
            out1->imag = diff02.imag + diff13.real;
            out3->real = diff02.real + diff13.imag;
            out3->imag = diff02.imag - diff13.real;
        }
        else
        {
            out1->real = diff02.real + diff13.imag;
            out1->imag = diff02.imag - diff13.real;
            out3->real = diff02.real - diff13.imag;
            out3->imag = diff02.imag + diff13.real;
        }
    }
    else if (count == 8)
    {
        // radix-4 recursion step for count == 8, with loop unrolled

        // this code will be called at the deepest recursion level for FFT sizes
        // that are not a power of 4.

        // Having a special case for count == 8 only speeds things up by a few
        // percent here. In the NEON implementation, however, this case is required,
        // since the general case processes 4 elements at a time, and this case only
        // requires 2 elements.

        // calculate FFT of each 1/4
        const CkFftComplex* in0 = input;
        CkFftComplex* out = output;
        CkFftComplex* outEnd = out + 8;
        int stride4 = stride * 4;
        while (out < outEnd)
        {
            // inlined radix-2 recursion step for count == 2
            const CkFftComplex* in1 = in0 + stride4;
            add(*in0, *in1, out[0]);
            subtract(*in0, *in1, out[1]);

            in0 += stride;
            out += 2;
        }

        int expTableStride1 = stride * expTableStride;
        const CkFftComplex* exp1 = expTable + expTableStride1;
        const CkFftComplex* exp2 = exp1 + expTableStride1;
        const CkFftComplex* exp3 = exp2 + expTableStride1;

        CkFftComplex f1w, f2w2, f3w3;
        CkFftComplex sum02, diff02, sum13, diff13;

        CkFftComplex* out0 = output;
        CkFftComplex* out1 = out0 + 2;
        CkFftComplex* out2 = out1 + 2;
        CkFftComplex* out3 = out2 + 2;

        ////////////////////////////////////////
        // unrolled loop i=0

        add(*out0, *out2, sum02);
        subtract(*out0, *out2, diff02);
        add(*out1, *out3, sum13);
        subtract(*out1, *out3, diff13);

        add(sum02, sum13, *out0);
        subtract(sum02, sum13, *out2);
        if (inverse)
        {
            out1->real = diff02.real - diff13.imag;
            out1->imag = diff02.imag + diff13.real;
            out3->real = diff02.real + diff13.imag;
            out3->imag = diff02.imag - diff13.real;
        }
        else
        {
            out1->real = diff02.real + diff13.imag;
            out1->imag = diff02.imag - diff13.real;
            out3->real = diff02.real - diff13.imag;
            out3->imag = diff02.imag + diff13.real;
        }

        ++out0;
        ++out1;
        ++out2;
        ++out3;

        ////////////////////////////////////////
        // unrolled loop i=1

        multiply(*out1, *exp1, f1w);
        multiply(*out2, *exp2, f2w2);
        multiply(*out3, *exp3, f3w3);

        add(*out0, f2w2, sum02);
        subtract(*out0, f2w2, diff02);
        add(f1w, f3w3, sum13);
        subtract(f1w, f3w3, diff13);

        add(sum02, sum13, *out0);
        subtract(sum02, sum13, *out2);
        if (inverse)
        {
            out1->real = diff02.real - diff13.imag;
            out1->imag = diff02.imag + diff13.real;
            out3->real = diff02.real + diff13.imag;
            out3->imag = diff02.imag - diff13.real;
        }
        else
        {
            out1->real = diff02.real + diff13.imag;
            out1->imag = diff02.imag - diff13.real;
            out3->real = diff02.real - diff13.imag;
            out3->imag = diff02.imag + diff13.real;
        }
    }
    else
    {
        // radix-4
        assert((count & 0x3) == 0);

        int n = count / 4;
        int stride4 = stride * 4;

        // calculate FFT of each 1/4
        const CkFftComplex* in = input;
        CkFftComplex* out = output;
        CkFftComplex* outEnd = out + count;
        while (out < outEnd)
        {
            fft_default(context, in, out, n, inverse, stride4, expTable, expTableStride);
            in += stride;
            out += n;
        }

        const CkFftComplex* exp1 = expTable;
        const CkFftComplex* exp2 = exp1;
        const CkFftComplex* exp3 = exp1;
        int expTableStride1 = stride * expTableStride;
        int expTableStride2 = expTableStride1 * 2;
        int expTableStride3 = expTableStride1 * 3;

        CkFftComplex f1w, f2w2, f3w3;
        CkFftComplex sum02, diff02, sum13, diff13;

        CkFftComplex* out0 = output;
        CkFftComplex* out1 = out0 + n;
        CkFftComplex* out2 = out1 + n;
        CkFftComplex* out3 = out2 + n;

        for (int i = 0; i < n; ++i)
        {
            /*
               W = exp(-2*pi*I/N)

               X0 = F0 +   F1*W + F2*W2 +   F3*W3
               X1 = F0 - I*F1*W - F2*W2 + I*F3*W3
               X2 = F0 -   F1*W + F2*W2 -   F3*W3
               X3 = F0 + I*F1*W - F2*W2 - I*F3*W3

               X0 = (F0 + F2*W2) +   (F1*W + F3*W3) = sum02 + sum13
               X1 = (F0 - F2*W2) - I*(F1*W - F3*W3) = diff02 - I*diff13
               X2 = (F0 + F2*W2) -   (F1*W + F3*W3) = sum02 - sum13
               X3 = (F0 - F2*W2) + I*(F1*W - F3*W3) = diff02 + I*diff13

             */

            // f1w = F1*W
            // f2w2 = F2*W2
            // f3w3 = F3*W3
            multiply(*out1, *exp1, f1w);
            multiply(*out2, *exp2, f2w2);
            multiply(*out3, *exp3, f3w3);

            // sum02  = F0 + f2w2
            // diff02 = F0 - f2w2
            // sum13  = f1w + f3w3
            // diff13 = f1w - f3w3
            add(*out0, f2w2, sum02);
            subtract(*out0, f2w2, diff02);
            add(f1w, f3w3, sum13);
            subtract(f1w, f3w3, diff13);

            // x + I*y = (x.real + I*x.imag) + I*(y.real + I*y.imag)
            //         = x.real + I*x.imag + I*y.real - y.imag
            //         = (x.real - y.imag) + I*(x.imag + y.real)
            // x - I*y = (x.real + I*x.imag) - I*(y.real + I*y.imag)
            //         = x.real + I*x.imag - I*y.real + y.imag
            //         = (x.real + y.imag) + I*(x.imag - y.real)
            add(sum02, sum13, *out0);
            subtract(sum02, sum13, *out2);
            if (inverse)
            {
                out1->real = diff02.real - diff13.imag;
                out1->imag = diff02.imag + diff13.real;
                out3->real = diff02.real + diff13.imag;
                out3->imag = diff02.imag - diff13.real;
            }
            else
            {
                out1->real = diff02.real + diff13.imag;
                out1->imag = diff02.imag - diff13.real;
                out3->real = diff02.real - diff13.imag;
                out3->imag = diff02.imag + diff13.real;
            }

            exp1 += expTableStride1;
            exp2 += expTableStride2;
            exp3 += expTableStride3;

            ++out0;
            ++out1;
            ++out2;
            ++out3;
        }
        /*
        else
        {
            // radix-2 algorithm
            //
            // This is never actually used, apart from trivial count=2 case in fft(),
            // but is left here for reference.

            int n = count / 2;
            int stride2 = stride * 2;

            // DFT of even and odd elements
            fft_default(context, input, output, n, inverse, stride2, expTable, expTableStride);
            fft_default(context, input + stride, output + n, n, inverse, stride2, expTable, expTableStride);

            // combine
            CkFftComplex* out0 = output;
            CkFftComplex* out1 = output + n;
            CkFftComplex* out0End = output + n;
            CkFftComplex* exp = context->expTable;
            CkFftComplex tmp, b;
            while (out0 < out0End)
            {
                // tmp = output[i];
                // output[i]           = tmp + exp(-2*pi*I*i/count) * output[i + count/2];
                // output[i + count/2] = tmp - exp(-2*pi*I*i/count) * output[i + count/2];

                multiply(*exp, *out1, b);

                tmp = *out0;
                add(tmp, b, *out0);
                subtract(tmp, b, *out1);

                ++out0;
                ++out1;
                exp += stride;
            }
        }
        */
    }
}

} // namespace ckfft
