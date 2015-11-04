package linyingwang.howareyoutoday;


import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Application extends android.app.Application {
	public final static String TAGCONTENTS = "Tag contents users have input";
	public final static String MOOD_CATEGORY = "Mood category user has selected";
	public final static String MOOD_DESCRIPTION = "Mood description user has entered";
	public final static String COMFORT = "If user would want comfort";
	public final static String DATE = "Time & date";
	private static final String TAG = Application.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();

		ParseUser.registerSubclass(User.class);

		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "cLe2sE7bLUTUgXMPUB3tRYIdJ0rUnG3PQMpKTaTk",
				"6kjPLkHALsND9yNn4zplofgt2zBdnntWjqL7JixK");
		ParseACL.setDefaultACL(new ParseACL(), true);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if(e==null){
					Log.d("push","push successfully set up");
				}else{
					Log.d("push","push set up error" + e.toString());

				}
			}
		});

	}
}
