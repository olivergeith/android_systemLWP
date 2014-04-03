package de.geithonline.systemlwp;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.settings.Settings;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends PreferenceFragment {
	public static final String STYLE_PICKER_KEY = "batt_style";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_style);
		// initialize Properties
		enableSettingsForStyle(Settings.getStyle());
		enableProFeatures();

		// connection the backgroundpicker with an intent
		final ListPreference style = (ListPreference) findPreference(STYLE_PICKER_KEY);
		// Proversion --> final andere Liste laden
		if (Settings.isPremium()) {
			style.setEntries(R.array.prostyl);
			style.setEntryValues(R.array.prostylValues);
		}
		style.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newStyle) {
				enableSettingsForStyle((String) newStyle);
				return true;
			}
		});
	}

	private void enableProFeatures() {
		if (Settings.prefs == null) {
			Settings.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		}
		// Nothing so far
	}

	private void enableSettingsForStyle(final String style) {
		if (Settings.prefs == null) {
			Settings.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		}
		// Find a Drawer for this Style
		final IBitmapDrawer drawer = Settings.getDrawerForStyle(style);
		final Preference zeiger = findPreference("show_zeiger");
		final Preference rand = findPreference("show_rand");
		final Preference center = findPreference("centerBattery");
		final Preference colorZeiger = findPreference("color_zeiger");
		final Preference battstyle = findPreference("batt_style");
		final Preference verticalPosition = findPreference("vertical_position");
		final Preference verticalPositionOnlyPortrait = findPreference("vertical_position_only_portrait");

		zeiger.setEnabled(drawer.supportsShowPointer());
		rand.setEnabled(drawer.supportsShowRand());
		colorZeiger.setEnabled(drawer.supportsPointerColor());
		center.setEnabled(drawer.supportsCenter());
		battstyle.setSummary("Current style: " + style);
		verticalPosition.setEnabled(drawer.supportsMoveUP());
		verticalPositionOnlyPortrait.setEnabled(drawer.supportsMoveUP());
	}

}
