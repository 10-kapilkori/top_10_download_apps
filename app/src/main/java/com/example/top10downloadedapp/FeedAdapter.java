package com.example.top10downloadedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int resource;
    private final LayoutInflater layoutInflater;
    List<FeedEntry> applications;

    public FeedAdapter(@NonNull Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
//        TextView tvLink = convertView.findViewById(R.id.tvLink);
//        TextView tvPublishDate = convertView.findViewById(R.id.tvPublishDate);

        FeedEntry currentApp = applications.get(position);

        viewHolder.tvTitle.setText(currentApp.getTitle());
        viewHolder.tvLink.setText(currentApp.getLink());
        viewHolder.tvPublishDate.setText(currentApp.getPublishDate());

        return convertView;
    }

    private class ViewHolder {
        final TextView tvTitle;
        final TextView tvLink;
        final TextView tvPublishDate;

        ViewHolder(View view) {
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvLink = view.findViewById(R.id.tvLink);
            this.tvPublishDate = view.findViewById(R.id.tvPublishDate);
        }
    }
}
