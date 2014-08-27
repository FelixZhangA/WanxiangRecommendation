package com.wanxiang.recommandationapp.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wanxiang.database.helper.CursorUtil;
import com.wanxiang.database.helper.SQLiteDatabaseInterface;
import com.wanxiang.database.helper.SQLiteDatabaseUtil;
import com.wanxiang.datamodel.Category;
import com.wanxiang.datamodel.Entity;
import com.wanxiang.datamodel.Recommendation;
import com.wanxiang.datamodel.User;
import com.wanxiang.recommandationapp.util.Utils;


public class RecommendationDao implements SQLiteDatabaseInterface
{

	private final SQLiteDatabaseUtil	mDbHelper;
	private static RecommendationDao	mDao	= null;

	private RecommendationDao( Context context )
	{
		mDbHelper = new SQLiteDatabaseUtil(context, DatabaseConstants.DATABASE_NAME, DatabaseConstants.version, this);
	}

	public static synchronized RecommendationDao getAdapterObject( Context context )
	{
		if (mDao == null)
		{
			mDao = new RecommendationDao(context);
		}

		return mDao;
	}

	@Override
	public void onCreateTable( SQLiteDatabase db, Context context )
	{
		String init_apps_table = "Create table " + DatabaseConstants.APPLICATIONS_TABLE_NAME + "(" + DatabaseConstants.COLUMN_APP_ID + " integer," + DatabaseConstants.COLUMN_RECOMMANDATION_UID + " varchar(20) primary key," + DatabaseConstants.COLUMN_ENTITY + " text, " + DatabaseConstants.COLUMN_CATEGORY + " text," + DatabaseConstants.COLUMN_CONTENT + " text,"+ DatabaseConstants.COLUMN_DATE + " timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," + DatabaseConstants.COLUMN_PHRAISE_NUM + " integer," + DatabaseConstants.COLUMN_COMMENT_NUM + " integer," + DatabaseConstants.COLUMN_USER + " text);";
		db.execSQL(init_apps_table);
	}

	@Override
	public void onUpgradeDataBase( SQLiteDatabase db, int oldVersion, int newVersion, Context context )
	{
		String destroy_apps_table = "DROP TABLE IF EXISTS " + DatabaseConstants.APPLICATIONS_TABLE_NAME;
		db.execSQL(destroy_apps_table);
		onCreateTable(db, context);
	}
	
	public synchronized Recommendation insertRecommandation(Recommendation r)
	{
		SQLiteDatabase db = null;
		try
		{
			db = mDbHelper.getDatabase(true);
			db.insertWithOnConflict(DatabaseConstants.APPLICATIONS_TABLE_NAME, null, getAppContentValues(r), SQLiteDatabase.CONFLICT_REPLACE);
		}
		finally
		{
			mDbHelper.close();
		}
		return r;
	}

	public synchronized Cursor getAllRecommendationsCursor()
	{
		SQLiteDatabase db = null;
		Cursor c = null;
		try
		{
			db = mDbHelper.getDatabase(false);
			c = db.query(DatabaseConstants.APPLICATIONS_TABLE_NAME, null, null, null, null, null, DatabaseConstants.COLUMN_DATE + " DESC");
			return CursorUtil.createCopy(c);
		}
		finally
		{
			CursorUtil.safeClose(c);
			mDbHelper.close();
		}
	}

	public synchronized Cursor getRecommendationsByCategoryCursor(String category)
	{
		SQLiteDatabase db = null;
		Cursor c = null;
		try
		{
			db = mDbHelper.getDatabase(false);
			c = db.rawQuery("select * from " + DatabaseConstants.APPLICATIONS_TABLE_NAME + " where category = '" + category + "' ORDER BY " + DatabaseConstants.COLUMN_DATE + " DESC;", null);
			return CursorUtil.createCopy(c);
		}
		catch (Exception ex)
		{
			Log.e("wanxiang", ex.getMessage());
		}
		finally
		{
			CursorUtil.safeClose(c);
			mDbHelper.close();
		}
		return c;
	}
	public Recommendation getRecommendationFromCursor(Cursor c)
	{
		if (c == null)
		{
			return null;
		}
		final Recommendation r = new Recommendation();
		r.setID(c.getLong(c.getColumnIndex(DatabaseConstants.COLUMN_RECOMMANDATION_UID)));
		r.setEntity(new Entity(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_ENTITY))));
		r.setCategory(new Category(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_CATEGORY))));
		r.setContent(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_CONTENT)));
		r.setDate(c.getLong(c.getColumnIndex(DatabaseConstants.COLUMN_DATE)));
		r.setPhraiseNum(c.getInt(c.getColumnIndex(DatabaseConstants.COLUMN_PHRAISE_NUM)));
		r.setCommentNum(c.getInt(c.getColumnIndex(DatabaseConstants.COLUMN_COMMENT_NUM)));
		r.setUser(new User(c.getString(c.getColumnIndex(DatabaseConstants.COLUMN_USER))));

		return r;
	}
	
	private ContentValues getAppContentValues(Recommendation r)
	{
		ContentValues cv = new ContentValues();

		cv.put(DatabaseConstants.COLUMN_RECOMMANDATION_UID, String.valueOf(System.currentTimeMillis()+1));
		cv.put(DatabaseConstants.COLUMN_ENTITY, r.getEntity().getEntityName());
		cv.put(DatabaseConstants.COLUMN_CATEGORY, r.getCategory().getCategoryName());
		cv.put(DatabaseConstants.COLUMN_CONTENT, r.getContent());
		cv.put(DatabaseConstants.COLUMN_DATE, r.getDate());
		cv.put(DatabaseConstants.COLUMN_USER, r.getUser().getName());
		cv.put(DatabaseConstants.COLUMN_PHRAISE_NUM, r.getPhraiseNum());
		cv.put(DatabaseConstants.COLUMN_COMMENT_NUM, r.getCommentNum());

		return cv;
	}
	


}
