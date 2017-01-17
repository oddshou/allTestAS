
package com.oddshou.testall;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Logger
{
    public static boolean isDebug = /*false*/true;  //这个标签其他地方有使用，慎改

    public static boolean isTrace = true;

    public static boolean canListAnimation = true;

    private static final int ERROR = 4;
    private static final int WARNING = 3;
    private static final int INFO = 2;
    private static final int DEBUG = 1;
    private static final int VIEW = 0;
    private static int mLevel = 4;  //显示error日志
    //################### one param
    public static String customTagPrefix = "";
    private static String generateTag()
    {
        StackTraceElement caller = Thread.currentThread( ).getStackTrace( )[4];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName( );
        callerClazzName = callerClazzName.substring( callerClazzName.lastIndexOf( "." ) + 1 );
        tag = String.format( tag , callerClazzName , caller.getMethodName( ) , caller.getLineNumber( ) );
        tag = TextUtils.isEmpty( customTagPrefix ) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void e(Object obj)
    {
        if (isDebug || mLevel <= ERROR)
        {
            Log.e(generateTag(), obj.toString());
        }
    }

    public static void w(Object obj)
    {
        if (isDebug || mLevel <= WARNING)
        {
            Log.w(generateTag(), obj.toString());
        }
    }

    public static void i(Object obj)
    {
        if (isDebug || mLevel <= INFO)
        {
            Log.i(generateTag(), obj.toString());
        }
    }

    public static void d( Object obj)
    {
        if (isDebug || mLevel <= DEBUG)
        {
            Log.d(generateTag(), obj.toString());
        }
    }

    public static void v(Object obj)
    {
        if (isDebug || mLevel <= VIEW)
        {
            Log.v(generateTag(), obj.toString());
        }
    }

    //################### two param
    public static void e(String TAG, Object obj)
    {
        if (isDebug || mLevel <= ERROR)
        {
            Log.e(TAG, obj.toString());
        }
    }

    public static void w(String TAG, Object obj)
    {
        if (isDebug || mLevel <= WARNING)
        {
            Log.w(TAG, obj.toString());
        }
    }

    public static void i(String TAG, Object obj)
    {
        if (isDebug || mLevel <= INFO)
        {
            Log.i(TAG, obj.toString());
        }
    }

    public static void d(String TAG, Object obj)
    {
        if (isDebug || mLevel <= DEBUG)
        {
            Log.d(TAG, obj.toString());
        }
    }

    public static void v(String TAG, Object obj)
    {
        if (isDebug || mLevel <= VIEW)
        {
            Log.v(TAG, obj.toString());
        }
    }

    //################ three param TAG + obj + name
    public static void e(String TAG, Object obj, String name)
    {
        if (isDebug || mLevel <= ERROR)
        {
            Log.e(TAG, name + " " + obj.toString());
        }
    }

    public static void w(String TAG, Object obj, String name)
    {
        if (isDebug || mLevel <= WARNING)
        {
            Log.w(TAG, name + " " + obj.toString());
        }
    }

    public static void i(String TAG, Object obj, String name)
    {
        if (isDebug || mLevel <= INFO)
        {
            Log.i(TAG, name + " " + obj.toString());
        }
    }

    public static void d(String TAG, Object obj, String name)
    {
        if (isDebug || mLevel <= DEBUG)
        {
            Log.d(TAG, name + " " + obj.toString());
        }
    }

    public static void v(String TAG, Object obj, String name)
    {
        if (isDebug || mLevel <= VIEW)
        {
            Log.v(TAG, name + " " + obj.toString());
        }
    }



    public static void writeToFile(String log, String fileName, String extra) {
        Message message = logHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("log", log);
        bundle.putString("filename", fileName);
        bundle.putString("extra", extra);
        message.setData(bundle);
        logHandler.sendMessage(message);
    }
    private static Looper mLooper = null;
    public static Looper getLogThreadLooper()
    {
        if( mLooper == null )
        {
            HandlerThread mHandlerThread = new HandlerThread( "hsgamecenterLogThreadtask" );
            mHandlerThread.start( );
            mLooper = mHandlerThread.getLooper( );
        }

        return mLooper;
    }

    private static Handler logHandler = new Handler(getLogThreadLooper()){
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            String log = data.getString("log");
            String fileName = data.getString("filename");
            String extra = data.getString("extra");
            if (TextUtils.isEmpty(fileName)) {
                fileName = "Gamecenter.txt";
            }
            File logFile = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
            try {
                Writer writer = new BufferedWriter(new FileWriter(logFile, true), 2048);
                writer.write("\n");
                writer.write("\n");
                writer.write(extra + "\n");
                writer.write(log);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
