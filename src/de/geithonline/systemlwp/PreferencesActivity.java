package de.geithonline.systemlwp;

import java.util.List;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;

public class PreferencesActivity extends PreferenceActivity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Add a button to the header list.
		if (hasHeaders()) {
			final Button button = new Button(this);
			button.setText("Some action");
			setListFooter(button);
		}
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(final List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_header, target);
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class Prefs1Fragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Make sure default values are applied. In a real app, you would
			// want this in a shared function that is used to retrieve the
			// SharedPreferences wherever they are needed.
			// PreferenceManager.setDefaultValues(getActivity(),
			// R.xml.advanced_preferences, false);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences1);
		}
	}

	/**
	 * This fragment shows the preferences for the second header.
	 */
	public static class Prefs2Fragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Can retrieve arguments from headers XML.
			// Log.i("args", "Arguments: " + getArguments());

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences2);
		}
	}
}