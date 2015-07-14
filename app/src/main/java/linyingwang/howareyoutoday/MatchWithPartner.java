package linyingwang.howareyoutoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MatchWithPartner extends Activity {
	protected static ParseACL partnerACL;
	protected static ParseUser partnerUser;
	protected static ParseRole partnerRole;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_with_partner);
	}

	public void matchPartner(View v) {
		String partnerName = ((EditText) findViewById(R.id.username)).getText().toString();
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.setLimit(1);
			query.whereEqualTo("username", partnerName);
			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> partner, ParseException e) {
					if (e == null) {
						partnerUser = partner.get(0);


						partnerACL = new ParseACL(currentUser);
						partnerACL.setReadAccess(partnerUser, true);
						partnerACL.setWriteAccess(partnerUser, true);
						ParseQuery<ParseRole> query = ParseQuery.getQuery(currentUser.getUsername() + "Couples");
						query.findInBackground(new FindCallback<ParseRole>() {
							@Override
							public void done(List<ParseRole> parseRoles, ParseException e) {
								if (e == null) {
									if (parseRoles.size() == 0) {
										partnerRole = new ParseRole(currentUser.getUsername() + "Couples", partnerACL);
									} else {
										partnerRole = parseRoles.get(0);
									}
									partnerRole.getUsers().add(currentUser);
									partnerRole.getUsers().add(partnerUser);
									partnerRole.saveInBackground(new SaveCallback() {
										@Override
										public void done(ParseException e) {
											if (e == null) {
												currentUser.put("partner", partnerUser.getUsername());
												currentUser.saveInBackground();
												partnerUser.saveInBackground(new SaveCallback() {
													@Override
													public void done(ParseException e) {
														Toast.makeText(MatchWithPartner.this,
																getString(R.string.toast_match) +
																		partnerUser.getUsername() +
																		getString((R.string.toast_match2)), Toast.LENGTH_SHORT).show();
													}
												});
												Intent intent = new Intent(MatchWithPartner.this, MainActivity.class);
												startActivity(intent);
											} else {
												Toast.makeText(MatchWithPartner.this, e.toString(), Toast.LENGTH_LONG).show();
											}

										}
									});
								}
							}
						});

					} else {
						Toast.makeText(MatchWithPartner.this, e.toString(), Toast.LENGTH_LONG).show();
					}
				}
			});
		} else {
			Intent i = new Intent(this, Welcome.class);
			startActivity(i);
		}

	}

	public void skipMatching(View v) {
		Toast.makeText(MatchWithPartner.this, R.string.toast_matchLater, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(MatchWithPartner.this, MainActivity.class);
		startActivity(intent);
	}

}
