package com.test.boatstagram;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BoatStagramListAdapter extends ArrayAdapter<BoatStagramItem> {

    public BoatStagramListAdapter(Context context, List<BoatStagramItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoatStagramItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.boatstagram_item, parent, false);
        }

        TextView captionTextView = (TextView) convertView.findViewById(R.id.caption);
        captionTextView.setText(Html.fromHtml(item.getCaption()));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.boat_image);
        imageView.setMinimumHeight(item.getHeight());
        Picasso.with(getContext())
                .load(item.getThumbnailUrl())
                .into(imageView);

        return convertView;
    }

}
