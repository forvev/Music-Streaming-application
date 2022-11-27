package com.example.musicfun.DiscoveryPart;

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
import com.example.musicfun.search.Songs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscoveryFragmentAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songsList = null;
    private ArrayList<Songs> arrayList;

    public DiscoveryFragmentAdapter(Context context, List<Songs> songsList){
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(songsList);
    }

    public class ViewHolder {
        TextView name;
        TextView artist;
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public Object getItem(int i) {
        return songsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.songs_custom_view, null);
        TextView name = (TextView) view.findViewById(R.id.custom_view_songtitle);
        TextView artist = (TextView) view.findViewById(R.id.custom_view_songartist);

        RelativeLayout clickField = view.findViewById(R.id.song_and_artist);
        //TODO: Make it play Music
        clickField.setOnClickListener(click -> playSong());

        ImageView songShare = (ImageView) view.findViewById(R.id.custom_view_songshare);
        songShare.setOnClickListener(share -> shareSong());

        ImageView songAdd = (ImageView) view.findViewById(R.id.custom_view_songadd);
        songAdd.setOnClickListener(add -> addSong());

        name.setText(songsList.get(i).getSongName());
        artist.setText(songsList.get(i).getArtist());
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
