package onlineind.com.Adapter_Package;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import onlineind.com.Activity_Package.Description_Activity;
import onlineind.com.Activity_Package.Slider_Description_Activity;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;

/**
 * Created by Android PC on 07-03-2018.
 */

public class CustomPageAdapter extends PagerAdapter {
    private Activity context;
    private List<Home_GS> Home_GSList;
    private LayoutInflater layoutInflater;
    int index = 10, pos;
    public CustomPageAdapter(Activity context, List<Home_GS> Home_GSList){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.Home_GSList = Home_GSList;
    }
    @Override
    public int getCount() {
        return Home_GSList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Home_GS home_gs = Home_GSList.get(position);
        View view = this.layoutInflater.inflate(R.layout.pager_list_items, container, false);

        view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //Toast.makeText(context, ""+home_gs.getnews_description(), Toast.LENGTH_SHORT).show();
                Log.d("hello",home_gs.getNews_image());
                Log.d("des",home_gs.getnews_description());
                Intent intent = new Intent(context, Slider_Description_Activity.class);
                intent.putExtra("title_dec", "मुख्य ख़बरें ");
                intent.putExtra("news_title",home_gs.getNews_title());
                intent.putExtra("news_image",home_gs.getNews_image());
                intent.putExtra("news_description",home_gs.getnews_description());
                intent.putExtra("news_date",home_gs.getNews_date());
                intent.putExtra("news_id",home_gs.getNews_id());
                context.startActivity(intent);
            }
        });

        ImageView displayImage = (ImageView)view.findViewById(R.id.large_image);
        TextView imageText = (TextView)view.findViewById(R.id.image_name);
        Picasso.with(context).load(home_gs.getNews_image()).into(displayImage);
        imageText.setText(Home_GSList.get(position).getNews_title());
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}