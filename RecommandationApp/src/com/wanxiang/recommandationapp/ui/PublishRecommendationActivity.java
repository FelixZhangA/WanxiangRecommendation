package com.wanxiang.recommandationapp.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.data.CategoryDao;
import com.wanxiang.recommandationapp.data.DatabaseConstants;
import com.wanxiang.recommandationapp.data.RecommendationDao;
import com.wanxiang.recommandationapp.util.AppConstants;
import com.wanxiang.recommandationapp.util.TextMaxLengthWatcher;

public class PublishRecommendationActivity extends Activity
{
	private static final int MAX_LENGTH = 60;
	private EditText mEditEntity;
	private AutoCompleteTextView mAutoCompleteCategory;
	private EditText mEditContent;
	ArrayAdapter<CharSequence> mCategoryAdapter;
    private String mCategory = null;
    private RecommendationDao mDao = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_publish_recommandation);
    	ActionBar actionBar = getActionBar();
    	actionBar.setDisplayShowHomeEnabled(false);
    	actionBar.setTitle(R.string.string_publish_recommandation);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mEditEntity = (EditText) findViewById(R.id.tv_entity);
		mAutoCompleteCategory = (AutoCompleteTextView) findViewById(R.id.tv_category);
		
		mEditContent = (EditText) findViewById(R.id.tv_content);
		TextView remainText = (TextView) findViewById(R.id.tv_remain_chars);
		mEditContent.addTextChangedListener(new TextMaxLengthWatcher(this, MAX_LENGTH, mEditContent, remainText));
		initDropDownList();
		mCategory = getIntent().getStringExtra(AppConstants.INTENT_CATEGORY);
		mDao = RecommendationDao.getAdapterObject(this);
    }

    private void initDropDownList()
    {
    	mCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.default_categories, android.R.layout.simple_spinner_item);
    	mAutoCompleteCategory.setAdapter(mCategoryAdapter);
    	mAutoCompleteCategory.setThreshold(1);
//    	mAutoCompleteCategory.setCompletionHint("show 5 items");
		mAutoCompleteCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() 
		{  
            @Override  
            public void onFocusChange(View v, boolean hasFocus)
            {  
                AutoCompleteTextView view = (AutoCompleteTextView) v;  
                if (hasFocus) 
                {  
                    view.showDropDown();  
                }  
            }  
        });  
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_publish_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			onBackPressed();
		} else if (item.getItemId() == R.id.action_attach_img)
		{
			onBackPressed();
		} else if (item.getItemId() == R.id.action_publish)
		{
			if (TextUtils.isEmpty(mCategory))
			{
				MyDialog dialog = new MyDialog(this);
				dialog.show();
			}
			else
			{
				publish();
			}

		}
		
		return super.onOptionsItemSelected(item);
	}

	private void publish() 
	{
		String entity = mEditEntity.getText().toString();
		String content = mEditContent.getText().toString();
	    String user = "MOCKER";

		if (TextUtils.isEmpty(entity) || TextUtils.isEmpty(mCategory))
		{
			Toast.makeText(this, R.string.error_publish_missing, Toast.LENGTH_LONG).show();
			return;
		} else if (!TextUtils.isEmpty(content) && content.length() >= MAX_LENGTH)
		{
			Toast.makeText(this, R.string.error_publish_exceed, Toast.LENGTH_LONG).show();
			return;
		}
        PublishRecommandationAsyncTask task = new PublishRecommandationAsyncTask(entity, mCategory, content, user);
		task.execute();
		
	}
	
	private class PublishRecommandationAsyncTask extends AsyncTask<Object, Object, Object>
	{
	    private String mEntity;
	    private String mCategory;
	    private String mContent;
	    private String mUser;
	    private ProgressDialog pd;
	    public PublishRecommandationAsyncTask(String entity, String category, String content, String user)
	    {
	        mEntity = entity;
	        mCategory = category;
	        mContent = content;
	        mUser = user;
	    }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(PublishRecommendationActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage(getString(R.string.string_publishing));
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
            finish();
        }
        @Override
        protected Object doInBackground(Object... params)
        {
            Recommendation r = new Recommendation(mEntity, mCategory, mUser, System.currentTimeMillis(), mContent, 0, 0);
            mDao.insertRecommandation(r);
            return null;
        }
	    
	}
	
	private class MyDialog extends Dialog implements OnClickListener 
	{
		private static final int	SUCCESS = 1;
	    Context context;
	    private Button mBtnPubish = null;
	    private EditText mEditText = null;
	    private Cursor mCursor = null;
	    private LinearLayout mainLayout;
	    private LinearLayout mainLayout2;
	    private Handler mHandler = new Handler()
	    {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == SUCCESS)
				{     
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);  					
					int i = 0;
					while (mCursor.moveToNext() && i < 6)
					{
						Button button = new Button(context);
						button.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseConstants.COLUMN_CATEGORY)));
						button.setBackgroundColor(mCursor.getInt(mCursor.getColumnIndex(DatabaseConstants.COLUMN_CATEGORY_COLOR)));
						button.setOnClickListener(MyDialog.this);
						button.setLayoutParams(lp);
						if (i < 3)
						{
							mainLayout.addView(button);
						}
						else
						{
							mainLayout2.addView(button);
						}
						i++;
					}
				}
			}
	    };
	    public MyDialog(Context context)
	    {
	        super(context);
	        // TODO Auto-generated constructor stub
	        this.context = context;
	    }
	    public MyDialog(Context context, int theme)
	    {
	        super(context, theme);
	        this.context = context;
	    }
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) 
	    {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.dialog_customization);
	        mainLayout = (LinearLayout)findViewById(R.id.default_category);
	        mainLayout2 = (LinearLayout)findViewById(R.id.default_category_2);
	        this.setTitle(context.getString(R.string.string_category));
	        new Thread()
	        {
	        	public void run()
	        	{
	        		CategoryDao dao = CategoryDao.getAdapterObject(context);
	        		mCursor = dao.getAllCategoryCursor();
	        		mHandler.sendEmptyMessage(SUCCESS);
	        	}
	        	
	        }.start();
	        
	        mBtnPubish = (Button) findViewById(R.id.btn_publish);
	        mBtnPubish.setOnClickListener(this);
	        mEditText = (EditText) findViewById(R.id.edit_category);
	    }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.btn_publish)
			{
				mCategory = mEditText.getText().toString();
			}
			else
			{
				Button btn = (Button)v;
				mCategory = btn.getText().toString();
			}
			publish();
		}
	}
}
