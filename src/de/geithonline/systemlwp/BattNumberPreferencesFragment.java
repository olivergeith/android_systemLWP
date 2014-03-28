package de.geithonline.systemlwp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import de.geithonline.systemlwp.settings.Settings;

public class BattNumberPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_batt_number);
		enableProFeatures();
	}

	private void enableProFeatures() {
		if (Settings.prefs == null) {
			Settings.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		}
		final Preference showNumber = findPreference("show_number");
		if (Settings.isPremium()) {
			showNumber.setEnabled(true);
		} else {
			Settings.prefs.edit().putBoolean("show_number", true).commit();
			showNumber.setEnabled(false);
		}

		final Preference showStatus = findPreference("show_status");
		if (Settings.isPremium()) {
			showStatus.setEnabled(true);
		} else {
			Settings.prefs.edit().putBoolean("show_status", false).commit();
			showStatus.setEnabled(false);
		}

	}

}
