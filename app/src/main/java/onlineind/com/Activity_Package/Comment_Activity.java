package onlineind.com.Activity_Package;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onlineind.com.Adapter_Package.Comment_Adapter;
import onlineind.com.GS_Package.Comment_GS;
import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.COMMENT;
import static onlineind.com.Https_Urls.Urls.SUBMITCOMMENT;

public class Comment_Activity extends AppCompatActivity {
    ListView l1;
    ArrayList<Comment_GS> comment_gsArrayList = new ArrayList<>();
    Comment_Adapter comment_adapter;
    TextView news_titel,news_description;
    ImageView news_image;
    LinearLayout comment_back_img,layout_visibility,btn_layout_visibility, hide_cmnt_box;
    public static final String PREFS_NAME = "LoginPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String google_singin_name, google_singin_email, google_singin_img,facebook_singin_name, facebook_singin_email, facebook_singin_img;
    Button continue_guest;
    String tbl_user_name, tbl_user_email,tbl_user_city,tbl_user_id;
    EditText edt_name,edt_email,edt_city,edt_opinion;
    Button btn_post_cmnt, sign_in_post, btn_user_getid;
    String get_news_id;
    String tbl_cmnt_name,tbl_cmnt_email,tbl_cmnt_city,tbl_cmnt_opinion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        news_titel = (TextView) findViewById(R.id.cmnt_titel1);
        news_description = (TextView) findViewById(R.id.cmnt_content1);
        news_image = (ImageView) findViewById(R.id.cmnt_image1);
        hide_cmnt_box = (LinearLayout)findViewById(R.id.hide_cmnt_box);
        l1 = (ListView)findViewById(R.id.list_comment);
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.commit();

        tbl_user_name = settings.getString("tbl_user_name", null);
        tbl_user_email = settings.getString("tbl_user_email", null);
        tbl_user_city = settings.getString("tbl_user_city", null);
        tbl_user_id = settings.getString("tbl_user_id", "");

        google_singin_name = settings.getString("google_singin_name", null);
        google_singin_email = settings.getString("google_singin_email", null);


        facebook_singin_name = settings.getString("facebook_singin_name", null);
        facebook_singin_email = settings.getString("facebook_singin_email", null);


        String get_news_title = getIntent().getStringExtra("news_title");
        String get_news_image = getIntent().getStringExtra("news_image" );
        String get_news_description = getIntent().getStringExtra("news_description");
        get_news_id = getIntent().getStringExtra("news_id");

        news_titel.setText((get_news_title));
       news_description.setText((get_news_description));
        Picasso.with(getApplicationContext()).load("http://onlineind.com/" + get_news_image).into(news_image);

        layout_visibility =(LinearLayout)findViewById(R.id.layout_visibility);
        btn_layout_visibility = (LinearLayout)findViewById(R.id.btn_layout_visibility);



            comment_back_img = (LinearLayout)findViewById(R.id.comment_back_img);
            comment_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        continue_guest = (Button)findViewById(R.id.continue_guest);
        continue_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_visibility.setVisibility(View.VISIBLE);
                 btn_layout_visibility.setVisibility(View.GONE);
            }
        });
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    if(!emailValidator(s)){
                        edt_email.setError("Please Enter Valid Email!");
                    }}}});

        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_opinion = (EditText)findViewById(R.id.edt_post_comment);
        sign_in_post = (Button)findViewById(R.id.sign_in_post);

        if(!tbl_user_id.isEmpty() ) {
            layout_visibility.setVisibility(View.VISIBLE);
            btn_layout_visibility.setVisibility(View.GONE);
            edt_name.setText(tbl_user_name);
            edt_email.setText(tbl_user_email);
            edt_city.setText(tbl_user_city);
        }
        else {
            edt_name.setText(google_singin_name);
            edt_email.setText(google_singin_email);

            edt_name.setText(facebook_singin_name);
            edt_email.setText(facebook_singin_email);

        }

        sign_in_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(Comment_Activity.this, LogIn_Activity.class);
                    startActivity(i);
            }
        });
        btn_post_cmnt = (Button) findViewById(R.id.btn_post_cmnt);
        btn_post_cmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbl_cmnt_name = edt_name.getText().toString().trim();
                tbl_cmnt_email = edt_email.getText().toString().trim();
                tbl_cmnt_city = edt_city.getText().toString().trim();
                tbl_cmnt_opinion = edt_opinion.getText().toString().trim();

                if (!tbl_cmnt_name.isEmpty() && !tbl_cmnt_email.isEmpty() && !tbl_cmnt_city.isEmpty()&& !tbl_cmnt_opinion.isEmpty() ) {
                    SubmitComment bt = new SubmitComment(Comment_Activity.this);
                    bt.execute(tbl_cmnt_name, tbl_cmnt_email, tbl_cmnt_city, tbl_cmnt_opinion, get_news_id);
                    hide_cmnt_box.setVisibility(View.GONE);

                   /* ListComment bt1 = new ListComment();
                    bt1.execute("df");*/
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }


            }
        });

        ListComment bt = new ListComment();
        bt.execute(get_news_id);
    }
    private boolean emailValidator(CharSequence email){
        Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        Matcher matcher1 = pattern1.matcher(email);
        return matcher1.matches();
    }



    public class SubmitComment extends AsyncTask<String,Void,String>
    {
        Context ctx;
        private ProgressDialog pDialog;
        public SubmitComment(Context ctx)
        {
            this.ctx=ctx;
        }
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
            pDialog.setMessage("We are submitting your details");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

                String tbl_cmnt_name=params[0];
                String tbl_cmnt_email=params[1];
                String  tbl_cmnt_city=params[2];
                String tbl_cmnt_opinion=params[3];
            String news_id=params[4];
                String data;
                try
                {

                    URL url = new URL(SUBMITCOMMENT);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                    data = URLEncoder.encode("tbl_cmnt_name", "UTF-8") + "=" + URLEncoder.encode(tbl_cmnt_name, "UTF-8") + "&" +
                            URLEncoder.encode("news_id", "UTF-8") + "=" + URLEncoder.encode(news_id, "UTF-8")+ "&" +
                            URLEncoder.encode("tbl_cmnt_email", "UTF-8") + "=" + URLEncoder.encode(tbl_cmnt_email, "UTF-8")+ "&" +
                            URLEncoder.encode("tbl_cmnt_city", "UTF-8") + "=" + URLEncoder.encode(tbl_cmnt_city, "UTF-8")+ "&" +
                            URLEncoder.encode("tbl_cmnt_opinion", "UTF-8") + "=" + URLEncoder.encode(tbl_cmnt_opinion, "UTF-8");
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
            hidePDialog();
          //  Toast.makeText(ctx, "result "+result, Toast.LENGTH_SHORT).show();
                 ListComment bt = new ListComment();
                bt.execute(get_news_id);



        }
        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }

    }
    public class ListComment extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
//            loader.setVisibility(View.VISIBLE);
//            l1.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String news_id = params[0];
            String data;
            try {
                URL url = new URL(COMMENT);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                data = URLEncoder.encode("news_id", "UTF-8") + "=" + URLEncoder.encode(news_id, "UTF-8");
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
          //  Log.d("json", result);
           //  Toast.makeText(Comment_Activity.this, "" + result.toString(), Toast.LENGTH_LONG).show();
            String response = result.toString();
            try {
                JSONObject new_array = new JSONObject(response);
                JSONArray arr = new_array.getJSONArray("result");
                comment_gsArrayList.clear();
                for (int i = 0, count = arr.length(); i < count; i++) {
                    new_array = arr.getJSONObject(i);

                    String tbl_cmnt_name = new_array.getString("tbl_cmnt_name");
                    String tbl_cmnt_opinion = new_array.getString("tbl_cmnt_opinion");
                    String tbl_cmnt_added_date = new_array.getString("tbl_cmnt_added_date");
                    String news_id = new_array.getString("news_id");
                    Comment_GS comment_gs = new Comment_GS(tbl_cmnt_name, tbl_cmnt_opinion, tbl_cmnt_added_date, news_id);
                    comment_gsArrayList.add(comment_gs);
                }
             //   comment_adapter.notifyDataSetChanged();
                l1.setVisibility(View.VISIBLE);
                comment_adapter = new Comment_Adapter(Comment_Activity.this, 0, comment_gsArrayList);
                l1.setAdapter(comment_adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void hidePDialog() {
//           loader.setVisibility(View.GONE);
//            l1.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
