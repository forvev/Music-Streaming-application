package com.example.musicfun.fragment.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.MusicbannerService;
import com.example.musicfun.activity.RegisterActivity;
import com.example.musicfun.adapter.SettingAdapter;
import com.example.musicfun.databinding.FragmentSettingBinding;

import java.util.ArrayList;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private SharedPreferences sp;
    private ArrayList<String> options;
    ListView listView;
    TextView tv;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        tv = binding.displayUsername;
        if(sp.getInt("logged", 999) != 1) {
            System.out.println("The user has not logged in, but can see the setting page!");
        }
        tv.setText("Welcome " + sp.getString("name", "") + "!");
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.listSettings;
        options = new ArrayList<>();
        String option1 = getString(R.string.reset_pw);
        String option2 = getString(R.string.change_genre);
        String option3 = getString(R.string.action_logout);
        options.add(option1);
        options.add(option2);
        options.add(option3);

        listView.setAdapter(new SettingAdapter(options, getContext()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new ResetFragment()).commit();
                        break;
                    case 1:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new GenreFragment().newInstance(true)).commit();
                        break;
                    case 2:
                        logout();
                        break;
                    default:
                        System.out.println("no match!");
                }
            }
        });
    }

    public void logout() {
        sp.edit().putInt("logged", -1).apply();
        sp.edit().putString("name", "").apply();
        sp.edit().putString("password", "").apply();

        getActivity().getApplicationContext().stopService(new Intent(getContext(), MusicbannerService.class));

        Intent i = new Intent(getActivity(), RegisterActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}
