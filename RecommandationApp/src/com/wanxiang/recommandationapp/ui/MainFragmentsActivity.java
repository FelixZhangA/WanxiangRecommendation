package com.wanxiang.recommandationapp.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.MotionEvent;

import com.wanxiang.recommandationapp.R;
import com.wanxiang.recommandationapp.ui.fragment.CasualStrollFragment;
import com.wanxiang.recommandationapp.ui.fragment.CategoryFragment;

public class MainFragmentsActivity extends FragmentActivity implements TabListener
{
	public interface MyOnTouchListener 
	{  
		public boolean onTouch(MotionEvent ev);  
	} 
	private AppSectionsPagerAdapter mPageAdapter;
	private ViewPager mViewPager;
	private ArrayList<MyOnTouchListener> onTouchListeners;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main_action_bar);

		setUpFragments();
		onTouchListeners = new ArrayList<MainFragmentsActivity.MyOnTouchListener>();
	}

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) 
	{  
		onTouchListeners.add(myOnTouchListener);  
	}  
		  
	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) 
	{  
		onTouchListeners.remove(myOnTouchListener);  
	}  
		
	@Override
	public boolean dispatchTouchEvent( MotionEvent ev )
	{
		for (MyOnTouchListener listener : onTouchListeners)
		{  
			listener.onTouch(ev);  
		}  
		return super.dispatchTouchEvent(ev);
	}

	private void setUpFragments()
	{
		mPageAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		
		
		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical parent.
		actionBar.setHomeButtonEnabled(false);
		actionBar.setTitle(Html.fromHtml("<small><b>" + getString(R.string.main_action_bar_name) + "</b></small>"));
		
		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPageAdapter);
		
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				// When swiping between different app sections, select
				// the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if
				// we have a reference to the Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		for (int i = 0; i < mPageAdapter.getCount(); i++)
		{
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mPageAdapter.getPageTitle(i)).setTabListener(this));
		}
		mViewPager.setCurrentItem(0);

	}
	
	public class AppSectionsPagerAdapter extends FragmentPagerAdapter
	{

		public AppSectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int i)
		{
			switch (i)
			{
				case 0:
					return new CategoryFragment();
				case 1:
					return new CasualStrollFragment();
				default:
					return new CategoryFragment();
			}
		}

		@Override
		public int getCount()
		{
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position)
			{
				case 0:
					return getString(R.string.string_category_fragment_name);
				case 1:
					return getString(R.string.string_casual_fragment_name);
						}
			return getString(R.string.string_category_fragment_name);
		}
	}

	@Override
	public void onTabReselected( Tab tab, FragmentTransaction ft )
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected( Tab tab, FragmentTransaction ft )
	{
		mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected( Tab tab, FragmentTransaction ft )
	{
		// TODO Auto-generated method stub
		
	}
}
