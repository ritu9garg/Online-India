package onlineind.com.GS_Package;

/**
 * Created by admin on 9/4/2017.
 */

public class Feedback_GS {
    private String news_title;
    private int profile_image;

    public Feedback_GS(String news_title, int profile_image) {
        this.news_title = news_title;
        this.profile_image = profile_image;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public int getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(int profile_image) {
       this.profile_image = profile_image;
    }
}