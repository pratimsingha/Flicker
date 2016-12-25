package com.example.pratim.flicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pratim on 03-07-2016.
 */
    public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder> {
    private List<Photo> photoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> photosList, Context context) {
        this.photoList = photosList;
        this.mContext = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, null);
        FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder(view);
        return flickrImageViewHolder;
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder flickrImageViewHolder, int position) {
        Photo photoItem = photoList.get(position);
        Picasso.with(mContext).load(photoItem.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(flickrImageViewHolder.thumbnail);
        flickrImageViewHolder.title.setText(photoItem.getTitle());
    }


    @Override
    public int getItemCount() {
        return (null != photoList ? photoList.size():0);
    }

    public void loadNewData(List<Photo> newPhotos){
        photoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return (null != photoList ? photoList.get(position): null);
    }
}
