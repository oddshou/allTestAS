package com.oddshou.testall.jniClass;

/**
 * Created by win7 on 2017/1/17.
 */

public class JniUtils {
    static {
        System.loadLibrary("native-lib");
    }
    public native String getFirstMethed();
//    public native String getSecondMethed();

//    public native int add(int i, int j);

    /**
     * jni 一般步骤说明：
     *
     * 新建一个沟通类如JniUtils，实现一些native方法，
     * 加载将要编译出来的so库
     * 使用javah命令编译出头文件，新建c、cpp实现头文件方法。
     * 在project 视图下module root 目录下新建CMakeLists.txt
     * 切换到Android视图，右键app模块, Link C++ Project with Gradle 将自动修改gradle
     *  sync，编译运行
     *
     */

}
