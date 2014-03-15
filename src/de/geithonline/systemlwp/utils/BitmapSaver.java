package de.geithonline.systemlwp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class BitmapSaver {

	public static void saveBitmap(final Bitmap bitmap, final String style, final int level) {
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		extStorageDirectory += File.separator + "Pictures" + File.separator + style;
		final String filename = style + "_" + level + ".png";
		OutputStream outStream = null;

		Log.i("GEITH", "Writing Bitmap to " + extStorageDirectory + File.separator + filename);
		final File file = new File(extStorageDirectory, filename);
		try {
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (final Exception e) {
		}
	}

}
