package com.example.musicfun.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.musicfun.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import com.example.musicfun.databinding.ActivitySettingBinding;
import com.example.musicfun.fragment.login.SettingFragment;

import java.util.Objects;

/**
 * SettingActivity directly starts SettingFragment and provides onBackPressed function.
 */
public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private Toolbar toolbar;
    private boolean isBound;
    private MutableLiveData<MusicbannerService> service;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        service = new MutableLiveData<>();
        getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new SettingFragment()).commit();
    }

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            MusicbannerService.ServiceBinder binder = (MusicbannerService.ServiceBinder) iBinder;
            service.setValue(binder.getMusicbannerService());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public MutableLiveData<MusicbannerService> getService (){
        return service;
    }

    @Override
    public void onStart() {
        // Bind to LocalService
        Intent musicbannerServiceIntent = new Intent(this, MusicbannerService.class);
        bindService(musicbannerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }


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
