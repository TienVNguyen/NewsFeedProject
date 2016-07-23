/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.newsfeedproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.training.tiennguyen.newsfeedproject.R;
import com.training.tiennguyen.newsfeedproject.models.NewsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NewsAdapter
 *
 * @author TienNguyen
 */
public class NewsAdapter extends ArrayAdapter<NewsModel> {
    /** mResource */
    private int mResource;

    /**
     * Constructor
     *
     * @param context  Context
     * @param resource int
     * @param objects  List
     */
    public NewsAdapter(Context context, int resource, List<NewsModel> objects) {
        super(context, 0, objects);
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Caching
        final NewsHolder holder;
        if (convertView != null) {
            holder = (NewsHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            holder = new NewsHolder(convertView);
            convertView.setTag(holder);
        }

        //Populating
        final NewsModel model = getItem(position);
        if (model != null) {
            holder.title.setText(model.getTitle());
            holder.sectionName.setText(model.getSectionName());
            holder.publishDate.setText(model.getPublishDate());
            holder.link.setText(model.getLink());
        }

        return convertView;
    }

    /**
     * NewsHolder
     */
    protected static class NewsHolder {
        /** list_item_title */
        @BindView(R.id.list_item_title)
        protected TextView title;
        /** list_item_tag */
        @BindView(R.id.list_item_sectionName)
        protected TextView sectionName;
        /** list_item_publish_date */
        @BindView(R.id.list_item_publish_date)
        protected TextView publishDate;
        /** list_item_link */
        @BindView(R.id.list_item_link)
        protected TextView link;

        /**
         * Constructor
         *
         * @param view View
         */
        protected NewsHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
