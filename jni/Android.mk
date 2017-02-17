LOCAL_PATH := $(call my-dir)

ifeq ($(TARGET_ARCH_ABI),x86)
#################################################

#include $(CLEAR_VARS)
#LOCAL_MODULE := dolphin_x86
#LOCAL_SRC_FILES := bindings.cpp main_x86.cpp
#LOCAL_LDLIBS += -llog -lm -ljnigraphics -ipp

#LOCAL_CFLAGS += -Wno-psabi -march=atom -mtune=atom
#LOCAL_C_INCLUDES := $(LOCAL_PATH)

#include $(BUILD_SHARED_LIBRARY)

#################################################
else
#################################################

include $(CLEAR_VARS)
LOCAL_MODULE := dolphin
LOCAL_SRC_FILES := bindings.cpp main.cpp
LOCAL_LDLIBS += -llog

LOCAL_CFLAGS += -Wno-psabi # fix warning about va_args in ndk r8b
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_STATIC_LIBRARIES := ckfft 

include $(BUILD_SHARED_LIBRARY)

$(call import-add-path,$(LOCAL_PATH)/src/ckfft/android)
$(call import-module,ckfft)

#################################################
endif