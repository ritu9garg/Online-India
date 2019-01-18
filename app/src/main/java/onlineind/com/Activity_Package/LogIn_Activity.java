package onlineind.com.Activity_Package;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.Check_Password;
import static onlineind.com.Https_Urls.Urls.LOGINUSER;
import static onlineind.com.Https_Urls.Urls.SIGNUP;


/**
 * Created by admin on 9/7/2017.
 */

public class LogIn_Activity extends AppCompatActivity {
    Button btn_signup;
    LinearLayout img_login_back;
    public static final String PREFS_NAME = "LoginPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String google_singin_name,google_singin_email,google_singin_img,facebook_singin_name, facebook_singin_email,facebook_singin_img;
    EditText edt_user_email, edt_user_password;
    String str_id, str_name,str_gender,str_city,str_email, str_password, str_images;
    Button btn_user_signin;
    LinearLayout loader;
    String pw;

    JSONObject jsonresponse,profile_pic_data, profile_pic_url;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0 ;
    private Button signOutButton;
    //fb
    FacebookCallback<LoginResult> callback;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    CallbackManager callbackManager;

    TextView forget_passwrod;

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }

        loader = (LinearLayout)findViewById(R.id.loader);

        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();

        forget_passwrod = (TextView)findViewById(R.id.forget_password);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {
            }};
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayMessage(newProfile);
                profile = newProfile;
            }};

        callback = new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json_object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                //displayMessage(profile,object);
                                try {
                                    jsonresponse = new JSONObject(json_object.toString());
                                    try {
                                   //     Log.d("json_object",json_object.toString());
                                        facebook_singin_email = jsonresponse.get("email").toString();
                                        facebook_singin_name = jsonresponse.get("name").toString();
                                        String gender = jsonresponse.get("gender").toString();

                                        profile_pic_data = new JSONObject(jsonresponse.get("picture").toString());
                                        profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                                        facebook_singin_img = profile_pic_url.getString("url").toString();

                                     /*   Log.d("facebook_singin_email",facebook_singin_email);
                                        Log.d("facebook_singin_name",facebook_singin_name);
                                        Log.d("facebook_singin_img",facebook_singin_img);*/
                                        editor.putString("fblogin", "fblogin");
                                        editor.putString("facebook_singin_name", facebook_singin_name);
                                        editor.putString("facebook_singin_img", facebook_singin_img);
                                        editor.putString("facebook_singin_email", facebook_singin_email);
                                        editor.commit();

                                        register(facebook_singin_name, gender,"", facebook_singin_email, "", facebook_singin_img);

                                    }catch (Exception ex){
                                        String gender = jsonresponse.get("gender").toString();
                                        facebook_singin_name = jsonresponse.get("name").toString();
                                        profile_pic_data = new JSONObject(jsonresponse.get("picture").toString());
                                        profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                                        facebook_singin_img = profile_pic_url.getString("url");
                                       // String fbId = jsonresponse.get("id").toString();

                                     /*   Log.d("facebook_singin_name",facebook_singin_name);
                                        Log.d("facebook_singin_img",facebook_singin_img);*/
                                        editor.putString("fblogin", "fblogin");
                                        editor.putString("facebook_singin_name", facebook_singin_name);
                                        editor.putString("facebook_singin_img", facebook_singin_img);
                                        editor.commit();

                                        register(facebook_singin_name, gender,"", facebook_singin_email, "", facebook_singin_img);
                                    }
                                } catch(Exception e){
                                    e.printStackTrace();
                                }

                            }});
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday, picture.width(120).height(120)");
                request.setParameters(parameters);
                request.executeAsync();

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                //displayMessage(profile);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
            // for google signin.........

        btn_signup = (Button)findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn_Activity.this, SignUp_Activity.class);
                startActivity(intent);
            }});

        img_login_back = (LinearLayout)findViewById(R.id.img_login_back);
        img_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_user_email = (EditText) findViewById(R.id.edt_user_email);
        edt_user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    if(!emailValidator(s)){
                        edt_user_email.setError("Please Enter Valid Email!");
                    }}}});

        edt_user_password = (EditText) findViewById(R.id.edt_user_password);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        btn_user_signin = (Button) findViewById(R.id.btn_user_signin);
        btn_user_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str_email = edt_user_email.getText().toString().trim();
                    str_password = edt_user_password.getText().toString().trim();
                    if (TextUtils.isEmpty(str_email)) {
                        edt_user_email.setError("Please Enter Email...");
                    } else if (TextUtils.isEmpty(str_password)) {
                        edt_user_password.setError("Please enter password...");
                    } else {
                        LoginValidation();
                    }}});

        forget_passwrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(LogIn_Activity.this, "forget", Toast.LENGTH_SHORT).show();
                forgetPassword();
            }
        });
    }

    private void forgetPassword() {
        final EditText et_pass = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Forgot your password")
                .setMessage("Enter your register email id")
                .setView(et_pass)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pw = et_pass.getText().toString();
                        if (TextUtils.isEmpty(pw)) {
                            et_pass.setError("Please Enter Event Name.");
                            et_pass.requestFocus();
                            return;
                        }else {
                            forgot_pass(pw);
                            // Toast.makeText(LogIn_Activity.this, ""+pw, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    private boolean emailValidator(CharSequence email){
        Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        Matcher matcher1 = pattern1.matcher(email);
        return matcher1.matches();
    }

    private void LoginValidation() {
        HashMap postData = new HashMap();
        postData.put("tbl_user_email", str_email);
        postData.put("tbl_user_password", str_password);
        PostResponseAsyncTask task1 = new PostResponseAsyncTask(LogIn_Activity.this, postData, new AsyncResponse() {
            public void processFinish(String s) {
          //      Log.d("LOGIN_JSON", s);
                if (s.equals("Invalid Username and Password")) {
                    edt_user_email.setError("Invalid");
                    edt_user_password.setError("Invalid");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s.toString());
                        JSONArray mJSONArray = jsonObject.getJSONArray("result");


                        JSONObject uid = mJSONArray.getJSONObject(0);
                        str_id = uid.getString("tbl_user_id");
                        str_name = uid.getString("tbl_user_name");
                        str_gender = uid.getString("tbl_user_gender");
                        str_city = uid.getString("tbl_user_city");
                        str_email = uid.getString("tbl_user_email");
                        str_password = uid.getString("tbl_user_password");
                     //  str_images = uid.getString("tbl_user_img");

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("logged", "logged");
                        editor.putString("adminlogin", "adminlogin");
                        editor.putString("tbl_user_id", str_id);
                        editor.putString("tbl_user_name", str_name);
                        editor.putString("tbl_user_gender", str_gender);
                        editor.putString("tbl_user_city", str_city);
                        editor.putString("tbl_user_email", str_email);
                        editor.putString("tbl_user_password", str_password);
                      //  editor.putString("tbl_user_image", str_images);

                        editor.commit();
                        validLogin();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        task1.execute(LOGINUSER);
    }
    private void validLogin() {
        Intent intent = new Intent(LogIn_Activity.this, MainActivity.class);
        startActivity(intent);
    }


    public void register(final String str_name, final String str_gender, final String str_city, final String str_email, final String str_password, final String str_img){

     //   Log.d("img", str_img);

        class Register extends AsyncTask<String, Void, String> {
            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
               loader.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String data;

                try {

                    URL url = new URL(SIGNUP);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                    data = URLEncoder.encode("tbl_user_name", "UTF-8") + "=" + URLEncoder.encode(str_name, "UTF-8") + "&" +
                            URLEncoder.encode("tbl_user_gender", "UTF-8") + "=" + URLEncoder.encode(str_gender, "UTF-8") + "&" +
                            URLEncoder.encode("tbl_user_city", "UTF-8") + "=" + URLEncoder.encode(str_city, "UTF-8") + "&" +
                            URLEncoder.encode("tbl_user_email", "UTF-8") + "=" + URLEncoder.encode(str_email, "UTF-8") + "&" +
                            URLEncoder.encode("tbl_user_img", "UTF-8") + "=" + URLEncoder.encode(str_img, "UTF-8") + "&" +
                            URLEncoder.encode("tbl_user_password", "UTF-8") + "=" + URLEncoder.encode(str_password, "UTF-8");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
               loader.setVisibility(View.GONE);
           //     Log.d("Result", result);
                if (TextUtils.isDigitsOnly(result)){
                    editor.putString("tbl_user_id", result);
                    editor.commit();
                    finish();
                    startActivity(new Intent(LogIn_Activity.this,MainActivity.class));
                }else {
                    LoginManager.getInstance().logOut();
                    editor.clear();
                    editor.commit();
                }
            }

        }
        Register r = new Register();
        r.execute();
    }

    public void forgot_pass(final String str_email){

    //    Log.d("email", str_email);

        class forgot_pass extends AsyncTask<String, Void, String> {
            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                loader.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String data;

                try {

                    URL url = new URL(Check_Password);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                    data = URLEncoder.encode("tbl_user_email", "UTF-8") + "=" + URLEncoder.encode(str_email, "UTF-8")+"&"+
                            URLEncoder.encode("tbl_user_", "UTF-8") + "=" + URLEncoder.encode("demo", "UTF-8");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                loader.setVisibility(View.GONE);
         //       Log.d("Result", result);
                if (result.toString().trim().equals("success")){
                   AlertDialog.Builder dialog = new AlertDialog.Builder(LogIn_Activity.this);
                   dialog.setTitle("Confirmation")
                           .setMessage("Your password send to your register email id please check. " +
                                   "\n\n Note -: Please check your spam \t also.")
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                               }
                           })
                           .show();
                }else {
                    Toast.makeText(LogIn_Activity.this, ""+result, Toast.LENGTH_SHORT).show();
                }
            }
        }
        forgot_pass r = new forgot_pass();
        r.execute();
    }



}
