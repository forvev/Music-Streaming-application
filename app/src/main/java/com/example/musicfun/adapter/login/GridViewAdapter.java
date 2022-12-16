package com.example.musicfun.adapter.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicfun.datatype.Genre;
import com.example.musicfun.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<Genre> genreArrayList;

    public GridViewAdapter(ArrayList<Genre> genreArrayList, Context context){
        this.genreArrayList = genreArrayList;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return genreArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = layoutInflater.inflate(R.layout.genre_row_items, viewGroup, false);
        }
        TextView tv = view.findViewById(R.id.genreType);
        ImageView iv = view.findViewById(R.id.imageView);

        tv.setText(genreArrayList.get(i).getGenre_name());
        iv.setImageResource(genreArrayList.get(i).getImage_id());
        return view;
    }
}
