package com.example.final_project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment {


    private Bundle data;
    private long id;
    private String title;
    private String url;
    private String category;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = getArguments();
        id = data.getLong(MainActivity.ITEM_ID);
        title = data.getString(MainActivity.ITEM_TITLE);
        url = data.getString(MainActivity.ITEM_URL);
        category = data.getString(MainActivity.ITEM_CATEGORY);

        View view = inflater.inflate(R.layout.fragment_story, container, false);

        // story title
        TextView titleView = (TextView) view.findViewById(R.id.title_value);
        titleView.setText(title);
        // story url
        TextView urlView = (TextView) view.findViewById(R.id.url_value);
        urlView.setText(url);
        // story category
        TextView categoryView = (TextView) view.findViewById(R.id.category_value);
        categoryView.setText(category);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}