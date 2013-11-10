package com.dbexample;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends FragmentActivity implements TabListener {
	public static final int	MY_CHILD_INTENT = 8456;

	private MyAdapter mAdapter = null;
	private ViewPager mPager = null;
	private MenuItem del_elem = null;
	private MenuItem cop_elem = null;
	private MenuItem mov_elem = null;

	private class PageListener extends ViewPager.SimpleOnPageChangeListener {
		MainActivity ma;

		public PageListener(MainActivity m) {
			ma = m;
		}

		@Override
		public void onPageSelected(int position) {
			ma.tabChanged(position);
			getActionBar().setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	public void tabChanged(int pos) {
		ListFragment l = (ListFragment)mAdapter.getItem(pos);
		changeMenuEnable(l.getChecked() > 0);
	}
	
	public void addTab(ActionBar bar, String s) {
		Tab tab = bar.newTab();
        tab.setText(s);
        tab.setTabListener(this);
        bar.addTab(tab);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAdapter = new MyAdapter(getSupportFragmentManager(), this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mPager.setOnPageChangeListener(new PageListener(this));

		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		addTab(bar, "db1");
		addTab(bar, "db2");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		cop_elem = (MenuItem)menu.findItem(R.id.cop_elem);
		mov_elem = (MenuItem)menu.findItem(R.id.mov_elem);
		del_elem = (MenuItem)menu.findItem(R.id.del_elem);

		changeMenuEnable(false);
		return true;
	}

	public void changeMenuEnable(boolean b) {
		if (cop_elem != null)
			cop_elem.setEnabled(b);
		if (mov_elem != null)
			mov_elem.setEnabled(b);
		if (del_elem != null)
			del_elem.setEnabled(b);
	}

	public void onCheckboxClicked(View v){
		boolean t = ((CheckBox)v).isChecked();
		int res;

		ListFragment l = (ListFragment)mAdapter.getItem(mPager.getCurrentItem());
		res = t ? l.addCheck(v.getId()) : l.removeCheck(v.getId());

		changeMenuEnable(res > 0);
	}

	public void	copyItems(ListFragment l1, ListFragment l2) {
		ArrayList<DataHandler> d = l1.getCheckedItem();

		for (DataHandler tmp : d) {
			l2.addValueToDb(tmp.title, tmp.description);
		}
		l1.drawAct();
		l2.drawAct();
	}

	public void	deleteItems(ListFragment l1) {
		ArrayList<DataHandler> d = l1.getCheckedItem();

		for (DataHandler tmp : d) {
			l1.removeItem(tmp);
		}
		l1.drawAct();
	}

	public void	moveItems(ListFragment l1, ListFragment l2) {
		ArrayList<DataHandler> d = l1.getCheckedItem();

		for (DataHandler tmp : d) {
			l2.addValueToDb(tmp.title, tmp.description);
		}
		for (DataHandler tmp : d) {
			l1.deleteEntry(tmp);
		}
		l1.drawAct();
		l2.drawAct();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ListFragment l1 = (ListFragment)mAdapter.getItem(0);
		ListFragment l2 = (ListFragment)mAdapter.getItem(1);

		switch (item.getItemId()) {
		case R.id.cop_elem:
			if (mPager.getCurrentItem() == 0)
				copyItems(l1, l2);
			else
				copyItems(l2, l1);
			return true;
		case R.id.del_elem:
			if (mPager.getCurrentItem() == 0)
				deleteItems(l1);
			else
				deleteItems(l2);
			return true;
		case R.id.mov_elem:
			if (mPager.getCurrentItem() == 0)
				moveItems(l1, l2);
			else
				moveItems(l2, l1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment>	lf = new ArrayList<Fragment>();
		MainActivity ma;

		public MyAdapter(FragmentManager fm, MainActivity m) {
			super(fm);
			ma = m;
			lf.clear();
			lf.add(new ListFragment());
			lf.add(new ListFragment());
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			if (position >= lf.size() || position < 0)
				return null;
			return lf.get(position);
		}

		public ArrayList<Fragment>	getPageList() {
			return lf;
		}
	}

	public void onClickOnAdd(View v){
		ListFragment l = (ListFragment)mAdapter.getItem(mPager.getCurrentItem());

		Intent intent = new Intent(l.getActivity(), AddElem.class);
		startActivityForResult(intent, MainActivity.MY_CHILD_INTENT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case (MainActivity.MY_CHILD_INTENT):
			if (resultCode == Activity.RESULT_OK) {
				ArrayList<String> s = data.getStringArrayListExtra(AddElem.EXTRA_RETURN);

				ListFragment l = (ListFragment)mAdapter.getItem(mPager.getCurrentItem());
				l.addValueToDb(s.get(0), s.get(1));
			}
		break;
		default:
			break;
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		int pos = arg0.getText() == "db1" ? 0 : 1;
		
		tabChanged(pos);
		mPager.setCurrentItem(pos);
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		int pos = arg0.getText() == "db1" ? 0 : 1;
		
		tabChanged(pos);
		mPager.setCurrentItem(pos);
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
}