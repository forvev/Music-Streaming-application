package com.example.musicfun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityRegisterBinding;
import com.example.musicfun.fragment.login.LoginFragment;
import com.example.musicfun.fragment.login.RegisterFragment;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.getInt("logged",0) > 0){
            goToMainActivity();
            return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.register_container, new RegisterFragment()).commit();
    }

    public void goToMainActivity(){
//        This indicates the user was already logged in, continue the playing from last time left.
        Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(myIntent);
        finish();
    }

    public void skipLogin(View view){
        Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        sp.edit().putInt("logged",0).apply();
        sp.edit().putInt("startItemIndex", 0).apply();
        sp.edit().putLong("startPosition", 0).apply();
        sp.edit().putString("saved_playlist", "").apply();
        RegisterActivity.this.startActivity(myIntent);
        finish();
    }

    public void directLogin(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.register_container, new LoginFragment()).commit();
    }
}