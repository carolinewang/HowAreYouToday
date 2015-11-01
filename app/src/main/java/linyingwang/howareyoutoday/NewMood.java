package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewMood extends Activity {
	public static final String MOOD_CATEGORY = "MOOD CATEGORY";
	private int moodCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_mood);
	}


	public void sad(View v) {
//		Intent intent = new Intent(this, Sad.class);
		Intent intent = new Intent(this, Mood.class);
		moodCategory = 4;
		intent.putExtra(MOOD_CATEGORY, moodCategory);
		startActivity(intent);
//		finish();
	}

	public void happy(View v) {
//		Intent intent = new Intent(this, Happy.class);
		Intent intent = new Intent(this, Mood.class);
		moodCategory = 1;
		intent.putExtra(MOOD_CATEGORY, moodCategory);
		startActivity(intent);
//		finish();
	}

	public void good(View v) {
//		Intent intent = new Intent(this, Good.class);
		Intent intent = new Intent(this, Mood.class);
		moodCategory = 2;
		intent.putExtra(MOOD_CATEGORY, moodCategory);
		startActivity(intent);
		finish();
	}

	public void ok(View v) {
//		Intent intent = new Intent(this, OK.class);
		Intent intent = new Intent(this, Mood.class);
		moodCategory = 3;
		intent.putExtra(MOOD_CATEGORY, moodCategory);
		startActivity(intent);
//		finish();
	}

	public void crying(View v) {
//		Intent intent = new Intent(this, Crying.class);
		Intent intent = new Intent(this, Mood.class);
		moodCategory = 6;
		intent.putExtra(MOOD_CATEGORY, moodCategory);
		startActivity(intent);
//		finish();
	}

	public void angry(View v) {
		Intent intent = new Intent(this, Mood.class);
		moodCategory = 5;
		intent.putExtra(MOOD_CATEGORY, moodCategory);
//		Intent intent = new Intent(this, Angry.class);
		startActivity(intent);
//		finish();
	}


}
