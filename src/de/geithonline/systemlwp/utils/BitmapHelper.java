package de.geithonline.systemlwp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

public class BitmapHelper {

	public static void saveBitmap(final Bitmap bitmap, final String style, final int level) {
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		extStorageDirectory += File.separator + "Pictures" + File.separator + style + File.separator;
		final String filename = style + "_" + level + ".png";
		OutputStream outStream = null;
		// Ordner anlegen fal snicht vorhanden
		final File out = new File(extStorageDirectory);
		out.mkdirs();
		Log.i("GEITH", "Writing Bitmap to " + extStorageDirectory + filename);
		final File file = new File(extStorageDirectory, filename);
		try {
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (final Exception e) {
		}
	}

	public static Bitmap rotate(final Bitmap bitmap, final float winkel) {
		final Matrix matrix = new Matrix();
		matrix.postRotate(winkel);
		final Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return rotated;
	}

	public static Bitmap flip(final Bitmap src) {
		final Matrix m = new Matrix();
		m.preScale(1, -1);
		final Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);
		return dst;
	}

}
