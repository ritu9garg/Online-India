package onlineind.com.Activity_Package;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import onlineind.com.R;


/**
 * Created by admin on 10/6/2017.
 */

public class Like_Discription_Activity extends AppCompatActivity {

    String get_news_title,get_news_image,get_news_date,get_news_description,get_news_id;
    TextView news_titel, news_description,news_date,set_titel_text ,marque;
    ImageView news_image;
    LinearLayout img_discription_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_discription_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.discription_toolbar);
        img_discription_back = (LinearLayout)findViewById(R.id.img_discription_back);
        img_discription_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        get_news_title = getIntent().getStringExtra("news_title");
        get_news_image = getIntent().getStringExtra("news_image");
        get_news_date = getIntent().getStringExtra("news_date");
        get_news_description = Html.fromHtml(getIntent().getStringExtra("news_description")).toString();
        get_news_id = getIntent().getStringExtra("news_id");


        news_titel = (TextView) findViewById(R.id.news_titel);
        news_description = (TextView) findViewById(R.id.news_discription);
        news_image = (ImageView) findViewById(R.id.news_image);
        news_date = (TextView) findViewById(R.id.news_date);

        news_titel.setText((get_news_title));
        news_description.setText((get_news_description));
        news_date.setText((get_news_date));
        Picasso.with(getApplicationContext()).load("http://onlineind.com/" + get_news_image).into(news_image);

    }

    }
