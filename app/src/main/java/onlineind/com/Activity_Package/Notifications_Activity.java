package onlineind.com.Activity_Package;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.STARTNOTIFICATION;
import static onlineind.com.Https_Urls.Urls.STOPNOTIFICATION;


public class Notifications_Activity extends AppCompatActivity {
    Switch switc;
    Context context;
    LinearLayout notification_back_arrow;
    String android_id;
    public static final String PREF_NOTIFICATION = "notification";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        switc = (Switch) findViewById(R.id.switc);

        settings = getSharedPreferences(PREF_NOTIFICATION, 0);
        editor = settings.edit();
        String status = settings.getString("status","");

        if (status.equals("1"))
        {
            switc.setChecked(true);
        }
        else
        {
            switc.setChecked(false);
        }

        switc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                    startNotification(android_id);

                    SharedPreferences settings = getSharedPreferences(PREF_NOTIFICATION, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("status", "1");
                    editor.commit();
                }
                else {
                    android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                    stopNotification(android_id);
                    SharedPreferences settings = getSharedPreferences(PREF_NOTIFICATION, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("status", "0");
                    editor.commit();
                }
            }
        });
        notification_back_arrow = (LinearLayout)findViewById(R.id.notification_back_img);
        notification_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Notifications_Activity.this, MainActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

    }

    public void onBackPressed() {
        startActivity(new Intent(Notifications_Activity.this,MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }


    private void startNotification(String uid) {
        HashMap postData = new HashMap();
        postData.put("androidId", uid);
        PostResponseAsyncTask task1 = new PostResponseAsyncTask(Notifications_Activity.this, postData,
                new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.toString().trim().equals("start"))
                        {
                            Toast.makeText(Notifications_Activity.this, "Push notification enabled", Toast.LENGTH_SHORT).show();

                        }
                        else if (s.toString().trim().equals("stop"))
                        {
                            Toast.makeText(Notifications_Activity.this, "Push notification disabled", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        task1.execute(STARTNOTIFICATION);
    }
    private void stopNotification(String uid) {
        HashMap postData = new HashMap();
        postData.put("androidId", uid);
        PostResponseAsyncTask task1 = new PostResponseAsyncTask(Notifications_Activity.this, postData,
                new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.toString().trim().equals("start"))
                        {
                            Toast.makeText(Notifications_Activity.this, "Push notification enabled", Toast.LENGTH_SHORT).show();

                        }
                        else if (s.toString().trim().equals("stop"))
                        {
                            Toast.makeText(Notifications_Activity.this, "Push notification disabled", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        task1.execute(STOPNOTIFICATION);
    }
}
