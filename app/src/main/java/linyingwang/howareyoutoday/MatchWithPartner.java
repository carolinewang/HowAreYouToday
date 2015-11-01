package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MatchWithPartner extends ActionBarActivity {
	private static final String TAG = MatchWithPartner.class.getSimpleName();
	protected static ParseACL partnerACL;
	private ProgressBar progressBar;
//	protected static User partnerUser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_with_partner);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			Intent i = new Intent(this, Welcome.class);
//			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
		}
	}

	public void skipMatching(View v) {
		Toast.makeText(MatchWithPartner.this, R.string.toast_matchLater, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(MatchWithPartner.this, MainActivity.class);
		startActivity(intent);
	}

	public void matchPartner(View v) {
		final String partnerName = ((EditText) findViewById(R.id.username)).getText().toString();
		final User currentUser = (User) ParseUser.getCurrentUser();

		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.setLimit(1);
		query.whereEqualTo("username", partnerName);
//		query.whereNotEqualTo("partnerName",partnerName);
		query.findInBackground(new FindCallback<ParseUser>() {
					@Override
					public void done(final List<ParseUser> partners, ParseException e) {
						progressBar.setVisibility(View.GONE);
						if (e == null) {
							if (partners.size() == 0) {
								Toast.makeText(MatchWithPartner.this, R.string.toast_match_no_user, Toast.LENGTH_SHORT).show();
								return;
							}
							final User partner = (User) partners.get(0);
							Log.e(TAG, "User found: " + partner.getUsername());
							progressBar.setVisibility(View.VISIBLE);
							if (partner.getPartner() != null) {
								progressBar.setVisibility(View.GONE);
								try {
									if (partner.getPartner().equals(currentUser)) {
										Toast.makeText(MatchWithPartner.this, R.string.toast_match_found, Toast.LENGTH_LONG).show();
									} else {
										Log.d(TAG, "The user already has another partner" + partner.getPartnerName());
										Toast.makeText(MatchWithPartner.this, R.string.toast_match_already, Toast.LENGTH_LONG).show();
										return;
									}
								} catch (Exception e2) {
								}
							} else {
								Toast.makeText(MatchWithPartner.this, R.string.toast_match_not_found, Toast.LENGTH_SHORT).show();
							}
							progressBar.setVisibility(View.VISIBLE);
							currentUser.setPartner(partner);
							currentUser.setPartnerName(partnerName);
							partnerACL = new ParseACL(currentUser);
							partnerACL.setReadAccess(partner, true);
//							partnerACL.setWriteAccess(partner, true);
							currentUser.saveEventually(new SaveCallback() {
								@Override
								public void done(ParseException e) {
									progressBar.setVisibility(View.GONE);
									Toast.makeText(MatchWithPartner.this,
											getString(R.string.toast_match) +
													partner.getUsername(), Toast.LENGTH_SHORT).show();
								}
							});

							Intent intent = new Intent(MatchWithPartner.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
//							finish();

						} else {
							Log.e(TAG, "Error" + e.getMessage());
						}

					}
				}


		);
		progressBar.setVisibility(View.VISIBLE);
	}


}
