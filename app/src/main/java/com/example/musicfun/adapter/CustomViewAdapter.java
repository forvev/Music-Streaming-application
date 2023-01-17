package com.example.musicfun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicfun.R;

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
        RelativeLayout clickField = view.findViewById(R.id.song_and_artist);
        clickField.setOnClickListener(click -> playSong());

        TextView songTitle = (TextView) view.findViewById(R.id.custom_view_songtitle);
        TextView songArtist = (TextView) view.findViewById(R.id.custom_view_songartist);

        ImageView songAdd = (ImageView) view.findViewById(R.id.custom_menu);
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
}
