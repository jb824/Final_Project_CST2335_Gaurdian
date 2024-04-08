package com.example.final_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public static final String ITEM_NAME = "NAME";
    public static final String ITEM_HEIGHT = "HEIGHT";
    public static final String ITEM_MASS = "MASS";
    List<Story> stories = new ArrayList<>();
    private ListAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        StarWars starWars = new StarWars();
        starWars.execute("https://swapi-node.vercel.app/api/people");

        ListView listView = findViewById(R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter = new ListAdapter());

        if (starWarsList == null) {
            System.out.println("list is null");
        }

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        listView.setOnItemClickListener((list, item, position, id) -> {
            People starwarsItem = starWarsList.get(position);
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_NAME, starwarsItem.getName());
            dataToPass.putDouble(ITEM_HEIGHT, starwarsItem.getHeight());
            dataToPass.putDouble(ITEM_MASS, starwarsItem.getMass());

            if(isTablet) {
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, detailsFragment) //Add the fragment in FrameLayout
                        .addToBackStack("Any String here")
                        .commit(); //actually load the fragment.
            } else {//is Phone
                Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });

//        for (People p:starWarsList) {
//            System.out.printf("%s, %d, %d, \n", p.getName(),  p.getHeight(), p.getMass());
//        }
    }

    class StarWars extends AsyncTask<String, Integer, String> {
        List<People> fieldsList = new ArrayList<>();
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
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                System.out.println("Number of fields: " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject fields = jsonArray.getJSONObject(i).getJSONObject("fields");
                    String jsonName = fields.getString("name");
                    String jsonHeight = fields.getString("height");
                    String jsonMass = fields.getString("mass");

                    People people = new People(
                            jsonName,
                            Double.parseDouble(jsonHeight),
                            Double.parseDouble(jsonMass)
                    );

                    fieldsList.add(people);

                }

//                for (People p:starWarsList) {
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
            for (People p : fieldsList) {
                starWarsList.add(p);
                System.out.printf("%s, %s, %s, \n", p.getName(), p.getHeight(), p.getMass());
            }
            adapter.notifyDataSetChanged();
        }
    }


    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return starWarsList.size();
        }

        @Override
        public People getItem(int position) {
            return starWarsList.get(position);
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

            TextView textView = view.findViewById(R.id.starwars_item);
            textView.setText(getItem(position).getName());

            return view;
        }
    }
}


}