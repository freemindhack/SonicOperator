#pragma once
#include "ckfft/platform.h"


void CkDebugPrintf(const char* fmt, ...);

#define CKFFT_PRINTF(fmt, ...) CkDebugPrintf(fmt, ##__VA_ARGS__)



