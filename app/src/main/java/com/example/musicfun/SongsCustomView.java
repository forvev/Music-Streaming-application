package com.example.musicfun;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SongsCustomView extends LinearLayout {

    //View clickArea;
    ImageView songShare;
    ImageView songAdd;
    TextView songTitle;
    TextView songArtist;

    public SongsCustomView(Context context) {
        super(context);
        init();
    }

    public SongsCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SongsCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SongsCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init(){
        inflate(getContext(),R.layout.songs_custom_view, this);
        songShare = findViewById(R.id.custom_view_songshare);
        songShare.setOnClickListener(share -> shareSong());

        songAdd = findViewById(R.id.custom_view_songadd);
        songAdd.setOnClickListener(add -> addSong());

        songTitle = findViewById(R.id.custom_view_songtitle);

        songArtist = findViewById(R.id.custom_view_songartist);
    }

    public void setCustomSong(String title, String artist){
        songTitle.setText(title);
        songArtist.setText(artist);
    }

    //Following Methods are executed onClick

    public void shareSong(){
    }

    public void addSong(){

    }
}
