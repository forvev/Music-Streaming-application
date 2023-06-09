package com.example.musicfun.viewmodel.login;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.R;
import com.example.musicfun.datatype.Genre;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.GenreRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class serves as an intermediate station for genre related database accesses. It is used in the GenreFragment.
 */
public class GenreViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Genre>> genreLiveList;
    private ArrayList<Genre> genreArrayList;
    Application application;
    GenreRepository genreRepository;
    SharedPreferences sp;

    public GenreViewModel(Application application){
        super(application);
        this.genreArrayList = new ArrayList<>();
        this.genreLiveList = new MutableLiveData<>();
        this.application = application;
        genreRepository = new GenreRepository(application.getApplicationContext());
        sp = getApplication().getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
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

    }

    public void submit() throws JSONException {
        ArrayList<String> selectedGerne = new ArrayList<>();
        for (int i = 0; i < genreArrayList.size(); i++) {
            Genre temp = genreArrayList.get(i);
            if (temp.getSelected()){
                selectedGerne.add(temp.getGenre_name());
            }
        }
        genreRepository.sendGenres(sp.getString("token", ""), selectedGerne, new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }
}
