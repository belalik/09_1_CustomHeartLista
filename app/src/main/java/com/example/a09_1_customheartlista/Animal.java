package com.example.a09_1_customheartlista;

public class Animal {

    private static final String TAG = "Animal - Thomas";

    private static int index = 1;

    private String title;
    private String subtitle;
    private int image;
    private boolean shortlisted;

    private int ID;

    public Animal(String title, String subtitle, int image, boolean shortlisted) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.shortlisted = shortlisted;

        ID = index;
        index++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }
}
