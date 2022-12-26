package com.example.musicfun.adapter.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Songs;

import java.util.List;

public class SearchSonglistAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songs;

    public SearchSonglistAdapter(Context context, List<Songs> songs) {
        mContext = context;
        this.songs = songs;
        inflater = LayoutInflater.from(mContext);
    }

    public class SearchSonglistViewHolder {
        TextView name;
        TextView artist;
        ImageView songlist_menu;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Songs getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final SearchSonglistViewHolder holder;
        if (view == null) {
            holder = new SearchSonglistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.songlist_row, null);
            holder.name = (TextView) view.findViewById(R.id.song_name);
            holder.artist = (TextView) view.findViewById(R.id.artist_name);
            holder.songlist_menu = (ImageView) view.findViewById(R.id.songlist_menu);
            holder.songlist_menu.setVisibility(View.GONE);
            view.setTag(holder);
        } else {
            holder = (SearchSonglistViewHolder) view.getTag();
        }
        // set the results into TextViews
        holder.name.setText(songs.get(position).getSongName());
        holder.artist.setText(songs.get(position).getArtist());
        return view;
    }
}
