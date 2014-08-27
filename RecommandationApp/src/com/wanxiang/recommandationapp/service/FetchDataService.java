package com.wanxiang.recommandationapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

public class FetchDataService extends IntentService
{

    public FetchDataService(String name)
    {
        super(name);
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // TODO Auto-generated method stub
        
    }

    
}
