package de.geithonline.systemlwp;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import de.geithonline.systemlwp.utils.IntendHelper;
import de.geithonline.systemlwp.utils.Toaster;

public class PreferencesActivity extends PreferenceActivity {

	private BillingManager billingManager;
	private static final int REQUEST = 999;
	private Button buttonSetWP;

	@Override
	protected boolean isValidFragment(final String fragmentName) {
		Log.i("GEITH", "isValidFragment Called for " + fragmentName);

		return AboutFragment.class.getName().equals(fragmentName) //
				|| BattPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattColorPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattNumberPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattChargingPreferencesFragment.class.getName().equals(fragmentName) //
				|| BackgroundPreferencesFragment.class.getName().equals(fragmentName);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		billingManager = new BillingManager(this);
		final boolean isPremium = billingManager.isPremium();
		// A button to set us as Wallpaper
		buttonSetWP = new Button(this);
		buttonSetWP.setText("Set Wallpaper");
		buttonSetWP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				setWallpaper();
			}
		});
		// A View because there might be another button (billing)
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setGravity(Gravity.CENTER);
		if (!isMyServiceRunning(LiveWallpaperService.class.getName())) {
			ll.addView(buttonSetWP);
		} else {
		}

		// Add a button to the header list.
		if (!isPremium) {
			final Button button = billingManager.getButton();
			ll.addView(button);
		}
		// set view with buttons to the list footer
		setListFooter(ll);
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(final List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_header, target);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		billingManager.onDestroy();
	}

	@SuppressLint("InlinedApi")
	private void setWallpaper() {
		final Intent i = new Intent();
		// Building the set wallpaper intend depending on SDK Version
		if (Build.VERSION.SDK_INT >= 16) {
			i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			final String p = LiveWallpaperService.class.getPackage().getName();
			final String c = LiveWallpaperService.class.getCanonicalName();
			i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));
		} else {
			i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
		}
		// Checking if intend is available
		if (IntendHelper.isAvailable(getApplicationContext(), i)) {
			startActivityForResult(i, REQUEST);
		} else {
			Toaster.showErrorToast(this, "Intend not available on your device/rom: " + i);
		}
	}

	private boolean isMyServiceRunning(final String className) {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (final RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			Log.i("GEITH", "Sevice = " + service.service.getClassName());
			if (className.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}