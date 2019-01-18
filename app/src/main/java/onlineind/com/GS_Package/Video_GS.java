package onlineind.com.GS_Package;

/**
 * Created by admin on 9/22/2017.
 */

public class Video_GS  {
    private String video;
    private String news_titel;
    private String news_description;
    private String news_date;
    private String news_id;

    public Video_GS(String video, String news_titel, String news_description, String news_date, String news_id) {
        this.video = video;
        this.news_titel = news_titel;
        this.news_description = news_description;
        this.news_date = news_date;
        this.news_id = news_id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getNews_titel() {
        return news_titel;
    }

    public void setNews_titel(String news_titel) {
        this.news_titel = news_titel;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }
}
