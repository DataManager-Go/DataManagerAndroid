package de.jojii.datamanagerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import Data.GlobalData;

import static constants.Constants.prefServer;
import static constants.Constants.prefToken;
import static constants.Constants.prefUser;
import static constants.Constants.preferences;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    // UI items
    private TextView tv_namespaces, tv_groups;
    private RecyclerView rv_recent_files;
    private ListView lv_namespaces;
    private SwipeRefreshLayout srl;

    private String url, token, username;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // init shared pref references
        SharedPreferences sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        url = sharedpreferences.getString(prefServer,"");
        username = sharedpreferences.getString(prefUser,"");
        token = sharedpreferences.getString(prefToken,"");

        GlobalData.setToken(token);
        GlobalData.setUrl(url);

        // init UI
        tv_namespaces = findViewById(R.id.tv_home_count_namespaces);
        tv_groups = findViewById(R.id.tv_home_count_groups);
        lv_namespaces = findViewById(R.id.lv_namespaces);
        rv_recent_files = findViewById(R.id.rv_recent_files);
        srl = findViewById(R.id.refreshLayoutHome);

        lv_namespaces.setOnItemClickListener((parent, view, position, id) -> {
            ArrayAdapter<String> aa = (ArrayAdapter<String>)lv_namespaces.getAdapter();
            String namespace =  aa.getItem(position);
            openFileListActivity(namespace);
        });

        srl.setOnRefreshListener(() -> {
            load();
            srl.setRefreshing(false);
        });

        load();
    }

    private void load(){
        // Load all data
        if (!GlobalData.loadAll()){
            logout();
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lv_namespace_items, GlobalData.getNamespaces());
        lv_namespaces.setAdapter(adapter);
        lv_namespaces.deferNotifyDataSetChanged();

        tv_namespaces.setText(String.valueOf(GlobalData.getNamespaces().length));
    }

    private void openFileListActivity(String namespace){
        Intent i = new Intent(HomeActivity.this, FileList.class);
        i.putExtra("ns", namespace);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home_logout:{
                logout();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Remove local preferences and start login activity
    private void logout(){
        Toast.makeText(this, getString(R.string.msg_logged_out), Toast.LENGTH_LONG).show();
        editor.remove(prefServer);
        editor.remove(prefToken);
        editor.remove(prefUser);
        editor.apply();
        Intent i = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
