package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewMood extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_mood);
	}

	public void sad(View v) {
		Intent intent = new Intent(this, Sad.class);
		startActivity(intent);
	}

	public void happy(View v) {
		Intent intent = new Intent(this, Sad.class);
		startActivity(intent);
	}

	public void good(View v) {
		Intent intent = new Intent(this, Sad.class);
		startActivity(intent);
	}

	public void ok(View v) {
		Intent intent = new Intent(this, Sad.class);
		startActivity(intent);
	}

	public void crying(View v) {
		Intent intent = new Intent(this, Sad.class);
		startActivity(intent);
	}

	public void angry(View v) {
		Intent intent = new Intent(this, Sad.class);
		startActivity(intent);
	}


}
