package com.wanxiang.recommandationapp.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.wanxiang.database.entity.RecommandationTable;
import com.wanxiang.database.helper.DatabaseHelper;
import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.util.TextMaxLengthWatcher;

public class PublishActivity extends Activity
{
	private static final int MAX_LENGTH = 60;
	private EditText mEditEntity;
	private AutoCompleteTextView mAutoCompleteCategory;
	private EditText mEditContent;
	ArrayAdapter<CharSequence> mCategoryAdapter;
	
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
			publish();

		}
		
		return super.onOptionsItemSelected(item);
	}

	private void publish() 
	{
		String entity = mEditEntity.getText().toString();
		String category  = mAutoCompleteCategory.getText().toString();
		String content = mEditContent.getText().toString();
		
		if (TextUtils.isEmpty(entity) || TextUtils.isEmpty(category))
		{
			Toast.makeText(this, R.string.error_publish_missing, Toast.LENGTH_LONG).show();
			return;
		} else if (!TextUtils.isEmpty(content) && content.length() >= MAX_LENGTH)
		{
			Toast.makeText(this, R.string.error_publish_exceed, Toast.LENGTH_LONG).show();
			return;
		}
		Recommendation r = new Recommendation(entity, category, "felix", System.currentTimeMillis(), content, 0, 0);
		PublishRecommandationAsyncTask task = new PublishRecommandationAsyncTask(entity, category, content);
		task.execute();
		
	}
	
	private class PublishRecommandationAsyncTask extends AsyncTask<Object, Object, Object>
	{
	    private String mEntity;
	    private String mCategory;
	    private String mContent;
	    private ProgressDialog pd;
	    public PublishRecommandationAsyncTask(String entity, String category, String content)
	    {
	        mEntity = entity;
	        mCategory = category;
	        mContent = content;
	    }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(PublishActivity.this);
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
            DbUtils db = DatabaseHelper.getInstance(getApplicationContext());
            RecommandationTable dbTable = new RecommandationTable();
            dbTable.entity = mEntity;
            dbTable.category = mCategory;
            dbTable.content = mContent;
            dbTable.date = System.currentTimeMillis();
            dbTable.phraise_num = 0;
            dbTable.comment_num = 0;
            try
            {
                db.saveBindingId(dbTable);
            } catch (DbException e)
            {
                e.printStackTrace();
            }
            return null;
        }
	    
	}
	
}
