#pragma once

#include "ckfft/ckfft.h"
#include "ckfft/platform.h"

#if CKFFT_ARM_NEON
#  include <arm_neon.h>
#endif

namespace ckfft
{
    inline bool isPowerOfTwo(unsigned int x)
    {
        return ((x != 0) && !(x & (x - 1)));
    }

    inline void add(const CkFftComplex& a, const CkFftComplex& b, CkFftComplex& out)
    {
        out.real = a.real + b.real;
        out.imag = a.imag + b.imag;
    }

    inline void subtract(const CkFftComplex& a, const CkFftComplex& b, CkFftComplex& out)
    {
        out.real = a.real - b.real;
        out.imag = a.imag - b.imag;
    }

    inline void multiply(const CkFftComplex& a, const CkFftComplex& b, CkFftComplex& out)
    {
        out.real = a.real * b.real - a.imag * b.imag;
        out.imag = a.imag * b.real + a.real * b.imag;
    }

#if CKFFT_ARM_NEON
    inline void multiply(const float32x4x2_t& x, const float32x4x2_t& y, float32x4x2_t& out)
    {
        // (a + bi)(c + di) = (ac - bd) + (bc + ad)i
        float32x4_t ac = vmulq_f32(x.val[0], y.val[0]);
        float32x4_t bd = vmulq_f32(x.val[1], y.val[1]);
        float32x4_t bc = vmulq_f32(x.val[1], y.val[0]);
        float32x4_t ad = vmulq_f32(x.val[0], y.val[1]);
        out.val[0] = vsubq_f32(ac, bd);
        out.val[1] = vaddq_f32(bc, ad);
    }

    inline void multiply(const float32x2x2_t& x, const float32x2x2_t& y, float32x2x2_t& out)
    {
        // (a + bi)(c + di) = (ac - bd) + (bc + ad)i
        float32x2_t ac = vmul_f32(x.val[0], y.val[0]);
        float32x2_t bd = vmul_f32(x.val[1], y.val[1]);
        float32x2_t bc = vmul_f32(x.val[1], y.val[0]);
        float32x2_t ad = vmul_f32(x.val[0], y.val[1]);
        out.val[0] = vsub_f32(ac, bd);
        out.val[1] = vadd_f32(bc, ad);
    }

    inline void add(const float32x4x2_t& x, const float32x4x2_t& y, float32x4x2_t& out)
    {
        out.val[0] = vaddq_f32(x.val[0], y.val[0]);
        out.val[1] = vaddq_f32(x.val[1], y.val[1]);
    }

    inline void add(const float32x2x2_t& x, const float32x2x2_t& y, float32x2x2_t& out)
    {
        out.val[0] = vadd_f32(x.val[0], y.val[0]);
        out.val[1] = vadd_f32(x.val[1], y.val[1]);
    }

    inline void subtract(const float32x4x2_t& x, const float32x4x2_t& y, float32x4x2_t& out)
    {
        out.val[0] = vsubq_f32(x.val[0], y.val[0]);
        out.val[1] = vsubq_f32(x.val[1], y.val[1]);
    }

    inline void subtract(const float32x2x2_t& x, const float32x2x2_t& y, float32x2x2_t& out)
    {
        out.val[0] = vsub_f32(x.val[0], y.val[0]);
        out.val[1] = vsub_f32(x.val[1], y.val[1]);
    }

#endif


}
