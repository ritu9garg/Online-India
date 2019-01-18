package onlineind.com.Activity_Package;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import onlineind.com.R;


/**
 * Created by admin on 12/14/2017.
 */



public class Layout_Slider extends AppCompatActivity {
    String get_news_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_slider);

         ImageView imageView = (ImageView)findViewById(R.id.image_slide);
        get_news_image = getIntent().getStringExtra("news_image");

        Picasso.with(getApplicationContext())
                .load("http://onlineind.com/" + get_news_image)
                .into(imageView);



    }
}
