package com.example.cr.danting;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cr on 2016/10/13.
 */

public class MusicActivity extends Activity {
    private TextView display;
    private ImageButton play, forward, next;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    MyHandler myHandler=new MyHandler();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicactivity_layout);
        display = (TextView) findViewById(R.id.display);
        play = (ImageButton) findViewById(R.id.play);
        forward = (ImageButton) findViewById(R.id.forward);
        next = (ImageButton) findViewById(R.id.next);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        MyThread myThread=new MyThread();
        new Thread(myThread).start();

        //handler



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){
                    Toast.makeText(MusicActivity.this, "time is "+mediaPlayer.getDuration(), Toast.LENGTH_SHORT).show();
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    display.setText(R.string.message_play);

                }
                else {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    display.setText(R.string.message_pause);

                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest=seekBar.getProgress();
                int time=mediaPlayer.getDuration();
                int max=seekBar.getMax();
                mediaPlayer.seekTo(time*dest/max);
                int p=mediaPlayer.getCurrentPosition();
                int t=mediaPlayer.getDuration();
                int m=seekBar.getMax();
                float dp=p*m/t;
                Toast.makeText(MusicActivity.this,"time"+dp,Toast.LENGTH_SHORT).show();


            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                    display.setText(R.string.message_stop);
                }


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                    display.setText(R.string.message_stop);
                }

            }
        });

        initMediaPlayer();


    }

    private void initMediaPlayer() {
        try{
            mediaPlayer = MediaPlayer.create(this, R.raw.yici);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //异步装载媒体资源
            mediaPlayer.prepareAsync();
            //后台线程发送消息进行进度条的更新


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {

        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();
    }

    class MyHandler extends Handler {


        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 此处可以更新UI
            Bundle b = msg.getData();
            int num = b.getInt("num");
            if(num==1){
                int position=mediaPlayer.getCurrentPosition();
                int time=mediaPlayer.getDuration();
                int max=seekBar.getMax();
                MusicActivity.this.seekBar.setProgress(position*max/time);
                System.out.println("time:"+position*max/time);
            }


        }
    }
    class MyThread implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("num",1);
                msg.setData(b);

                MusicActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息，更新UI
            }

        }
    }
}
