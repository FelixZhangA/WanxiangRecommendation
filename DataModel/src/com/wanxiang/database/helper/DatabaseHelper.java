package com.wanxiang.database.helper;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

public class DatabaseHelper
{
    private static final String DB_NAME = "wanxiang.db";
    private static DbUtils mInstance = null;
    
    public static DbUtils getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = DbUtils.create(context, DB_NAME);
            mInstance.configAllowTransaction(true);
            mInstance.configDebug(true);
        }
        return mInstance;
    }
    
}
