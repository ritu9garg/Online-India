package onlineind.com.Activity_Package;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import onlineind.com.Adapter_Package.Likes_Adapter;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;


/**
 * Created by admin on 9/6/2017.
 */

public class Downloads_Activity extends AppCompatActivity {
    ArrayList<Home_GS> arrayList = new ArrayList<>();
    Likes_Adapter likes_adapter;
    LinearLayout img_download_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloads_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.downloads_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        img_download_back = (LinearLayout)findViewById(R.id.img_download_back);
        img_download_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ListView l1 = (ListView) findViewById(R.id.list_View);
       // method();
        likes_adapter = new Likes_Adapter(this, 0, arrayList);
        l1.setAdapter(likes_adapter);
         }
}
