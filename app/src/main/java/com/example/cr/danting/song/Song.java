package com.example.cr.danting.song;

/**
 * Created by cr on 2016/10/20.
 */

public class Song {
    //歌曲名
    private String name;
    //歌曲图片
    private int imageId;
    //构造函数
    public Song(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    //获取歌曲名
    public String getName(){

        return name;
    }
    //获取歌曲图片
    public int getImageId(){

        return imageId;
    }
}
