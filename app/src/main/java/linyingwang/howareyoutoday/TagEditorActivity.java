package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.gujun.android.taggroup.TagGroup;


public class TagEditorActivity extends ActionBarActivity {
	public final static String TAGSEDITED = "Tags edited by user";
	private TagGroup mTagGroup;
	private String[] tags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_editor);
		Intent i = getIntent();
		tags = i.getStringArrayExtra(Sad.TAGCONTENTS);
		mTagGroup = (TagGroup) findViewById(R.id.tag_group);

		if (tags != null && tags.length > 0) {
			mTagGroup.setTags(tags);
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