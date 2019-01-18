package onlineind.com.Adapter_Package;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import onlineind.com.GS_Package.Comment_GS;
import onlineind.com.R;

/**
 * Created by admin on 9/16/2017.
 */

public class Comment_Adapter extends ArrayAdapter<Comment_GS> {
    ArrayList<Comment_GS> arrayList;
    Activity activity;
    ImageButton remove_list;


    public Comment_Adapter(Activity activity, int i, ArrayList<Comment_GS> oList) {
        super(activity, i, oList);
        this.arrayList = oList;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.comment_designview, null);

        }
        final Comment_GS comment_gs = arrayList.get(position);
        TextView tbl_cmnt_name = (TextView) v.findViewById(R.id.tbl_cmnt_name);
        TextView tbl_cmnt_opinion = (TextView) v.findViewById(R.id.tbl_cmnt_opinion);
        TextView tbl_cmnt_added_date = (TextView) v.findViewById(R.id.tbl_cmnt_added_date);

        tbl_cmnt_name.setText(Html.fromHtml(comment_gs.getTbl_cmnt_name()));
        tbl_cmnt_opinion.setText(Html.fromHtml(comment_gs.getTbl_cmnt_opinion()));
        tbl_cmnt_added_date.setText(Html.fromHtml(comment_gs.getTbl_cmnt_added_date()));
        return v;
    }
    }
