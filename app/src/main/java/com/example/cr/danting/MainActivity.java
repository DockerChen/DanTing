package com.example.cr.danting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cr.danting.song.Song;
import com.example.cr.danting.song.SongAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cr on 2016/10/10.
 */

public class MainActivity extends Activity {
    private List<Song> songList = new ArrayList<Song>();
    private Context context;  //运行上下文

    //    public static final String ENCODING = "UTF-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        initSongs();
        SongAdapter adapter = new SongAdapter(MainActivity.this, R.layout.song_item, songList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song=songList.get(i);
                Intent intent=new Intent("com.example.cr.danting.ACTION_START");
                intent.putExtra("song_name",song.getName());
                System.out.println("click------"+song.getName());
                startActivity(intent);

            }
        });
    }

    private void initSongs() {
        getFiles("");



    }

    private void getFiles(String assetDir) {
        String[] files = null;

        try {
            // 获得Assets一共有几多文件
            files = this.getResources().getAssets().list(assetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        string = getFromAssets("lrc/演员.lrc");
//        System.out.println(string);

        for (int i = 0; i < files.length; i++) {


            if (files[i].equals("song")) {
                System.out.println(files[i]);
                getFiles(assetDir+files[i]);


            }
            if (files[i].equals("lrc")) {

                System.out.println(files[i]);
                getFiles(assetDir+files[i]);

            }
            if (files[i].endsWith(".mp3")) {
                int len=files[i].length();
                String string=files[i].substring(0,len-4);
                System.out.println("mp3---" + files[i].substring(0,len-4));
                songList.add(new Song(string,R.drawable.play_32));

            }
            if (files[i].endsWith(".lrc")) {
                int len=files[i].length();
                System.out.println("lrc---" + files[i].substring(0,len-4));
            }


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}


