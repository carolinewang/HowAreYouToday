package linyingwang.howareyoutoday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	protected int moodCategory;
	protected String moodDescription;
	protected Date date;
	protected int myIfWantComfort;
	protected ArrayList<String> tags;
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
	private ImageView partnerMoodEmoji;
	private ImageView checkPartner;
	private TagGroup partnerTags;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_today_fragment, container, false);
		//inflate the layout of my mood
		myMoodLayout = (LinearLayout) rootView.findViewById(R.id.my_mood);

		myMoodTitle = (TextView) rootView.findViewById(R.id.mood_title);
		myMoodDescription = (TextView) rootView.findViewById(R.id.my_mood_description);
		myDate = (TextView) rootView.findViewById(R.id.my_date);
		ifWantComfort = (TextView) rootView.findViewById(R.id.if_want_comfort);
		myMoodEmoji = (ImageView) rootView.findViewById(R.id.my_mood_emoji);
		check = (ImageView) rootView.findViewById(R.id.my_check);
		myTags = (TagGroup) rootView.findViewById(R.id.tag_group);

		//inflate the layout of my partner's mood
		partnerMoodLayout = (LinearLayout) rootView.findViewById(R.id.partner_mood);
		partnerMoodTitle = (TextView) rootView.findViewById(R.id.mood_title_partner);
		partnerMoodDescription = (TextView) rootView.findViewById(R.id.partner_mood_description);
		ifWantComfortPartner = (TextView) rootView.findViewById(R.id.partner_if_want_comfort);
		partnerMoodEmoji = (ImageView) rootView.findViewById(R.id.partner_mood_emoji);
		checkPartner = (ImageView) rootView.findViewById(R.id.partner_check);
		partnerTags = (TagGroup) rootView.findViewById(R.id.partner_tag_group);

//		changeMyMoodBg();
//		displayMyMood();
		changeMyMoodBgFromIntent();
		displayMyMoodFromIntent();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		changeMyMoodBgFromIntent();
		displayMyMoodFromIntent();
		queryMood();
//		changeMyMoodBg();
	}

	public void displayMyMoodFromParse() {

		if (moodCategory != 0) {
			myMoodTitle.setText(R.string.display_mood);
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
//			String time = new SimpleDateFormat("HH:mm MM/dd").format(MainActivity.date);
			String time = DateFormat.getTimeInstance().format(MainActivity.date);
			myDate.setText(time);
			myMoodDescription.setText(MainActivity.moodDescription);
			if (tags != null) {
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
		Calendar calStart = new GregorianCalendar();
		calStart.setTime(new Date());
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);
		Date midnightYesterday = calStart.getTime();

		Calendar calEnd = new GregorianCalendar();
		calEnd.setTime(new Date());
		calEnd.set(Calendar.DAY_OF_YEAR, calEnd.get(Calendar.DAY_OF_YEAR) + 1);
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 59);
		Date midnightTonight = calEnd.getTime();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Mood");
//		query.fromLocalDatastore();
		query.whereEqualTo("user", MainActivity.currentUser);
		query.whereGreaterThanOrEqualTo("createdAt", midnightYesterday);
		query.whereLessThan("createdAt", midnightTonight);
		query.orderByDescending("createdAt");
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject mood, com.parse.ParseException e) {
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
	}

	public void changeMyMoodBg() {
		switch (moodCategory) {
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
}
