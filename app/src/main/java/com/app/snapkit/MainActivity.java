package com.app.snapkit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.snapchat.kit.sdk.SnapLogin;
import com.snapchat.kit.sdk.core.controller.LoginStateController;
import com.snapchat.kit.sdk.login.models.MeData;
import com.snapchat.kit.sdk.login.models.UserDataResponse;
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements LoginStateController.OnLoginStateChangedListener {
    private TextView _name;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SnapLogin.getLoginStateController(this).removeOnLoginStateChangedListener(this);
    }

    void initSnapChat(){
        SnapLogin.getAuthTokenManager(getApplicationContext()).startTokenGrant();
    }

    void logOut(){
        SnapLogin.getAuthTokenManager(getApplicationContext()).revokeToken();
    }

    void fetchUserData(){
        String query = "{me{bitmoji{avatar},displayName,externalId}}";
        SnapLogin.fetchUserData(this, query, null, new FetchUserDataCallback() {
            @Override
            public void onSuccess(@Nullable UserDataResponse userDataResponse) {
                if (userDataResponse == null || userDataResponse.getData() == null) {
                    return;
                }

                MeData meData = userDataResponse.getData().getMe();
                if (meData == null) {
                    return;
                }
                _name.setText(userDataResponse.getData().getMe().getDisplayName());

                System.out.println(userDataResponse.getData().getMe().getExternalId());
            }

            @Override
            public void onFailure(boolean isNetworkError, int statusCode) {

            }
        });
    }

    @Override
    public void onLoginSucceeded() {
        Log.d("snapchat", "onLoginSucceeded: ");
        fetchUserData();
    }

    @Override
    public void onLoginFailed() {
        Log.d("snapchat", "onLoginFailed: ");
    }

    @Override
    public void onLogout() {
        Log.d("snapchat", "onLogout: ");
    }
}
