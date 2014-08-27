package com.wanxiang.recommandationapp.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.data.RecommendationCursorAdapter;
import com.wanxiang.recommandationapp.data.RecommendationDao;
import com.wanxiang.recommandationapp.util.AppConstants;


public class CategoryDetailsActivity extends Activity implements OnItemClickListener, OnClickListener
{
	private static final int			SUCCESS			= 1111;
	private static final int			PUBLISH_REQUEST	= 1;
	private String						mCategory;
	private long						mCategoryId;
	private PullToRefreshListView		mListView;
	private Button						mPublishBtn;
	private TextView					mEmptyTextView;
	private RecommendationCursorAdapter	mAdapter;
	private Cursor						mCursor;
	private Handler						mHandler		= new Handler()
														{
															@Override
															public void handleMessage( Message msg )
															{
																if (msg.what == SUCCESS)
																{
																	if (mCursor == null || mCursor.getCount() <= 0)
																	{
																		mEmptyTextView.setVisibility(View.VISIBLE);
																	}
																	else
																	{
																		mEmptyTextView.setVisibility(View.GONE);
																		mAdapter = new RecommendationCursorAdapter(CategoryDetailsActivity.this, mCursor, false);
																		mListView.setAdapter(mAdapter);
																	}

																}
															}

														};

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_homepage);
		mCategory = getIntent().getStringExtra(AppConstants.INTENT_CATEGORY);

		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(Html.fromHtml("<b>" + mCategory + "</b>"));

		if (!TextUtils.isEmpty(mCategory))
		{
			mListView = (PullToRefreshListView)findViewById(R.id.lst_recomandation);
			mListView.setOnItemClickListener(this);
			mListView.setOnRefreshListener(new OnRefreshListener<ListView>()
			{
				@Override
				public void onRefresh( PullToRefreshBase<ListView> refreshView )
				{
					// Do work to refresh the list here.
					new FetchRecommendationAsyncTask(CategoryDetailsActivity.this).execute();
				}
			});
			mPublishBtn = (Button)findViewById(R.id.btn_recommand);
			mPublishBtn.setOnClickListener(this);
			mEmptyTextView = (TextView)findViewById(R.id.tv_empty);
			FetchRecommendationAsyncTask task = new FetchRecommendationAsyncTask(CategoryDetailsActivity.this);
			task.execute();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	private class FetchRecommendationAsyncTask extends AsyncTask<Object, Object, Exception>
	{
		private Context	context;

		public FetchRecommendationAsyncTask( Context context )
		{
			this.context = context;
		}

		@Override
		protected Exception doInBackground( Object... params )
		{
			try
			{
				RecommendationDao dao = RecommendationDao.getAdapterObject(context);
				mCursor = dao.getRecommendationsByCategoryCursor(mCategory);
			}
			catch (Exception ex)
			{
				return ex;
			}
			return null;
		}

		@Override
		protected void onPostExecute( Exception result )
		{
			super.onPostExecute(result);
			mHandler.sendEmptyMessage(SUCCESS);
			mListView.onRefreshComplete();
			if (result != null)
			{
				Log.e("wanxiang", result.getMessage());
			}
		}
	}

	public boolean onOptionsItemSelected( MenuItem item )
	{
		if (item.getItemId() == android.R.id.home)
		{
			onBackPressed();
		}
		return true;
	}

	@Override
	public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 )
	{
		Log.d("WANXIANG", "Item click listener " + arg2);
		// get recommendation id and send to comment activity
		Intent intent = new Intent();
		intent.setClass(this, CommentsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(AppConstants.RECOMMENDATION, mAdapter.getItem(arg2 - 1));
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PUBLISH_REQUEST)
		{
			FetchRecommendationAsyncTask task = new FetchRecommendationAsyncTask(CategoryDetailsActivity.this);
			task.execute();
		}
	}

	@Override
	public void onClick( View v )
	{
		if (R.id.btn_recommand == v.getId())
		{
			Log.d("WANXIANG", "button click ");
			Intent intent = new Intent();
			intent.setClass(this, PublishRecommendationActivity.class);
			intent.putExtra(AppConstants.INTENT_CATEGORY, mCategory);
			startActivityForResult(intent, PUBLISH_REQUEST);
		}
	}
}
