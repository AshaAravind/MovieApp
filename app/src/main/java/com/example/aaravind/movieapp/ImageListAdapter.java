package com.example.aaravind.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aaravind on 07/09/2016.
 */
public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<String> imageUrls;

    public ImageListAdapter(Context context, List<String> imageUrls) {
        super(context, R.layout.grid_view_item, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = new ImageView(context);
        }

        Picasso
                .with(context)
                .load(imageUrls.get(position))
                .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }

    public void add(String s){
        imageUrls.add(s);
    }
}