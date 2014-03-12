package de.geithonline.systemlwp;

import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.settings.Settings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {

	public static SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// unregisterReceiver(mBatInfoReceiver);
	}

	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	// #####################################################################################
	// Wallpaper Engine
	// #####################################################################################
	class MyWallpaperEngine extends Engine {
		private boolean initialized = false;
		private int level = 0;
		private boolean isCharging = false;
		private boolean usbCharge = false;
		private boolean acCharge = false;
		private int i;
		private final Handler handler = new Handler();
		private boolean visible = true;
		private Bitmap backgroundImage = null;
		private  IBitmapDrawer bitmapDrawer = Settings.getBatteryStyle();
		private int width = 0;
		private int height = 0;
		private float dx = 0.0f;
		private String filePath = "aaa";
		private int oldWidth = 0;

		MyWallpaperEngine() {
			registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

			// getBackgroundImage();
			i = 0;
			// drawMe();
		}

		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};

		public synchronized void drawMe() {
			handler.removeCallbacks(drawRunner);
			if (visible && initialized)
				handler.post(drawRunner);
		}

		private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(final Context arg0, final Intent intent) {
				level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				// final int scale =
				// intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				// Are we charging charged?
				isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
				// How are we charging?
				final int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
				usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
				acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

				drawMe();

			}
		};

		synchronized void draw() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				// get the size of canvas
				if (canvas != null) {
					width = canvas.getWidth();
					height = canvas.getHeight();
					// clear the canvas
					canvas.drawColor(Color.BLACK);
					// drawing the bgImage
					drawBackgroundImage(canvas);
					// schaun, ob der Bitmapdrawer sich geändert hat
					bitmapDrawer = Settings.getBatteryStyle();
					if (!isCharging) {
						bitmapDrawer.draw(level, canvas);
					}

					if (isCharging) {
						bitmapDrawer.draw(i, canvas);
						i += 1;
						if (i > 100)
							i = 0;
					}
				}
			} catch (final IllegalArgumentException ex) {
				// do nothing
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}

			handler.removeCallbacks(drawRunner);
			if (visible && isCharging) {
				handler.postDelayed(drawRunner, 50); // delay mileseconds
			}

		}

		/**
		 * TODO comment drawBackground
		 * 
		 * @param canvas
		 * @param width
		 * @param height
		 */
		private void drawBackgroundImage(final Canvas canvas) {
			// draw the background image and stretch it to canvas
			if (backgroundImage == null //
					|| !filePath.equals(prefs.getString(PreferencesActivity.BACKGROUND_PICKER_KEY, "aaa")) //
					|| width != oldWidth) {
				backgroundImage = getBackgroundImage();
				oldWidth = width;
			}
			canvas.save();
			canvas.translate(dx, 0);
			canvas.drawBitmap(backgroundImage, 0, 0, null);

			canvas.restore();

		}

		/**
		 * TODO comment initBackgroundImage
		 */
		private Bitmap getBackgroundImage() {
			Bitmap bg;
			// sollen wir ein custom BG laden ?
			if (prefs.getBoolean("customBackground", false)) {
				bg = getCustomBackground();
				if (bg == null)
					bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			} else {
				bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			}
			// now we should have a BG
			// lets scale it
			final int w = bg.getWidth();
			final int h = bg.getHeight();
			final float aspectCanvas = (float) width / (float) height;
			final float aspectBG = (float) w / (float) h;

			Log.i("GEITH", "Aspect BG = " + aspectBG);
			Log.i("GEITH", "Aspect CA = " + aspectCanvas);
			// bild ist schmaler aber länger
			if (aspectBG <= aspectCanvas) {
				//
				final int dstW = (int) (width * 1.4);
				final int dstH = Math.round(dstW * aspectBG);
				Log.i("GEITH", "dstW = " + dstW);
				Log.i("GEITH", "dstH = " + dstH);
				bg = Bitmap.createScaledBitmap(bg, dstW, dstH, true);
			} else {
				// bild ist zu breit ;-) also skalierten wir es auf die
				// canvashöhe
				final int dstH = height;
				final float factor = (float) height / (float) h;
				final int dstW = Math.round(w * factor);
				Log.i("GEITH", "dstW = " + dstW);
				Log.i("GEITH", "dstH = " + dstH);
				bg = Bitmap.createScaledBitmap(bg, dstW, dstH, true);
			}

			return bg;
		}

		private Bitmap getCustomBackground() {
			Bitmap bg = null;
			filePath = prefs.getString(PreferencesActivity.BACKGROUND_PICKER_KEY, "aaa");
			if (!filePath.equals("aaa")) {
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				bg = BitmapFactory.decodeFile(prefs.getString(PreferencesActivity.BACKGROUND_PICKER_KEY, "aaa"), options);
			}
			Log.i("Geith", "Custom BG = " + filePath);
			return bg;
		}

		@Override
		public void onCreate(final SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onVisibilityChanged(final boolean visible) {
			this.visible = visible;
			// if screen wallpaper is visible then draw the image otherwise do
			// not draw
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(final SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			visible = false;
			handler.removeCallbacks(drawRunner);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			unregisterReceiver(mBatInfoReceiver);
		}

		@Override
		public void onSurfaceCreated(final SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			initialized = true;
		}

		@Override
		public void onSurfaceRedrawNeeded(final SurfaceHolder holder) {
			super.onSurfaceRedrawNeeded(holder);
		}

		@Override
		public void onTouchEvent(final MotionEvent event) {
			super.onTouchEvent(event);
		}

		@Override
		public void onOffsetsChanged(final float xOffset, final float yOffset, final float xStep, final float yStep, final int xPixels, final int yPixels) {
			if (backgroundImage != null) {
				dx = (width - backgroundImage.getWidth()) * (xOffset);
				// dx = (width - backgroundImage.getWidth()) * (1 - xOffset);
				// dx = (width) * (xOffset);
			}
			drawMe();
		}

		@Override
		public void onSurfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
			super.onSurfaceChanged(holder, format, width, height);
			drawMe();
		}

	}
}