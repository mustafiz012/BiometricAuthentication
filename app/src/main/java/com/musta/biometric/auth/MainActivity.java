package com.musta.biometric.auth;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BiometricManager biometricManager = BiometricManager.from(this);

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            attemptBiometricAuth();
        } else {
            Toast.makeText(this, "No Biometric Sensor available/registered", Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*It's recommended to use authentication within onCreate or onCreateView respectively for Activity and Fragment,
                that's why I've recreated activity here to recheck how it works :P*/
                recreate();
            }
        });
    }

    private void attemptBiometricAuth() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = getAuthenticationCallback();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);

        BiometricPrompt.PromptInfo promptInfo = getPromptInfo("Biometric Authentication", "Please login to get into the app",
                "This app is using Biometric Authentication to recognize user", true);
        biometricPrompt.authenticate(promptInfo);
    }

    /**
     * These are the detail info for prompt info
     *
     * @param title                     prompt title
     * @param subtitle                  prompt subtitle
     * @param description               prompt description
     * @param isDeviceCredentialAllowed should it use
     * @return
     */
    private BiometricPrompt.PromptInfo getPromptInfo(String title, String subtitle, String description, boolean isDeviceCredentialAllowed) {

        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setDeviceCredentialAllowed(isDeviceCredentialAllowed)
                .build();
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                Toast.makeText(MainActivity.this, "Authentication error", Toast.LENGTH_SHORT).show();
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Toast.makeText(MainActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                super.onAuthenticationFailed();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
