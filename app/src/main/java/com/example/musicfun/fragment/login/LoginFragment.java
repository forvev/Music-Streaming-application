package com.example.musicfun.fragment.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.databinding.FragmentLoginBinding;
import com.example.musicfun.R;
import com.example.musicfun.viewmodel.login.RegisterViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

public class LoginFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private FragmentLoginBinding binding;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextInputLayout reset_current;
    private TextInputLayout reset_new;
    private ProgressBar loadingProgressBar;
    SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reset_current = binding.resetCurrent;
        reset_current.setEndIconMode(TextInputLayout.END_ICON_NONE);
        reset_current.setHint(R.string.prompt_username);

        reset_new = binding.resetNew;
        reset_new.setHint(R.string.prompt_password);

        usernameEditText = binding.loginUsername;
        usernameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        passwordEditText = binding.loginPassword;
        loginBtn = binding.login;
        loadingProgressBar = binding.loading;
        registerViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loginResult) {
                loadingProgressBar.setVisibility(View.GONE);
                if (!loginResult) {
                    Toast.makeText(getContext(), "Wrong password or username", Toast.LENGTH_SHORT).show();
                }
                else{
                    sp.edit().putInt("logged",1).apply();
                    sp.edit().putString("name",usernameEditText.getText().toString()).apply();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(myIntent);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        registerViewModel.login(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    registerViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                closeKeyboard(v);
            }
        });
    }

    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
