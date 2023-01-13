package com.example.musicfun.adapter.mymusic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.SonglistMenuClick;
import com.example.musicfun.R;
import com.google.android.exoplayer2.Player;

import java.util.List;

public class SongListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songList;
    private boolean click = false;
    private PopupMenu popup;
    SonglistMenuClick songlistMenuClick;
    public PassDataInterface mOnInputListner;

    public SongListAdapter(Context context, List<Songs> songList, SonglistMenuClick songlistMenuClick) {
        mContext = context;
        this.songList = songList;
        inflater = LayoutInflater.from(mContext);
        this.songlistMenuClick = songlistMenuClick;
    }

    public SongListAdapter(Context context, List<Songs> songList){
        mContext = context;
        this.songList = songList;
        inflater = LayoutInflater.from(mContext);
    }

    public class SonglistViewHolder {
        TextView name;
        TextView artist;
        ImageView imageView;
        RelativeLayout rl_clickable_song;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Songs getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final SonglistViewHolder holder;
        if (view == null) {
            holder = new SonglistViewHolder();
            view = inflater.inflate(R.layout.row_songlist, null);
            holder.name = (TextView) view.findViewById(R.id.song_name);
            holder.artist = (TextView) view.findViewById(R.id.artist_name);
            holder.imageView = (ImageView) view.findViewById(R.id.add_to_default) ;
            if (songlistMenuClick != null){
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup = new PopupMenu(mContext, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.songlist_option_menu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.remove_from_playlist:
                                        AlertDialog.Builder adb=new AlertDialog.Builder(mContext);
                                        adb.setTitle(R.string.delete_playlist);
                                        adb.setMessage(mContext.getString(R.string.sure_delete_song));
                                        final int positionToRemove = position;
                                        adb.setNegativeButton("Cancel", null);
                                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                songlistMenuClick.removeFromPlaylist(position);
                                            }});
                                        adb.show();
                                        break;
                                    case R.id.add_to_playlist:
                                        songlistMenuClick.addToPlaylist(songList.get(position).getSongId());
                                        break;
                                    case R.id.share_song:
                                        songlistMenuClick.share(position);
                                        break;
                                }
                                return false;
                            }
                        });
                    }
                });
            }
            else{
                holder.imageView.setVisibility(View.INVISIBLE);
            }
            view.setTag(holder);
        } else {
            holder = (SonglistViewHolder) view.getTag();
        }
        holder.rl_clickable_song = view.findViewById(R.id.rl_clickable_song);
        if (songlistMenuClick != null){
            mOnInputListner = (PassDataInterface) mContext;
            holder.rl_clickable_song.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnInputListner.playSong(songList.subList(position, songList.size()), Player.REPEAT_MODE_ALL, false);
                }
            });
        }
        else{
            holder.rl_clickable_song.setClickable(false);
        }

        // set the results into TextViews
        holder.name.setText(songList.get(position).getSongName());
        holder.artist.setText(songList.get(position).getArtist());
        return view;
    }
}
