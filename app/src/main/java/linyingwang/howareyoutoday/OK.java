package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.Date;

import me.gujun.android.taggroup.TagGroup;

public class OK extends Activity {
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
	private TextView mPromptText;
	private EditText moodDescription;
	private TagGroup mTagGroup;
	private String[] tags;
	private int moodCategory = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ok);

		moodDescription = (EditText) findViewById(R.id.mood_description);
		wordCount = (TextView) findViewById(R.id.textView);
		mTagGroup = (TagGroup) findViewById(R.id.tag_group);
		mPromptText = (TextView) findViewById(R.id.tv_prompt);
//		if(!(tags == null || tags.length == 0)){
//			mPromptText.setVisibility(View.GONE);
//		}

	}

	@Override
	protected void onResume() {
		super.onResume();

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
		Intent intent = new Intent(OK.this, TagEditorActivity.class);
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

	public void RecordMood(View v) {
//		Toast.makeText(Sad.this, tags[1], Toast.LENGTH_SHORT).show();
		ParseObject mood = new ParseObject("Mood");
		User currentUser = (User) ParseUser.getCurrentUser();
//		String date = new SimpleDateFormat("HH:mm MM/dd/yyyy").format(new Date());
		final Date date = new Date();
		mood.put("category", moodCategory);
		if (tags != null) {
			mood.addAllUnique("tags", Arrays.asList(tags));
		}
		mood.put("description", moodDescription.getText().toString());
		mood.put("user", currentUser);
//		mood.put("date", date);

		ParseACL moodACL = new ParseACL(currentUser);
		moodACL.setReadAccess(currentUser.getPartner(), true);
		mood.setACL(moodACL);
//		mood.pinInBackground(new SaveCallback() {
//			@Override
//			public void done(ParseException e) {
//				if(e == null){
//					Toast.makeText(Good.this, R.string.toast_record_mood, Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(Good.this, MainActivity.class);
//					intent.putExtra(Application.MOOD_CATEGORY, moodCategory);
//					intent.putExtra(Application.MOOD_DESCRIPTION, moodDescription.getText().toString());
//					intent.putExtra(Application.TAGCONTENTS, tags);
//					intent.putExtra(Application.DATE, date.getTime());
//					startActivity(intent);
//				}else{
//					Toast.makeText(Good.this, e.toString(), Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		mood.saveEventually();
		mood.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(OK.this, R.string.toast_record_mood, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(OK.this, MainActivity.class);
					intent.putExtra(Application.MOOD_CATEGORY, moodCategory);
					intent.putExtra(Application.MOOD_DESCRIPTION, moodDescription.getText().toString());
					intent.putExtra(Application.TAGCONTENTS, tags);
					intent.putExtra(Application.DATE, date.getTime());
					startActivity(intent);
				} else {
					Toast.makeText(OK.this, e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});


	}
}
