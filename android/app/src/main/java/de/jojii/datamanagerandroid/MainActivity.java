package de.jojii.datamanagerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.net.MalformedURLException;
import java.net.URL;

import gobind.Gobind;


public class MainActivity extends AppCompatActivity {
    final String prefToken = "token";
    final String prefServer = "surl";
    final String prefUser = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // Check already logged in
        if (sharedpreferences.getString(prefToken,"").length() == 64 &&
                sharedpreferences.getString(prefServer, "").length() > 0 &&
                sharedpreferences.getString(prefUser, "").length() > 0){

            startHome();
            return;
        }

        setContentView(R.layout.activity_main);

        TextInputEditText et_username = findViewById(R.id.et_login_username);
        TextInputEditText et_url = findViewById(R.id.et_login_url);
        TextInputEditText et_password = findViewById(R.id.et_login_password);
        ProgressBar pb_load = findViewById(R.id.pb_login);

        // Exit button
        findViewById(R.id.btn_login_exit).setOnClickListener(v -> {
            finish();
        });

        // Login button
        findViewById(R.id.btn_login_login).setOnClickListener(v ->{
            et_url.setError(null);
            if (!isUrlValid(et_url.getText().toString())){
                et_url.setError("Invalid Url");
                return;
            }

            if (!checkInput(et_username,et_password)){
                return;
            }

            pb_load.setVisibility(View.VISIBLE);

            // Check if client can connect to tservec
            String cnnct = Gobind.canConnect(et_url.getText().toString());
            if (cnnct.length() > 0){
                Toast.makeText(this, "Can't connect to server: "+cnnct, Toast.LENGTH_SHORT).show();
                pb_load.setVisibility(View.GONE);
                return;
            }

            String data = Gobind.login(et_url.getText().toString(), et_username.getText().toString(), et_password.getText().toString());
            if (data.length() != 64){
                Toast.makeText(this, "Error logging in!", Toast.LENGTH_SHORT).show();
            }else{
                editor.putString(prefToken, data);
                editor.putString(prefServer, et_url.getText().toString());
                editor.putString(prefUser, et_username.getText().toString());
                editor.apply();
                pb_load.setVisibility(View.GONE);
                startHome();
            }
        });

    }

    private void startHome(){
        Intent i = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private boolean checkInput(TextInputEditText username, TextInputEditText password) {
        if (username == null || password == null || username.getText() == null || password.getText() == null) {
            return false;
        }
        username.setError(null);
        password.setError(null);

        if (username.getText().length() == 0) {
            username.setError("Missing username");
            return false;
        }

        if (password.getText().length() == 0) {
            password.setError("Missing password");
            return false;
        }
        return true;
    }

    private boolean isUrlValid(String url){
        try {
            new URL(url);
        } catch (MalformedURLException malformedURLException) {
            return false;
        }
        return true;
    }
}