package com.wanxiang.recommandationapp.data;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.util.Utils;


public class RecommendationCursorAdapter extends CursorAdapter
{
	private Cursor mCursor;
	private RecommendationDao mDao;

	public RecommendationCursorAdapter( Context context, Cursor c, boolean autoRequery )
	{
		super(context, c, autoRequery);
		mCursor = c;
		mDao = RecommendationDao.getAdapterObject(context);
		
	}

	@Override
	public int getCount()
	{
		return mCursor != null ? mCursor.getCount() : 0;
	}

	@Override
	public Recommendation getItem( int position )
	{
		if (position > -1)
		{
			mCursor.moveToPosition(position);
			return mDao.getRecommendationFromCursor(mCursor);
		}
		return null;
	}

	@Override
	public long getItemId( int position )
	{
		return super.getItemId(position);
	}

	@Override
	public void bindView( View arg0, Context arg1, Cursor c )
	{
		ViewHolder holder = (ViewHolder)arg0.getTag();
		if (holder != null)
		{
			holder.tvEntityName.setText(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_ENTITY)));
			holder.tvCategory.setText(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_CATEGORY)));
			holder.tvName.setText(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_USER)));
			holder.tvContent.setText(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_CONTENT)));

			long date = c.getLong(c.getColumnIndex(DatabaseConstants.COLUMN_DATE));
			String dateTxt = Utils.formatDate(mContext, date);
			holder.tvDate.setText(dateTxt);
			
			holder.tvPhraise.setText(String.valueOf(c.getInt(c.getColumnIndex(DatabaseConstants.COLUMN_PHRAISE_NUM))));
			holder.tvComment.setText(String.valueOf(c.getInt(c.getColumnIndex(DatabaseConstants.COLUMN_COMMENT_NUM))));
		}

	}

	@Override
	public View newView( Context context, Cursor cursor, ViewGroup parent )
	{
		final View view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_recommandation_item, null);
		ViewHolder holder = new ViewHolder();

		holder = new ViewHolder();

		holder.tvEntityName = (TextView)view.findViewById(R.id.tv_entity_name);
		holder.tvCategory = (TextView)view.findViewById(R.id.tv_category);
		holder.tvName = (TextView)view.findViewById(R.id.tv_username);
		holder.tvContent = (TextView)view.findViewById(R.id.tv_rc_content);
		holder.tvDate = (TextView)view.findViewById(R.id.tv_timestamp);
		holder.tvPhraise = (TextView)view.findViewById(R.id.tv_praise);
		holder.tvComment = (TextView)view.findViewById(R.id.tv_comment);
		view.setTag(holder);
		return view;
	}

	static class ViewHolder
	{
		TextView	tvEntityName;

		TextView	tvCategory;

		TextView	tvName;

		TextView	tvContent;

		TextView	tvDate;
		
		TextView 	tvPhraise;
		
		TextView 	tvComment;

	}
}
