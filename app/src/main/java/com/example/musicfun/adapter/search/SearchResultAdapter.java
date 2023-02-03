package com.example.musicfun.adapter.search;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musicfun.R;
import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.DiscoveryItemClick;
import com.example.musicfun.interfaces.PassDataInterface;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for searching songs in Discovery
 */
public class SearchResultAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songsList = null;
    public PassDataInterface mOnInputListner;
    private ArrayList<Songs> arraylist;
    private SharedPreferences sp;

    public SearchResultAdapter(Context context, List<Songs> songsList) {
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(songsList);
        sp = context.getSharedPreferences("login",MODE_PRIVATE);
    }
    /**
     * ViewHolder structure prevents repeated use of findViewById() for a list adapter
     */
    private class ViewHolder {
        TextView name;
        TextView artist;
        RelativeLayout clickField;
        ImageView songlist_menu;
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public Songs getItem(int position) {
        return songsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.row_songlist, null);
            holder.name = (TextView) view.findViewById(R.id.song_name);
            holder.artist = (TextView) view.findViewById(R.id.artist_name);
            holder.clickField = view.findViewById(R.id.rl_clickable_song);
            holder.songlist_menu = (ImageView) view.findViewById(R.id.add_to_default);
            holder.songlist_menu.setVisibility(View.INVISIBLE);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(songsList.get(position).getSongName());
        holder.artist.setText(songsList.get(position).getArtist());
        mOnInputListner = (PassDataInterface) mContext;

        return view;
    }
}