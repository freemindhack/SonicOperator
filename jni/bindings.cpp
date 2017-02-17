#include <jni.h>
#include <stddef.h>
#include <android/log.h>

#ifndef CKFFT_PRINTF
#  define CKFFT_PRINTF(fmt, ...) __android_log_print(ANDROID_LOG_INFO, "CKFFT", fmt, ##__VA_ARGS__)
#endif

#ifndef FFTSIZE
#define FFTSIZE 4096
#endif

int fft(jshort *input,jint *output,int count);

extern "C"
{

JNIEXPORT jintArray JNICALL
Java_sdu_embedded_Sonic_core_FFT_fft(JNIEnv *env, jobject obj, jshortArray jshort_array, jint count)
{
    // CKFFT_PRINTF("bindings start");
	int rs;
	
	jint *output = new jint[FFTSIZE/2 + 1];
    jshort *input = env->GetShortArrayElements(jshort_array, 0);

    // CKFFT_PRINTF("before fft start");
    rs = fft(input,output,count);
    // CKFFT_PRINTF("after fft end");
    jintArray jint_array;
    if(rs == 1)
    {
        //// CKFFT_PRINTF("rs == 1");
        jint_array = env->NewIntArray(FFTSIZE/2 + 1);
		env->SetIntArrayRegion(jint_array, 0, FFTSIZE/2 + 1, output);
		//env->ReleaseIntArrayElements (jint_array, jint_tmp, count/2 + 1);
    }
    else
    {
        //// CKFFT_PRINTF("rs == 0");
    	jint_array = env->NewIntArray(1);
    }

    // CKFFT_PRINTF("before clean");
    //delete [] input;
    env->ReleaseShortArrayElements(jshort_array, input, 0);
    //env->DeleteLocalRef(jshort_array);
    delete [] output;
    // CKFFT_PRINTF("after clean");

    return jint_array;
}

}


