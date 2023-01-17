package com.example.musicfun.adapter.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.SelectPlaylistInterface;

import java.util.List;

public class ChoosePlaylistAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<Playlist> playlist;
    private SharedPreferences sp;
    private RadioButton temp;
    private SelectPlaylistInterface selectPlaylistInterface;


    public ChoosePlaylistAdapter (Context context, List<Playlist> playlist, SelectPlaylistInterface selectPlaylistInterface) {
        mContext = context;
        this.playlist = playlist;
        inflater = LayoutInflater.from(mContext);
        sp = context.getSharedPreferences("login", MODE_PRIVATE);
        this.selectPlaylistInterface = selectPlaylistInterface;
    }

    private class ChoosePlaylistViewHolder {
        private TextView playlist_name;
        private TextView owner_name;
        private ImageView default_icon;
        private ImageView playlist_owner_icon;
        private RadioButton btn_select;
        private LinearLayout linearLayout;
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

    public View getView(int i, View view, ViewGroup viewGroup) {
        final ChoosePlaylistViewHolder holder;
        if (view == null) {
            holder = new ChoosePlaylistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml

            view = inflater.inflate(R.layout.row_shared_playlist, null);
            view.findViewById(R.id.shared_playlist_menu).setVisibility(View.GONE);
            holder.playlist_name = (TextView) view.findViewById(R.id.shared_playlist_name);
            holder.owner_name = (TextView) view.findViewById(R.id.shared_playlist_owner);
            holder.default_icon = (ImageView) view.findViewById(R.id.default_icon);
            holder.playlist_owner_icon = (ImageView) view.findViewById(R.id.playlist_owner_icon);
            holder.btn_select = (RadioButton) view.findViewById(R.id.btn_select);
            holder.btn_select.setVisibility(View.VISIBLE);

            view.setTag(holder);
        } else {
            holder = (ChoosePlaylistViewHolder) view.getTag();
        }


        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (temp == null){
                    holder.btn_select.setChecked(true);
                    temp = holder.btn_select;
                }
                else if (temp != holder.btn_select){
                    temp.setChecked(false);
                    holder.btn_select.setChecked(true);
                    temp = holder.btn_select;
                }
                selectPlaylistInterface.setSelectedPlaylistIndex(i);
            }
        });
        holder.linearLayout = view.findViewById(R.id.ll_shared_playlist_name);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp == null){
                    holder.btn_select.setChecked(true);
                    temp = holder.btn_select;
                }
                else if (temp != holder.btn_select){
                    temp.setChecked(false);
                    holder.btn_select.setChecked(true);
                    temp = holder.btn_select;
                }
                selectPlaylistInterface.setSelectedPlaylistIndex(i);
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
        if(playlist.get(i).isDefault()){
            holder.default_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.default_icon.setVisibility(View.GONE);
        }
        if(playlist.get(i).isShared()){
            holder.playlist_owner_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.playlist_owner_icon.setVisibility(View.GONE);
        }

        return view;
    }


}
