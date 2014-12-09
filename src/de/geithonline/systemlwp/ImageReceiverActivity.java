package de.geithonline.systemlwp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ImageReceiverActivity extends Activity {

	private TextView button;
	private String image;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_receiver_activity);

		// Get intent, action and MIME type
		final Intent intent = getIntent();
		final String action = intent.getAction();
		final String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				image = handleSendImage(intent); // Handle single image being sent
				if (image == null) {
					finish();
				}
			}
		}
		button = (TextView) findViewById(R.id.setBackground);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				setBackground(image);
				finish();
			}
		});

	}

	private String handleSendImage(final Intent intent) {
		final Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			final String sourceFilename = imageUri.getPath();
			Log.i("SEND_RECEIVED", "SourcePath = " + sourceFilename);
			String outname = "BatteryLWP.jpg";
			if (sourceFilename.endsWith("jpg")) {
				outname = "BatteryLWP.jpg";
			} else if (sourceFilename.endsWith("png")) {
				outname = "BatteryLWP.png";
			} else {
				return null;
			}

			// Saving file to datadir
			final String savefile = savefile(imageUri, outname);
			return savefile;
			// setBackground(savefile);

		}
		return null;
	}

	private void setBackground(final String savefile) {
		final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(BackgroundPreferencesFragment.BACKGROUND_PICKER_KEY, savefile);
		Log.i(this.getClass().getSimpleName(), "ImagePath written to preferences: " + savefile);
		editor.commit();
		// Triggering the LiveWallpaperService for the change!
		LiveWallpaperService.filePath = "aaa";
	}

	private String savefile(final Uri sourceuri, final String filename) {
		final String sourceFilename = sourceuri.getPath();
		final String destinationDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "data"
				+ File.separator + "BatteryLWP" + File.separator;
		final File dir = new File(destinationDir);
		dir.mkdirs();
		final String destinationFilename = destinationDir + filename;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFilename));
			bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
			final byte[] buf = new byte[1024];
			bis.read(buf);
			do {
				bos.write(buf);
			} while (bis.read(buf) != -1);
		} catch (final IOException e) {
			return null;
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (final IOException e) {

			}
		}
		return destinationFilename;
	}

}
