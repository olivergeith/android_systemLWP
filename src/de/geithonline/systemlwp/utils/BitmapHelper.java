package de.geithonline.systemlwp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

	public static Drawable resizeToIcon(final Bitmap bitmap, final int width, final int height) {
		final Bitmap b = Bitmap.createScaledBitmap(bitmap, width, height, true);
		final Drawable d = new BitmapDrawable(Resources.getSystem(), b);
		return d;
	}

	public static Drawable bitmapToIcon(final Bitmap bitmap) {
		final Drawable d = new BitmapDrawable(Resources.getSystem(), bitmap);
		return d;
	}

	public static Drawable resizeToIcon64(final Bitmap bitmap) {
		return resizeToIcon(bitmap, 64, 64);
	}

	public static Drawable resizeToIcon128(final Bitmap bitmap) {
		return resizeToIcon(bitmap, 128, 128);
	}

	public static void logBackgroundFileInfo(final String path) {
		Log.i("Geith", "Custom BG = " + path);
		final File f = new File(path);
		if (!f.exists()) {
			Log.i("Geith", "Custom BG = " + path + " does not exist!");
			return;
		}
		if (f.isDirectory()) {
			Log.i("Geith", "Custom BG = " + path + " is a Directory!");
			return;
		}
		final long size = f.length();
		Log.i("Geith", "Custom BG = " + path + " -> size is: " + size);
	}

}
