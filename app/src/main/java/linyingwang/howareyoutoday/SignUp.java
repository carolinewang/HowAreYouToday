package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.SignUpCallback;

public class SignUp extends ActionBarActivity {
	private RadioButton radioButtonMale;
	private RadioButton radioButtonFemale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		radioButtonMale = (RadioButton) findViewById(R.id.radiobutton_male);
		radioButtonFemale = (RadioButton) findViewById(R.id.radiobutton_female);

	}

	public void register(View v) {
		EditText editText = (EditText) findViewById(R.id.name);
		final String username = editText.getText().toString();
		final String password = ((EditText) findViewById(R.id.password)).getText().toString();
		String password2 = ((EditText) findViewById(R.id.password2)).getText().toString();
		final String email = ((EditText) findViewById(R.id.email)).getText().toString();

		if (username.equals("") | email.equals("") || password.equals("") || password2.equals("")) {
			Toast.makeText(SignUp.this, R.string.toast_signup_alert, Toast.LENGTH_SHORT).show();
			return;
		}
		String gender;
		if (radioButtonMale.isChecked())
			gender = "male";
		else if (radioButtonFemale.isChecked())
			gender = "female";
		else {
			Toast.makeText(this, R.string.toast_signup_alert_gender, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!(password.equals(password2))) {
			Toast.makeText(SignUp.this, R.string.toast_password, Toast.LENGTH_SHORT).show();
		} else {
//			ParseUser user = new ParseUser();
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
//			user.put("gender", gender);
			user.setGender(gender);
			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					if (e == null) {
						Toast.makeText(SignUp.this, R.string.toast_signup,
								Toast.LENGTH_SHORT).show();
						Toast.makeText(SignUp.this, R.string.toast_login, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SignUp.this, MatchWithPartner.class);
						startActivity(intent);
					} else {
						Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
			case R.id.radiobutton_female:
				radioButtonMale.setChecked(false);
				break;
			case R.id.radiobutton_male:
				radioButtonFemale.setChecked(false);
				break;
		}
	}
}

