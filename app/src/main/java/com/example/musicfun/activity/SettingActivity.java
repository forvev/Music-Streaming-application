package com.example.musicfun.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.musicfun.R;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicfun.databinding.ActivitySettingBinding;
import com.example.musicfun.fragment.login.SettingFragment;


public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent musicbannerServiceIntent = new Intent(this, MusicbannerService.class);
        bindService(musicbannerServiceIntent, playerServiceConnection, 0);

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new SettingFragment()).commit();
    }

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onStop() {
        doUnbindService();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    private void doUnbindService(){
        if (isBound){
            unbindService(playerServiceConnection);
            isBound = false;
        }
    }

}
