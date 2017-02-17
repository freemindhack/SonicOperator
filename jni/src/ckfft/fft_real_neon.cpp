#include "ckfft/platform.h"
#include "ckfft/debug.h"
#include "ckfft/fft_neon.h"
#include "ckfft/context.h"
#include "ckfft/math_util.h"
#include <assert.h>

#if CKFFT_ARM_NEON
#  include <arm_neon.h>
#endif

namespace ckfft
{

#if CKFFT_ARM_NEON

void fft_real_neon(
        CkFftContext* context, 
        const float* input, 
        CkFftComplex* output, 
        int count)
{
    int countDiv2 = count/2;

    fft_neon(context, (const CkFftComplex*) input, output, countDiv2, false, 1, context->fwdExpTable, context->maxCount / countDiv2);

    output[countDiv2] = output[0];

    int expTableStride = context->maxCount/count;
    const CkFftComplex* exp0 = context->fwdExpTable;
    const CkFftComplex* exp1 = context->fwdExpTable + countDiv2 * expTableStride;

    CkFftComplex* p0 = output;
    CkFftComplex* p1 = output + countDiv2 - 3;
    const CkFftComplex* pEnd = p0 + count/4;
    while (p0 < pEnd)
    {
        float32x4x2_t z0_v = vld2q_f32((const float32_t*) p0);
        float32x4x2_t z1_v = vld2q_f32((const float32_t*) p1);

        float32x2_t hi, lo;

        // reverse z1 real
        z1_v.val[0] = vrev64q_f32(z1_v.val[0]);
        hi = vget_high_f32(z1_v.val[0]);
        lo = vget_low_f32(z1_v.val[0]);
        z1_v.val[0] = vcombine_f32(hi, lo);

        // reverse z1 imaginary
        z1_v.val[1] = vrev64q_f32(z1_v.val[1]);
        hi = vget_high_f32(z1_v.val[1]);
        lo = vget_low_f32(z1_v.val[1]);
        z1_v.val[1] = vcombine_f32(hi, lo);

        float32x4x2_t sum_v;
        sum_v.val[0] = vaddq_f32(z0_v.val[0], z1_v.val[0]);
        sum_v.val[1] = vsubq_f32(z0_v.val[1], z1_v.val[1]);

        float32x4x2_t diff_v;
        diff_v.val[0] = vsubq_f32(z0_v.val[0], z1_v.val[0]);
        diff_v.val[1] = vaddq_f32(z0_v.val[1], z1_v.val[1]);

        float32x4x2_t exp_v;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 0);
        exp0 += expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 1);
        exp0 += expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 2);
        exp0 += expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 3);
        exp0 += expTableStride;

        float32x4x2_t f_v;
        f_v.val[0] = vnegq_f32(exp_v.val[1]);
        f_v.val[1] = exp_v.val[0];

        float32x4x2_t c_v;
        multiply(f_v, diff_v, c_v);
        subtract(sum_v, c_v, z0_v);
        vst2q_f32((float32_t*) p0, z0_v);

        diff_v.val[0] = vnegq_f32(diff_v.val[0]);
        sum_v.val[1] = vnegq_f32(sum_v.val[1]);

        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 0);
        exp1 -= expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 1);
        exp1 -= expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 2);
        exp1 -= expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 3);
        exp1 -= expTableStride;

        f_v.val[0] = vnegq_f32(exp_v.val[1]);
        f_v.val[1] = exp_v.val[0];

        multiply(f_v, diff_v, c_v);
        subtract(sum_v, c_v, z1_v);

        // reverse z1 real
        z1_v.val[0] = vrev64q_f32(z1_v.val[0]);
        hi = vget_high_f32(z1_v.val[0]);
        lo = vget_low_f32(z1_v.val[0]);
        z1_v.val[0] = vcombine_f32(hi, lo);

        // reverse z1 imaginary
        z1_v.val[1] = vrev64q_f32(z1_v.val[1]);
        hi = vget_high_f32(z1_v.val[1]);
        lo = vget_low_f32(z1_v.val[1]);
        z1_v.val[1] = vcombine_f32(hi, lo);

        vst2q_f32((float32_t*) p1, z1_v);

        p0 += 4;
        p1 -= 4;
    }

    if (count > 8)
    {
        // middle:
        p0->real = p0->real * 2.0f;
        p0->imag = -p0->imag * 2.0f;
    }
}

void fft_real_inverse_neon(
        CkFftContext* context, 
        const CkFftComplex* input, 
        float* output, 
        int count,
        CkFftComplex* tmpBuf)
{
    int countDiv2 = count/2;

    int expTableStride = context->maxCount/count;
    const CkFftComplex* exp0 = context->invExpTable;
    const CkFftComplex* exp1 = context->invExpTable + countDiv2 * expTableStride;

    const CkFftComplex* p0 = input;
    const CkFftComplex* p1 = input + countDiv2 - 3;
    CkFftComplex* tmp0 = tmpBuf;
    CkFftComplex* tmp1 = tmpBuf + countDiv2 - 3;
    const CkFftComplex* pEnd = p0 + count/4;
    while (p0 < pEnd)
    {
        float32x4x2_t z0_v = vld2q_f32((const float32_t*) p0);
        float32x4x2_t z1_v = vld2q_f32((const float32_t*) p1);

        float32x2_t hi, lo;

        // reverse z1 real
        z1_v.val[0] = vrev64q_f32(z1_v.val[0]);
        hi = vget_high_f32(z1_v.val[0]);
        lo = vget_low_f32(z1_v.val[0]);
        z1_v.val[0] = vcombine_f32(hi, lo);

        // reverse z1 imaginary
        z1_v.val[1] = vrev64q_f32(z1_v.val[1]);
        hi = vget_high_f32(z1_v.val[1]);
        lo = vget_low_f32(z1_v.val[1]);
        z1_v.val[1] = vcombine_f32(hi, lo);

        float32x4x2_t sum_v;
        sum_v.val[0] = vaddq_f32(z0_v.val[0], z1_v.val[0]);
        sum_v.val[1] = vsubq_f32(z0_v.val[1], z1_v.val[1]);

        float32x4x2_t diff_v;
        diff_v.val[0] = vsubq_f32(z0_v.val[0], z1_v.val[0]);
        diff_v.val[1] = vaddq_f32(z0_v.val[1], z1_v.val[1]);

        float32x4x2_t exp_v;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 0);
        exp0 += expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 1);
        exp0 += expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 2);
        exp0 += expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp0, exp_v, 3);
        exp0 += expTableStride;

        float32x4x2_t f_v;
        f_v.val[0] = vnegq_f32(exp_v.val[1]);
        f_v.val[1] = exp_v.val[0];

        float32x4x2_t c_v;
        multiply(f_v, diff_v, c_v);
        add(sum_v, c_v, z0_v);
        vst2q_f32((float32_t*) tmp0, z0_v);

        diff_v.val[0] = vnegq_f32(diff_v.val[0]);
        sum_v.val[1] = vnegq_f32(sum_v.val[1]);

        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 0);
        exp1 -= expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 1);
        exp1 -= expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 2);
        exp1 -= expTableStride;
        exp_v = vld2q_lane_f32((const float32_t*) exp1, exp_v, 3);
        exp1 -= expTableStride;

        f_v.val[0] = vnegq_f32(exp_v.val[1]);
        f_v.val[1] = exp_v.val[0];

        multiply(f_v, diff_v, c_v);
        add(sum_v, c_v, z1_v);

        // reverse z1 real
        z1_v.val[0] = vrev64q_f32(z1_v.val[0]);
        hi = vget_high_f32(z1_v.val[0]);
        lo = vget_low_f32(z1_v.val[0]);
        z1_v.val[0] = vcombine_f32(hi, lo);

        // reverse z1 imaginary
        z1_v.val[1] = vrev64q_f32(z1_v.val[1]);
        hi = vget_high_f32(z1_v.val[1]);
        lo = vget_low_f32(z1_v.val[1]);
        z1_v.val[1] = vcombine_f32(hi, lo);

        vst2q_f32((float32_t*) tmp1, z1_v);

        p0 += 4;
        tmp0 += 4;
        p1 -= 4;
        tmp1 -= 4;
    }

    // middle:
    tmp0->real = p0->real * 2.0f;
    tmp0->imag = -p0->imag * 2.0f;

    fft_neon(context, tmpBuf, (CkFftComplex*) output, countDiv2, true, 1, context->invExpTable, context->maxCount / countDiv2);
}

#else

void fft_real_neon(
        CkFftContext* context, 
        const float* input, 
        CkFftComplex* output, 
        int count)
{}

void fft_real_inverse_neon(
        CkFftContext* context, 
        const CkFftComplex* input, 
        float* output, 
        int count,
        CkFftComplex* tmpBuf)
{}

#endif

} // namespace ckfft
