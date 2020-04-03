package de.jojii.datamanagerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class FileList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        Intent i = getIntent();
        String namespace = i.getStringExtra("ns");
        if (namespace == null || namespace.length() == 0){
            finish();
            return;
        }
        setTitle(namespace);
    }
}
