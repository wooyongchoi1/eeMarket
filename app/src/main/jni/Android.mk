LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

#opencv library
<<<<<<< HEAD
OPENCVROOT:= C:\GraduationDesign_android_git\sdk
=======
OPENCVROOT:= C:\Users\hmm62\AndroidStudioProjects\ee-market_ver2\sdk
>>>>>>> bc4a85a73c021931a501b512568ec23d0d57d94e
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}\native\jni\OpenCV.mk


LOCAL_MODULE    := native-lib
LOCAL_SRC_FILES := main.cpp
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)