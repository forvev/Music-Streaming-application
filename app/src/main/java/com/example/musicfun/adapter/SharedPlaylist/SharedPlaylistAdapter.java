package com.example.musicfun.adapter.SharedPlaylist;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicfun.R;
import com.example.musicfun.adapter.mymusic.PlaylistAdapter;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.FragmentTransfer;
import com.example.musicfun.interfaces.PlaylistMenuClick;
import com.example.musicfun.ui.friends.Friends_friend_Fragment;
import com.example.musicfun.ui.friends.List_of_friends_fragment;

import java.util.List;

/**
 * Adapter for shared playlists. It provides three functions:
 *      1. User can rename a playlist, if he owns this playlist
 *      2. User can remove a playlist, if he owns this playlist
 *      3. User can invite friend to this playlist, if he owns this playlist
 */
public class SharedPlaylistAdapter  extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Playlist> playlist;
    private PopupMenu popup;
    private PlaylistMenuClick playlistMenuClick;
    private FragmentTransfer fragmentTransfer;
    private SharedPreferences sp;
    private String username;
    private boolean isOwner;

    public SharedPlaylistAdapter(Context context, List<Playlist> playlist, PlaylistMenuClick playlistMenuClick, FragmentTransfer fragmentTransfer) {
        mContext = context;
        this.playlist = playlist;
        inflater = LayoutInflater.from(mContext);
        this.playlistMenuClick = playlistMenuClick;
        this.fragmentTransfer = fragmentTransfer;
        sp = context.getSharedPreferences("login", MODE_PRIVATE);
        username = sp.getString("name", "");
    }
    /**
     * ViewHolder structure prevents repeated use of findViewById() for a list adapter
     */
    private class SharedPlaylistViewHolder {
        private TextView playlist_name;
        private TextView owner_name;
        private ImageView menu;
        private ImageView playlist_owner_icon;
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

    public View getView(final int position, View view, ViewGroup parent) {
        final SharedPlaylistViewHolder holder;
        if (view == null) {
            holder = new SharedPlaylistAdapter.SharedPlaylistViewHolder();
            // Locate the TextViews in song_search_result_lv.xml
            view = inflater.inflate(R.layout.row_shared_playlist, null);
            view.findViewById(R.id.btn_select).setVisibility(View.GONE);
            view.findViewById(R.id.default_icon).setVisibility(View.GONE);
            holder.playlist_name = (TextView) view.findViewById(R.id.shared_playlist_name);
            holder.owner_name = (TextView) view.findViewById(R.id.shared_playlist_owner);
            holder.menu = (ImageView) view.findViewById(R.id.shared_playlist_menu);
            holder.playlist_owner_icon = (ImageView) view.findViewById(R.id.playlist_owner_icon);
            isOwner = playlist.get(position).getOwner().equals(username);
            if (isOwner){
                holder.playlist_owner_icon.setVisibility(View.VISIBLE);
                holder.menu.setVisibility(View.VISIBLE);
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup = new PopupMenu(mContext, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.shared_playlist_option_menu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.rename_playlist:
                                        final Dialog dialog = new Dialog(mContext);
                                        //We have added a title in the custom layout. So let's disable the default title.
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                                        dialog.setCancelable(true);
                                        //Mention the name of the layout of your custom dialog.
                                        dialog.setContentView(R.layout.dialog_rename_playlist);
                                        dialog.show();

                                        //Initializing the views of the dialog.
                                        Button submitButton = dialog.findViewById(R.id.submit_button);
                                        submitButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final EditText nameEt = dialog.findViewById(R.id.name_et);
                                                String playlistName = nameEt.getText().toString();
                                                // send the input playlist name to the server
                                                if(TextUtils.isEmpty(playlistName)) {
                                                    nameEt.setError(mContext.getString(R.string.playlist_need_name));
                                                }
                                                else{
                                                    playlistMenuClick.renamePlaylist(playlistName, position);
                                                    playlist.get(position).setPlaylist_name(playlistName);
                                                    dialog.dismiss();

                                                }
                                            }
                                        });
                                        break;
                                    case R.id.remove_playlist:
                                        // remove this list item
                                        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                                        adb.setTitle(R.string.delete_playlist);
                                        adb.setMessage(mContext.getString(R.string.sure_delete_playlist));
                                        final int positionToRemove = position;
                                        adb.setNegativeButton(mContext.getString(R.string.cancel), null);
                                        adb.setPositiveButton(mContext.getString(R.string.confirm), new AlertDialog.OnClickListener() {
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
                                        playlistMenuClick.share(playlist, position);
                                        break;

                                }
                                return false;
                            }
                        });
                    }
                });
            }
            else{
                holder.menu.setVisibility(View.INVISIBLE);
                holder.playlist_owner_icon.setVisibility(View.GONE);
            }
            view.setTag(holder);
        } else {
            holder = (SharedPlaylistViewHolder) view.getTag();
        }


        holder.linearLayout = view.findViewById(R.id.ll_shared_playlist_name);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransfer.transferFragment(playlist.get(position).getPlaylist_id(), playlist.get(position).getOwner().equals(username));
            }
        });
        // set list view content
        holder.playlist_name.setText(playlist.get(position).getPlaylist_name());
        if(playlist.get(position).getOwner().equals(sp.getString("name", ""))){
            holder.owner_name.setText(sp.getString("name", ""));
            holder.playlist_owner_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.owner_name.setText(playlist.get(position).getOwner());
            holder.playlist_owner_icon.setVisibility(View.GONE);
        }
        return view;
    }
}