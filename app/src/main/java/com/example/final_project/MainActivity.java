package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.final_project.data.DBOpener;
import com.example.final_project.data.Story;

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

public class MainActivity extends AppCompatActivity {

//    public static final String ITEM_NAME = "NAME";
//    public static final String ITEM_HEIGHT = "HEIGHT";
//    public static final String ITEM_MASS = "MASS";
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
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.search_button);
        EditText editText = findViewById(R.id.search_text);
        progressBar = findViewById(R.id.progressBar);

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
        });

        ListView listView = findViewById(R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter = new ListAdapter());


        if (stories == null) {
            System.out.println("list is null");
        }

        listView.setOnItemClickListener((list, item, position, id) -> {
            Story storyItem = stories.get(position);

            Bundle dataToPass = new Bundle(); // passing to fragment
            dataToPass.putLong(ITEM_ID, storyItem.getId());
            dataToPass.putString(ITEM_TITLE, storyItem.getTitle());
            dataToPass.putString(ITEM_URL, storyItem.getUrl());
            dataToPass.putString(ITEM_CATEGORY, storyItem.getCategory());

            Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition

//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle(storyItem.getTitle());
//            builder.setMessage(getString(R.string.message));
//            builder.setPositiveButton(R.string.positiveButton, (DialogInterface.OnClickListener) (dialog, which) -> {
//                // open story in Chrome
//                String storyUrl = storyItem.getUrl();
//                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storyUrl));
//                startActivity(urlIntent);
//            });
//            builder.setNegativeButton(R.string.negativeButton, (dialog, which) -> {
//                dialog.cancel();
//            });
//            builder.setNeutralButton(R.string.neutralButton, (dialog, which) -> {
//                storyItem.setFavourite(1);
//
//            });
//            builder.setView(getLayoutInflater().inflate(R.layout.activity_item, null));
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();

        });

//        for (Story s:stories) {
//            System.out.printf("%s, %d, %d, \n", p.getName(),  p.getHeight(), p.getMass());
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle your menu item clicks here
        if (id == R.id.item3) {
            // Do something
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Articles extends AsyncTask<String, Integer, String> {
        List<Story> fieldsList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
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

                    Story story = new Story(
                            storyID,
                            date,
                            title,
                            url
                    );


                    fieldsList.add(story);
                }

//                for (Story s:stories) {
//                    System.out.printf("%s, %s, %s, \n", p.getName(), p.getHeight(), p.getMass());
//                }

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

                // add to db
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(DBOpener.COL_STORY_ID, story.getStoryID());
//                contentValues.put(DBOpener.COL_DATE, story.getDate());
//                contentValues.put(DBOpener.COL_TITLE, story.getTitle());
//                contentValues.put(DBOpener.COL_URL, story.getUrl());
//
//                // add new row to database
//                long id = db.insert(DBOpener.TABLE_NAME, null, contentValues);
            }
            adapter.notifyDataSetChanged();
            cancel(true);
            progressBar.setVisibility(View.GONE);
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
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.heart_unfilled);
                    imageButton.setImageDrawable(drawable);

                    story.setFavourite(0);
                }

                // update database
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBOpener.COL_FAVOURITE, story.isFavourite());

                long id = db.update(
                        DBOpener.TABLE_NAME,
                        contentValues,
                        DBOpener.COL_ID + "=?",
                        new String[]{ String.valueOf(story.getId())});
            });

            return view;
        }
    }
}