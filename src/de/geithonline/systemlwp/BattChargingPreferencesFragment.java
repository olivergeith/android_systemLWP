package de.geithonline.systemlwp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class BattChargingPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_batt_charging);
	}
}
