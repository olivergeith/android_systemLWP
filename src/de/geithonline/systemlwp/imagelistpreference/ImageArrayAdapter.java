/**
 * Copyright CMW Mobile.com, 2010. 
 */
package de.geithonline.systemlwp.imagelistpreference;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import de.geithonline.systemlwp.R;

/**
 * The ImageArrayAdapter is the array adapter used for displaying an additional
 * image to a list preference item.
 * 
 * @author Casper Wakkers
 */
public class ImageArrayAdapter extends ArrayAdapter<CharSequence> {
	private int index = 0;
	private int[] resourceIds = null;

	/**
	 * ImageArrayAdapter constructor.
	 * 
	 * @param context
	 *            the context.
	 * @param textViewResourceId
	 *            resource id of the text view.
	 * @param objects
	 *            to be displayed.
	 * @param ids
	 *            resource id of the images to be displayed.
	 * @param i
	 *            index of the previous selected item.
	 */
	public ImageArrayAdapter(final Context context, final int textViewResourceId, final CharSequence[] objects, final int[] ids, final int i) {
		super(context, textViewResourceId, objects);

		index = i;
		resourceIds = ids;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		final View row = inflater.inflate(R.layout.image_list_preference_row, parent, false);

		final ImageView imageView = (ImageView) row.findViewById(R.id.image);
		imageView.setImageResource(resourceIds[position]);

		final CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(R.id.check);

		checkedTextView.setText(getItem(position));

		if (position == index) {
			checkedTextView.setChecked(true);
		}

		return row;
	}
}
