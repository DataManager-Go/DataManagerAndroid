package de.jojii.datamanagerandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.net.MalformedURLException;
import java.net.URL;

import gobind.Gobind;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextInputEditText et_username = findViewById(R.id.et_login_username);
        TextInputEditText et_url = findViewById(R.id.et_login_url);
        TextInputEditText et_password = findViewById(R.id.et_login_password);

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

            String cnnct = Gobind.canConnect();
            if (cnnct.length() > 0){
                Toast.makeText(this, "Can't connect to wifi: "+cnnct, Toast.LENGTH_SHORT).show();
                return;
            }

            String data = Gobind.login(et_url.getText().toString(), et_username.getText().toString(), et_password.getText().toString());
            if (data.length() != 2){
                Toast.makeText(this, "Error logging in!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }

        });

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