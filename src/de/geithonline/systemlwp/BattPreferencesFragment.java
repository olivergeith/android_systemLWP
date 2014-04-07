package de.geithonline.systemlwp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends PreferenceFragment {
	public static final String STYLE_PICKER_KEY = "batt_style";
	private int displayWidth = 1080;
	private ListPreference stylePref;

	private int getDisplayWidth(final Context context) {
		final DisplayMetrics metrics = new DisplayMetrics();
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);

		return metrics.widthPixels;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_style);

		// initializing Members
		displayWidth = getDisplayWidth(getActivity().getApplicationContext());
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
		final Bitmap b = Settings.getIconForDrawer(style, Math.round(displayWidth * 0.12f));
		if (b != null) {
			stylePref.setIcon(BitmapHelper.bitmapToIcon(b));
		}
		if (Settings.prefs == null) {
			Settings.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		}
		// Find a Drawer for this Style
		final IBitmapDrawer drawer = Settings.getDrawerForStyle(style);
		final Preference zeiger = findPreference("show_zeiger");
		final Preference rand = findPreference("show_rand");
		final Preference colorZeiger = findPreference("color_zeiger");

		zeiger.setEnabled(drawer.supportsShowPointer());
		rand.setEnabled(drawer.supportsShowRand());
		colorZeiger.setEnabled(drawer.supportsPointerColor());
		stylePref.setSummary("Current style: " + style);
	}

}
