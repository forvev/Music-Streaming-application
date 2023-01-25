package com.example.musicfun.adapter.discovery;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SonglistMenuClick songlistMenuClick;
    private SharedPreferences sp;
    private PopupMenu popup;


    public SongListAdapter(Context context, List<Songs> songsList, SonglistMenuClick songlistMenuClick){
        mContext = context;
        this.songsList = songsList;
        inflater = LayoutInflater.from(mContext);
        this.songlistMenuClick = songlistMenuClick;
        sp = context.getSharedPreferences("login",MODE_PRIVATE);
    }

    private class SongListViewHolder {
        TextView name;
        TextView artist;
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

            holder.imageView = (ImageView) view.findViewById(R.id.custom_menu);

            view.setTag(holder);
        }
        else{
            holder = (SongListViewHolder) view.getTag();
        }

//      hide "add to playlist" icon, if user did not log in
        if(sp.getInt("logged", 999) != 1){
            holder.imageView.setVisibility(View.INVISIBLE);
        }
        else{
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup = new PopupMenu(mContext, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.discovery_menu, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.add_to_default:
                                    songlistMenuClick.addToDefault(songsList.get(i).getSongId());
                                    break;
                                case R.id.add_to_playlist:
                                    songlistMenuClick.addToPlaylist(songsList.get(i).getSongId());
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
        }
        holder.clickField.setOnClickListener(click -> {
            playSong(i);
        });
        holder.name.setText(songsList.get(i).getSongName());
        holder.artist.setText(songsList.get(i).getArtist());
        mOnInputListner = (PassDataInterface) mContext;
        return view;
    }

    private void playSong(int i) {
        mOnInputListner.playSong(songsList.subList(i, songsList.size()), Player.REPEAT_MODE_ALL);
    }

}
