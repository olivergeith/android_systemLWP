package de.geithonline.systemlwp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
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

		// connection the backgroundpicker with an intent
		final Preference style = findPreference(STYLE_PICKER_KEY);
		style.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newStyle) {
				enableSettingsForStyle((String) newStyle);
				return true;
			}
		});
	}

	private void enableSettingsForStyle(final String style) {
		// Find a Drawer for this Style
		final IBitmapDrawer drawer = Settings.getDrawerForStyle(style);
		final Preference rotation = findPreference("rotation");
		final Preference zeiger = findPreference("show_zeiger");
		final Preference center = findPreference("centerBattery");
		final Preference colorZeiger = findPreference("color_zeiger");

		rotation.setEnabled(drawer.supportsOrientation());
		zeiger.setEnabled(drawer.supportsShowPointer());
		colorZeiger.setEnabled(drawer.supportsPointerColor());
		center.setEnabled(drawer.supportsCenter());
	}

}
