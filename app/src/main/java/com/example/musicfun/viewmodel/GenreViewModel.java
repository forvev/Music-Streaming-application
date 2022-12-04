package com.example.musicfun.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Genre;
import com.example.musicfun.repository.GenreRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class GenreViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Genre>> genreLiveList;
    private ArrayList<Genre> genreArrayList;
    Application application;
    GenreRepository genreRepository;

    public GenreViewModel(Application application){
        super(application);
        this.genreArrayList = new ArrayList<>();
        this.genreLiveList = new MutableLiveData<>();
        this.application = application;
        genreRepository = new GenreRepository(application.getApplicationContext());
    }

    public MutableLiveData<ArrayList<Genre>> getGenreLiveList() {
        return genreLiveList;
    }

    public void init() throws JSONException {
        int[] names = {R.string.genre_country, R.string.genre_electronic, R.string.genre_hiphop, R.string.genre_jazz, R.string.genre_pop, R.string.genre_rock, R.string.genre_classic};
        int[] images = {R.drawable.ic_genre_country, R.drawable.ic_genre_electronic, R.drawable.ic_genre_hiphop, R.drawable.ic_genre_jazz, R.drawable.ic_genre_pop, R.drawable.ic_genre_rock, R.drawable.ic_genre_classic};
        if (names.length != images.length){
            System.out.println("error!!!!");
        }
        else{
            for (int i = 0; i < names.length; i++) {
                Genre genre = new Genre(application.getString(names[i]), i, images[i], false);
                genreArrayList.add(genre);
            }
            genreLiveList.setValue(genreArrayList);
        }

//        genreArrayList.clear();
//        genreRepository.getAllGenres(new ServerCallBack() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                try {
//                    JSONArray genres = (JSONArray) result.get("All genres");
//                    for (int i = 0; i < genres.length(); i++) {
//                        Genre s = new Genre(genres.getJSONObject(i).getString("genre_name"), genres.getJSONObject(i).getInt("genre_id"), genres.getJSONObject(i).getInt("image_id"), false);
//                        genreArrayList.add(s);
//                    }
//                    genreLiveList.setValue(genreArrayList);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    public void submit(){
        for (int i = 0; i < genreArrayList.size(); i++) {
            Genre temp = genreArrayList.get(i);
            System.out.println("name: " + temp.getGenre_name() + " selected: " + temp.getSelected());
        }
        // TODO:

    }
}
