package com.example.musicfun.datatype;

public class Genre {
    private String genre_name;
    private int genre_id;
    private int image_id;
    private boolean selected;

    public Genre(String genre_name, int id, int image_id, boolean selected){
        this.genre_name = genre_name;
        this.genre_id = id;
        this.image_id = image_id;
        this.selected = selected;
    }

    public String getGenre_name(){
        return genre_name;
    }

    public int getGenre_id(){
        return genre_id;
    }

    public int getImage_id(){
        return image_id;
    }

    public boolean getSelected() {
        return selected;
    }
    public void setSelected(){
        selected = !selected;
    }
}
