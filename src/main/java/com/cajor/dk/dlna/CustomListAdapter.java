package com.cajor.dk.dlna;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class CustomListAdapter extends ArrayAdapter<CustomListItem>
{
    private final int layout;
    private Context context;

    public CustomListAdapter(Context context)
    {
        super(context, 0);
        this.layout = R.layout.list;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final CustomListItem entry = getItem(position);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (!prefs.getBoolean("settings_show_icons", true)) {
            viewHolder.imageView.setImageResource(android.R.color.transparent);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.containerView.setPadding(16,
                    viewHolder.containerView.getPaddingTop(),
                    viewHolder.containerView.getPaddingRight(),
                    viewHolder.containerView.getPaddingBottom());
        } else {
            if (entry.getHideIcon()) {
                viewHolder.imageView.setImageResource(android.R.color.transparent);
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.containerView.setPadding(16,
                        viewHolder.containerView.getPaddingTop(),
                        viewHolder.containerView.getPaddingRight(),
                        viewHolder.containerView.getPaddingBottom());
            } else {
                String iconUrl = entry.getIconUrl();

                if (iconUrl != null && prefs.getBoolean("settings_show_device_icons", true)) {
                    UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, iconUrl, entry.getIcon());
                } else {
                    viewHolder.imageView.setImageResource(entry.getIcon());
                }

                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.containerView.setPadding(0,
                        viewHolder.containerView.getPaddingTop(),
                        viewHolder.containerView.getPaddingRight(),
                        viewHolder.containerView.getPaddingBottom());
            }
        }

        viewHolder.titleView.setText(entry.getTitle());

        String description = entry.getDescription();
        if (description == null)
            viewHolder.description2View.setVisibility(View.GONE);
        else {
            viewHolder.description2View.setVisibility(View.VISIBLE);
            viewHolder.descriptionView.setText(description);
        }

        String description2 = entry.getDescription2();
        if (description2 == null)
            viewHolder.description2View.setVisibility(View.GONE);
        else {
            viewHolder.description2View.setVisibility(View.VISIBLE);
            viewHolder.description2View.setText(description2);
        }

        return view;
    }

    private View getWorkingView(final View convertView)
    {
        View workingView;

        if(null == convertView)
        {
            final Context context = getContext();
            final LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(layout, null);
        }
        else
            workingView = convertView;

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView)
    {
        // The viewHolder allows us to avoid re-looking up view references
        // Since views are recycled, these references will never change
        final Object tag = workingView.getTag();
        ViewHolder viewHolder;

        if(null == tag || !(tag instanceof ViewHolder))
        {
            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView)workingView.findViewById(R.id.icon);
            viewHolder.titleView = (TextView)workingView.findViewById(R.id.title);
            viewHolder.containerView = (RelativeLayout)workingView.findViewById(R.id.container);
            viewHolder.descriptionView = (TextView)workingView.findViewById(R.id.description);
            viewHolder.description2View = (TextView)workingView.findViewById(R.id.description2);

            workingView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) tag;

        return viewHolder;
    }

    private static class ViewHolder
    {
        public TextView titleView;
        public RelativeLayout containerView;
        public TextView descriptionView;
        public TextView description2View;
        public ImageView imageView;
    }
}

