package onlineind.com.Adapter_Package;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import onlineind.com.Activity_Package.Description_Activity;
import onlineind.com.Activity_Package.MainActivity;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;


/**
 * Created by admin on 9/9/2017.
 */

public class Drawr_Rajya_Adapter extends ArrayAdapter<Home_GS> {
    ArrayList<Home_GS> oList;
    Activity activity;

    public Drawr_Rajya_Adapter(Activity activity , int i, ArrayList<Home_GS> oList) {
        super(activity,i, oList);
        this.oList=oList;
        this.activity=activity;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.fragment_news, null);

        }
        final Home_GS db = oList.get(position);
        if (db != null) {
            LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity bb = (MainActivity)activity;
                    Intent intent = new Intent(activity, Description_Activity.class);

                    bb.startActivity(intent);
                }
            });



            TextView titel = (TextView) v.findViewById(R.id.titel1);
            TextView content = (TextView) v.findViewById(R.id.content1);
            ImageView img = (ImageView) v.findViewById(R.id.image1);


            titel.setText(Html.fromHtml(db.getNews_title()));
            content.setText(Html.fromHtml(db.getnews_description()));
            Picasso.with(getContext()).load(db.getNews_image()).into(img);

        }
        return v;
    }

    @Override
    public void clear() {
        oList.clear();
    }
}
