package de.geithonline.systemlwp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.BatteryManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {
	private int i;
	private int level = 0;
	private boolean isCharging = false;
	private MyWallpaperEngine engine = null;

	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context arg0, final Intent intent) {
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			final int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			// Are we charging charged?
			isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
			// How are we charging?
			final int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			final boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
			final boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
			if (engine != null) {
				engine.drawMe();
			}

		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		this.registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBatInfoReceiver);
	}

	@Override
	public Engine onCreateEngine() {
		engine = new MyWallpaperEngine();
		return engine;
	}

	class MyWallpaperEngine extends Engine {

		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};
		private boolean visible = true;
		public Bitmap image1, backgroundImage;

		MyWallpaperEngine() {
			backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			i = 0;

		}

		@Override
		public void onCreate(final SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		public void drawMe() {
			handler.post(drawRunner);
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
		public void onOffsetsChanged(final float xOffset, final float yOffset, final float xStep, final float yStep, final int xPixels, final int yPixels) {
			drawMe();
		}

		void draw() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				// get the size of canvas
				final int width = canvas.getWidth();
				final int height = canvas.getHeight();
				if (canvas != null) {
					// clear the canvas
					canvas.drawColor(Color.BLACK);
					// draw the background image and stretch it to canvas
					canvas.drawBitmap(backgroundImage, null, new RectF(0, 0, width, height), null);

					final BitmapDrawerBigArc bigArc = new BitmapDrawerBigArc(canvas);
					if (!isCharging) {
						bigArc.draw(level);
					}

					if (isCharging) {
						bigArc.draw(i);
						i += 1;
						if (i > 100)
							i = 0;
					}
				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}

			handler.removeCallbacks(drawRunner);
			if (visible && isCharging) {
				handler.postDelayed(drawRunner, 50); // delay 10 mileseconds
			}

		}
	}
}