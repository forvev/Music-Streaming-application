package com.example.musicfun.fragment.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
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

import com.example.musicfun.R;
import com.example.musicfun.databinding.FragmentLoginBinding;
import com.example.musicfun.viewmodel.login.RegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Displays the fragment where you can reset your password (view based on fragment login)
 */
public class ResetFragment extends Fragment {
    private RegisterViewModel registerViewModel;
    private FragmentLoginBinding binding;
    private EditText currentPassword;
    private EditText newPassword;
    private Button saveBtn;
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
        reset_current.setHint(R.string.current_password);

        reset_new = binding.resetNew;
        reset_new.setHint(R.string.new_password);

        currentPassword = binding.loginUsername;
        newPassword = binding.loginPassword;
        saveBtn = binding.login;
        saveBtn.setText(R.string.save);
        loadingProgressBar = binding.loading;

        registerViewModel.getResetResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean resetResult) {
                loadingProgressBar.setVisibility(View.GONE);
                if (!resetResult) {
                    Toast.makeText(getContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                }
                else{
                    registerViewModel.reset(currentPassword.getText().toString(), newPassword.getText().toString(), sp.getString("token", ""));
                    registerViewModel.getResetResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            Toast.makeText(getContext(), getString(R.string.pw_saved), Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new SettingFragment()).commit();
                        }
                    });
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
                registerViewModel.resetPasswordChanged(newPassword.getText().toString());
            }
        };
        newPassword.addTextChangedListener(afterTextChangedListener);
        newPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.reset(currentPassword.getText().toString(), newPassword.getText().toString(), sp.getString("token", ""));
                }
                return false;
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.reset(currentPassword.getText().toString(), newPassword.getText().toString(), sp.getString("token", ""));
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
