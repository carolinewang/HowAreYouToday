package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class MyAccount extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ImageView profileImg = (ImageView) findViewById(R.id.profileImg);
		ParseUser currentUser = ParseUser.getCurrentUser();
		String gender = currentUser.getString("gender");
		if (gender.equals("male")) {
			profileImg.setImageResource(R.drawable.profile_default_male);
		} else if (gender.equals("female")) {
			profileImg.setImageResource(R.drawable.profile_default_female);
		}
		TextView username = (TextView) findViewById(R.id.username);
		username.setText(currentUser.getString("username"));
		TextView email = (TextView) findViewById(R.id.email);
		email.setText(currentUser.getString("email"));
		TextView partner = (TextView) findViewById((R.id.partner));
		String partnerUser = currentUser.getString("partner");
		if (partnerUser != null) {
			partner.setText(partnerUser);
		}

	}

	public void goToMatchPage(View v) {
		ParseUser currentUser = ParseUser.getCurrentUser();
		String partner = currentUser.getString("partner");
		if (partner == null) {
			Intent i = new Intent(this, MatchWithPartner.class);
			startActivity(i);
		} else {
			Toast.makeText(MyAccount.this, "You have already matched with: " + partner + " !",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void logOut(View v) {
		ParseUser.logOut();
		ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
		if (currentUser == null) {
			Toast.makeText(MyAccount.this, R.string.toast_logout, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MyAccount.this, Welcome.class);
			startActivity(intent);
		}
	}
}
