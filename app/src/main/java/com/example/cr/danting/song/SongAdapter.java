package com.example.cr.danting.song;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cr.danting.R;

import java.util.List;

import static com.example.cr.danting.R.id.song_image;

/**
 * Created by cr on 2016/10/20.
 */

public class SongAdapter extends ArrayAdapter<Song> {
    private int resourceId;

    public SongAdapter(Context context, int textViewResourceId, List<Song> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.songImage=(ImageButton)view.findViewById(song_image);
            viewHolder.songName=(TextView)view.findViewById(R.id.song_name);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.songImage.setImageResource(song.getImageId());
        viewHolder.songName.setText(song.getName());
        return view;

    }
    class ViewHolder{
        ImageView songImage;
        TextView songName;
    }
}
