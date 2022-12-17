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
import com.example.musicfun.interfaces.DiscoveryItemClick;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.datatype.Songs;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songsList;
    private ArrayList<Songs> arrayList;
    public PassDataInterface mOnInputListner;
    private boolean click = false;
    private DiscoveryItemClick discoveryItemClick;


    public SongListAdapter(Context context, List<Songs> songsList, DiscoveryItemClick discoveryItemClick){
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(songsList);
        this.discoveryItemClick = discoveryItemClick;
    }

    private class SongListViewHolder {
        TextView name;
        TextView artist;
        ImageView share;
        ImageView setDefault;
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

    public View getView(int i, View view, ViewGroup viewGroup) {
        final SongListViewHolder holder;
        if(view == null){
            holder = new SongListViewHolder();
            view = inflater.inflate(R.layout.songs_custom_view, null);
            holder.name = (TextView) view.findViewById(R.id.custom_view_songtitle);
            holder.artist = (TextView) view.findViewById(R.id.custom_view_songartist);
            RelativeLayout clickField = view.findViewById(R.id.song_and_artist);

            clickField.setOnClickListener(click -> playSong(i));

            holder.share = (ImageView) view.findViewById(R.id.custom_view_songshare);
            holder.share.setOnClickListener(share -> shareSong());

            holder.setDefault = (ImageView) view.findViewById(R.id.custom_view_songadd);
            holder.setDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!click){
                        holder.setDefault.setImageResource(R.drawable.ic_baseline_star_24);
                        click = true;
                        discoveryItemClick.addToDefault(songsList.get(i).getSongId());
                    }
                    else{
                        holder.setDefault.setImageResource(R.drawable.ic_baseline_star_border_24);
                        click = false;
                        discoveryItemClick.removeFromDefault(songsList.get(i).getSongId());
                    }
                }
            });
            view.setTag(holder);
        }
        else{
            holder = (SongListViewHolder) view.getTag();
        }
        holder.name.setText(songsList.get(i).getSongName());
        holder.artist.setText(songsList.get(i).getArtist());
        mOnInputListner = (PassDataInterface) mContext;

        return view;
    }

    private void playSong(int i) {
        Songs s = songsList.get(i);
        String id = s.getSongId();
        mOnInputListner.sendInput(id);
        Toast.makeText(inflater.getContext(), s.getSongName() + " is played", Toast.LENGTH_SHORT).show();
    }

    private void shareSong() {
        Toast.makeText(inflater.getContext(), "Clicked: Share", Toast.LENGTH_SHORT).show();
    }

}
