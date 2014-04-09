package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.settings.DrawerManager;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends PreferenceFragment {
	public static final String STYLE_PICKER_KEY = "batt_style";
	private ListPreference stylePref;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_style);

		// initializing Members
		stylePref = (ListPreference) findPreference(STYLE_PICKER_KEY);
		// changelistener auf stylepicker
		stylePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newStyle) {
				enableSettingsForStyle((String) newStyle);
				return true;
			}
		});

		// initialize Properties
		enableSettingsForStyle(Settings.getStyle());
		enableProFeatures();
	}

	private void enableProFeatures() {
		// Nothing so far
	}

	private void enableSettingsForStyle(final String style) {
		final Bitmap b = DrawerManager.getIconForDrawer(style, Settings.getIconSize());
		// Find a Drawer for this Style
		final IBitmapDrawer drawer = DrawerManager.getDrawer(style);
		final Preference zeiger = findPreference("show_zeiger");
		final Preference rand = findPreference("show_rand");
		final Preference colorZeiger = findPreference("color_zeiger");
		if (b != null) {
			stylePref.setIcon(BitmapHelper.bitmapToIcon(b));
		}
		zeiger.setEnabled(drawer.supportsShowPointer());
		rand.setEnabled(drawer.supportsShowRand());
		colorZeiger.setEnabled(drawer.supportsPointerColor());
		stylePref.setSummary("Current style: " + style);
	}

}
