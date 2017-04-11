package com.iot_projects.taas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fitbit.authentication.AuthenticationConfiguration;
import com.fitbit.authentication.AuthenticationConfigurationBuilder;
import com.fitbit.authentication.AuthenticationHandler;
import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.authentication.AuthenticationResult;
import com.fitbit.authentication.ClientCredentials;
import com.fitbit.authentication.Scope;
import com.iot_projects.taas.util.Constants;

public class FitbitLogin extends AppCompatActivity implements AuthenticationHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClientCredentials clientCredentials = new ClientCredentials(Constants.clientId, Constants.clientSecret, Constants.callbackUrl);
        String secureKey = Constants.secureKey;

        AuthenticationConfiguration config = new AuthenticationConfigurationBuilder()
                .setClientCredentials(clientCredentials)
                .setEncryptionKey(secureKey)
                .addRequiredScopes(Scope.sleep, Scope.heartrate)
                .build();

        AuthenticationManager.configure(this, config);
        AuthenticationManager.login(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager.onActivityResult(requestCode, resultCode, data, (AuthenticationHandler) this);
    }

    @Override
    public void onAuthFinished(AuthenticationResult result) {
        if (result.isSuccessful()) {
            Log.d("Debug","Authentication successful!");
        } else {
            Log.d("Debug","Authentication failed! "+ result);
        }
        finish();
    }
}
