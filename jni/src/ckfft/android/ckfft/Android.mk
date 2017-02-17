LOCAL_PATH := $(call my-dir)

#################################################

include $(CLEAR_VARS)

LOCAL_MODULE := ckfft

LOCAL_SRC_FILES := \
    ../../ckfft.cpp \
    ../../context.cpp \
    ../../debug.cpp \
    ../../fft.cpp \
    ../../fft_default.cpp \
    ../../fft_real.cpp \
    ../../fft_real_default.cpp 

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
LOCAL_SRC_FILES += \
    ../../fft_neon.cpp.neon \
    ../../fft_real_neon.cpp.neon
else
LOCAL_SRC_FILES += \
    ../../fft_neon.cpp \
    ../../fft_real_neon.cpp
endif
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../../inc $(LOCAL_PATH)/../../../../src
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../../inc
LOCAL_STATIC_LIBRARIES := cpufeatures

include $(BUILD_STATIC_LIBRARY)

$(call import-module,android/cpufeatures)



