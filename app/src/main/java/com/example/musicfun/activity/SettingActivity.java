package com.example.musicfun.activity;

import android.os.Bundle;
import com.example.musicfun.R;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicfun.databinding.ActivitySettingBinding;
import com.example.musicfun.fragment.login.SettingFragment;


public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new SettingFragment()).commit();
    }
}
