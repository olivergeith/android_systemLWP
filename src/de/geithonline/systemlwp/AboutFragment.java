package de.geithonline.systemlwp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import de.geithonline.systemlwp.settings.Settings;

public class AboutFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_about);
		setSpecialThings();
	}

	private void setSpecialThings() {
		if (Settings.prefs == null) {
			Settings.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		}
		final Preference proBox = findPreference("premium");

		if (Settings.isPremium()) {
			proBox.setTitle("This is the Premium Version");
		} else {
			proBox.setTitle("This is the Free Version");
		}
	}

}
