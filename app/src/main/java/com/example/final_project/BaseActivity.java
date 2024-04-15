package com.example.final_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.final_project.data.DBOpener;
import com.example.final_project.data.Story;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences pref;
    private static final String GUARDIAN_NEWS_API = "https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.home);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    @LayoutRes
    protected abstract int getLayoutResourceId();


    public void onCreateAddNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        int num = 0;
        if (id == R.id.home) {
            Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                System.out.println("clicked on home");
        } else if (id == R.id.search_text) {
            Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                System.out.println("clicked on search");
        } else if (id == R.id.favourite) {
            Intent intentFavourite = new Intent(this, FavouritesActivity.class);
                startActivity(intentFavourite);
                System.out.println("clicked on favourites");
        } else if (id == R.id.exit) {
            finishAffinity();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.settings);
            builder.setMessage(R.string.settings_message);
            builder.setPositiveButton(R.string.positiveButton, (DialogInterface.OnClickListener) (dialog, which) -> {
                // Handle positive button click
            });
            builder.setNegativeButton(R.string.negativeButton, (dialog, which) -> {
                // Handle negative button click
                dialog.cancel();
            });

            builder.setView(getLayoutInflater().inflate(R.layout.activity_permission, null));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("api", GUARDIAN_NEWS_API);
            editor.commit();
            finish();
    }

}
