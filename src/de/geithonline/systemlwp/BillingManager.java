package de.geithonline.systemlwp;

import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import de.geithonline.systemlwp.billinghelper.IabHelper;
import de.geithonline.systemlwp.billinghelper.IabResult;
import de.geithonline.systemlwp.billinghelper.Inventory;
import de.geithonline.systemlwp.billinghelper.Purchase;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.Toaster;

public class BillingManager {
	private final Activity activity;
	private final Button button;

	public BillingManager(final Activity activity, final Button button) {
		this.activity = activity;
		this.button = button;
		button.setText("Initializing Billing");
		button.setVisibility(View.GONE);
	}

	/*
	 * base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY (that you
	 * got from the Google Play developer console). This is not your developer
	 * public key, it's the *app-specific* public key.
	 * 
	 * Instead of just storing the entire literal string here embedded in the
	 * program, construct the key at runtime from pieces or use bit manipulation
	 * (for example, XOR with some other string) to hide the actual key. The key
	 * itself is not secret information, but we don't want to make it easy for
	 * an attacker to replace the public key with one of their own and then fake
	 * messages from the server.
	 */
	private final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnPZVeL8OkQIVc6mi0hdXPySB7yDkdovB8rKHAeGg3/IlE9i56N2WFFIovtXh3y5VSC76jpOXSCzHjdY7MJOy1eVu0Q41U1ppTF6Hd1EYel4KJlvlbjn1KlGQgZr9sVM65/nzgMcKFgn9X570NJJMHiIpwEPBDVJ5bMJQxmYsn1KE3WNu+57Rb01TcEEa1plhMkqmDeui4zTrZUKRXfyk0AveDEN4ZzQoRdcz4fhueEZH0XyBkiDpYPcQuO6m/d8oQm1a1Q4e13Im0oPoQ9xFgueZ7RW/mTKGeEaLbUS0MMIS+WG/lR1fJ+R6ZvwfNt8HlK2zXibLo2xY+zs7ZC+5rQIDAQAB";
	// The helper object
	private IabHelper mHelper;
	private final String TAG = "Billing";
	// Does the user have the premium upgrade?
	boolean mIsPremium = false;
	static final String SKU_PREMIUM = "premium";
	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;
	public boolean setupSuccessfull = false;

	protected void onCreate(final Bundle savedInstanceState) {
		setupBilling();
	}

	public boolean isPremium() {
		return mIsPremium;
	}

	protected void onDestroy() {
		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	protected void setupBilling() {
		setupSuccessfull = false;
		mHelper = new IabHelper(activity.getApplicationContext(), base64EncodedPublicKey);
		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(final IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					setupSuccessfull = false;
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) {
					return;
				}

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
				setupSuccessfull = true;
				button.setVisibility(View.VISIBLE);
			}
		});
	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(final IabResult result, final Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) {
				return;
			}

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			final Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
			mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
			saveStatus();
			Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	// User clicked the "Upgrade to Premium" button.
	public void onUpgradeAppButtonClicked(final View arg0) {
		Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
		final String payload = "Premiumz";
		mHelper.launchPurchaseFlow(activity, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, payload);
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null) {
				return;
			}

			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_PREMIUM)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
				alert("Thank you for upgrading to premium!");
				mIsPremium = true;
				saveStatus();
			}
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(final Purchase p) {
		final String payload = p.getDeveloperPayload();
		if (payload.equals("Premiumz")) {
			return true;
		}
		return false;
	}

	void complain(final String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(final String message) {
		final AlertDialog.Builder bld = new AlertDialog.Builder(activity);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	private void saveStatus() {
		final String username = getUsername();
		if (Settings.isDebuggingMessages() && username.equalsIgnoreCase("oliver.geith")) {
			mIsPremium = true;
			Toaster.showInfoToast(activity, "Username = " + username + "! The Master is here!!!");
		}
		Settings.saveProStatus(mIsPremium);
		if (mIsPremium) {
			button.setVisibility(View.GONE);
		}

	}

	public String getUsername() {
		final AccountManager manager = AccountManager.get(activity.getApplicationContext());
		final Account[] accounts = manager.getAccountsByType("com.google");
		final List<String> possibleEmails = new LinkedList<String>();

		for (final Account account : accounts) {
			// TODO: Check possibleEmail against an email regex or treat
			// account.name as an email address only for certain account.type
			// values.
			possibleEmails.add(account.name);
		}

		if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
			final String email = possibleEmails.get(0);
			final String[] parts = email.split("@");
			if (parts.length > 0 && parts[0] != null) {
				return parts[0];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
