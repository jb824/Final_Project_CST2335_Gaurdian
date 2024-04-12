package com.example.final_project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.final_project.data.DBOpener;
import com.example.final_project.data.Story;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private String searchText;
    private Articles article;
    List<Story> stories = new ArrayList<>();
    public static final String ITEM_ID = "ID";
    public static final String ITEM_TITLE = "TITLE";
    public static final String ITEM_URL = "URL";
    public static final String ITEM_CATEGORY = "CATEGORY";
    private ProgressBar progressBar;
    private ListAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button button = findViewById(R.id.search_button);
        EditText editText = findViewById(R.id.search_text);
        progressBar = findViewById(R.id.progressBar);

//        DBOpener dbOpener = new DBOpener(MainActivity.this);
//        db = dbOpener.getWritableDatabase();
//        dbOpener.onCreate(db);

        button.setOnClickListener(e -> {
            // IllegalStateException caused if AsyncTask is not instantiated each search
            Articles article = new Articles();

            // clear the list view
            stories.clear();

            // grab keywords from EditText and send JSON request
            searchText = editText.getText().toString();
            String url = "https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=" + searchText;
            article.execute(url);

            // empty search request for next API request
            searchText = "";
            editText.setText("");

            // clear previous favourites
            adapter.notifyDataSetChanged();
        });

        ListView listView = findViewById(R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter = new ListAdapter());

        listView.setOnItemClickListener((list, item, position, id) -> {
            Story storyItem = stories.get(position);

            Bundle dataToPass = new Bundle(); // passing to fragment
            dataToPass.putLong(ITEM_ID, storyItem.getId());
            dataToPass.putString(ITEM_TITLE, storyItem.getTitle());
            dataToPass.putString(ITEM_URL, storyItem.getUrl());
            dataToPass.putString(ITEM_CATEGORY, storyItem.getCategory());

            Intent nextActivity = new Intent(SearchActivity.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition


        });

        for (Story story : stories) {
            System.out.printf("*** article ***%n");
            System.out.printf("%s%n, %s%n, %s%n, %s%n %s%n %s%n\n", story.getStoryID(), story.getCategory(), story.getDate(), story.getTitle(), story.getUrl(), story.isFavourite());
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        // Handle your menu item clicks here
//        if (id == R.id.item3) {
//            // Do something
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    class Articles extends AsyncTask<String, Integer, String> {
        List<Story> fieldsList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            DBOpener dbOpener = new DBOpener(SearchActivity.this);
            db = dbOpener.getWritableDatabase();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL json = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) json.openConnection();
                urlConnection.connect();

                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    System.out.println(line);
                }

                String result = stringBuilder.toString();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("results");
                System.out.println("Number of articles: " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject article = jsonArray.getJSONObject(i);
                    String storyID = article.getString("id");
                    String date = article.getString("webPublicationDate");
                    String title = article.getString("webTitle");
                    String url = article.getString("webUrl");
                    String category = article.getString("sectionName");

                    Story story = new Story(
                            storyID,
                            date,
                            title,
                            url,
                            category
                    );

                    fieldsList.add(story);

                    DBOpener dbOpener = new DBOpener(SearchActivity.this);
                    dbOpener.addNewStory(story.getStoryID(), story.getCategory(), story.getDate(), story.getTitle(), story.getUrl(), story.isFavourite());

                    System.out.printf("*** article ***%n");
                    System.out.printf("%s%n, %s%n, %s%n, %s%n %s%n %s%n\n", story.getStoryID(), story.getCategory(), story.getDate(), story.getTitle(), story.getUrl(), story.isFavourite());
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            for (Story story : fieldsList) {
                // persist in memory
                stories.add(story);
                System.out.printf("*** article ***");
                System.out.printf("%s%n, %s%n, %s%n, %s%n \n", story.getStoryID(), story.getDate(), story.getTitle(), story.getUrl());
            }
            adapter.notifyDataSetChanged();
            cancel(true);
            progressBar.setVisibility(View.GONE);
//            db.close();
        }

    }


    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stories.size();
        }

        @Override
        public Story getItem(int position) {
            return stories.get(position);
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
            imageButton.setOnClickListener(e -> {

                if (story.isFavourite() == 0) {
                    Drawable drawable = getResources().getDrawable(R.drawable.heart_filled);
                    imageButton.setImageDrawable(drawable);

                    story.setFavourite(1);
                    notifyDataSetChanged();
                    DBOpener dbOpener = new DBOpener(SearchActivity.this);
                    dbOpener.updateStory(story);
                }
                else {
                    Drawable drawable = getResources().getDrawable(R.drawable.heart_unfilled);
                    imageButton.setImageDrawable(drawable);

                    story.setFavourite(0);
                    notifyDataSetChanged();
                    DBOpener dbOpener = new DBOpener(SearchActivity.this);
                    dbOpener.updateStory(story);
                }

                stories.set(position, story);
                notifyDataSetChanged();
                DBOpener dbOpener = new DBOpener(SearchActivity.this);
//                dbOpener.updateStory(stories.get(position));

                String[] columns = {DBOpener.COL_ID, DBOpener.COL_STORY_ID, DBOpener.COL_CATEGORY, DBOpener.COL_DATE, DBOpener.COL_TITLE, DBOpener.COL_URL, DBOpener.COL_FAVOURITE};
                Cursor results = db.query(false, DBOpener.TABLE_NAME, columns, null, null, null, null, null, null);
                dbOpener.printCursor(results);
            });
            return view;
        }
    }
}
