package com.example.cr.danting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cr on 2016/10/13.
 */

public class MusicActivity extends Activity {
    //完整歌词显示
    private TextView display_lrc;
    //歌曲状态显示
    private TextView display;
    //单句歌词显示
    private TextView lrc;
    //歌曲播放时间显示
    private TextView time_start, time_end;
    //歌曲名
    private TextView songname;
    //歌曲切换，播放停止按钮
    private ImageButton play, forward, next;
    private MediaPlayer mediaPlayer;
    //歌曲播放进度条
    private SeekBar seekBar;

    private View statueBar;
    private LyricInfo lyricInfo;
    //单个歌曲名字
    private String song_name;
    //所有歌曲名字
    private String song_names[] = null;
    //当前播放歌曲位置
    private int position = 0;
    //定义Handle，用于接受子线程发送的数据， 并用此数据配合主线程更新UI
    MyHandler myHandler = new MyHandler();

    //时间格式化
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("mm:ss.ms");

    //定义传感器
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicactivity_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        Intent intent = getIntent();
        song_name = intent.getStringExtra("song_name");
        System.out.println("intent----" + song_name);
        //获取所有歌曲名
        song_names = this.getResources().getStringArray(R.array.song_names);
        //初始化
        initAllViews();
        initAllDatum(song_name);
        //传感器
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);


        //播放，暂停
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
//                    Toast.makeText(MusicActivity.this, "time is "+mediaPlayer.getDuration(), Toast.LENGTH_SHORT).show();
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    display.setText(R.string.message_play);

                } else {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    display.setText(R.string.message_pause);

                }

            }
        });


        //切换到上一首歌
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                try {
                    int current=0;
                    for(int i=0;i<song_names.length;i++){
                        if(song_names[i].equals(song_name)){
                            current=i;
                        }
                    }
                    position=current;
                    position--;
                    if(position<0){
                        position=song_names.length-1;
                    }
                    song_name=song_names[position];
                    initMediaPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                display.setText("previous song...");



            }
        });
        //切换到下一首歌
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                try {
                    int current=0;
                    for(int i=0;i<song_names.length;i++){
                        if(song_names[i].equals(song_name)){
                            current=i;
                        }
                    }
                    position=current;
                    position++;
                    if(position>=song_names.length){
                        position=0;
                    }
                    song_name=song_names[position];
                    initMediaPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                display.setText("next song...");


            }
        });

        //进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = mediaPlayer.getDuration();
                int max = seekBar.getMax();
                mediaPlayer.seekTo(time * dest / max);
                int p = mediaPlayer.getCurrentPosition();
                int t = mediaPlayer.getDuration();
                int m = seekBar.getMax();
                float dp = p * m / t;
                Toast.makeText(MusicActivity.this, "time:" + simpleDateFormat.format(p), Toast.LENGTH_SHORT).show();


            }
        });

        //初始化mediaplayer
        try {
            initMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    /*监听返回键*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.setClass(MusicActivity.this, MainActivity.class);
            startActivity(intent);
            MusicActivity.this.finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);


    }


    /*视图初始化*/
    private void initAllViews() {
        display = (TextView) findViewById(R.id.display);
        display_lrc = (TextView) findViewById(R.id.display_lrc);
        lrc = (TextView) findViewById(R.id.lrc);
        time_start = (TextView) findViewById(R.id.time_start);
        time_end = (TextView) findViewById(R.id.time_end);
        songname=(TextView)findViewById(R.id.songname);
        play = (ImageButton) findViewById(R.id.play);
        forward = (ImageButton) findViewById(R.id.forward);
        next = (ImageButton) findViewById(R.id.next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        MyThread myThread = new MyThread();
        new Thread(myThread).start();
        statueBar = findViewById(R.id.statue_bar);
        statueBar.getLayoutParams().height = getStatusBarHeight();


    }

    private void setTranslucentStatus() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        final int status = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        params.flags |= status;
        window.setAttributes(params);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }


    private void initAllDatum(String song_name) {
        String string = "lrc/" + song_name + ".lrc";
        System.out.println("initalldatum--------" + string);

//        String string_lrc = getFromAssets(string);
//        System.out.println(string_lrc);
//        InputStream in = getResources().openRawResource(R.raw.yici_lrc);
//        InputStream in= null;
//        try {
//            in = getAssets().open(string);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        display_lrc.setText(string_lrc);
        try {
            setupLyricResource(string);
            StringBuffer stringBuffer = new StringBuffer();
            if (lyricInfo != null && lyricInfo.song_lines != null) {
                int size = lyricInfo.song_lines.size();
                for (int i = 0; i < size; i++) {
                    stringBuffer.append(lyricInfo.song_lines.get(i).content + "\n");
                    System.out.println("s_time: " + lyricInfo.song_lines.get(i).s_time);
                    System.out.println(lyricInfo.song_lines.get(i).content);
                }
                songname.setText(song_name);
                display_lrc.setText(stringBuffer.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    public String getFromAssets(String fileName){
//        String Result="";
//        try {
//            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            String line="";
//
//            while((line = bufReader.readLine()) != null)
//                Result += line;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Result;
//    }


    private void setupLyricResource(String fileName) {

        try {
            lyricInfo = new LyricInfo();
            lyricInfo.song_lines = new ArrayList<>();
            InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                analyzeLyric(lyricInfo, line);

            }
            reader.close();
            inputStreamReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*逐行解析歌词内容*/
    private void analyzeLyric(LyricInfo lyricInfo, String line) {
        int index = line.lastIndexOf("]");
        if (line != null && line.startsWith("[offset:")) {
            // 时间偏移量
            String string = line.substring(8, index).trim();
            lyricInfo.song_offset = Long.parseLong(string);
            return;
        }
        if (line != null && line.startsWith("[ti:")) {
            //title标题
            String string = line.substring(4, index).trim();
            lyricInfo.song_title = string;
            return;
        }
        if (line != null && line.startsWith("[ar:")) {
            // artist 作者
            String string = line.substring(4, index).trim();
            lyricInfo.song_artist = string;
            return;
        }
        if (line != null && line.startsWith("[al:")) {
            // album 所属专辑
            String string = line.substring(4, index).trim();
            lyricInfo.song_album = string;
            return;
        }
        if (line != null && line.startsWith("[by:")) {
            return;
        }
        if (line != null && index == 9 && line.trim().length() > 10) {
            // 歌词内容
            LineInfo lineInfo = new LineInfo();
            lineInfo.content = line.substring(10, line.length());
            lineInfo.start = measureStarTimeMillis(line.substring(0, 10));
            lineInfo.s_time = line.substring(1, 9);
            lyricInfo.song_lines.add(lineInfo);

        }
    }

    private long measureStarTimeMillis(String str) {
        long minute = Long.parseLong(str.substring(1, 3));
        long second = Long.parseLong(str.substring(4, 6));
        long millisecond = Long.parseLong(str.substring(7, 9));
        return millisecond + second * 1000 + minute * 60 * 1000;

    }

    class LyricInfo {
        List<LineInfo> song_lines;
        String song_artist; //歌手
        String song_title;  //标题
        String song_album;  //专辑

        long song_offset;   //偏移量
    }

    class LineInfo {
        String content; //歌词内容
        long start; //开始时间
        String s_time;

    }

    /*音乐播放器初始化*/
    private void initMediaPlayer() throws IOException {
        AssetManager am = this.getAssets();//获得该应用的AssetManager
        AssetFileDescriptor afd = null;
        String string = "song/" + song_name + ".mp3";
        System.out.println("initMediaPlayer------" + string);
        try {
            afd = am.openFd(string);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    try {
                        int current=0;
                        for(int i=0;i<song_names.length;i++){
                            if(song_names[i].equals(song_name)){
                                current=i;
                            }
                        }
                        position=current;
                        position++;
                        if(position>=song_names.length){
                            position=0;
                        }
                        song_name=song_names[position];
                        initMediaPlayer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    display.setText("auto change song...");

                }
            });
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare(); //准备
            mediaPlayer.start();    //开始
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            mediaPlayer = MediaPlayer.create(this, R.raw.yici);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            //异步装载媒体资源
//            mediaPlayer.prepareAsync();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(sensorManager!=null){
            sensorManager.unregisterListener(listener);
        }

    }

    /*为sensor设置监听器*/
    private SensorEventListener listener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float xValue=Math.abs(sensorEvent.values[0]);
            float yValue=Math.abs(sensorEvent.values[1]);
            float zValue=Math.abs(sensorEvent.values[2]);
            if(xValue>15||yValue>15||zValue>15){
                Toast.makeText(MusicActivity.this,"摇一摇",Toast.LENGTH_SHORT).show();
                Random random=new Random();
                int random_song=random.nextInt(song_names.length-1);

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                try {
                    int current=random_song;
                    position=current;

                    song_name=song_names[position];
                    initMediaPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                display.setText("music change...");

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    class MyHandler extends Handler {


        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 此处可以更新UI
            Bundle b = msg.getData();
            int num = b.getInt("num");
            if (num == 1) {
                int position_song = mediaPlayer.getCurrentPosition();
                int time = mediaPlayer.getDuration();
                int max = seekBar.getMax();
                MusicActivity.this.seekBar.setProgress(position_song * max / time);
//                System.out.println("time:"+position*max/time);
//                System.out.println(simpleDateFormat.format(position));
                time_start.setText(simpleDateFormat.format(position_song).toString());
//                System.out.println("position: "+simpleDateFormat1.format(position));
                time_end.setText(simpleDateFormat.format(time).toString());
                String string = "lrc/" + song_name + ".lrc";

                /*ceshi*/
//                InputStream in = null;
//                try {
//                    in = getResources().getAssets().open(string);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (in != null) {
                try {
                    String geci;
                    setupLyricResource(string);
                    StringBuffer stringBuffer = new StringBuffer();
                    if (lyricInfo != null && lyricInfo.song_lines != null) {
                        int size = lyricInfo.song_lines.size();
                        for (int i = 0; i < size; i++) {
                            stringBuffer.append(lyricInfo.song_lines.get(i).content + "\n");
                            geci = lyricInfo.song_lines.get(i).content;
                            if (position_song>= 0 && (position_song >= lyricInfo.song_lines.get(i).start)) {
                                lrc.setText(geci);

                            }
//                                System.out.println("s_time: "+lyricInfo.song_lines.get(i).s_time);
//                                System.out.println(lyricInfo.song_lines.get(i).content);

                        }
                        songname.setText(song_name);
                        display_lrc.setText(stringBuffer.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                }
            }


        }
    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("num", 1);
                msg.setData(b);

                MusicActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息，更新UI
            }

        }
    }


}
