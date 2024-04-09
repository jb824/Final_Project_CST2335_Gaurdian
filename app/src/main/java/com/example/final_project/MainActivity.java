package com.example.final_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    List<Story> stories = new ArrayList<>();
    private ListAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        EditText editText = findViewById(R.id.search);
        searchText = editText.getText().toString();
        String url = "https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=" + searchText;

        Articles article = new Articles();
        if (searchText != null || searchText != "") {
            article.execute(url);
        }

        ListView listView = findViewById(R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter = new ListAdapter());

        if (stories == null) {
            System.out.println("list is null");
        }

        listView.setOnItemClickListener((list, item, position, id) -> {
            Story storyItem = stories.get(position);
            Bundle dataToPass = new Bundle(); // passing to fragment
//            dataToPass.putString(ITEM_NAME, starwarsItem.getName());
//            dataToPass.putDouble(ITEM_HEIGHT, starwarsItem.getHeight());
//            dataToPass.putDouble(ITEM_MASS, starwarsItem.getMass());

//            String storyUrl = storyItem.getUrl();
//            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storyUrl));
//            startActivity(urlIntent);
        });

//        for (Story s:stories) {
//            System.out.printf("%s, %d, %d, \n", p.getName(),  p.getHeight(), p.getMass());
//        }
    }

    class Articles extends AsyncTask<String, Integer, String> {
        List<Story> fieldsList = new ArrayList<>();
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
                stories.add(story);
                System.out.printf("*** article ***");
                System.out.printf("%s%n, %s%n, %s%n, %s%n \n", story.getStoryID(), story.getDate(), story.getTitle(), story.getUrl());
            }
            adapter.notifyDataSetChanged();
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

            if (view == null) {
                view = inflater.inflate(R.layout.activity_item, parent, false);
                view.setClickable(false);
            }

            TextView textView = view.findViewById(R.id.story_item);
            textView.setText(getItem(position).getTitle());

            return view;
        }
    }
}