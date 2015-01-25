package com.codepath.instagramviewer;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagramviewer.InstagramPhoto;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by nehadike on 1/24/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> photos){
        
        super(context, android.R.layout.simple_list_item_1, photos);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        
        //Take the data source at position
        //Get the data item
        InstagramPhoto photo = getItem(position);
        //Check if we are using a recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent,false);
        }
        //Lookup the subview within the template
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivContentImage);
        ImageView ivUserphoto = (ImageView) convertView.findViewById(R.id.ivUserimage);
        
        //Populate the subviews with correct data
        tvUsername.setText(photo.username);
        String likesText = "";
        if (photo.likesCount>1) {
            likesText = String.valueOf(photo.likesCount) + " likes.";
        } else if (photo.likesCount == 1) {
            likesText = "1 like";
        }
        tvLikes.setText(likesText);
        tvCaption.setText(photo.caption);
        ivPhoto.getLayoutParams().height = photo.imageHeight;
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        Picasso.with(getContext()).load(photo.userImage).into(ivUserphoto);
        //Return the view for that data item
        return  convertView;
    }
}
