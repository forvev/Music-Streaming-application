package com.example.musicfun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityChatBinding;
import com.example.musicfun.fragment.chat.ChatFragment;
import com.example.musicfun.ui.friends.Friends_friend_Fragment;

/**
 * MessageListActivity starts chatting function directly and provides onBackPressed function.
 */
public class MessageListActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String name = intent.getStringExtra(Friends_friend_Fragment.ARG_PARAM_NAME);
        //Log.d("disTest", name);
        Bundle data = new Bundle();
        data.putString("Username", name);

        ChatFragment myChat = new ChatFragment();
        myChat.setArguments(data);

        getSupportFragmentManager().beginTransaction().replace(R.id.chat_container, myChat).commit();
    }

    private View.OnClickListener backPress = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    public View.OnClickListener getBackPress (){
        return backPress;
    }
}