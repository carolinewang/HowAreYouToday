package linyingwang.howareyoutoday;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;


public class CustomAdapter extends ArrayAdapter<ParseObject> {
	private String myMoodDate;
	private String partnerMoodDate;
	private int myMoodCategory;
	private int partnerMoodCategory;
	private ProgressBar progressBar;

	public CustomAdapter(Activity context, List<ParseObject> moods) {
		super(context, 0, moods);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			convertView =
					LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, null);
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

		ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
//		progressBar =(ProgressBar)convertView.findViewById(R.id.progressBar2);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete the Mood")
						.setMessage("Are you sure you want to delete the mood?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mood.deleteInBackground(new DeleteCallback() {
									@Override
									public void done(ParseException e) {
										remove(mood);
										notifyDataSetChanged();
										Toast.makeText(getContext(), "Mood deleted", Toast.LENGTH_SHORT).show();
									}
								});
							}
						}).setNegativeButton("No", null).show();

//				progressBar.setVisibility(View.VISIBLE);
			}
		});
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

