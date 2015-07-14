package linyingwang.howareyoutoday;


import com.parse.Parse;
import com.parse.ParseACL;

public class Application extends android.app.Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "cLe2sE7bLUTUgXMPUB3tRYIdJ0rUnG3PQMpKTaTk", "6kjPLkHALsND9yNn4zplofgt2zBdnntWjqL7JixK");
		ParseACL.setDefaultACL(new ParseACL(), true);
	}
}
