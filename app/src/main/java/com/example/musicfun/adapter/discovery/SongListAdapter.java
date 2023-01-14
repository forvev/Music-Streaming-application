package com.example.musicfun.adapter.discovery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicfun.R;
import com.example.musicfun.interfaces.DiscoveryItemClick;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.SonglistMenuClick;
import com.google.android.exoplayer2.Player;

import java.util.List;

public class SongListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songsList;
    public PassDataInterface mOnInputListner;
    private boolean click = false;
    private SonglistMenuClick songlistMenuClick;
    private PopupMenu popup;


    public SongListAdapter(Context context, List<Songs> songsList, SonglistMenuClick songlistMenuClick){
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.songlistMenuClick = songlistMenuClick;
    }

    private class SongListViewHolder {
        TextView name;
        TextView artist;
        ImageView share;
        ImageView imageView;
        RelativeLayout clickField;
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
        if(view == null) {
            holder = new SongListViewHolder();
            view = inflater.inflate(R.layout.songs_custom_view, null);
            holder.name = (TextView) view.findViewById(R.id.custom_view_songtitle);
            holder.name.setSelected(true);
            holder.artist = (TextView) view.findViewById(R.id.custom_view_songartist);
            holder.artist.setSelected(true);
            holder.clickField = view.findViewById(R.id.song_and_artist);

            holder.share = (ImageView) view.findViewById(R.id.custom_view_songshare);
            holder.share.setOnClickListener(share -> shareSong());

            holder.imageView = (ImageView) view.findViewById(R.id.custom_menu);

            view.setTag(holder);
        }
        else{
            holder = (SongListViewHolder) view.getTag();
        }
        holder.imageView.setOnClickListener(view1 -> {
            popup = new PopupMenu(mContext, view1);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.discovery_menu, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.add_to_playlist:
                            songlistMenuClick.addToPlaylist(songsList.get(i).getSongId());
                            break;
                        case R.id.share_song:
                            Toast.makeText(mContext, "NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show();
                            songlistMenuClick.share(i);
                            break;
                    }
                    return false;
                }
            });
        });
        holder.clickField.setOnClickListener(click -> {
            playSong(i);
        });
        holder.name.setText(songsList.get(i).getSongName());
        holder.artist.setText(songsList.get(i).getArtist());
        mOnInputListner = (PassDataInterface) mContext;
        return view;
    }

    private void playSong(int i) {
        Songs s = songsList.get(i);
        mOnInputListner.playSong(songsList.subList(i, songsList.size()), Player.REPEAT_MODE_ALL, false);
    }

    private void shareSong() {
        Toast.makeText(inflater.getContext(), "Clicked: Share", Toast.LENGTH_SHORT).show();
    }

}
