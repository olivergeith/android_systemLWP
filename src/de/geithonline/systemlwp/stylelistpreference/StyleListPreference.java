package de.geithonline.systemlwp.stylelistpreference;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import de.geithonline.systemlwp.R;
import de.geithonline.systemlwp.settings.Settings;

/**
 * 
 * @author oliver
 * 
 */
public class StyleListPreference extends ListPreference {

	private IconListPreferenceScreenAdapter iconListPreferenceAdapter = null;
	private final Context mContext;
	private final LayoutInflater mInflater;
	private CharSequence[] entries;
	private CharSequence[] entryValues;
	private int[] mEntryIcons = null;
	private boolean[] mEntryBooleans = null;
	private final SharedPreferences prefs;
	private final SharedPreferences.Editor editor;
	private final String mKey;
	private int selectedEntry = -1;
	private int displayWidth = 1080;

	public StyleListPreference(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StyleListPreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs);
		mContext = context;

		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StyleListPreference, defStyle, 0);

		final int entryIconsResId = a.getResourceId(R.styleable.StyleListPreference_entryIcons, -1);
		if (entryIconsResId != -1) {
			setEntryIcons(entryIconsResId);
		}
		final int entryBooleansId = a.getResourceId(R.styleable.StyleListPreference_entryBooleans, -1);
		if (entryBooleansId != -1) {
			setEntryBooleans(entryBooleansId);
		}
		mInflater = LayoutInflater.from(context);
		mKey = getKey();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		a.recycle();

		displayWidth = getDisplayWidth(context);
		Log.i("STYLIST", "DisplayWidth = " + displayWidth);

	}

	private int getDisplayWidth(final Context context) {
		final DisplayMetrics metrics = new DisplayMetrics();
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);

		return metrics.widthPixels;
	}

	@Override
	public CharSequence getEntry() {
		if (selectedEntry != -1) {
			return entries[selectedEntry];
		}
		if (super.getEntry() == null) {
			return "";
		} else {
			return super.getEntry().toString();
		}
	}

	@Override
	public String getValue() {
		if (selectedEntry != -1) {
			return entryValues[selectedEntry].toString();
		}
		return super.getValue();
	}

	public void setEntryIcons(final int[] entryIcons) {
		mEntryIcons = entryIcons;
	}

	public void setEntryIcons(final int entryIconsResId) {
		final TypedArray icons_array = mContext.getResources().obtainTypedArray(entryIconsResId);
		final int[] icon_ids_array = new int[icons_array.length()];
		for (int i = 0; i < icons_array.length(); i++) {
			icon_ids_array[i] = icons_array.getResourceId(i, -1);
		}
		setEntryIcons(icon_ids_array);
		icons_array.recycle();
	}

	public void setEntryBooleans(final int entryBooleansId) {
		final TypedArray booleans_array = mContext.getResources().obtainTypedArray(entryBooleansId);
		final boolean[] icon_booleans_array = new boolean[booleans_array.length()];
		for (int i = 0; i < booleans_array.length(); i++) {
			icon_booleans_array[i] = booleans_array.getBoolean(i, false);
		}
		setEntryBooleans(icon_booleans_array);
		booleans_array.recycle();
	}

	public void setEntryBooleans(final boolean[] entryBooleans) {
		mEntryBooleans = entryBooleans;
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		super.onPrepareDialogBuilder(builder);

		entries = getEntries();
		entryValues = getEntryValues();

		if (entries.length != entryValues.length) {
			throw new IllegalStateException("ListPreference requires an entries array and an entryValues array which are both the same length");
		}
		if (mEntryIcons != null && entries.length != mEntryIcons.length) {
			throw new IllegalStateException("IconListPreference requires the icons entries array be the same length than entries or null");
		}
		if (mEntryBooleans != null && entries.length != mEntryBooleans.length) {
			throw new IllegalStateException("IconListPreference requires the boolean entries array be the same length than entries or null");
		}

		// searching the selected index
		final String selectedValue = prefs.getString(mKey, "");
		for (int i = 0; i < entryValues.length; i++) {
			if (selectedValue.compareTo((String) entryValues[i]) == 0) {
				selectedEntry = i;
				break;
			}
		}

		iconListPreferenceAdapter = new IconListPreferenceScreenAdapter(mContext);
		builder.setSingleChoiceItems(iconListPreferenceAdapter, selectedEntry, null);
		// builder.setSingleChoiceItems(iconListPreferenceAdapter,
		// selectedEntry, new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(final DialogInterface dialog, final int which) {
		//
		// }
		// });

	}

	private class IconListPreferenceScreenAdapter extends BaseAdapter {
		public IconListPreferenceScreenAdapter(final Context context) {
		}

		@Override
		public int getCount() {
			return entries.length;
		}

		class CustomHolder {
			private CheckedTextView text = null;

			// @SuppressLint("NewApi")
			CustomHolder(final View row, final int position) {

				final ImageView imageView = (ImageView) row.findViewById(R.id.image);

				text = (CheckedTextView) row.findViewById(R.id.image_list_view_row_text_view);
				text.setText(entries[position]);
				text.setChecked(selectedEntry == position);
				// in
				final Bitmap b = Settings.getIconForDrawer(text.getText().toString(), Math.round(displayWidth * 0.12f));
				if (b != null) {
					imageView.setImageBitmap(b);
					text.setText(" " + text.getText());
					// auch ne Variante um das icon zu setzen
					// if (Build.VERSION.SDK_INT >= 17) {
					// text.setCompoundDrawablesRelativeWithIntrinsicBounds(BitmapHelper.bitmapToIcon(b),
					// null, null, null);
					// }
				}

				if (mEntryBooleans != null && !Settings.isPremium()) {
					// diabling some rows depending on isPremium and the defined
					// booleans
					row.setClickable(mEntryBooleans[position]);
					row.setEnabled(mEntryBooleans[position]);
					text.setEnabled(mEntryBooleans[position]);
					if (mEntryBooleans[position] == false) {
						text.setText(text.getText() + " (Premium)");
					}
				}
			}
		}

		@Override
		public Object getItem(final int position) {
			return null;
		}

		@Override
		public long getItemId(final int position) {
			return position;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View row = convertView;
			CustomHolder holder = null;
			final int p = position;
			row = mInflater.inflate(R.layout.style_list_preference_row, parent, false);
			holder = new CustomHolder(row, position);

			row.setTag(holder);
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					v.requestFocus();

					final Dialog mDialog = getDialog();
					mDialog.dismiss();

					callChangeListener(entryValues[p]);
					editor.putString(mKey, entryValues[p].toString());
					selectedEntry = p;
					editor.commit();
				}
			});
			return row;
		}
	}
}
