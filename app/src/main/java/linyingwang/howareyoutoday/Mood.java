package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.Arrays;
import java.util.Date;

import me.gujun.android.taggroup.TagGroup;

public class Mood extends Activity {
	private ProgressBar progressBar;
	private TextView wordCount;

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//This sets a textview to the current length
			wordCount.setText(String.valueOf(50 - s.length()));
		}

		public void afterTextChanged(Editable s) {
		}
	};
	private RelativeLayout relativeLayout;
	private LinearLayout layout;
	private ImageView myMoodEmoji;
	private TextView mPromptText;
	private EditText moodDescription;
	private TagGroup mTagGroup;
	private String[] tags;
	private Button button;
	private int moodCategory;
	private int ifWantComfort = 0;
	private View root;
	private boolean badMood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood);
//		View layout = findViewById(R.id.relative_layout);

		// Find the root view


		moodCategory = getIntent().getIntExtra(NewMood.MOOD_CATEGORY, 0);

		relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
//		layout = (LinearLayout)findViewById(R.id.layout);
		myMoodEmoji = (ImageView) findViewById(R.id.imageView);
		root = myMoodEmoji.getRootView();
		Log.d("Mood", root.toString());
		button = (Button) findViewById(R.id.button2);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		moodDescription = (EditText) findViewById(R.id.mood_description);
		wordCount = (TextView) findViewById(R.id.textView);
		mTagGroup = (TagGroup) findViewById(R.id.tag_group);
		mPromptText = (TextView) findViewById(R.id.tv_prompt);

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMoodContext();
		moodDescription.addTextChangedListener(mTextEditorWatcher);

		mPromptText.setVisibility((tags == null || tags.length == 0) ? View.VISIBLE : View.GONE);
		mPromptText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchTagEditorActivity();
			}
		});
		if (tags != null && tags.length > 0) {
			mTagGroup.setTags(tags);

		}
//		mTagGroup.setTags(getResources().getStringArray(R.array.tags_sad));
		mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
			@Override
			public void onTagClick(String s) {
//				if (s !=  null) {
//					Toast.makeText(Sad.this, s, Toast.LENGTH_SHORT).show();
//				}else Toast.makeText(Sad.this, "null", Toast.LENGTH_SHORT).show();
				launchTagEditorActivity();
			}
		});
	}

	protected void launchTagEditorActivity() {
		Intent intent = new Intent(Mood.this, TagEditorActivity.class);
		intent.putExtra(Application.TAGCONTENTS, tags);
		intent.putExtra(Application.MOOD_CATEGORY, moodCategory);
		startActivityForResult(intent, 1234);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234 && resultCode == RESULT_OK) {
			tags = data.getStringArrayExtra(TagEditorActivity.TAGSEDITED);

		}
	}

	public void onRadioButtonClicked(View view) {
		RadioButton radioButtonAlone = (RadioButton) findViewById(R.id.radioButton_alone);
		RadioButton radioButtonComfort = (RadioButton) findViewById(R.id.radioButton_comfort);
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
			case R.id.radioButton_alone:
				if (checked)
					ifWantComfort = 2;
				radioButtonComfort.setChecked(false);
				break;
			case R.id.radioButton_comfort:
				if (checked)
					ifWantComfort = 1;
				radioButtonAlone.setChecked(false);
				break;
		}
	}


	public void RecordMood(View v) throws ParseException {
		if (MainActivity.isOnline) {
//		Toast.makeText(Sad.this, tags[1], Toast.LENGTH_SHORT).show();
			ParseObject mood = new ParseObject("Mood");
			final User currentUser = (User) ParseUser.getCurrentUser();
//		String date = new SimpleDateFormat("HH:mm MM/dd/yyyy").format(new Date());
			final Date date = new Date();
			mood.put("category", moodCategory);
			if (tags != null) {
				mood.addAllUnique("tags", Arrays.asList(tags));
			}
			mood.put("description", moodDescription.getText().toString());
			mood.put("ifWantComfort", ifWantComfort);
			mood.put("user", currentUser);
			mood.put("date", date);

			ParseACL moodACL = new ParseACL(currentUser);
			if (currentUser.getPartner() != null) {
				moodACL.setReadAccess(currentUser.getPartner(), true);
			}
			mood.setACL(moodACL);

//		mood.pinInBackground(new SaveCallback() {
//			@Override
//			public void done(ParseException e) {
//				if(e==null){
//					Toast.makeText(Sad.this, R.string.toast_record_mood, Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(Sad.this, MainActivity.class);
//					intent.putExtra(Application.MOOD_CATEGORY, moodCategory);
//					intent.putExtra(Application.MOOD_DESCRIPTION, moodDescription.getText().toString());
//					intent.putExtra(Application.TAGCONTENTS, tags);
//					intent.putExtra(Application.DATE, date.getTime());
//					startActivity(intent);
//				}else{
//					Toast.makeText(Sad.this,e.toString(),Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		mood.saveEventually();
			mood.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					progressBar.setVisibility(View.GONE);
					if (e == null) {
						Toast.makeText(Mood.this, R.string.toast_record_mood, Toast.LENGTH_SHORT).show();

						ParsePush.subscribeInBackground("Couple" + "_" + currentUser.getUsername());
						ParsePush push = new ParsePush();
						push.setChannel("Couple" + "_" + currentUser.getPartnerName());
						if(badMood){
							push.setMessage("Your partner is having a bad mood today. Check it out.");
						}else{
							push.setMessage("Your partner has just recorded a new mood for today. Check it out.");
						}
						push.sendInBackground();

						Intent intent = new Intent(Mood.this, MainActivity.class);
//						intent.putExtra(Application.MOOD_CATEGORY, moodCategory);
//						intent.putExtra(Application.MOOD_DESCRIPTION, moodDescription.getText().toString());
//						intent.putExtra(Application.TAGCONTENTS, tags);
//						intent.putExtra(Application.DATE, date.getTime());
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);

//					finish();
					} else {
						Toast.makeText(Mood.this, e.toString(), Toast.LENGTH_SHORT).show();
					}
				}
			});
			progressBar.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(Mood.this, R.string.toast_no_internet, Toast.LENGTH_LONG).show();
		}
	}

	public void setUpMoodContext() {
		switch (moodCategory) {
			case 1:
				relativeLayout.setBackgroundColor(getResources().getColor(R.color.primary));
				myMoodEmoji.setImageResource(R.drawable.happy);
				button.setTextColor(getResources().getColor(R.color.primary));
				moodDescription.setHint(R.string.mood_description_hint_happy);
				break;
			case 2:
				relativeLayout.setBackgroundColor(getResources().getColor(R.color.good));
				myMoodEmoji.setImageResource(R.drawable.good);
				button.setTextColor(getResources().getColor(R.color.good));
				moodDescription.setHint(R.string.mood_description_hint_good);
				break;
			case 3:
				relativeLayout.setBackgroundColor(getResources().getColor(R.color.ok));
				myMoodEmoji.setImageResource(R.drawable.ok);
				button.setTextColor(getResources().getColor(R.color.ok));
				moodDescription.setHint(R.string.mood_description_hint_ok);
				break;
			case 4:
				relativeLayout.setBackgroundColor(getResources().getColor(R.color.sad));
				myMoodEmoji.setImageResource(R.drawable.sad);
				button.setTextColor(getResources().getColor(R.color.sad));
				moodDescription.setHint(R.string.mood_description_hint_sad);
				break;
			case 5:
				relativeLayout.setBackgroundColor(getResources().getColor(R.color.angry));
				myMoodEmoji.setImageResource(R.drawable.angry);
				button.setTextColor(getResources().getColor(R.color.angry));
				moodDescription.setHint(R.string.mood_description_hint_angry);
				break;
			case 6:
				relativeLayout.setBackgroundColor(getResources().getColor(R.color.crying));
				myMoodEmoji.setImageResource(R.drawable.cry);
				button.setTextColor(getResources().getColor(R.color.crying));
				moodDescription.setHint(R.string.mood_description_hint_crying);
				break;
		}
		if (moodCategory == 4 || moodCategory == 5 || moodCategory == 6) {
			RadioButton radioButtonAlone = (RadioButton) findViewById(R.id.radioButton_alone);
			RadioButton radioButtonComfort = (RadioButton) findViewById(R.id.radioButton_comfort);
			radioButtonAlone.setVisibility(View.VISIBLE);
			radioButtonComfort.setVisibility(View.VISIBLE);
			badMood = true;
		}
	}

}
