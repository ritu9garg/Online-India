package onlineind.com.Adapter_Package;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.REMOVEMYLIST;


/**
 * Created by admin on 9/6/2017.
 */

     public class MyList_Adapter extends ArrayAdapter<Home_GS> {
    ArrayList<Home_GS> oList;
    Activity activity;
    public static final String PREFS_NAME1 = "LoginPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String str_user_id;
    String newsId;


    public MyList_Adapter(Activity activity, int i, ArrayList<Home_GS> oList) {
        super(activity, i, oList);
        this.oList = oList;
        this.activity = activity;
    }

    private class ViewHolder {
        ImageButton remove_from_list;



    }
    ViewHolder holder = null;

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Home_GS lists = getItem(position);
        final int pos=position;


        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.likes_activity, null);
            holder = new ViewHolder();

            final Home_GS db = oList.get(position);

            settings = activity.getSharedPreferences(PREFS_NAME1, 0);
            editor = settings.edit();
            str_user_id = settings.getString("tbl_user_id","");
           // Toast.makeText(activity, "str_user_id", Toast.LENGTH_SHORT).show();


            TextView titel = (TextView) convertView.findViewById(R.id.titel1);
            TextView content = (TextView) convertView.findViewById(R.id.content1);
            ImageView img = (ImageView) convertView.findViewById(R.id.image1);
            titel.setText(Html.fromHtml(db.getNews_title()));
            content.setText(Html.fromHtml(db.getnews_description()));
               newsId     =   db.getNews_id();
            Picasso.with(activity).load("http://onlineind.com/"+db.getNews_image()).into(img);


            holder.remove_from_list = (ImageButton) convertView.findViewById(R.id.remove_from_list);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        try {
            final View finalConvertView = convertView;
            holder.remove_from_list.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

       PopupMenu popup = new PopupMenu(activity, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu,
          popup.getMenu());
     popup.show();
     popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
              case R.id.remove_from_list:
                //  String android_id = Settings.Secure.getString(activity.getContentResolver(),Settings.Secure.ANDROID_ID);
                  RemoveMyList bl = new RemoveMyList();
                  bl.execute(newsId,str_user_id);
                  removeRow(finalConvertView, position);
                  break;
            }
           return true;
           }
     });
     }});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }

    private void removeRow(final View row, final int position) {
        final int initialHeight = row.getHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                int newHeight = (int) (initialHeight * (1 - interpolatedTime));
                if (newHeight > 0) {
                    row.getLayoutParams().height = newHeight;
                    row.requestLayout();
                }
            }
        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                row.getLayoutParams().height = initialHeight;
                row.requestLayout();
                oList.remove(position);
            }
        });
        animation.setDuration(300);
        row.startAnimation(animation);
    }





     }

class RemoveMyList extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        //Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();

         /*   likeloder.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);*/
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String str_news_id = params[0];
        String str_user_id = params[1];


        String data;
        try {
            URL url = new URL(REMOVEMYLIST);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
            data = URLEncoder.encode("news_id", "UTF-8") + "=" + URLEncoder.encode(str_news_id, "UTF-8") + "&" +
                    URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(str_user_id, "UTF-8");
            outputStream.write(data.getBytes());
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.flush();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        // hidePDialog();
        super.onPostExecute(result);
        //   Log.d("str_android_id", result);
       // Toast.makeText(getApplicationContext(), "Deleting..." , Toast.LENGTH_LONG).show();
        String response = result.toString();
    }
}




