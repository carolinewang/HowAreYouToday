package linyingwang.howareyoutoday;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.gujun.android.taggroup.TagGroup;


public class TagEditorActivity extends ActionBarActivity {
	public final static String TAGSEDITED = "Tags edited by user";
	private TagGroup mTagGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_editor);
		Intent i = getIntent();
		String[] tags = i.getStringArrayExtra(Application.TAGCONTENTS);
		int moodCategory = i.getIntExtra(Application.MOOD_CATEGORY, 1);
		mTagGroup = (TagGroup) findViewById(R.id.tag_group);

		if (tags != null && tags.length > 0) {
			mTagGroup.setTags(tags);
		}
		ActionBar bar = getSupportActionBar();
		switch (moodCategory) {

			case 2:
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.good)));
				break;
			case 3:
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ok)));
				break;
			case 4:
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sad)));
				break;
			case 5:
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.angry)));
				break;
			case 6:
				bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.crying)));
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_tag_editor_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
//			mTagsManager.updateTags(mTagGroup.getTags());
			finish();
			return true;
		} else if (item.getItemId() == R.id.action_submit) {

			String[] tagsNew = mTagGroup.getTags();
			Intent intent = new Intent();
			intent.putExtra(TAGSEDITED, tagsNew);
			setResult(RESULT_OK, intent);
			finish();
			mTagGroup.submitTag();
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
//		mTagsManager.updateTags(mTagGroup.getTags());
		super.onBackPressed();
	}
}