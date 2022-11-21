package com.example.musicfun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        TextView title = (TextView) view.findViewById(R.id.custom_view_songtitle);
        TextView artist = (TextView) view.findViewById(R.id.custom_view_songartist);
        title.setText(listTitles[i]);
        artist.setText(listArtists[i]);
        return view;
    }
}
