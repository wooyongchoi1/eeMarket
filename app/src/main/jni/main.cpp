#include <jni.h>
#include "com_example_market_MainActivity_LableDetectionTask.h"
#include "com_example_market_MainActivity_Crolling.h"
#include "com_example_market_MainActivity.h"

//#include <opencv/opencv.hpp>
//
//using namespace cv;

extern "C" JNIEXPORT jint JNICALL Java_com_example_market_MainActivity_test
        (JNIEnv * env, jobject obj, jint v) {

    jint a = v + 10;

    return a;
}

//extern "C" JNIEXPORT jint JNICALL Java_com_example_market_MainActivity_imgCrop
//        (JNIEnv * env, jobject obj, jint v) {
//
//    jint a = v + 10;
//
//    return a;
//}