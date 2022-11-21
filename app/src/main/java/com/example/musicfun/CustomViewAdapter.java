package com.example.musicfun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomViewAdapter extends BaseAdapter {

    Context context;
    String listTitles[];
    String listArtists[];
    LayoutInflater inflater;

    //will probably have to be changed later
    public CustomViewAdapter(Context ctx, String[] songTitleList, String[] songArtistList){
        this.context = ctx;
        this.listTitles = songTitleList;
        this.listArtists = songArtistList;
        inflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return listTitles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.songs_custom_view,null);
        TextView songTitle = (TextView) view.findViewById(R.id.custom_view_songtitle);
        songTitle.setOnClickListener(title -> playSong());

        TextView songArtist = (TextView) view.findViewById(R.id.custom_view_songartist);
        songArtist.setOnClickListener(artist -> playSong());

        ImageView songShare = (ImageView) view.findViewById(R.id.custom_view_songshare);
        songShare.setOnClickListener(share -> shareSong());

        ImageView songAdd = (ImageView) view.findViewById(R.id.custom_view_songadd);
        songAdd.setOnClickListener(add -> addSong());

        songTitle.setText(listTitles[i]);
        songArtist.setText(listArtists[i]);
        return view;
    }

    private void playSong() {
        Toast.makeText(inflater.getContext(), "Now music plays", Toast.LENGTH_SHORT).show();
    }

    private void addSong() {
        Toast.makeText(inflater.getContext(), "Clicked: Add", Toast.LENGTH_SHORT).show();
    }

    private void shareSong() {
        Toast.makeText(inflater.getContext(), "Clicked: Share", Toast.LENGTH_SHORT).show();

    }
}
