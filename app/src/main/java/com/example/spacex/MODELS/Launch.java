package com.example.spacex.MODELS;

public class Launch {
    private String mission, date, time,daysBetween, rocketName, rocketType,imgUrl, launch_year, article_link, wikipedia, video_link ;
    private Boolean successful;

    public Launch() {
    }

    public Launch(String mission, String date, String time, String daysBetween, String rocketName, String rocketType, String imgUrl, String launch_year, String article_link, String wikipedia, String video_link, Boolean successful) {
        this.mission = mission;
        this.date = date;
        this.time = time;
        this.daysBetween = daysBetween;
        this.rocketName = rocketName;
        this.rocketType = rocketType;
        this.imgUrl = imgUrl;
        this.launch_year = launch_year;
        this.article_link = article_link;
        this.wikipedia = wikipedia;
        this.video_link = video_link;
        this.successful = successful;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDaysBetween() {
        return daysBetween;
    }

    public void setDaysBetween(String daysBetween) {
        this.daysBetween = daysBetween;
    }

    public String getRocketName() {
        return rocketName;
    }

    public void setRocketName(String rocketName) {
        this.rocketName = rocketName;
    }

    public String getRocketType() {
        return rocketType;
    }

    public void setRocketType(String rocketType) {
        this.rocketType = rocketType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLaunch_year() {
        return launch_year;
    }

    public void setLaunch_year(String launch_year) {
        this.launch_year = launch_year;
    }

    public String getArticle_link() {
        return article_link;
    }

    public void setArticle_link(String article_link) {
        this.article_link = article_link;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }
}
