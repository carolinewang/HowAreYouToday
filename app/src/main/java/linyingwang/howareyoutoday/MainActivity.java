package linyingwang.howareyoutoday;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	protected static int moodCategory = 0;
	protected static String moodDescription;
	protected static Date date;
	protected static int ifWantComfort;
	protected static String[] tags;
	protected static User currentUser;
	protected static boolean isOnline;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpTabs();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_new) {
			Intent intent = new Intent(this, NewMood.class);
			startActivity(intent);
			return true;
		}

		if (id == R.id.action_call) {
			Intent i = new Intent(Intent.ACTION_DIAL);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			if (i.resolveActivity(getPackageManager()) != null) {
				startActivity(i);
			}
			return true;
		}

		if (id == R.id.action_profile) {
			Intent i = new Intent(this, MyAccount.class);
			startActivity(i);
			return true;
		}

		if (id == R.id.action_settings) {
			Intent i = new Intent(this, Settings.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isOnline()) {
//			setUpTabs();
			currentUser = (User) ParseUser.getCurrentUser();
			if (currentUser != null) {
//				getRecentMoodFromIntent();
//				changeActionBarColor();
//				queryRecentMoodFromParse();
			} else {
				Intent i = new Intent(this, Welcome.class);
				startActivity(i);
			}
		} else {
//			Toast.makeText(MainActivity.this, R.string.toast_no_internet, Toast.LENGTH_LONG).show();
			showDialogWhenOffline();
		}

	}
	public void showDialogWhenOffline(){
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("No Internet Connection")
				.setMessage("Oops, seems you are not connected to Internet. Please retry when you are connected.")
				.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onResume();
					}
				}).setNegativeButton("OK", null).show();
	}

	public void getRecentMoodFromIntent() {
		Intent i = getIntent();
		moodCategory = i.getIntExtra(Application.MOOD_CATEGORY, 0);
		moodDescription = i.getStringExtra(Application.MOOD_DESCRIPTION);
		long time = i.getLongExtra(Application.DATE, -1);
		date = new Date();
		date.setTime(time);
		tags = i.getStringArrayExtra(Application.TAGCONTENTS);
		ifWantComfort = i.getIntExtra(Application.COMFORT, 0);
	}


	public void goToMatchPage(View v) {
		Intent i = new Intent(this, MatchWithPartner.class);
		startActivity(i);
	}

	public boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		isOnline = networkInfo != null && networkInfo.isConnected();
		return isOnline;
	}

	public void setUpTabs() {
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			switch (position) {
				case 0:
					return new TodayFragment();
				case 1:
					return new HistoryFragment();
				case 2:
					return new HistoryPartnerFragment();
				default:
					return new TodayFragment();
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}

