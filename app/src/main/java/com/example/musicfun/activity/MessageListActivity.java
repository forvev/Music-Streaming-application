package com.example.musicfun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityChatBinding;
import com.example.musicfun.fragment.chat.ChatFragment;
import com.example.musicfun.ui.friends.Friends_friend_Fragment;

public class MessageListActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Intent intent = getIntent();
        String name = intent.getStringExtra(Friends_friend_Fragment.ARG_PARAM_NAME);
        //Log.d("disTest", name);
        Bundle data = new Bundle();
        data.putString("Username", name);

        ChatFragment myChat = new ChatFragment();
        myChat.setArguments(data);

        //change Title of Actionbar
        getSupportActionBar().setTitle(name);

        getSupportFragmentManager().beginTransaction().replace(R.id.chat_container, myChat).commit();


    }
}