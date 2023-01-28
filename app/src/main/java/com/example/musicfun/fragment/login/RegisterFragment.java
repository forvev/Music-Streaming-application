package com.example.musicfun.fragment.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.constant.LoginFormState;
import com.example.musicfun.R;
import com.example.musicfun.databinding.FragmentRegisterBinding;
import com.example.musicfun.viewmodel.login.RegisterViewModel;

import org.json.JSONException;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerBtn;
    private ProgressBar loadingProgressBar;

    SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameEditText = binding.registerUsername;
        passwordEditText = binding.registerPassword;
        registerBtn = binding.register;
        loadingProgressBar = binding.loading;

        registerViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                registerBtn.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()), null);
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()), null);
                }
            }
        });

        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean registerResult) {
                loadingProgressBar.setVisibility(View.GONE);
                if (!registerResult) {
                    Toast.makeText(getContext(), getString(R.string.duplicate_username), Toast.LENGTH_SHORT).show();
                }
                else{

                    Bundle result = new Bundle();
                    result.putBoolean("bundleKey", false);
                    getParentFragmentManager().setFragmentResult("requestKey", result);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_container, new GenreFragment().newInstance(false)).commit();
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
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    registerViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                closeKeyboard(v);
            }
        });
    }

    private void closeKeyboard(View view) {
        if (view != null) {
            // assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
