package com.example.cr.danting.song;

/**
 * Created by cr on 2016/10/20.
 */

public class Song {
    private String name;
    private int imageId;
    public Song(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
