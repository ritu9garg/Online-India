package onlineind.com.Activity_Package;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import onlineind.com.Adapter_Package.Likes_Adapter;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.LISTLIKE;


public class Likes_Activity extends AppCompatActivity {

    ArrayList<Home_GS> arrayList = new ArrayList<>();
    Likes_Adapter likes_Adapter;
    ListView l1;
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String PREFS_NAME1 = "ThemePrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Toolbar toolbar;


    RelativeLayout likeloder;
    String str_user_id;
    LinearLayout img_likes_back, empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes_listview);

        toolbar = (Toolbar) findViewById(R.id.likes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        settings=getSharedPreferences(PREFS_NAME1, 0);
        editor = settings.edit();
        String color_code=settings.getString("color","");
        setColor(color_code);


        likeloder = (RelativeLayout)findViewById(R.id.likeloader) ;

        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        str_user_id = settings.getString("tbl_user_id","");

        empty = (LinearLayout) findViewById(R.id.empty);
        img_likes_back = (LinearLayout) findViewById(R.id.img_likes_back);
        img_likes_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        l1 = (ListView) findViewById(R.id.list_View);
        likes_Adapter = new Likes_Adapter(this, 0, arrayList);
        l1.setAdapter(likes_Adapter);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Likes_Activity.this, Like_Discription_Activity.class);
                intent.putExtra("news_title", arrayList.get(position).getNews_title());
                intent.putExtra("news_image", arrayList.get(position).getNews_image());
                intent.putExtra("news_description", arrayList.get(position).getnews_description());
                intent.putExtra("news_date", arrayList.get(position).getNews_date());
                intent.putExtra("news_id", arrayList.get(position).getNews_id());
                startActivity(intent);
            }
        });

        ListLike bt = new ListLike();
        bt.execute(str_user_id);

    }


    private void setColor(String color_code)
    {
        if(color_code.equals("BLUE")){

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blue)));


        }else if(color_code.equals("GREEN")){

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Green)));

        }else if(color_code.equals("ORANGE")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DarkOrange)));


        }else if(color_code .equals("SKYBLUE")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.SkyBlue)));
        }

    }




    public class ListLike extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

            likeloder.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
           /* String news_id = params[0];*/
            String user_id = params[0];
            String data;
            try {
                URL url = new URL(LISTLIKE);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                data = URLEncoder.encode("tbl_like_user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8")/*+ "&" +
                        URLEncoder.encode("tbl_like_news_id", "UTF-8") + "=" + URLEncoder.encode(news_id, "UTF-8")*/;
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

            hidePDialog();
            super.onPostExecute(result);
         //   Log.d("str_user_id", result);
            //  Toast.makeText(Description_Activity.this, "" + result.toString(), Toast.LENGTH_LONG).show();
            String response = result.toString();
            arrayList.clear();
            try {
                JSONObject new_array = new JSONObject(response);
                JSONArray arr = new_array.getJSONArray("result");
                arrayList.clear();
                for (int i = 0, count = arr.length(); i < count; i++) {
                    new_array = arr.getJSONObject(i);
                    empty.setVisibility(View.GONE);

                    String news_title = new_array.getString("news_title");
                    String news_image = new_array.getString("news_image");
                    String news_description = new_array.getString("news_description");
                    String news_date = new_array.getString("news_date");
                    String news_id = new_array.getString("news_id");
//                    String news_cate = new_array.getString("category_name");


                    Home_GS home_gs = new Home_GS(news_title, news_image, news_description, news_id, news_date, "");
                    arrayList.add(home_gs);
                }
                likes_Adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void hidePDialog() {
            likeloder.setVisibility(View.GONE);
            l1.setVisibility(View.VISIBLE);
        }

    }




}