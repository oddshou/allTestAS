//
// Created by win7 on 2017/1/23.
//

#include "com_oddshou_testall_jniClass_JniUtils.h"


JNIEXPORT jstring JNICALL Java_com_oddshou_testall_jniClass_JniUtils_getFirstMethed
(JNIEnv *env, jobject obj)
{
   return (*env)->NewStringUTF(env,"This just a test for Android Studio NDK JNI developer!");
}


