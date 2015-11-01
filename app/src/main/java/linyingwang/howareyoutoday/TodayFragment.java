package linyingwang.howareyoutoday;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.gujun.android.taggroup.TagGroup;

public class TodayFragment extends Fragment {
	private static final String TAG = TodayFragment.class.getSimpleName();
	protected int moodCategory = 0;
	protected String moodDescription;
	protected Date date;
	protected int myIfWantComfort;
	protected ArrayList<String> tags;

	protected int moodCategoryPartner = 0;
	protected String moodDescriptionPartner;
	protected Date datePartner;
	protected int partnerIfWantComfort;
	protected ArrayList<String> tagsPartner;

	private LinearLayout myMoodLayout;
	private TextView myMoodTitle;
	private TextView myMoodDescription;
	private TextView ifWantComfort;
	private TextView myDate;
	private ImageView myMoodEmoji;
	private ImageView check;
	private TagGroup myTags;

	private LinearLayout partnerMoodLayout;
	private TextView partnerMoodTitle;
	private TextView partnerMoodDescription;
	private TextView ifWantComfortPartner;
	private TextView partnerDate;
	private ImageView partnerMoodEmoji;
	private ImageView checkPartner;
	private TagGroup partnerTags;
	private Date midnightYesterday;
	private Date midnightTonight;
	private Button matchButton;
	private Button newMoodButton;
	private ProgressBar progressBarMe;
	private ProgressBar progressBarPartner;

	private LinearLayout line;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_today_fragment, container, false);
		//inflate the layout of my mood
		myMoodLayout = (LinearLayout) rootView.findViewById(R.id.my_mood);
		Log.d("Today Fragment", myMoodLayout.toString());
		myMoodTitle = (TextView) rootView.findViewById(R.id.mood_title);
		myMoodDescription = (TextView) rootView.findViewById(R.id.my_mood_description);
		myDate = (TextView) rootView.findViewById(R.id.my_date);
		ifWantComfort = (TextView) rootView.findViewById(R.id.if_want_comfort);
		myMoodEmoji = (ImageView) rootView.findViewById(R.id.my_mood_emoji);
		check = (ImageView) rootView.findViewById(R.id.my_check);
		myTags = (TagGroup) rootView.findViewById(R.id.tag_group);
		progressBarMe = (ProgressBar) rootView.findViewById(R.id.progressBarMe);


		//inflate the layout of my partner's mood
		partnerMoodLayout = (LinearLayout) rootView.findViewById(R.id.partner_mood);
		partnerMoodTitle = (TextView) rootView.findViewById(R.id.partner_mood_title);
		partnerDate = (TextView) rootView.findViewById(R.id.partner_date);
		partnerMoodDescription = (TextView) rootView.findViewById(R.id.partner_mood_description);
		ifWantComfortPartner = (TextView) rootView.findViewById(R.id.partner_if_want_comfort);
		partnerMoodEmoji = (ImageView) rootView.findViewById(R.id.partner_mood_emoji);
		checkPartner = (ImageView) rootView.findViewById(R.id.partner_check);
		partnerTags = (TagGroup) rootView.findViewById(R.id.partner_tag_group);
		matchButton = (Button) rootView.findViewById(R.id.button_match);
		progressBarPartner = (ProgressBar) rootView.findViewById(R.id.progressBarPartner);

		line = (LinearLayout) rootView.findViewById(R.id.line);

//		setUpMoodContext();
//		displayMyMood();
//		changeMyMoodBgFromIntent();
//		displayMyMoodFromIntent();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (MainActivity.isOnline) {
//			changeMyMoodBgFromIntent();
//			displayMyMoodFromIntent();
			queryMood();
			queryPartnerMood();
//		setUpMoodContext();
			if (moodCategory != 0 && moodCategoryPartner != 0 && moodCategory == moodCategoryPartner) {
				line.setVisibility(View.VISIBLE);
//			partnerMoodEmoji.setVisibility(View.GONE);
			}
		}

	}

	public void displayMyMoodFromParse() {

		if (moodCategory != 0) {
			myMoodTitle.setText(R.string.display_mood);
//			myMoodTitle.setTextColor(getResources().getColor(R.color.white));
//			String time = new SimpleDateFormat("HH:mm MM/dd").format(MainActivity.date);
			String time = DateFormat.getTimeInstance().format(date);
			myDate.setText(time);
			myMoodDescription.setText(moodDescription);
			if (tags != null) {
				myTags.setTags(tags);
			}
			if (moodCategory == 4 || moodCategory == 5 || moodCategory == 6) {
				switch (myIfWantComfort) {
					case 1:
						check.setImageResource(R.drawable.check);
						ifWantComfort.setText(R.string.choice_comfort);
						break;
					case 2:
						check.setImageResource(R.drawable.check);
						ifWantComfort.setText(R.string.choice_alone);
						break;
				}
			}
		}
	}

	public void displayMyMoodFromIntent() {

		if (MainActivity.moodCategory != 0) {
			myMoodTitle.setText(R.string.display_mood);
//			myMoodTitle.setTextColor(getResources().getColor(R.color.white));
//			String time = new SimpleDateFormat("HH:mm MM/dd").format(MainActivity.date);
			String time = DateFormat.getTimeInstance().format(MainActivity.date);
			myDate.setText(time);
			myMoodDescription.setText(MainActivity.moodDescription);
			if (MainActivity.tags != null) {
				myTags.setTags(MainActivity.tags);
			}
			if (MainActivity.moodCategory == 4 || MainActivity.moodCategory == 5 || MainActivity.moodCategory == 6) {
				switch (MainActivity.ifWantComfort) {
					case 1:
						check.setImageResource(R.drawable.check);
						ifWantComfort.setText(R.string.choice_comfort);
						break;
					case 2:
						check.setImageResource(R.drawable.check);
						ifWantComfort.setText(R.string.choice_alone);
						break;
				}
			}
		}
	}


	public void queryMood() {
		getTimeOfToday();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Mood");
//		query.fromLocalDatastore();
		query.whereEqualTo("user", MainActivity.currentUser);
		query.whereGreaterThanOrEqualTo("createdAt", midnightYesterday);
		query.whereLessThan("createdAt", midnightTonight);
		query.orderByDescending("createdAt");
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject mood, com.parse.ParseException e) {
				progressBarMe.setVisibility(View.GONE);
				if (mood != null) {
					moodCategory = mood.getInt("category");
					moodDescription = mood.getString("description");
					myIfWantComfort = mood.getInt("ifWantComfort");
					date = mood.getCreatedAt();
					tags = (ArrayList<String>) mood.get("tags");
					Log.d(TAG, "query in TodayFragment" + moodCategory + moodDescription + date + tags);
					changeMyMoodBg();
					displayMyMoodFromParse();
				} else {
					Log.d("error", e.toString());
				}
			}
		});
		progressBarMe.setVisibility(View.VISIBLE);
	}

	public void changeMyMoodBg() {
		ActionBar bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		switch (moodCategory) {
			case 1:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.primary));
				Log.d("Today Fragment", myMoodLayout.toString());
				myMoodEmoji.setImageResource(R.drawable.happy);
				break;
			case 2:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.good));
				myMoodEmoji.setImageResource(R.drawable.good);
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.good)));
				bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.good)));
				break;
			case 3:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.ok));
				myMoodEmoji.setImageResource(R.drawable.ok);
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ok)));
				bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ok)));
				break;
			case 4:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.sad));
				myMoodEmoji.setImageResource(R.drawable.sad);
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sad)));
				bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sad)));
				break;
			case 5:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.angry));
				myMoodEmoji.setImageResource(R.drawable.angry);
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.angry)));
				bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.angry)));
				break;
			case 6:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.crying));
				myMoodEmoji.setImageResource(R.drawable.cry);
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.crying)));
				bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.crying)));
				break;
		}
	}

	public void changeMyMoodBgFromIntent() {
		switch (MainActivity.moodCategory) {
			case 1:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.primary));
				myMoodEmoji.setImageResource(R.drawable.happy);
				break;
			case 2:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.good));
				myMoodEmoji.setImageResource(R.drawable.good);
				break;
			case 3:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.ok));
				myMoodEmoji.setImageResource(R.drawable.ok);
				break;
			case 4:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.sad));
				myMoodEmoji.setImageResource(R.drawable.sad);
				break;
			case 5:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.angry));
				myMoodEmoji.setImageResource(R.drawable.angry);
				break;
			case 6:
				myMoodLayout.setBackgroundColor(getResources().getColor(R.color.crying));
				myMoodEmoji.setImageResource(R.drawable.cry);
				break;
		}
	}

	public void getTimeOfToday() {
		Calendar calStart = new GregorianCalendar();
		calStart.setTime(new Date());
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);
		midnightYesterday = calStart.getTime();

		Calendar calEnd = new GregorianCalendar();
		calEnd.setTime(new Date());
		calEnd.set(Calendar.DAY_OF_YEAR, calEnd.get(Calendar.DAY_OF_YEAR) + 1);
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 59);
		midnightTonight = calEnd.getTime();
	}

	public void queryPartnerMood() {
		User partner = MainActivity.currentUser.getPartner();
		if (partner == null) {
			matchButton.setVisibility(View.VISIBLE);
			partnerMoodTitle.setText(R.string.partner_notmatch);
		} else {
			getTimeOfToday();
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Mood");
//		query.fromLocalDatastore();
			query.whereEqualTo("user", partner);
			query.whereGreaterThanOrEqualTo("createdAt", midnightYesterday);
			query.whereLessThan("createdAt", midnightTonight);
			query.orderByDescending("createdAt");
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject mood, com.parse.ParseException e) {
					progressBarPartner.setVisibility(View.GONE);
					if (mood != null) {
						moodCategoryPartner = mood.getInt("category");
						moodDescriptionPartner = mood.getString("description");
						partnerIfWantComfort = mood.getInt("ifWantComfort");
						datePartner = mood.getCreatedAt();
						tagsPartner = (ArrayList<String>) mood.get("tags");
						Log.d(TAG, "partner query in TodayFragment" + moodCategoryPartner + moodDescriptionPartner + datePartner + tagsPartner);
						changePartnerMoodBg();
						displayPartnerMood();
					} else {
						Log.d("error", e.toString());
					}
				}
			});
			progressBarPartner.setVisibility(View.VISIBLE);
		}
	}

	public void displayPartnerMood() {

		if (moodCategoryPartner != 0) {
			partnerMoodTitle.setText(R.string.display_mood_partner);
			partnerMoodTitle.setTextColor(getResources().getColor(R.color.white));
//			String time = new SimpleDateFormat("HH:mm MM/dd").format(MainActivity.date);
			String time = DateFormat.getTimeInstance().format(datePartner);
			partnerDate.setText(time);
			partnerMoodDescription.setText(moodDescriptionPartner);
			if (tagsPartner != null) {
				partnerTags.setTags(tagsPartner);
			}
			if (moodCategoryPartner == 4 || moodCategoryPartner == 5 || moodCategoryPartner == 6) {
				switch (partnerIfWantComfort) {
					case 1:
						checkPartner.setImageResource(R.drawable.check);
						ifWantComfortPartner.setText(R.string.choice_comfort);
						break;
					case 2:
						checkPartner.setImageResource(R.drawable.check);
						ifWantComfortPartner.setText(R.string.choice_alone);
						break;
				}
			}
		}
	}

	public void changePartnerMoodBg() {

		switch (moodCategoryPartner) {
			case 1:
				partnerMoodLayout.setBackgroundColor(getResources().getColor(R.color.primary));
				partnerMoodEmoji.setImageResource(R.drawable.happy);
				break;
			case 2:
				partnerMoodLayout.setBackgroundColor(getResources().getColor(R.color.good));
				partnerMoodEmoji.setImageResource(R.drawable.good);
				break;
			case 3:
				partnerMoodLayout.setBackgroundColor(getResources().getColor(R.color.ok));
				partnerMoodEmoji.setImageResource(R.drawable.ok);
				break;
			case 4:
				partnerMoodLayout.setBackgroundColor(getResources().getColor(R.color.sad));
				partnerMoodEmoji.setImageResource(R.drawable.sad);
				break;
			case 5:
				partnerMoodLayout.setBackgroundColor(getResources().getColor(R.color.angry));
				partnerMoodEmoji.setImageResource(R.drawable.angry);
				break;
			case 6:
				partnerMoodLayout.setBackgroundColor(getResources().getColor(R.color.crying));
				partnerMoodEmoji.setImageResource(R.drawable.cry);
				break;
		}
	}

}
