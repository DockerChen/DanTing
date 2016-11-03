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

    //构造函数
    public SongAdapter(Context context, int textViewResourceId, List<Song> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    //重写getView()方法，该方法在每个子项被滚动到屏幕内的时候被调用
    public View getView(int position, View convertView, ViewGroup parent) {
        //通过getItem()方法，得到当前项的Song实例
        Song song=getItem(position);
        View view;
        ViewHolder viewHolder;
        //convertView这个参数用于将之前加载好的布局进行缓存
        if(convertView==null)
        {
            //加载传入的布局
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
    //内部类ViewHolder，用于对控件的实例进行缓存
    class ViewHolder{
        ImageView songImage;
        TextView songName;
    }
}
