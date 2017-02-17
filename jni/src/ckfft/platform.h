#pragma once

// software platform
#undef CKFFT_PLATFORM_IOS
#undef CKFFT_PLATFORM_ANDROID
#undef CKFFT_PLATFORM_MACOS
#undef CKFFT_PLATFORM_WIN
#undef CKFFT_ARM_NEON

#if __APPLE__
#  include <TargetConditionals.h>
#  if TARGET_OS_IPHONE
#    define CKFFT_PLATFORM_IOS 1
#  else
#    define CKFFT_PLATFORM_MACOS 1
#  endif
#elif __ANDROID__
#  define CKFFT_PLATFORM_ANDROID 1
#elif defined(_WIN64) || defined(_WIN32)
#  define CKFFT_PLATFORM_WIN 1
#endif

#if __arm__ && __ARM_NEON__
#  define CKFFT_ARM_NEON 1
#endif

#if !CKFFT_PLATFORM_IOS && !CKFFT_PLATFORM_ANDROID && !CKFFT_PLATFORM_MACOS && !CKFFT_PLATFORM_WIN
#  error "Unsupported platform!"
#endif

namespace ckfft
{

typedef unsigned char       uchar;
typedef unsigned short      ushort;
typedef unsigned int        uint;
typedef unsigned long       ulong;

typedef signed char         int8;
typedef unsigned char       uint8;

typedef signed short        int16;
typedef unsigned short      uint16;

typedef signed int          int32;
typedef unsigned int        uint32;

typedef signed long long    int64;
typedef unsigned long long  uint64;

}
