package linyingwang.howareyoutoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;

import java.util.List;

public class MatchWithPartner extends ActionBarActivity {
	protected static ParseACL partnerACL;
	protected static ParseUser partnerUser;
	protected static ParseRole partnerRole;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_with_partner);
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
		final ParseUser currentUser = ParseUser.getCurrentUser();

		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.setLimit(1);
		query.whereEqualTo("username", partnerName);
		query.findInBackground(new FindCallback<ParseUser>() {

			                       @Override
			                       public void done(final List<ParseUser> partner, ParseException e) {
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
								                       try {
									                       partnerRole.pin();
								                       } catch (ParseException e1) {
									                       e1.printStackTrace();
								                       }
								                       partnerRole.saveEventually();
								                       currentUser.put("partner", partnerUser.getUsername());
								                       currentUser.saveEventually();
								                       partnerUser.saveEventually();
								                       Toast.makeText(MatchWithPartner.this,
										                       getString(R.string.toast_match) + " " +
												                       partnerUser.getUsername() +
												                       getString((R.string.toast_match2)), Toast.LENGTH_SHORT).show();
							                       }
							                       ParseQuery<ParseRole> queryPartnerRole = ParseQuery.getQuery(partnerName + "Couples");
							                       queryPartnerRole.findInBackground(new FindCallback<ParseRole>() {
								                       @Override
								                       public void done(List<ParseRole> parseRoles, ParseException e) {
									                       if (e == null) {
										                       if (parseRoles.size() == 0) {
											                       Toast.makeText
													                       (MatchWithPartner.this,
															                       "Your partner hasn't matched with you yet. "
																	                       + getString((R.string.toast_match2)),
															                       Toast.LENGTH_SHORT).show();
										                       } else {
											                       Toast.makeText
													                       (MatchWithPartner.this,
															                       "Congrats! You are now both matched with each other! ",
															                       Toast.LENGTH_SHORT).show();
										                       }

									                       }
								                       }
							                       });
							                       Intent intent = new Intent(MatchWithPartner.this, MainActivity.class);
							                       startActivity(intent);
						                       }


					                       });
				                       }
			                       }
		                       }

		);

	}


}
