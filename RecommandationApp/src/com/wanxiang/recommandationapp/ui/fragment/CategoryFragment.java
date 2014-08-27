package com.wanxiang.recommandationapp.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wanxiang.datamodel.Category;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.data.CategoryCursorAdapter;
import com.wanxiang.recommandationapp.data.CategoryDao;
import com.wanxiang.recommandationapp.ui.CategoryDetailsActivity;
import com.wanxiang.recommandationapp.ui.CommentsActivity;
import com.wanxiang.recommandationapp.util.AppConstants;


public class CategoryFragment extends Fragment implements OnItemClickListener
{

	private PullToRefreshListView	mListView;
	private CategoryCursorAdapter	mAdapter;
	private Cursor					mCursor;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		return inflater.inflate(R.layout.layout_category_fragment, container, false);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		mListView = (PullToRefreshListView)getView().findViewById(R.id.lst_recomandation);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
			@Override
			public void onRefresh( PullToRefreshBase<ListView> refreshView )
			{
				// Do work to refresh the list here.
				new FetchCategoryAsyncTask(getActivity()).execute();
			}
		});
		FetchCategoryAsyncTask task = new FetchCategoryAsyncTask(getActivity());
		task.execute();
	}

	@Override
	public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 )
	{
		Log.d("WANXIANG", "Item click listener " + arg2);
		Intent intent = new Intent();
		intent.setClass(getActivity(), CategoryDetailsActivity.class);
		Category cat = mAdapter.getItem(arg2 - 1);
		if (cat == null)
		{
			return;
		}
		intent.putExtra(AppConstants.INTENT_CATEGORY, cat.getCategoryName());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);

	}

	private class FetchCategoryAsyncTask extends AsyncTask<Object, Object, Exception>
	{
		private Context	mContext;

		public FetchCategoryAsyncTask( Context context )
		{
			mContext = context;
		}

		@Override
		protected Exception doInBackground( Object... arg0 )
		{
			CategoryDao dao = CategoryDao.getAdapterObject(mContext);
			Cursor c = dao.getAllCategoryCursor();
			if (c.getCount() <= 0) // Mock data
			{
				Category test = new Category("美食", Color.YELLOW);
				dao.insertCategory(test);
				test = new Category("电影", Color.CYAN);
				dao.insertCategory(test);
				test = new Category("图书", Color.GREEN);
				dao.insertCategory(test);
				test = new Category("游戏", Color.MAGENTA);
				dao.insertCategory(test);
				test = new Category("娱乐", Color.LTGRAY);
				dao.insertCategory(test);
				test = new Category("电子产品", Color.BLUE);
				dao.insertCategory(test);
			}
			mCursor = CategoryDao.getAdapterObject(mContext).getAllCategoryCursor();
			return null;
		}

		@Override
		protected void onPostExecute( Exception result )
		{
			super.onPostExecute(result);
			if (result == null)
			{
				mAdapter = new CategoryCursorAdapter(getActivity(), mCursor, false);
				mListView.setAdapter(mAdapter);
				mListView.onRefreshComplete();
			}
			else
			{
				Log.e("wanxiang", result.getMessage());
			}
		}

	}
}
