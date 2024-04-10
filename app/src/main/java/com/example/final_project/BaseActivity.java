package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(getLayoutResourceId());
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
//                drawer, toolbar, R.string.open, R.string.close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
////        if (this instanceof DadJoke) {
////            TextView answer = findViewById(R.id.joke_answer);
////            answer.setVisibility(View.GONE);
////
////            Button button = findViewById(R.id.button);
////            button.setOnClickListener(e -> {answer.setVisibility(View.VISIBLE);});
////        }
//
//    }
//    @LayoutRes
//    protected abstract int getLayoutResourceId();
//
//
//    public void onCreateAddNavigation() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
//                drawer, toolbar, R.string.open, R.string.close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
////        switch(item.getItemId()) {
////            case R.id.home:
////                Intent intentHome = new Intent(this, MainActivity.class);
////                startActivity(intentHome);
////                break;
////            case R.id.joke:
////                Intent intentJoke = new Intent(this, DadJoke.class);
////                startActivity(intentJoke);
////                break;
////            case R.id.exit:
////                finishAffinity();
////                break;
////        }
//
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////        int num = 0;
////        if (id == R.id.item1) {
////            num = 1;
////        } else if (id == R.id.item2) {
////            num = 2;
////        } else if (id == R.id.item3) {
////            num = 3;
////        }
////        String message =  "You clicked on item " + num;
////
////        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
}
