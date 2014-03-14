package de.geithonline.systemlwp;

import java.util.List;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

public class PreferencesActivity extends PreferenceActivity {
	public static final String BACKGROUND_PICKER_KEY = "backgroundPicker";
	private final int PICK_IMAGE = 1;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Add a button to the header list.
		if (hasHeaders()) {
			// final Button button = new Button(this);
			// button.setText("Some action");
			// setListFooter(button);
		}
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(final List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_header, target);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		Log.d(BACKGROUND_PICKER_KEY, "Data Recieved! ");

		if (resultCode == RESULT_OK) {
			final Uri selectedImage = imageReturnedIntent.getData();

			// filepath ermitteln....
			final String[] filePathColumn = {
				MediaStore.Images.Media.DATA
			};
			final Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			final String filePath = cursor.getString(columnIndex);
			cursor.close();

			// und in die SharedPreferences schreiben
			final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			final SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(BACKGROUND_PICKER_KEY, filePath);
			editor.commit();

			final String data = sharedPref.getString(BACKGROUND_PICKER_KEY, "");

			Log.d(BACKGROUND_PICKER_KEY, "Data Recieved! " + data);
			Log.d(BACKGROUND_PICKER_KEY, "Data Recieved! " + filePath);

		}
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class AboutFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences_about);
		}
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class BattPreferencesFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences3);
		}
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class BattColorPreferencesFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences1);
		}
	}

	// ################################################################################################

	/**
	 * This fragment shows the preferences for the second header.
	 */
	public static class BackgroundPreferencesFragment extends PreferenceFragment {
		private final int PICK_IMAGE = 1;

		@Override
		public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) {
			super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
			Log.d(BACKGROUND_PICKER_KEY, "Data Recieved! ");

			if (resultCode == RESULT_OK) {
				final Uri selectedImage = imageReturnedIntent.getData();

				// filepath ermitteln....
				final String[] filePathColumn = {
					MediaStore.Images.Media.DATA
				};

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

				Log.d(BACKGROUND_PICKER_KEY, "Data Recieved! " + data);
				Log.d(BACKGROUND_PICKER_KEY, "Data Recieved! " + filePath);

			}
		}

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences2);

			// connection the backgroundpicker with an intent
			final Preference backgroundPicker = findPreference(BACKGROUND_PICKER_KEY);
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

	}
}