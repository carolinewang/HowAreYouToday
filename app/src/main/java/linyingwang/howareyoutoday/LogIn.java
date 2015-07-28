package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

public class LogIn extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
	}

	public void logIn(View v) {
		EditText editText = (EditText) findViewById(R.id.name);
		final String username = editText.getText().toString();
		final String password = ((EditText) findViewById(R.id.password)).getText().toString();
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser parseUser, com.parse.ParseException e) {
				if (e == null) {
					Toast.makeText(LogIn.this, R.string.toast_login, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LogIn.this, MainActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(LogIn.this, e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void forgetPassword(View v) {
		Intent i = new Intent(this, ForgetPassword.class);
		startActivity(i);
	}
}
