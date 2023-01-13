package com.example.musicfun.adapter.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Playlist;

import java.util.List;

public class ChoosePlaylistAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<Playlist> playlist;
    private SharedPreferences sp;

    public ChoosePlaylistAdapter (Context context, List<Playlist> playlist) {
        mContext = context;
        this.playlist = playlist;
        inflater = LayoutInflater.from(mContext);
        sp = context.getSharedPreferences("login", MODE_PRIVATE);
    }

    public class ChoosePlaylistViewHolder {
        TextView playlist_name;
        TextView owner_name;
        ImageView imageView;
        LinearLayout linearLayout;
        ImageView playlist_owner_icon;
    }

    @Override
    public int getCount() {
        return 0;
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
        final ChoosePlaylistViewHolder holder;
        if (view == null) {
            holder = new ChoosePlaylistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.row_shared_playlist, null);
            holder.playlist_name = (TextView) view.findViewById(R.id.shared_playlist_name);
            holder.owner_name = (TextView) view.findViewById(R.id.shared_playlist_owner);
            holder.imageView = (ImageView) view.findViewById(R.id.shared_playlist_menu);
            holder.playlist_owner_icon = (ImageView) view.findViewById(R.id.playlist_owner_icon);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            view.setTag(holder);
        } else {
            holder = (ChoosePlaylistViewHolder) view.getTag();
        }


        holder.linearLayout = view.findViewById(R.id.ll_shared_playlist_name);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragmentTransfer.transferFragment(playlist.get(i).getPlaylist_id());
            }
        });
        // set list view content
        holder.playlist_name.setText(playlist.get(i).getPlaylist_name());
        if(playlist.get(i).getOwner().equals(sp.getString("name", ""))){
            holder.owner_name.setText(sp.getString("name", ""));
            holder.playlist_owner_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.owner_name.setText(playlist.get(i).getOwner());
            holder.playlist_owner_icon.setVisibility(View.GONE);
        }
        return view;
    }


}
