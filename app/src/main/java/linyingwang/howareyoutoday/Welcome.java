package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class Welcome extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}

	public void goToSignUpPage(View view) {
		Intent intent = new Intent(this, SignUp.class);
		startActivity(intent);
	}

	public void goToLogInPage(View view) {
		Intent intent = new Intent(this, LogIn.class);
		startActivity(intent);
	}
}
