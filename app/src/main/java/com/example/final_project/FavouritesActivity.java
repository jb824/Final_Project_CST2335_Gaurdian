package com.example.final_project;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.final_project.data.DBOpener;
import com.example.final_project.data.Story;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends BaseActivity {
    List<Story> favourites = new ArrayList<>();
    private ListAdapter adapter;
    SQLiteDatabase db;
    public static final String ITEM_ID = "ID";
    public static final String ITEM_TITLE = "TITLE";
    public static final String ITEM_URL = "URL";
    public static final String ITEM_CATEGORY = "CATEGORY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.favourite);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String[] columns = {DBOpener.COL_ID, DBOpener.COL_STORY_ID, DBOpener.COL_CATEGORY, DBOpener.COL_DATE, DBOpener.COL_TITLE, DBOpener.COL_URL, DBOpener.COL_FAVOURITE};
        db = new DBOpener(this).getReadableDatabase();
        Cursor results = db.query(false, DBOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        DBOpener dbOpener = new DBOpener(FavouritesActivity.this);
        favourites = dbOpener.getFavourites(results);



        ListView listView = findViewById(R.id.favourite_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter = new ListAdapter());

        if (favourites.isEmpty()) {
//            Toast.makeText(this, "No Stories Found.", Toast.LENGTH_LONG).show();
            Snackbar.make(listView, "No stories found. Visit search tab to favourite a story.", Snackbar.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener((list, item, position, id) -> {
            Story storyItem = favourites.get(position);

            Bundle dataToPass = new Bundle(); // passing to fragment
            dataToPass.putLong(ITEM_ID, storyItem.getId());
            dataToPass.putString(ITEM_TITLE, storyItem.getTitle());
            dataToPass.putString(ITEM_URL, storyItem.getUrl());
            dataToPass.putString(ITEM_CATEGORY, storyItem.getCategory());

            Intent nextActivity = new Intent(FavouritesActivity.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition
        });


    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_favourite;
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favourites.size();
        }

        @Override
        public Story getItem(int position) {
            return favourites.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            Story story = getItem(position);
            if (view == null) {
                view = inflater.inflate(R.layout.activity_item, parent, false);
                view.setClickable(false);
            }

            TextView textView = view.findViewById(R.id.story_item);
            textView.setText(story.getTitle());

            ImageButton imageButton = view.findViewById(R.id.favourite_button);

            if (story.isFavourite() == 1) {
                Drawable drawable = getResources().getDrawable(R.drawable.heart_filled);
                imageButton.setImageDrawable(drawable);
            }

            imageButton.setOnClickListener(e -> {

                if (story.isFavourite() == 1) {
                    Drawable drawable = getResources().getDrawable(R.drawable.heart_unfilled);
                    imageButton.setImageDrawable(drawable);

                    story.setFavourite(0);
                    notifyDataSetChanged();
                    DBOpener dbOpener = new DBOpener(FavouritesActivity.this);
                    dbOpener.updateStory(story);
                }
                else {
                    Drawable drawable = getResources().getDrawable(R.drawable.heart_filled);
                    imageButton.setImageDrawable(drawable);

                    story.setFavourite(1);
                    notifyDataSetChanged();
                    DBOpener dbOpener = new DBOpener(FavouritesActivity.this);
                    dbOpener.updateStory(story);
                }

                favourites.set(position, story);
                notifyDataSetChanged();
            });
            return view;
        }
    }
}
