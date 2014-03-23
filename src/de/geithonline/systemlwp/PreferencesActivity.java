package de.geithonline.systemlwp;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PreferencesActivity extends PreferenceActivity {

	private BillingManager billingManager;

	@Override
	protected boolean isValidFragment(final String fragmentName) {
		Log.i("GEITH", "isValidFragment Called for " + fragmentName);

		return AboutFragment.class.getName().equals(fragmentName) //
				|| BattPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattColorPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattChargingPreferencesFragment.class.getName().equals(fragmentName) //
				|| BackgroundPreferencesFragment.class.getName().equals(fragmentName);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Button button = new Button(this);
		billingManager = new BillingManager(this, button);
		billingManager.setupBilling();
		final boolean isPremium = billingManager.isPremium();

		// Add a button to the header list.
		if (!isPremium) {
			button.setText("Upgrade to Premium-Version");
			setListFooter(button);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					billingManager.onUpgradeAppButtonClicked(v);
				}
			});
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
	protected void onDestroy() {
		super.onDestroy();
		billingManager.onDestroy();
	}
}