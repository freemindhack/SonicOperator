#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/context.h"
#include "ckfft/math_util.h"
#include <assert.h>

#if CKFFT_ARM_NEON
#  include <arm_neon.h>
#endif 

namespace ckfft
{

#if CKFFT_ARM_NEON

void fft_neon(
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
        const CkFftComplex* in = input;
        CkFftComplex* out = output;
        CkFftComplex* outEnd = out + 4;
        while (out < outEnd)
        {
            *out = *in;
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
        const CkFftComplex* in0 = input;
        CkFftComplex* out = output;
        CkFftComplex* outEnd = out + 8;
        int stride4 = stride * 4;
        while (out < outEnd)
        {
            const CkFftComplex* in1 = in0 + stride4;
            add(*in0, *in1, out[0]);
            subtract(*in0, *in1, out[1]);

            in0 += stride;
            out += 2;
        }

        int expTableStride1 = stride * expTableStride;
        const CkFftComplex* exp = expTable;

        CkFftComplex* out0 = output;
        CkFftComplex* out1 = out0 + 2;
        CkFftComplex* out2 = out1 + 2;
        CkFftComplex* out3 = out2 + 2;

        float32x2x2_t f1w_v, f2w2_v, f3w3_v;
        float32x2x2_t sum02_v, diff02_v, sum13_v, diff13_v;

        float32x2x2_t out0_v = vld2_f32((const float32_t*) out0);
        float32x2x2_t out1_v = vld2_f32((const float32_t*) out1);
        float32x2x2_t out2_v = vld2_f32((const float32_t*) out2);
        float32x2x2_t out3_v = vld2_f32((const float32_t*) out3);

        float32x2x2_t exp1_v, exp2_v, exp3_v;
        exp1_v = vld2_lane_f32((const float32_t*) exp, exp1_v, 0);
        exp += expTableStride1;
        exp1_v = vld2_lane_f32((const float32_t*) exp, exp1_v, 1);
        exp2_v = exp1_v;
        exp += expTableStride1;
        exp2_v = vld2_lane_f32((const float32_t*) exp, exp2_v, 1);
        exp3_v = exp1_v;
        exp += expTableStride1;
        exp3_v = vld2_lane_f32((const float32_t*) exp, exp3_v, 1);

        multiply(out1_v, exp1_v, f1w_v);
        multiply(out2_v, exp2_v, f2w2_v);
        multiply(out3_v, exp3_v, f3w3_v);

        add(out0_v, f2w2_v, sum02_v);
        subtract(out0_v, f2w2_v, diff02_v);
        add(f1w_v, f3w3_v, sum13_v);
        subtract(f1w_v, f3w3_v, diff13_v);

        add(sum02_v, sum13_v, out0_v);
        subtract(sum02_v, sum13_v, out2_v);

        // TODO optimize this?
        if (inverse)
        {
            out1_v.val[0] = vsub_f32(diff02_v.val[0], diff13_v.val[1]);
            out1_v.val[1] = vadd_f32(diff02_v.val[1], diff13_v.val[0]);
            out3_v.val[0] = vadd_f32(diff02_v.val[0], diff13_v.val[1]);
            out3_v.val[1] = vsub_f32(diff02_v.val[1], diff13_v.val[0]);
        }
        else
        {
            out1_v.val[0] = vadd_f32(diff02_v.val[0], diff13_v.val[1]);
            out1_v.val[1] = vsub_f32(diff02_v.val[1], diff13_v.val[0]);
            out3_v.val[0] = vsub_f32(diff02_v.val[0], diff13_v.val[1]);
            out3_v.val[1] = vadd_f32(diff02_v.val[1], diff13_v.val[0]);
        }

        vst2_f32((float32_t*) out0, out0_v);
        vst2_f32((float32_t*) out1, out1_v);
        vst2_f32((float32_t*) out2, out2_v);
        vst2_f32((float32_t*) out3, out3_v);
    }
    else
    {
        assert((count & 0x3) == 0);

        int n = count / 4;

        const CkFftComplex* in = input;
        CkFftComplex* out = output;
        CkFftComplex* outEnd = out + count;
        int stride4 = stride * 4;
        while (out < outEnd)
        {
            fft_neon(context, in, out, n, inverse, stride4, expTable, expTableStride);
            in += stride;
            out += n;
        }

        const CkFftComplex* exp1 = expTable;
        const CkFftComplex* exp2 = exp1;
        const CkFftComplex* exp3 = exp1;
        int expTableStride1 = stride * expTableStride;
        int expTableStride2 = expTableStride1 * 2;
        int expTableStride3 = expTableStride1 * 3;

        CkFftComplex* out0 = output;
        CkFftComplex* out1 = out0 + n;
        CkFftComplex* out2 = out1 + n;
        CkFftComplex* out3 = out2 + n;

        float32x4x2_t f1w_v, f2w2_v, f3w3_v;
        float32x4x2_t sum02_v, diff02_v, sum13_v, diff13_v;


        int m = n/4;
        for (int i = 0; i < m; ++i)
        {
            float32x4x2_t out0_v = vld2q_f32((const float32_t*) out0);
            float32x4x2_t out1_v = vld2q_f32((const float32_t*) out1);
            float32x4x2_t out2_v = vld2q_f32((const float32_t*) out2);
            float32x4x2_t out3_v = vld2q_f32((const float32_t*) out3);

            float32x4x2_t exp1_v;
            exp1_v = vld2q_lane_f32((const float32_t*) exp1, exp1_v, 0);
            exp1 += expTableStride1;
            exp1_v = vld2q_lane_f32((const float32_t*) exp1, exp1_v, 1);
            exp1 += expTableStride1;
            exp1_v = vld2q_lane_f32((const float32_t*) exp1, exp1_v, 2);
            exp1 += expTableStride1;
            exp1_v = vld2q_lane_f32((const float32_t*) exp1, exp1_v, 3);
            exp1 += expTableStride1;

            float32x4x2_t exp2_v;
            exp2_v = vld2q_lane_f32((const float32_t*) exp2, exp2_v, 0);
            exp2 += expTableStride2;
            exp2_v = vld2q_lane_f32((const float32_t*) exp2, exp2_v, 1);
            exp2 += expTableStride2;
            exp2_v = vld2q_lane_f32((const float32_t*) exp2, exp2_v, 2);
            exp2 += expTableStride2;
            exp2_v = vld2q_lane_f32((const float32_t*) exp2, exp2_v, 3);
            exp2 += expTableStride2;

            float32x4x2_t exp3_v;
            exp3_v = vld2q_lane_f32((const float32_t*) exp3, exp3_v, 0);
            exp3 += expTableStride3;
            exp3_v = vld2q_lane_f32((const float32_t*) exp3, exp3_v, 1);
            exp3 += expTableStride3;
            exp3_v = vld2q_lane_f32((const float32_t*) exp3, exp3_v, 2);
            exp3 += expTableStride3;
            exp3_v = vld2q_lane_f32((const float32_t*) exp3, exp3_v, 3);
            exp3 += expTableStride3;

            // TODO use vmla, vmls?
            // alignment?

            multiply(out1_v, exp1_v, f1w_v);
            multiply(out2_v, exp2_v, f2w2_v);
            multiply(out3_v, exp3_v, f3w3_v);

            add(out0_v, f2w2_v, sum02_v);
            subtract(out0_v, f2w2_v, diff02_v);
            add(f1w_v, f3w3_v, sum13_v);
            subtract(f1w_v, f3w3_v, diff13_v);

            add(sum02_v, sum13_v, out0_v);
            subtract(sum02_v, sum13_v, out2_v);

            // TODO optimize this?
            if (inverse)
            {
                out1_v.val[0] = vsubq_f32(diff02_v.val[0], diff13_v.val[1]);
                out1_v.val[1] = vaddq_f32(diff02_v.val[1], diff13_v.val[0]);
                out3_v.val[0] = vaddq_f32(diff02_v.val[0], diff13_v.val[1]);
                out3_v.val[1] = vsubq_f32(diff02_v.val[1], diff13_v.val[0]);
            }
            else
            {
                out1_v.val[0] = vaddq_f32(diff02_v.val[0], diff13_v.val[1]);
                out1_v.val[1] = vsubq_f32(diff02_v.val[1], diff13_v.val[0]);
                out3_v.val[0] = vsubq_f32(diff02_v.val[0], diff13_v.val[1]);
                out3_v.val[1] = vaddq_f32(diff02_v.val[1], diff13_v.val[0]);
            }

            vst2q_f32((float32_t*) out0, out0_v);
            vst2q_f32((float32_t*) out1, out1_v);
            vst2q_f32((float32_t*) out2, out2_v);
            vst2q_f32((float32_t*) out3, out3_v);

            out0 += 4;
            out1 += 4;
            out2 += 4;
            out3 += 4;
        }
    }
}

#else // CKFFT_ARM_NEON

void fft_neon(
        CkFftContext* context, 
        const CkFftComplex* input, 
        CkFftComplex* output, 
        int count, 
        bool inverse,
        int stride, 
        const CkFftComplex* expTable,
        int expTableStride)
{}

#endif // CKFFT_ARM_NEON

} // namespace ckfft



