package onlineind.com.Activity_Package;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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
import java.util.List;

import onlineind.com.Adapter_Package.Discription_Adapter;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static onlineind.com.Https_Urls.Urls.BOOKMARK;
import static onlineind.com.Https_Urls.Urls.CHECKBMLIKE;
import static onlineind.com.Https_Urls.Urls.HOME;
import static onlineind.com.Https_Urls.Urls.REMOVELIKE;
import static onlineind.com.Https_Urls.Urls.REMOVEMYLIST;
import static onlineind.com.Https_Urls.Urls.SUBMITLIKE;

public class Slider_Description_Activity extends AppCompatActivity {
    private static List<Home_GS> discription_list = new ArrayList<>();
    private RecyclerView recyclerView;
    TextView news_titel, news_description,news_date,set_titel_text ,marque;
    GifImageView loader,loaderitem;
    private RadioButton radiosmallfont, radiomedium,radiolarge;
    PhotoViewAttacher pAttacher;
    String get_news_title,get_news_image,get_news_date,get_news_description,get_news_id, get_liked, get_bookmark;
    ImageView news_image;
    LinearLayout img_discription_back;
    ImageView iv_bookmark,iv_like,share,iv_red_like, iv_gold_bookmark;
    String str_user_id,android_id;
    private Discription_Adapter discription_adapter;
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String PREFS_NAME1 = "ThemePrefs";
    public static final String PREFS_NAME2 = "AndroididPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap1;
    String position,get_user_id;
    String user_id,liked,bookmark;


    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (shouldAskPermissions()) {
            askPermissions();
        }
        setContentView(R.layout.activity_description);


        Toolbar toolbar = (Toolbar) findViewById(R.id.discription_toolbar);

        String title = getIntent().getStringExtra("title_dec");
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        str_user_id = settings.getString("tbl_user_id","");


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        settings = getSharedPreferences(PREFS_NAME2, 0);
        editor = settings.edit();
        editor.putString("tbl_android_id", android_id);
        editor.commit();


        get_news_title = getIntent().getStringExtra("news_title");
        get_news_image = getIntent().getStringExtra("news_image");
       // Toast.makeText(this, ""+get_news_image, Toast.LENGTH_SHORT).show();
        get_news_date = getIntent().getStringExtra("news_date");
        get_news_description =Html.fromHtml(getIntent().getStringExtra("news_description")).toString();
        get_news_id = getIntent().getStringExtra("news_id");
        get_liked = getIntent().getStringExtra("liked");
        get_bookmark = getIntent().getStringExtra("bm");
        get_user_id = getIntent().getStringExtra("str_user_id");
                loader = (GifImageView) findViewById(R.id.loader);


        news_titel = (TextView) findViewById(R.id.news_titel);
        news_titel.setTextIsSelectable(true);
        news_description = (TextView) findViewById(R.id.news_discription);
        news_description.setTextIsSelectable(true);
        news_image = (ImageView) findViewById(R.id.news_image);
        news_date = (TextView) findViewById(R.id.news_date);
        set_titel_text = (TextView)findViewById(R.id.set_titel);

        set_titel_text.setText(title);

        marque = (TextView)findViewById(R.id.sliding_text_marquee);
        marque.setSelected(true);
        marque.setText(get_news_title);

        news_titel.setText((get_news_title));
        news_description.setText((get_news_description));
        news_date.setText((get_news_date));
        Picasso.with(getApplicationContext())
                .load(get_news_image)
                .into(news_image);


        news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog nagDialog = new Dialog(Slider_Description_Activity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.image_zoom_dailog);
                ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.dailog_zoom_image);
                ImageView cross = (ImageView) nagDialog.findViewById(R.id.cross);
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nagDialog.dismiss();
                    }
                });
                Picasso.with(getApplicationContext())
                        .load(get_news_image)
                        .into(ivPreview);
                pAttacher = new PhotoViewAttacher(ivPreview);
                pAttacher.update();
                nagDialog.show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        discription_adapter = new Discription_Adapter(discription_list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(discription_adapter);
        recyclerView.setClickable(false);
        discription_adapter.clear();

       /* news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.onlineind.com/"));
                startActivity(browserIntent);
            }
        });*/

        loadRecomendedNews();

                img_discription_back = (LinearLayout) findViewById(R.id.img_discription_back);
                img_discription_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     /*   startActivity(new Intent(Description_Activity.this, MainActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
                        finish();
                    }
                });

        iv_bookmark = (ImageView) findViewById(R.id.bookmark);
        iv_gold_bookmark = (ImageView)findViewById(R.id.gold_bookmark);
        iv_bookmark.setVisibility(View.VISIBLE);
        iv_gold_bookmark.setVisibility(View.GONE);

        CheckLikeId bl = new CheckLikeId();
        bl.execute(str_user_id,get_news_id, String.valueOf(position),str_user_id);

        iv_gold_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               RemoveMyList bl = new RemoveMyList();
                bl.execute(get_news_id,str_user_id);
                iv_gold_bookmark.setVisibility(View.GONE);
                iv_bookmark.setVisibility(View.VISIBLE);
            }
        });
        iv_bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!str_user_id.isEmpty()) {
                            SubmitBookmark sb = new SubmitBookmark(Slider_Description_Activity.this);
                            sb.execute(get_news_id, str_user_id);
                            iv_bookmark.setVisibility(View.GONE);
                            iv_gold_bookmark.setVisibility(View.VISIBLE);

                        }else {
                            Toast.makeText(Slider_Description_Activity.this, "Please Register Your Self..", Toast.LENGTH_SHORT).show();
                            iv_like.setVisibility(View.VISIBLE);

                        }
                    }
                });

        iv_like = (ImageView) findViewById(R.id.like);
        iv_red_like = (ImageView)findViewById(R.id.redlike1);
        iv_like.setVisibility(View.VISIBLE);
        iv_red_like.setVisibility(View.GONE);

        CheckLikeId b2 = new CheckLikeId();
        b2.execute(str_user_id,get_news_id, String.valueOf(position),str_user_id);
        iv_red_like.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         RemoveLike bl = new RemoveLike();
                         bl.execute(get_news_id,str_user_id);
                         iv_like.setVisibility(View.VISIBLE);
                         iv_red_like.setVisibility(View.GONE);
                     }
                 });
        iv_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                          if (!str_user_id.isEmpty()) {
                              SubmitLike bt = new SubmitLike(Slider_Description_Activity.this);
                              bt.execute(str_user_id, get_news_id);
                              iv_like.setVisibility(View.GONE);
                              iv_red_like.setVisibility(View.VISIBLE);


                          }else {
                              Toast.makeText(Slider_Description_Activity.this, "Please Register Your Self..", Toast.LENGTH_SHORT).show();
                              iv_like.setVisibility(View.VISIBLE);

                        }
                    }
                });



                share = (ImageView) findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bitmapDrawable = (BitmapDrawable) news_image.getDrawable();// get the from imageview or use your drawable from drawable folder
                        bitmap1 = bitmapDrawable.getBitmap();
                        String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap1,"title",null);
                        Uri imgBitmapUri=Uri.parse(imgBitmapPath);
                        String shareText = Html.fromHtml(get_news_title) + "\n " + "\n" + Html.fromHtml(get_news_description);
                        Intent shareIntent=new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("*/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM,imgBitmapUri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        startActivity(Intent.createChooser(shareIntent,"Share Wallpaper using"));

                    }
                });

        settings=getSharedPreferences(PREFS_NAME1, 0);
        editor = settings.edit();
        String color_code=settings.getString("color","");
        setColor(color_code);

     //  Log.d("USERID",""+str_user_id);
     //   Log.d("NEWSID",""+get_news_id);

    }
    private void setColor(String color_code)
    {
        if(color_code.equals("BLUE")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blue)));
            marque.setBackgroundColor(getResources().getColor(R.color.Light_Blue));

        }else if(color_code.equals("GREEN")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Green)));
            marque.setBackgroundColor(getResources().getColor(R.color.LightGreen));
        }else if(color_code.equals("ORANGE")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DarkOrange)));
            marque.setBackgroundColor(getResources().getColor(R.color.LightOrange));
        }else if(color_code .equals("SKYBLUE")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.SkyBlue)));
            marque.setBackgroundColor(getResources().getColor(R.color.LightSkyBlue));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.comment), getResources().getString(R.string.comment)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.font_size), getResources().getString(R.string.Font)));
//        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.download_icon), getResources().getString(R.string.download)));
        return true;
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:

                Intent intent = new Intent(Slider_Description_Activity.this, Comment_Activity.class);
               // finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                Bundle bundle = new Bundle();
                intent.putExtra("news_title", String.valueOf(get_news_title));
                intent.putExtra("news_image", String.valueOf(get_news_image));
                intent.putExtra("news_description", String.valueOf(get_news_description));
                intent.putExtra("news_id", String.valueOf(get_news_id));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case 2:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_dailog, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.show();
                radiosmallfont = (RadioButton) view.findViewById(R.id.btn_small);
                radiosmallfont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        news_titel.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size12));
                        news_description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size12));
                        news_date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size12));
                        alertDialog.dismiss();
                    }
                });
                radiomedium = (RadioButton) view.findViewById(R.id.btn_medium);
                radiomedium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        news_titel.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size14));
                        news_description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size14));
                        news_date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size14));
                        alertDialog.dismiss();
                    }
                });
                radiolarge = (RadioButton) view.findViewById(R.id.btn_large);
                radiolarge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        news_titel.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size16));
                        news_description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size16));
                        news_date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.size16));
                        alertDialog.dismiss();
                    }
                });
//                builder.show();
                return true;
//            case 3:
//                Toast.makeText(this, "download", Toast.LENGTH_SHORT).show();
//                return true;
        }
        return onOptionsItemSelected(item);
        }

    @Override
    public void onBackPressed() {

       /* startActivity(new Intent(Description_Activity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
        finish();
        super.onBackPressed();
    }

    public class SubmitLike extends AsyncTask<String,Void,String>
    {
        Context ctx;
        public SubmitLike(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String str_user_id = params[0];
            String str_news_id = params[1];
            String data;
            try
            {

                URL url = new URL(SUBMITLIKE);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                data = URLEncoder.encode("tbl_like_user_id", "UTF-8") + "=" + URLEncoder.encode(str_user_id, "UTF-8") + "&" +
                        URLEncoder.encode("tbl_like_news_id", "UTF-8") + "=" + URLEncoder.encode(str_news_id, "UTF-8");
                outputStream.write(data.getBytes());

                bufferedWriter.write(data);
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Result_like", result);
            Toast.makeText(ctx, "Added to Likes...", Toast.LENGTH_SHORT).show();
        }

    }

    public class SubmitBookmark extends AsyncTask<String,Void,String>
    {
        Context ctx;
        private ProgressDialog pDialog;
        public SubmitBookmark(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String str_news_id = params[0];
            String str_user_id = params[1];
            String data;
            try
            {

                URL url = new URL(BOOKMARK);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                data = URLEncoder.encode("news_id", "UTF-8") + "=" + URLEncoder.encode(str_news_id, "UTF-8") + "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(str_user_id, "UTF-8");
                outputStream.write(data.getBytes());

                bufferedWriter.write(data);
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //hidePDialog();
            Toast.makeText(ctx, "Added to MyList.. ", Toast.LENGTH_SHORT).show();
        }
        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }

    }

    private void loadRecomendedNews() {
        //  Toast.makeText(this, "loadherors", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                        Log.d("json", result);
                        String response = result.toString();
                        loader.setVisibility(View.GONE);

                        try {
                            JSONObject new_array = new JSONObject(response);
                            JSONArray arr = new_array.getJSONArray("result");

                            for (int i = 0, count = arr.length(); i < count; i++) {
                                new_array = arr.getJSONObject(i);

                                String news_title = new_array.getString("news_title");
                                String news_image = new_array.getString("news_image");
                                String news_description = new_array.getString("news_description");
                                String news_date = new_array.getString("news_date");
                                String news_id = new_array.getString("news_id");


                                Home_GS home_gs = new Home_GS(news_title, news_image, news_description, news_id, news_date,"");
                                discription_list.add(home_gs);
                            }
                            discription_adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        //   Toast.makeText(this, ""+stringRequest, Toast.LENGTH_SHORT).show();
    }


    public class CheckLikeId extends AsyncTask<String,Void,String>
    {
        Context ctx;


        @Override
        protected String doInBackground(String... params) {
            String str_user_id = params[0];
            String str_news_id = params[1];
            position = params[2];
            user_id = params[3];
            String data;
            try
            {

                URL url = new URL(CHECKBMLIKE);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                data = URLEncoder.encode("tbl_like_user_id", "UTF-8") + "=" + URLEncoder.encode(str_user_id, "UTF-8") + "&" +
                        URLEncoder.encode("tbl_like_news_id", "UTF-8") + "=" + URLEncoder.encode(str_news_id, "UTF-8")+ "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                outputStream.write(data.getBytes());

                bufferedWriter.write(data);
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
         //   Log.d("Result_like", result);
         //   Toast.makeText(ctx, "Result like  "+result, Toast.LENGTH_SHORT).show();
            String res=result.toString().trim();

         //   Log.d("POsoion",""+Integer.parseInt(position));
            if (res.equals("both"))
            {
                liked = "1";
                bookmark = "1";

                iv_like.setVisibility(View.GONE);
                iv_bookmark.setVisibility(View.GONE);
                iv_red_like.setVisibility(View.VISIBLE);
                iv_gold_bookmark.setVisibility(View.VISIBLE);


            }
            else if (res.equals("onlylike"))
            {
                liked = "1";
                bookmark = "0";

                iv_like.setVisibility(View.GONE);
                iv_bookmark.setVisibility(View.VISIBLE);
                iv_red_like.setVisibility(View.VISIBLE);
                iv_gold_bookmark.setVisibility(View.GONE);

            }
            else if (res.equals("bm"))
            {
                liked = "0";
                bookmark = "1";

                iv_like.setVisibility(View.VISIBLE);
                iv_bookmark.setVisibility(View.GONE);
                iv_red_like.setVisibility(View.GONE);
                iv_gold_bookmark.setVisibility(View.VISIBLE);

            }
            else if (res.equals("nothing"))
            {
                liked = "0";
                bookmark = "0";

                iv_like.setVisibility(View.VISIBLE);
                iv_bookmark.setVisibility(View.VISIBLE);
                iv_red_like.setVisibility(View.GONE);
                iv_gold_bookmark.setVisibility(View.GONE);
            }
        }

    }

    class RemoveLike extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
          //  Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();

         /*   likeloder.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);*/
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String news_id = params[0];
            String user_id = params[1];

            String data;
            try {
                URL url = new URL(REMOVELIKE);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                data = URLEncoder.encode("tbl_like_news_id", "UTF-8") + "=" + URLEncoder.encode(news_id, "UTF-8")+ "&" +
                        URLEncoder.encode("tbl_like_user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
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
         //   Log.d("str_user_id", result);
            Toast.makeText(getApplicationContext(), "Deleting..." , Toast.LENGTH_LONG).show();
            String response = result.toString();
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
            Toast.makeText(getApplicationContext(), "Deleting..." , Toast.LENGTH_LONG).show();
            String response = result.toString();
        }
    }

}
