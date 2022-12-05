package com.example.musicfun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.repository.Database;
import com.example.musicfun.viewmodel.DiscoveryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songsList = null;
    private ArrayList<Songs> arraylist;

    public SearchResultAdapter(Context context, List<Songs> songsList) {
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(songsList);
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
            view = inflater.inflate(R.layout.song_search_result_lv, null);
            holder.name = (TextView) view.findViewById(R.id.song_name);
            holder.artist = (TextView) view.findViewById(R.id.artist);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // set the results into TextViews
        holder.name.setText(songsList.get(position).getSongName());
        holder.artist.setText(songsList.get(position).getArtist());
        return view;
    }
}