package com.example.final_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project.data.DBOpener;
import com.example.final_project.data.Story;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment {


    private Bundle data;
    private long id;
    private String storyId;
    private String date;
    private String title;
    private String url;
    private String category;
    private AppCompatActivity parentActivity;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = getArguments();
        id = data.getLong(SearchActivity.ITEM_ID);
        storyId = data.getString(SearchActivity.ITEM_STORY_ID);
        date = data.getString(SearchActivity.ITEM_DATE);
        title = data.getString(SearchActivity.ITEM_TITLE);
        url = data.getString(SearchActivity.ITEM_URL);
        category = data.getString(SearchActivity.ITEM_CATEGORY);

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

        urlView.setOnClickListener(e -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Visit TheGuardian.com");
            builder.setMessage(getString(R.string.message));
            builder.setPositiveButton(R.string.positiveButton, (DialogInterface.OnClickListener) (dialog, which) -> {
                // open story in Chrome
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            });
            builder.setNegativeButton(R.string.negativeButton, (dialog, which) -> {
                dialog.cancel();
            });
            builder.setNeutralButton(R.string.neutralButton, (dialog, which) -> {
                Story story = new Story(storyId, date, title, url, category);
                story.setFavourite(1);

                try {
                    DBOpener dbOpener = new DBOpener(this.parentActivity);
                    dbOpener.updateStory(story);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }

            });
            builder.setView(getLayoutInflater().inflate(R.layout.activity_permission, null));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            parentActivity = (AppCompatActivity) context;
        } else {
            throw new IllegalArgumentException("Parent activity must be AppCompatActivity");
        }
    }
}