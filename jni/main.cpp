#include "ckfft/ckfft.h"
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

int window = -1;
CkFftComplex forwardOutput[FFTSIZE/2 + 1];
float input[FFTSIZE*2];


jint CkFftComplexToJInt(CkFftComplex& complex)
{
    return (jint) floor(sqrt(complex.real*complex.real+complex.imag*complex.imag));// + complex.imag*complex.imag
}

int fft(jshort *input_shorts,jint *output,jint count)
{
    //CKFFT_PRINTF("fft start");
    int rs;

    memset(forwardOutput, 0, sizeof(CkFftComplex)*(FFTSIZE/2 + 1)); 
    //memset(input, 0, sizeof(float)*(FFTSIZE*2));

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

    //CKFFT_PRINTF("trans input end");

    CkFftContext* context = CkFftInit(FFTSIZE, kCkFftDirection_Both, NULL, NULL);

    rs = CkFftRealForward(context, FFTSIZE, input, forwardOutput);

    //CKFFT_PRINTF("rs: %d\n", rs);

    if(rs == 1)
    {
        //CKFFT_PRINTF("before copy result");
        for (i = 0; i < FFTSIZE/2 + 1; ++i)
        {
            ////CKFFT_PRINTF("%d: ( %f, %f )\n", i ,forwardOutput[i].real, forwardOutput[i].imag);
            output[i] = CkFftComplexToJInt(forwardOutput[i]);
            //CKFFT_PRINTF("%d: [%d]\n", i ,output[i]);
        }
    }
    CkFftShutdown(context);

    return rs;
}

