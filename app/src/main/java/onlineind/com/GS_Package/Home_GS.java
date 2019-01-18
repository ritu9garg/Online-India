package onlineind.com.GS_Package;

/**
 * Created by admin on 8/30/2017.
 */

public class Home_GS {
    private String news_title;
    private String news_description;
    private String news_image;
    private String news_date;
    private String news_id;
    private String cate;



    public Home_GS(String news_title, String news_image, String news_description, String news_id , String news_date , String cate) {
        this.news_title = news_title;
        this.news_description = news_description;
        this.news_image = news_image;
        this.news_date = news_date;
        this.news_id = news_id;
        this.cate = cate;
    }


    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getnews_description() {
        return news_description;
    }

    public void setnews_description(String news_description) {
        this.news_description = news_description;
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

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
}