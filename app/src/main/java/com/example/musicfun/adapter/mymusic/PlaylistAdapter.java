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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.FragmentTransfer;
import com.example.musicfun.interfaces.PlaylistMenuClick;
import com.example.musicfun.R;

import java.util.List;

public class PlaylistAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Playlist> playlist;
    private PopupMenu popup;
    private PlaylistMenuClick playlistMenuClick;
    private FragmentTransfer fragmentTransfer;

    public PlaylistAdapter(Context context, List<Playlist> playlist, PlaylistMenuClick playlistMenuClick, FragmentTransfer fragmentTransfer) {
        mContext = context;
        this.playlist = playlist;
        inflater = LayoutInflater.from(mContext);
        this.playlistMenuClick = playlistMenuClick;
        this.fragmentTransfer = fragmentTransfer;
    }

    public class PlaylistViewHolder {
        TextView name;
        ImageView imageView;
        ImageView pin;
        LinearLayout linearLayout;
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
        final PlaylistViewHolder holder;
        if (view == null) {
            holder = new PlaylistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.row_playlist, null);
            holder.name = (TextView) view.findViewById(R.id.playlist_name);
            holder.pin = (ImageView) view.findViewById(R.id.default_icon);
            holder.imageView = (ImageView) view.findViewById(R.id.playlist_menu);

            view.setTag(holder);
        } else {
            holder = (PlaylistViewHolder) view.getTag();
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup = new PopupMenu(mContext, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.playlist_option_menu, popup.getMenu());
                if (playlist.get(position).isDefault()) {
                    popup.getMenu().getItem(1).setTitle(R.string.unpin);
                }
                else{
                    popup.getMenu().getItem(1).setTitle(R.string.set_as_default);
                }
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.rename_playlist:
                                AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
                                final EditText edittext = new EditText(mContext);
                                alert.setTitle(R.string.give_new_name);
                                alert.setView(edittext);
                                alert.setNegativeButton("Cancel", null);
                                alert.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        playlistMenuClick.renamePlaylist(edittext.getText().toString(), position);
                                        playlist.get(position).setPlaylist_name(edittext.getText().toString());
                                    }});
                                alert.show();
                                break;
                            case R.id.set_as_default:
                                if (!playlist.get(position).isDefault()) {
                                    playlist.get(position).setDefault(true);
                                    for (int i = 0; i < playlist.size(); i++){
                                        if (playlist.get(i).isDefault() && i != position){
                                            playlist.get(i).setDefault(false);
                                        }
                                    }
                                    notifyDataSetChanged();
                                }
                                else{
                                    playlist.get(position).setDefault(false);
//                                        holder.pin.setVisibility(View.GONE);
                                    int temp = 0;
                                    for (int i = 0; i < playlist.size(); i++){
                                        if (playlist.get(i).isDefault()){
                                            temp += 1;
                                        }
                                    }
                                    if(temp == 0){
                                        playlist.get(0).setDefault(true);
                                    }
                                    notifyDataSetChanged();
                                }
                                playlistMenuClick.setDefaultPlaylist(position, playlist.get(position).isDefault());
                                break;
                            case R.id.remove_playlist:
                                if(playlist.size() == 1){
                                    Toast.makeText(mContext, "You need to have at least on playlist!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // remove this list item
                                    AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                                    adb.setTitle(R.string.delete_playlist);
                                    adb.setMessage(mContext.getString(R.string.sure_delete_playlist));
                                    final int positionToRemove = position;
                                    adb.setNegativeButton("Cancel", null);
                                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (playlist.get(position).isDefault()) {
                                                playlist.get(position).setDefault(false);
                                                playlist.get(0).setDefault(true);
                                            }
                                            playlistMenuClick.deletePlaylist(position);
                                        }
                                    });
                                    adb.show();
                                }
                                break;
                            case R.id.share_playlist:
                                playlistMenuClick.share(playlist,position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        holder.linearLayout = view.findViewById(R.id.ll_playlist_name);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransfer.transferFragment(playlist.get(position).getPlaylist_id());
            }
        });
        // set list view content
        holder.name.setText(playlist.get(position).getPlaylist_name());
        if (playlist.get(position).isDefault()){
            holder.pin.setVisibility(View.VISIBLE);
        }
        else {
            holder.pin.setVisibility(View.GONE);
        }
        return view;
    }
}
