package de.geithonline.systemlwp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.settings.DrawerManager;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;
import de.geithonline.systemlwp.utils.Toaster;
import de.geithonline.systemlwp.utils.URIHelper;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends PreferenceFragment {
	private final int PICK_LOGO = 3;
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

		final Preference logov1 = findPreference("logov1");
		logov1.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(final Preference preference) {
				showLogoV1Message();
				return true;
			}
		});

		// initialize Properties
		enableSettingsForStyle(Settings.getStyle());
		enableProFeatures();
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);
		if (resultData == null) {
			Log.e(this.getClass().getSimpleName(), "onActivityResult: Data Recieved was null !!");
			return;
		}
		Log.i(this.getClass().getSimpleName(), "onActivityResult: Data Recieved: " + resultData.toString());

		if (resultCode != Activity.RESULT_OK) {
			Log.i(this.getClass().getSimpleName(), "No ImagePath Received -> Cancel");
			return;
		}
		if (requestCode != PICK_LOGO) {
			Log.i(this.getClass().getSimpleName(), "No ImagePath Received -> RequestCode wrong...: " + requestCode);
			return;
		}

		final Uri selectedImage = resultData.getData();

		// Pfad zum Image suchen
		final String filePath = URIHelper.getPath(getActivity().getApplicationContext(), selectedImage);
		Log.i(this.getClass().getSimpleName(), "ImagePath Received via URIHelper! " + filePath);

		// und in die SharedPreferences schreiben
		final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		if (sharedPref == null) {
			Log.e(this.getClass().getSimpleName(), "SharedPreferences were null!!");
			Toaster.showErrorToast(
					getActivity(),
					"Could not save imagepath "
							+ filePath
							+ "Sharedfreferences not found!!! (null). Make sure you set the Wallpaper at least once before editing preferences of it (SystemSettings->Display->Wallpaper->LiveWallpaper->Choose BatteryLWP and set it!");
			return;
		}
		final SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("logoPicker", filePath);
		Log.i(this.getClass().getSimpleName(), "ImagePath written to preferences: " + filePath);
		editor.commit();
		if (Settings.isDebuggingMessages()) {
			Toaster.showInfoToast(getActivity(), "SetBG to " + filePath);
		}
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
		if (style.equals("LogoV1")) {
			showLogoV1Message();
		}
	}

	private void showLogoV1Message() {
		Toaster.alertInfo(getActivity(), "The LogoV1 style is no longer available!" //
				+ "\nIt has moved to its own APP!" //
				+ "\n...with lots of new special settings only for this style!" //
				+ "\n...like:" //
				+ "\n- brightness of grayscaled background" //
				+ "\n- changing hue of logo-image" //
				+ "\n- re-coloring of logo-image" //
				+ "\n- masking logo-image to lots of different shapes" //
				+ "\n- ... and many more ..." //
				+ "\n" //
				+ "\nCheck it out...it is free (and addfree) too!", "Goto Play Store ...",
				"https://play.google.com/store/apps/details?id=de.geithonline.logolwp");
	}

}
