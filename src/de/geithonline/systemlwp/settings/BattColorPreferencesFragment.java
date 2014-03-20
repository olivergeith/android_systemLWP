package de.geithonline.systemlwp.settings;

import de.geithonline.systemlwp.R;
import de.geithonline.systemlwp.R.xml;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class BattColorPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_batt_colors);
	}
}
