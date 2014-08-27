package com.wanxiang.recommandationapp.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.wanxiang.database.entity.CommentTable;
import com.wanxiang.database.helper.DatabaseHelper;
import com.wanxiang.datamodel.Comment;
import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.data.CommentDao;
import com.wanxiang.recommandationapp.data.CommentsAdapter;
import com.wanxiang.recommandationapp.util.AppConstants;
import com.wanxiang.recommandationapp.util.Utils;

public class CommentsActivity extends Activity implements OnClickListener
{
    private Recommendation mRecommendation;
    private PullToRefreshListView       mListView;
    private CommentsAdapter mAdapter;
    private EditText       mEditContent;
    private Button         mBtnSend;
    private Cursor			mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment_page);
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is
        // no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle(Html.fromHtml("<b>"
                + getString(R.string.main_action_bar_name) + "</b>"));
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        mRecommendation = (Recommendation) intent
                .getSerializableExtra(AppConstants.RECOMMENDATION);
        if (mRecommendation == null)
        {
            return;
        }

        mListView = (PullToRefreshListView) findViewById(R.id.list_comments);

        mEditContent = (EditText) findViewById(R.id.edit_content);
        mEditContent.setHint(getString(R.string.string_send_hint,
                mRecommendation.getUser().getName()));

        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        initRecommendation();
        mockData();

    }

    private void initRecommendation()
    {
        View rLayout = findViewById(R.id.recommandation_item);
        TextView tvEntityName = (TextView) rLayout.findViewById(R.id.tv_entity_name); 
        tvEntityName.setText(mRecommendation.getEntity().getEntityName());
        
        TextView tvCategory = (TextView) rLayout.findViewById(R.id.tv_category);
        tvCategory.setText(mRecommendation.getCategory().getCategoryName());
        
        TextView tvName = (TextView) rLayout.findViewById(R.id.tv_username); 
        tvName.setText(mRecommendation.getUser().getName());

        TextView tvContent = (TextView) rLayout.findViewById(R.id.tv_rc_content); 
        tvContent.setText(mRecommendation.getContent());

        TextView tvDate = (TextView) rLayout.findViewById(R.id.tv_timestamp); 
        long date = mRecommendation.getDate();
        String dateTxt = Utils.formatDate(this, date);
        tvDate.setText(dateTxt);
    }

    private void mockData()
    {
        FetchCommentAsynTask task = new FetchCommentAsynTask(this);
        task.execute();
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        String content = mEditContent.getText().toString();
        if (TextUtils.isEmpty(content))
        {
            Toast.makeText(CommentsActivity.this,
                    R.string.error_publish_no_text, Toast.LENGTH_LONG).show();
        } else
        {
            PublishCommentTask task = new PublishCommentTask(content);
            task.execute();
        }
    }
    
    public class FetchCommentAsynTask extends AsyncTask<Object, Object, Object>
    {

        private Context context;
        private CommentsAdapter adapter;
        private ProgressDialog pd;
        
        
        public FetchCommentAsynTask(Context context)
        {
            this.context = context;
        }
        
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing())
            {
                pd.dismiss();
            }
            mAdapter = new CommentsAdapter(context, mCursor, false);
            mListView.setAdapter(mAdapter);

            
        }

        @Override
        protected Object doInBackground(Object... params)
        {
        	try
        	{
        		CommentDao dao = CommentDao.getAdapterObject(context);
        		mCursor = dao.getAllCommentsByIdCursor(mRecommendation.getID());
        	}
        	catch (Exception ex)
        	{
        		Log.e("wanxiang", ex.getMessage());
        	}
            return null;
        }

    }

    private class PublishCommentTask extends AsyncTask<Object, Object, Boolean>
    {
        private String content;

        public PublishCommentTask(String content)
        {
            this.content = content;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result)
            {
                mEditContent.setText(null);
                Toast.makeText(CommentsActivity.this, R.string.string_comment_succ, Toast.LENGTH_LONG).show();
                FetchCommentAsynTask task = new FetchCommentAsynTask(CommentsActivity.this);
                task.execute();
            } else
            {
                Toast.makeText(CommentsActivity.this, R.string.string_comment_fail, Toast.LENGTH_LONG).show();
            }
            
        }

        @Override
        protected Boolean doInBackground(Object... params)
        {
            Boolean ret = false;
            CommentDao dao = CommentDao.getAdapterObject(CommentsActivity.this);
            String user = "Comment";
            String content = mEditContent.getText().toString();
            ret = dao.insertComment(new Comment(mRecommendation.getID(), user, content, System.currentTimeMillis()));
            return ret;
        }

    }

}
