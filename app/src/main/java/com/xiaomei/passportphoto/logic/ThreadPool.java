package com.xiaomei.passportphoto.logic;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static ThreadPool mInstance = null;
    public ThreadPoolExecutor mExecutor;

    public static ThreadPool getInstance(){
        if(mInstance == null){
            mInstance = new ThreadPool();
        }
        return  mInstance;
    }

    public ThreadPool(){
        mExecutor = new ThreadPoolExecutor(3,5,2000, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }
}
