package linyingwang.howareyoutoday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class HistoryPartnerFragment extends Fragment {
	private ImageView bg;
	private ArrayAdapter<ParseObject> mAdapter;
	private ListView list;
	private TextView noMoodPrompt;
	private Button button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_history_fragment, container, false);
		list = (ListView) rootView.findViewById(R.id.list);
		bg = (ImageView) rootView.findViewById(R.id.bg);
		noMoodPrompt = (TextView) rootView.findViewById(R.id.no_mood_prompt);
		button = (Button) rootView.findViewById(R.id.button_match);
		return rootView;

	}

	@Override
	public void onResume() {
		super.onResume();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Mood");
		User partner = MainActivity.currentUser.getPartner();
		if (partner != null) {
			query.whereEqualTo("user", partner);
			query.orderByDescending("createdAt");
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> moods, ParseException e) {
					if (e == null) {
						if (moods.size() == 0) {

							bg.setImageResource(R.drawable.unknown_grey);

						} else {
							noMoodPrompt.setVisibility(View.GONE);
							bg.setVisibility(View.GONE);
							mAdapter = new CustomAdapterPartner(getActivity(), moods);
							list.setAdapter(mAdapter);
						}
					} else {
						Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
					}
				}
			});
		} else {
			noMoodPrompt.setText(R.string.partner_notmatch);
			button.setVisibility(View.VISIBLE);
		}
	}


}


