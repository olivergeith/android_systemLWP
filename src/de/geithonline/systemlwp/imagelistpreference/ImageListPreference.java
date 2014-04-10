/**
 * Copyright CMW Mobile.com, 2010. 
 */
package de.geithonline.systemlwp.imagelistpreference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import de.geithonline.systemlwp.R;

/**
 * The ImageListPreference class responsible for displaying an image for each
 * item within the list.
 * 
 * @author Casper Wakkers
 */
public class ImageListPreference extends ListPreference {
	private int[] resourceIds = null;

	/**
	 * Constructor of the ImageListPreference. Initializes the custom images.
	 * 
	 * @param context
	 *            application context.
	 * @param attrs
	 *            custom xml attributes.
	 */
	public ImageListPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageListPreference);

		final String[] imageNames = context.getResources().getStringArray(typedArray.getResourceId(typedArray.getIndexCount() - 1, -1));

		resourceIds = new int[imageNames.length];

		for (int i = 0; i < imageNames.length; i++) {
			final String imageName = imageNames[i].substring(imageNames[i].indexOf('/') + 1, imageNames[i].lastIndexOf('.'));

			resourceIds[i] = context.getResources().getIdentifier(imageName, null, context.getPackageName());
		}

		typedArray.recycle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		final int index = findIndexOfValue(getSharedPreferences().getString(getKey(), "1"));

		final ListAdapter listAdapter = new ImageArrayAdapter(getContext(), R.layout.image_list_preference_row, getEntries(), resourceIds, index);

		// Order matters.
		builder.setAdapter(listAdapter, this);
		super.onPrepareDialogBuilder(builder);
	}
}
