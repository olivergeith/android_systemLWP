package de.geithonline.systemlwp;

import java.util.List;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class PreferencesActivity extends PreferenceActivity {
	public static final String BACKGROUND_PICKER_KEY = "backgroundPicker";

	protected boolean isValidFragment(final String fragmentName) {
		Log.i("GEITH", "isValidFragment Called for " + fragmentName);

		return AboutFragment.class.getName().equals(fragmentName) //
				|| BattPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattColorPreferencesFragment.class.getName().equals(fragmentName) //
				|| BackgroundPreferencesFragment.class.getName().equals(fragmentName);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Add a button to the header list.
		if (hasHeaders()) {
			// final Button button = new Button(this);
			// button.setText("Some action");
			// setListFooter(button);
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
	public static class AboutFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences_about);
		}
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class BattColorPreferencesFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences1);
		}
	}
}