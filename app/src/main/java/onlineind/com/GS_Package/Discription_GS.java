package onlineind.com.GS_Package;

/**
 * Created by admin on 9/12/2017.
 */

public class Discription_GS {
    private String news_title;
    private String news_description;
    private String news_image;
    private String news_date;
    public Discription_GS(String news_title, String news_image, String news_short_description, String news_date) {
        this.news_title = news_title;
        this.news_description = news_short_description;
        this.news_image = news_image;
        this.news_date = news_date;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_short_description() {
        return news_description;
    }

    public void setNews_short_description(String news_short_description) {
        this.news_description = news_short_description;
    }

    public String getNews_image() {
        return news_image;
    }

    public void setNews_image(String news_image) {
        this.news_image = news_image;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }
}
