#include "ckfft/debug.h"

#include <stdarg.h>
#include <stdio.h>

#if CKFFT_PLATFORM_ANDROID
#  include <android/log.h>
#elif CKFFT_PLATFORM_WIN
#  include <windows.h>
#endif


void CkDebugPrintf(const char* fmt, ...)
{
    va_list args;
    va_start(args, fmt);

#if CKFFT_PLATFORM_WIN
    char buf[4096];
    int n = _vsnprintf_s(buf, sizeof(buf), sizeof(buf)-1, fmt, args);
    buf[n] = '\0';
    OutputDebugString(buf);
    printf(buf);
#elif CKFFT_PLATFORM_ANDROID
    __android_log_vprint(ANDROID_LOG_INFO, "CKFFT", fmt, args);
#else
    vprintf(fmt, args);
#endif

    va_end(args);
}

