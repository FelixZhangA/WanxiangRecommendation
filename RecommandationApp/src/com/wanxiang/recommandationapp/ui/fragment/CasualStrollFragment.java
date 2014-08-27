package com.wanxiang.recommandationapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.data.RecommendationCursorAdapter;
import com.wanxiang.recommandationapp.data.RecommendationDao;
import com.wanxiang.recommandationapp.ui.CommentsActivity;
import com.wanxiang.recommandationapp.ui.MainFragmentsActivity;
import com.wanxiang.recommandationapp.ui.MainFragmentsActivity.MyOnTouchListener;
import com.wanxiang.recommandationapp.ui.PublishRecommendationActivity;
import com.wanxiang.recommandationapp.util.AppConstants;

public class CasualStrollFragment extends Fragment implements OnItemClickListener, OnClickListener, MyOnTouchListener
{
	private static final int SUCCESS	= 1111;
	private static final int PUBLISH_REQUEST = 1;

	private PullToRefreshListView	mListView;
	private Button	mPublishBtn;
	private RecommendationCursorAdapter	mAdapter;
	private Cursor	mCursor;

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == SUCCESS)
			{
				mAdapter = new RecommendationCursorAdapter(getActivity(), mCursor, false);
				mListView.setAdapter(mAdapter);
			}
		}
		
	};

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		return inflater.inflate(R.layout.layout_homepage, container, false);
	}
	@Override
	public void onPause()
	{
		super.onPause();
		((MainFragmentsActivity)getActivity()).unregisterMyOnTouchListener(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		((MainFragmentsActivity)getActivity()).registerMyOnTouchListener(this);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		mListView = (PullToRefreshListView) getView().findViewById(R.id.lst_recomandation);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
				new FetchRecommendationAsyncTask(getActivity()).execute();
			}
		});
		mPublishBtn = (Button) getView().findViewById(R.id.btn_recommand);
		mPublishBtn.setOnClickListener(this);
		
		FetchRecommendationAsyncTask task = new FetchRecommendationAsyncTask(getActivity());
		task.execute();
	}
	
	
	private class FetchRecommendationAsyncTask extends AsyncTask<Object, Object, Exception>
	{
		private Context context;
	    public FetchRecommendationAsyncTask(Context context)
	    {
	        this.context = context;
	    }
	    @Override
	    protected Exception doInBackground(Object... params)
	    {
	    	try
	    	{
	    		RecommendationDao dao = RecommendationDao.getAdapterObject(context);
	        	Cursor c = dao.getAllRecommendationsCursor();
	        	if (c.getCount() <= 0) // Mock data
	        	{
	        		Recommendation test = new Recommendation("万历十五年", "图书", "张同学", System.currentTimeMillis(), "是日，上御门毕，召辅臣时行等见于皇极门暖阁。上出陕西巡抚赵可怀奏报虏情本手授时行曰：“朕近览陕西总督抚梅友松等所奏。说虏王引兵过河，侵犯内地，这事情如何？”", 4, 5);
				    test.setID(1000001);
				    dao.insertRecommandation(test);
				    Recommendation test2 = new Recommendation("申德勒码头", "美食", "Felix1", System.currentTimeMillis(), "德国肘子相当好吃，适合吃货", 42, 15);
					test2.setID(1000002);
					dao.insertRecommandation(test2);
					Recommendation test3 = new Recommendation("平凡之路", "音乐", "张同学", System.currentTimeMillis(), "非常棒，朴树唱的", 1, 1);
					test3.setID(1000003);
					dao.insertRecommandation(test3);
					Recommendation test4 = new Recommendation("FM2012", "游戏", "Felix2", System.currentTimeMillis(), "经营类游戏NO.1", 2, 4);
					test4.setID(1000004);
					dao.insertRecommandation(test4);
	        	}
	        	mCursor = dao.getAllRecommendationsCursor();
	    	}
	    	catch (Exception ex)
	    	{
	    		return ex;
	    	}
	        return null;
	    }
	    
	    @Override
		protected void onPostExecute(Exception result) 
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

	@Override
	public void onClick( View v )
	{
		if (R.id.btn_recommand == v.getId()) {
			Log.d("WANXIANG", "button click ");
			Intent intent = new Intent();
			intent.setClass(getActivity(), PublishRecommendationActivity.class);
			startActivityForResult(intent, PUBLISH_REQUEST);
		}		
	}

	@Override
	public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 )
	{
		Log.d("WANXIANG", "Item click listener " + arg2);
		// get recommendation id and send to comment activity
		Intent intent = new Intent();
		intent.setClass(getActivity(), CommentsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(AppConstants.RECOMMENDATION,
				mAdapter.getItem(arg2 - 1));
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);		
	}

	@Override
	public boolean onTouch( MotionEvent ev )
	{
		mPublishBtn.bringToFront();
		return false;
	}
}
