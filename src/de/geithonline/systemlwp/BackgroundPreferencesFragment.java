package de.geithonline.systemlwp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.util.Log;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

/**
 * This fragment shows the preferences for the second header.
 */
public class BackgroundPreferencesFragment extends PreferenceFragment {
	private final int PICK_IMAGE = 1;
	public static final String BACKGROUND_PICKER_KEY = "backgroundPicker";
	private final Preference backgroundPicker = findPreference(BACKGROUND_PICKER_KEY);

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_background);

		// backgroundPicker.setSummary(Settings.getCustomBackgroundFilePath());
		// backgroundPicker.setIcon(new BitmapDrawable(getResources(),
		// Settings.getCustomBackground()));
		final Resources resources = getResources();
		Resources.getSystem();
		// connection the backgroundpicker with an intent
		backgroundPicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(final Preference preference) {
				final Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Background Picture"), PICK_IMAGE);
				return true;
			}
		});
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		Log.d(this.getClass().getSimpleName(), "onActivityResult: Data Recieved: " + imageReturnedIntent.toString());

		if (resultCode == Activity.RESULT_OK) {
			final Uri selectedImage = imageReturnedIntent.getData();

			// filepath ermitteln....
			final String[] filePathColumn = { MediaStore.Images.Media.DATA };

			final Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			final String filePath = cursor.getString(columnIndex);
			cursor.close();

			// und in die SharedPreferences schreiben
			final SharedPreferences sharedPref = LiveWallpaperService.prefs;
			final SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(BACKGROUND_PICKER_KEY, filePath);
			editor.commit();

			final String data = sharedPref.getString(BACKGROUND_PICKER_KEY, "");

			Log.i(this.getClass().getSimpleName(), "Data Recieved! " + data);
			Log.i(this.getClass().getSimpleName(), "Data Recieved! " + filePath);

		}
	}

	private void setBackgroundPickerData() {
		final Bitmap b = Settings.getCustomBackground();
		if (b != null) {
			final Drawable dr = BitmapHelper.resizeToIcon64(b);
			backgroundPicker.setSummary(Settings.getCustomBackgroundFilePath());
			backgroundPicker.setIcon(dr);
		} else {
			backgroundPicker.setSummary(R.string.choose_background_summary);
			backgroundPicker.setIcon(R.drawable.icon); // TODO anderes Icon
		}
	}

}
