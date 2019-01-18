package onlineind.com.GS_Package;

/**
 * Created by admin on 9/16/2017.
 */

public class Comment_GS {
    private String tbl_cmnt_name;
    private String tbl_cmnt_opinion;
    private String tbl_cmnt_added_date;
    private String news_id;



    public Comment_GS(String tbl_cmnt_name, String tbl_cmnt_opinion, String tbl_cmnt_added_date, String news_id ) {
        this.tbl_cmnt_name = tbl_cmnt_name;
        this.tbl_cmnt_opinion = tbl_cmnt_opinion;
        this.tbl_cmnt_added_date = tbl_cmnt_added_date;
        this.news_id = news_id;
    }


    public String getTbl_cmnt_name() {
        return tbl_cmnt_name;
    }

    public void setTbl_cmnt_name(String tbl_cmnt_name) {
        this.tbl_cmnt_name = tbl_cmnt_name;
    }

    public String getTbl_cmnt_opinion() {
        return tbl_cmnt_opinion;
    }

    public void setTbl_cmnt_opinion(String tbl_cmnt_opinion) {
        this.tbl_cmnt_opinion = tbl_cmnt_opinion;
    }

    public String getTbl_cmnt_added_date() {
        return tbl_cmnt_added_date;
    }

    public void setTbl_cmnt_added_date(String tbl_cmnt_added_date) {
        this.tbl_cmnt_added_date = tbl_cmnt_added_date;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }
}
