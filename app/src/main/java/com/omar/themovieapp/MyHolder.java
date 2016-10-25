package com.omar.themovieapp;

import android.view.View;
import android.widget.ImageView;


class MyHolder {
    ImageView imageView;

    public MyHolder(View v) {
        this.imageView = (ImageView) v.findViewById(R.id.movieImageView);
    }

}
