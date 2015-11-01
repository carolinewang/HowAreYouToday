package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MyAccount extends ActionBarActivity {

	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ImageView profileImg = (ImageView) findViewById(R.id.profileImg);
		User currentUser = (User) ParseUser.getCurrentUser();
		String gender = currentUser.getGender();
		if (gender.equals("male")) {
			profileImg.setImageResource(R.drawable.profile_default_male);
		} else if (gender.equals("female")) {
			profileImg.setImageResource(R.drawable.profile_default_female);
		}
		TextView username = (TextView) findViewById(R.id.username);
		username.setText(currentUser.getUsername());
		TextView email = (TextView) findViewById(R.id.email);
		email.setText(currentUser.getEmail());
		TextView partner = (TextView) findViewById((R.id.partner));
		String partnerUser = currentUser.getPartnerName();
		if (partnerUser != null) {
			partner.setText(partnerUser);
		}

	}

	public void goToMatchPage(View v) {
		User currentUser = (User) ParseUser.getCurrentUser();
		User partner = currentUser.getPartner();
		if (partner == null) {
			Intent i = new Intent(this, MatchWithPartner.class);
			startActivity(i);
		} else {
			Toast.makeText(MyAccount.this, "You have already matched with: " + currentUser.getPartnerName() + " !",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void logOut(View v) {
		ParseUser.logOutInBackground(new LogOutCallback() {
			@Override
			public void done(ParseException e) {
				progressBar.setVisibility(View.GONE);
				if (e == null) {
					Toast.makeText(MyAccount.this, R.string.toast_logout, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MyAccount.this, Welcome.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
		});
		progressBar.setVisibility(View.VISIBLE);
	}
}
