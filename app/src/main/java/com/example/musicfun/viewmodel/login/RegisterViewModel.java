package com.example.musicfun.viewmodel.login;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.R;
import com.example.musicfun.constant.LoginFormState;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.LoginRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class RegisterViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> resetResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    SharedPreferences sp;

    public RegisterViewModel(Application application){
        super(application);
        this.loginRepository = new LoginRepository(application.getApplicationContext());
        sp = getApplication().getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }
    public LiveData<Boolean> getRegisterResult() {
        return registerResult;
    }

    public LiveData<Boolean> getResetResult() {
        return resetResult;
    }

    public void login(String username, String password) throws JSONException {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password, new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    sp.edit().putString("token",result.getString("token")).apply();
                    loginResult.setValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse.statusCode == 401){
                    Toast.makeText(getApplication().getApplicationContext(), R.string.invalid_login, Toast.LENGTH_SHORT).show();
                    loginResult.setValue(false);
                }
            }
        });
    }

    public void register(String username, String password) throws JSONException {
        loginRepository.register(username, password, new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    JSONObject objectUser = (JSONObject) result.get("user");
                    sp.edit().putInt("logged",1).apply();
                    sp.edit().putString("name",objectUser.getString("username")).apply();
                    registerResult.setValue(true);
                    login(username, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse.statusCode == 401){
                    Toast.makeText(getApplication().getApplicationContext(), R.string.duplicate_username, Toast.LENGTH_SHORT).show();
                    registerResult.setValue(false);
                }
            }
        });
    }

    public void reset(String password, String n_password, String token) {
        loginRepository.reset(password, n_password, token, new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject objectUser = (JSONObject) result.get("user");
                    System.out.println(result.getString("message"));
                    resetResult.setValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse.statusCode == 401){
                    Toast.makeText(getApplication().getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                    resetResult.setValue(false);
                }
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void resetPasswordChanged(String password) {
        if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (Objects.equals(username, "null")) {
            return false;
        }
        else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}