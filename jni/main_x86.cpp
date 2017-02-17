#include "ipp.h"
#include <jni.h>
#include <math.h>
#include <string.h>

#ifndef CKFFT_PRINTF
#  include <android/log.h>
#  define CKFFT_PRINTF(fmt, ...) __android_log_print(ANDROID_LOG_INFO, "CKFFT", fmt, ##__VA_ARGS__)
#endif

#ifndef FFTSIZE
#define FFTSIZE 4096
#endif

#ifndef WINSIZE
#define WINSIZE 2048
#endif

#ifndef WINNUM
#define WINNUM FFTSIZE/WINSIZE
#endif

//Set the size
const int N=FFTSIZE;
const int order=(int)(log((double)N)/log(2.0));

int window = -1;
float input[FFTSIZE*2];
// Allocate complex buffers
Ipp32fc *pSrc=ippsMalloc_32fc(N);
Ipp32fc *pDst=ippsMalloc_32fc(N); 


jint IPPFftComplexToJInt(Ipp32fc& complex)
{
    return (jint) floor(sqrt(complex.re*complex.re+complex.im*complex.im));// + complex.imag*complex.imag
}

int fft(jshort *input_shorts,jint *output,jint count)
{
    //CKFFT_PRINTF("fft start");
    int rs;

    //CKFFT_PRINTF("trans input start");
    float *readbuffer = NULL;
    float *copybuffer = NULL;
    float *inputbuffer = NULL;

    window++;
    if(window < WINNUM){
        readbuffer = input + window*WINSIZE;

        for (int i = 0; i < WINSIZE; i++)
        {
            readbuffer[i] = input_shorts[i];
            readbuffer[i] = readbuffer[i]/100;
        }
    }else{
        //CKFFT_PRINTF("window:%d",window);

        readbuffer = input + window*WINSIZE;
        copybuffer = input + (window - WINNUM)*WINSIZE;
        inputbuffer = copybuffer + WINSIZE;
        if(window == WINNUM*2-1) window -= WINNUM;
        for (int i = 0; i < WINSIZE; i++)
        {
            readbuffer[i] = input_shorts[i];
            readbuffer[i] = readbuffer[i]/100;

        }
        //CKFFT_PRINTF("before memcpy");
        memcpy(copybuffer,readbuffer,sizeof(float)*WINSIZE);
        //CKFFT_PRINTF("after memcpy");
    }

    if(window < WINNUM-1)
    {
        rs = 0;
        return rs;
    }

    int i = 0;

    // Spec and working buffers
    IppsFFTSpec_C_32fc *pFFTSpec=0;
    Ipp8u *pFFTSpecBuf, *pFFTInitBuf, *pFFTWorkBuf;

    
    for (i = 0; i < N; ++i) {
        pSrc[i].re = 2*input[i];
        pSrc[i].im = 0;
    }

    // Query to get buffer sizes
    int sizeFFTSpec,sizeFFTInitBuf,sizeFFTWorkBuf;
    ippsFFTGetSize_C_32fc(order, IPP_FFT_NODIV_BY_ANY, 
        ippAlgHintAccurate, &sizeFFTSpec, &sizeFFTInitBuf, &sizeFFTWorkBuf);

    // Alloc FFT buffers
    pFFTSpecBuf = ippsMalloc_8u(sizeFFTSpec);
    pFFTInitBuf = ippsMalloc_8u(sizeFFTInitBuf);
    pFFTWorkBuf = ippsMalloc_8u(sizeFFTWorkBuf);

    // Initialize FFT
    ippsFFTInit_C_32fc(&pFFTSpec, order, IPP_FFT_NODIV_BY_ANY, 
        ippAlgHintAccurate, pFFTSpecBuf, pFFTInitBuf);
    if (pFFTInitBuf) ippFree(pFFTInitBuf);

    // Do the FFT
    ippsFFTFwd_CToC_32fc(pSrc,pDst,pFFTSpec,pFFTWorkBuf);

    //check results

    // for (i=0;i<N && i<10;i++) {
    //     MY_PRINTF("%f, %f", pDst[i].re, pDst[i].im);
    // }

    for (i = 0; i < FFTSIZE/2 + 1; ++i){
        ////CKFFT_PRINTF("%d: ( %f, %f )\n", i ,forwardOutput[i].real, forwardOutput[i].imag);
        output[i] = IPPFftComplexToJInt(pDst[i]);
        //CKFFT_PRINTF("%d: [%d]\n", i ,output[i]);
    }

    if (pFFTWorkBuf) ippFree(pFFTWorkBuf);
    if (pFFTSpecBuf) ippFree(pFFTSpecBuf);

    // ippFree(pSrc);
    // ippFree(pDst);

    rs = 1;

    return rs;
}

