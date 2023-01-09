package com.example.musicfun.adapter.search;

import android.content.Context;
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

public class SearchResultAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songsList = null;
    private boolean click = false;
    public PassDataInterface mOnInputListner;
    private ArrayList<Songs> arraylist;
    private DiscoveryItemClick discoveryItemClick;

    public SearchResultAdapter(Context context, List<Songs> songsList, DiscoveryItemClick discoveryItemClick) {
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(songsList);
        this.discoveryItemClick = discoveryItemClick;
    }

    public class ViewHolder {
        TextView name;
        TextView artist;
        ImageView setDefault;
        RelativeLayout clickField;
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
            view = inflater.inflate(R.layout.row_song_search, null);
            holder.name = (TextView) view.findViewById(R.id.song_name);
            holder.artist = (TextView) view.findViewById(R.id.artist);
            holder.clickField = view.findViewById(R.id.rl_clickable_song);

            holder.setDefault = (ImageView) view.findViewById(R.id.add_to_default);
            holder.setDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!click){
                        holder.setDefault.setImageResource(R.drawable.ic_baseline_star_24);
                        click = true;
                        discoveryItemClick.addToDefault(songsList.get(position).getSongId());
                    }
                    else{
                        holder.setDefault.setImageResource(R.drawable.ic_baseline_star_border_24);
                        click = false;
                        discoveryItemClick.removeFromDefault(songsList.get(position).getSongId());
                    }
                }
            });
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        holder.clickField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong(position);
                if (mContext instanceof MainActivity) {
                    ((MainActivity)mContext).closeSearchView(view);
                }
            }
        });

        holder.name.setText(songsList.get(position).getSongName());
        holder.artist.setText(songsList.get(position).getArtist());
        mOnInputListner = (PassDataInterface) mContext;

        return view;
    }

    private void playSong(int i) {
        Songs s = songsList.get(i);
        mOnInputListner.playSong(songsList.subList(i, songsList.size()), Player.REPEAT_MODE_ALL, false);
    }
}