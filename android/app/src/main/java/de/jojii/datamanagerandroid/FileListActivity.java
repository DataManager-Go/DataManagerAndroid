package de.jojii.datamanagerandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import Adapter.FileListAdapter;
import Models.File;

public class FileListActivity extends AppCompatActivity {

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

        ProgressBar pb_load = findViewById(R.id.pb_load_files);
        TextView tv_no_files_available = findViewById(R.id.tv_no_files_available);
        RecyclerView rv_files = findViewById(R.id.rv_file_list);

        new Thread(() -> {
            // Load files
            File[] files = File.getFiles("", namespace, null, null, 0);

            FileListActivity.this.runOnUiThread(() -> {
                pb_load.setVisibility(View.GONE);

                if (files.length == 0){
                    tv_no_files_available.setVisibility(View.VISIBLE);
                }else{
                    loadFiles(files, rv_files);
                }

            });
        }).start();
    }

    private void loadFiles(File[] files, RecyclerView rv_files){
        Toast.makeText(this, files[0].creationDate, Toast.LENGTH_SHORT).show();
        FileListAdapter adapter = new FileListAdapter(Arrays.asList(files));
        rv_files.setHasFixedSize(true);
        rv_files.setAdapter(adapter);
        rv_files.setLayoutManager(new LinearLayoutManager(this));
    }
}
