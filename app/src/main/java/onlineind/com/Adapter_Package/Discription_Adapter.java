package onlineind.com.Adapter_Package;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import onlineind.com.Activity_Package.Description_Activity;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;
import pl.droidsonroids.gif.GifImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Discription_Adapter extends RecyclerView.Adapter<Discription_Adapter.MyViewHolder> {

    String str_android_id,str_user_id;
    private List<Home_GS> discription_list;
    Activity activity;
    public static final String PREFS_NAME = "LoginPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, news_id;
        ImageView img;
        GifImageView loaderitem;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);


            loaderitem = (GifImageView)view.findViewById(R.id.loaderitem);
            loaderitem.setVisibility(GONE);
            title = (TextView) view.findViewById(R.id.titel1);
            content = (TextView) view.findViewById(R.id.content1);
            img = (ImageView) view.findViewById(R.id.image1);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout);
            settings = activity.getSharedPreferences(PREFS_NAME, 0);
            editor = settings.edit();
            str_user_id = settings.getString("tbl_user_id","");


        }
    }


    public Discription_Adapter(List<Home_GS> discription_list, Activity activity) {
        this.discription_list = discription_list;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_news, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Home_GS home_gs = discription_list.get(position);
        holder.title.setText(Html.fromHtml(home_gs.getNews_title()));
        holder.content.setText(Html.fromHtml (home_gs.getnews_description()));
        Picasso.with(activity).load("http://onlineind.com/"+home_gs.getNews_image()).into(holder.img);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // loader when click in listview items
                holder.loaderitem.setVisibility(VISIBLE);
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        holder.loaderitem.setVisibility(GONE);
                    }
                }, 1000);
                //_____*______________


                Intent intent = new Intent(activity, Description_Activity.class);
                intent.putExtra("title_dec", "मुख्य ख़बरें ");
                intent.putExtra("news_title", home_gs.getNews_title());
                intent.putExtra("news_image", home_gs.getNews_image());
                intent.putExtra("news_description", home_gs.getnews_description());
                intent.putExtra("news_date", home_gs.getNews_date());
                intent.putExtra("news_id",home_gs.getNews_id());
                activity.startActivity(intent);




            }
        });
    }

    @Override
    public int getItemCount() {
        return discription_list.size();
    }

    public void clear() {
        discription_list.clear();
    }


}

