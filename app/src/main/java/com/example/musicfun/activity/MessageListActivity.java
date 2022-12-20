package com.example.musicfun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityChatBinding;
import com.example.musicfun.databinding.ChatViewBinding;
import com.example.musicfun.ui.friends.ChatFragment;

public class MessageListActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.chat_container, new ChatFragment()).commit();

    }
}