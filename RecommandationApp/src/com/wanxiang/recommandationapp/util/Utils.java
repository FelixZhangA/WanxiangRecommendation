package com.wanxiang.recommandationapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.wanxiang.database.entity.RecommandationTable;
import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.recommandationapp.R;

public class Utils
{
    public static String formatDate(Context context, long date)
    {
    	if (date != 0)
    	{
    		Date tmpDate = new Date(date);
    		SimpleDateFormat mDateFormat;
    		ContentResolver cv = context.getContentResolver();
    		String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
    		if(!TextUtils.isEmpty(strTimeFormat) && strTimeFormat.equals("24"))
    			mDateFormat = new SimpleDateFormat(context.getString(R.string.scan_date_time_format_24hour_clock));
    		else
    			mDateFormat = new SimpleDateFormat(context.getString(R.string.scan_date_time_format_12hour_clock));
    		
    		if (null != tmpDate)
    		{
    			return mDateFormat.format(tmpDate);
    		}
    	}
	    return null;
    }
    
    public static RecommandationTable getRecommandationTable(Recommendation r)
    {
    	RecommandationTable ret = new RecommandationTable();
    	ret.setId(System.currentTimeMillis());
    	ret.entity = r.getEntity().getEntityName();
    	ret.category = r.getCategory().getCategoryName();
    	ret.content = r.getContent();
    	ret.date = System.currentTimeMillis();
    	ret.phraise_num = 0;
    	ret.comment_num = 0;
    	ret.user = r.getUser().getName();
    	return ret;
    }
    
}
