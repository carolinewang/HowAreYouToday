package linyingwang.howareyoutoday;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;


public class CustomAdapterPartner extends ArrayAdapter<ParseObject> {
	private String myMoodDate;
	private String partnerMoodDate;
	private int myMoodCategory;
	private int partnerMoodCategory;

	public CustomAdapterPartner(Activity context, List<ParseObject> moods) {
		super(context, 0, moods);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView =
					LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item_partner, null);
		}

		final ParseObject mood = getItem(position);

		TextView date = (TextView) convertView.findViewById(R.id.date);
		TextView description = (TextView) convertView.findViewById(R.id.description);
		String moodTime = DateFormat.getDateTimeInstance().format(mood.getCreatedAt());
		date.setText(moodTime);

		ImageView myMood = (ImageView) convertView.findViewById(R.id.my_mood_emoji);

		myMoodCategory = mood.getInt("category");

		displayMoodEmoji(myMoodCategory, myMood);
		description.setText(mood.getString("description"));

		TagGroup tagGroup = (TagGroup) convertView.findViewById(R.id.tag_group);
		ArrayList<String> tags = (ArrayList<String>) mood.get("tags");
		if (tags != null) {
			tagGroup.setTags(tags);
		}

		return convertView;
	}

	public void displayMoodEmoji(int moodCategory, ImageView mood) {

		switch (moodCategory) {
			case 1:
				mood.setImageResource(R.drawable.happy_emoji);
				break;
			case 2:
				mood.setImageResource(R.drawable.good_emoji);
				break;
			case 3:
				mood.setImageResource(R.drawable.ok_emoji);
				break;
			case 4:
				mood.setImageResource(R.drawable.sad_emoji);
				break;
			case 5:
				mood.setImageResource(R.drawable.angry_emoji);
				break;
			case 6:
				mood.setImageResource(R.drawable.cry_emoji);
				break;
		}
	}

}

