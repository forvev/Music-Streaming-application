package com.example.musicfun.adapter.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.R;

import java.util.List;

/**
 * Adapter for searching playlists
 */
public class SearchPlaylistAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Playlist> playlist;

    public SearchPlaylistAdapter(Context context, List<Playlist> playlist) {
        mContext = context;
        this.playlist = playlist;
        inflater = LayoutInflater.from(mContext);
    }
    /**
     * ViewHolder structure prevents repeated use of findViewById() for a list adapter
     */
    public class SearchPlaylistViewHolder {
        TextView name;
        ImageView playlist_menu;
    }

    @Override
    public int getCount() {
        return playlist.size();
    }

    @Override
    public Playlist getItem(int position) {
        return playlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final SearchPlaylistViewHolder holder;
        if (view == null) {
            holder = new SearchPlaylistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.row_playlist, null);
            holder.name = (TextView) view.findViewById(R.id.playlist_name);
            holder.playlist_menu = (ImageView) view.findViewById(R.id.playlist_menu);
            holder.playlist_menu.setVisibility(View.INVISIBLE);
            view.setTag(holder);
        } else {
            holder = (SearchPlaylistViewHolder) view.getTag();
        }
        // set the results into TextViews
        holder.name.setText(playlist.get(position).getPlaylist_name());
        return view;
    }
}
