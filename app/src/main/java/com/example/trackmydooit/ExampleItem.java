package com.example.trackmydooit;

public class ExampleItem {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private String mText3;

    public ExampleItem(int imageResource,String text1,String text2,String text3){
        mImageResource=imageResource;
        mText1=text1;
        mText2=text2;
        mText3=text3;
    }

    public void changeText1(String text){
        mText1=text;
    }

    public int getImageResource(){
        return mImageResource;
    }
    public String getText1(){
        return mText1;
    }
    public String getText2(){
        return mText2;
    }

    public String getText3(){
        return mText3;
    }
}

