package de.geithonline.systemlwp;

import de.geithonline.systemlwp.R;
import de.geithonline.systemlwp.R.xml;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AboutFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_about);
	}
}
