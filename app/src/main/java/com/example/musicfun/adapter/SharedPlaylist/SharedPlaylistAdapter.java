package com.example.musicfun.adapter.SharedPlaylist;

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

import com.example.musicfun.R;
import com.example.musicfun.adapter.mymusic.PlaylistAdapter;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.FragmentTransfer;
import com.example.musicfun.interfaces.PlaylistMenuClick;

import java.util.List;

public class SharedPlaylistAdapter  extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Playlist> playlist;
    private PopupMenu popup;
    private PlaylistMenuClick playlistMenuClick;
    private FragmentTransfer fragmentTransfer;

    public SharedPlaylistAdapter(Context context, List<Playlist> playlist, PlaylistMenuClick playlistMenuClick, FragmentTransfer fragmentTransfer) {
        mContext = context;
        this.playlist = playlist;
        inflater = LayoutInflater.from(mContext);
        this.playlistMenuClick = playlistMenuClick;
        this.fragmentTransfer = fragmentTransfer;
    }

    public class SharedPlaylistViewHolder {
        TextView name;
        ImageView imageView;
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
        final SharedPlaylistViewHolder holder;
        if (view == null) {
            holder = new SharedPlaylistAdapter.SharedPlaylistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.row_playlist, null);
            holder.name = (TextView) view.findViewById(R.id.playlist_name);
            holder.imageView = (ImageView) view.findViewById(R.id.playlist_menu);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup = new PopupMenu(mContext, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.shared_playlist_option_menu, popup.getMenu());
//                    TODO: set little figure if I am the owner of this list
                    if (playlist.get(position).isDefault()) {
//                        popup.getMenu().getItem(1).setTitle(R.string.unpin);
                    } else {
//                        popup.getMenu().getItem(1).setTitle(R.string.set_as_default);
                    }
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.rename_playlist:
                                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                    final EditText edittext = new EditText(mContext);
                                    alert.setTitle(R.string.give_new_name);
                                    alert.setView(edittext);
                                    alert.setNegativeButton("Cancel", null);
                                    alert.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            playlistMenuClick.renamePlaylist(edittext.getText().toString(), position);
                                            playlist.get(position).setPlaylist_name(edittext.getText().toString());
                                        }
                                    });
                                    alert.show();
                                    break;
                                case R.id.remove_playlist:
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
                                    break;
                                case R.id.add_friend:
                                    playlistMenuClick.share(position);
                                    break;

                                case R.id.listen_together:
//                                    playlistMenuClick.share(position);
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
            view.setTag(holder);
        } else {
            holder = (SharedPlaylistViewHolder) view.getTag();
        }


        holder.linearLayout = view.findViewById(R.id.ll_playlist_name);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransfer.transferFragment(playlist.get(position).getPlaylist_id());
            }
        });
        // set list view content
        holder.name.setText(playlist.get(position).getPlaylist_name());
//                    TODO: set little figure if I am the owner of this list
        if (playlist.get(position).isDefault()) {
//            holder.pin.setVisibility(View.VISIBLE);
        } else {
//            holder.pin.setVisibility(View.GONE);
        }
        return view;
    }
}