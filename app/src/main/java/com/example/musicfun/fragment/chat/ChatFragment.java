package com.example.musicfun.fragment.chat;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicfun.databinding.FragmentChatBinding;
import com.example.musicfun.ui.friends.FriendsViewModel;

public class ChatFragment extends Fragment {

    //private ChatViewBinding binding;
    private FragmentChatBinding binding;
    private SharedPreferences sp;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: start implementing the request to the server
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        int state = sp.getInt("logged", 999);


    }




}
