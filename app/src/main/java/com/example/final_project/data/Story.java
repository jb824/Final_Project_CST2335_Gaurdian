package com.example.final_project.data;

public class Story {
    private long id;
    private String storyID;
    private String category;
    private String date;
    private String title;
    private String url;
    private int favourite;
    public Story(String storyID, String date, String title, String url, String category) {
        id++;
        this.storyID = storyID;
        this.date = date;
        this.title = title;
        this.url = url;
        this.category = category;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String content) {
        this.category = content;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int isFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

}
