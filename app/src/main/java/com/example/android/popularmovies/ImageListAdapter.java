package com.example.android.popularmovies;

/**
 * Created by vaibhav.seth on 12/17/15.
 */
import java.util.ArrayList;
import android.app.Activity;
import android.app.LauncherActivity;
//import android.app.LauncherActivity;
import android.content.Context;
import android.database.Cursor;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class ImageListAdapter extends CursorAdapter {

    private Context context;
    private LayoutInflater inflater;

    private  ArrayList<String> imageUrls;

    public ImageListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
//        inflater = LayoutInflater.from(context);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (null == convertView) {
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.list_item_image, parent, false);
////                    inflater.inflate(R.layout.list_item_image, parent, false);
//        }
//        Movie movie = getItem(position);
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.List_item_image);
//
//        System.out.println("Hello" + position);
//        System.out.println(movie);
//        Picasso
//                .with(context)
//                .load(movie.PosterPath)
////                .fit() // will explain later
//                .into((ImageView) imageView);
//
//        return convertView;
//    }

    @Override
    public ImageView newView(Context context, Cursor cursor, ViewGroup parent) {
        ImageView view = (ImageView) LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView tv = (ImageView)view;
        Picasso
                .with(context)
                .load(cursor.getString(1))
//                .fit() // will explain later
                .into((ImageView) tv);


    }

}
