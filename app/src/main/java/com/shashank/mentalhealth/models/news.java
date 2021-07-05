package com.shashank.mentalhealth.models;

public class news {
    String title;
    String des;
    String imgUrl;
    String url;

    public news(String title, String des, String imgUrl, String url) {
        this.title = title;
        this.des = des;
        this.imgUrl = imgUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getUrl() {
        return url;
    }
}
