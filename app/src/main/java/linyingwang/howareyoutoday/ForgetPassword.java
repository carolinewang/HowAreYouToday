package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgetPassword extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
	}

	public void resetPassword(View v) {
		String email = ((EditText) findViewById(R.id.email)).getText().toString();
		ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
			public void done(ParseException e) {
				if (e == null) {
					// An email was successfully sent with reset instructions.
					Toast.makeText(ForgetPassword.this, R.string.toast_forgetPw,
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ForgetPassword.this, LogIn.class);
					startActivity(intent);
				} else {
					// Something went wrong. Look at the ParseException to see what's up.
					Toast.makeText(ForgetPassword.this, e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
