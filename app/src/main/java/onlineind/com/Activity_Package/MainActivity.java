package onlineind.com.Activity_Package;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

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

import de.hdodenhof.circleimageview.CircleImageView;
import onlineind.com.R;
import onlineind.com.fragment_package.Fragment_Astha;
import onlineind.com.fragment_package.Fragment_DeshVidesh;
import onlineind.com.fragment_package.Fragment_Health;
import onlineind.com.fragment_package.Fragment_Home;
import onlineind.com.fragment_package.Fragment_Khel;
import onlineind.com.fragment_package.Fragment_LifeStyle;
import onlineind.com.fragment_package.Fragment_Manoranjan;
import onlineind.com.fragment_package.Fragment_Profile;
import onlineind.com.fragment_package.Fragment_Rajniti;
import onlineind.com.fragment_package.Fragment_Rajya;
import onlineind.com.fragment_package.Fragment_Takniki;
import onlineind.com.fragment_package.Fragment_Trending;
import onlineind.com.fragment_package.Fragment_Videos;
import onlineind.com.fragment_package.Fragment_Vyapar;

import static android.view.View.GONE;
import static onlineind.com.Https_Urls.Urls.TOKENREGISTER;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    public static final String PREFS_NAME = "ThemePrefs";
    public static final int C_FRAGMENTS_TO_KEEP_IN_MEMORY = 2;
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    LinearLayout home, videos, profile, main_layout;
    public static TabLayout tabLayout;
    ViewPager viewPager;
    String discription_titel = "";
    String title = "";
    Fragment myFragment = null;
    Toolbar toolbar;
   // ImageView logo;
    TextView logo1;
    public boolean isHome=true, isOther=false;
    DrawerLayout drawer;
    String color_code;

    SharedPreferences sf;
    SharedPreferences.Editor et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isHome=true;
        isOther=false;
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
       // logo = (ImageView) findViewById(R.id.logo);
        logo1 = (TextView) findViewById(R.id.logo1);
        String android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        String recent_token= FirebaseInstanceId.getInstance().getToken();

        sf = getSharedPreferences("video", 0);
        et = sf.edit();
        //drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drwar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.getDrawerArrowDrawable();
        drawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);



        //
        View v = navigationView.inflateHeaderView(R.layout.header_layout);
        LinearLayout headerLayout=(LinearLayout) v.findViewById(R.id.headerLayout);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer = (DrawerLayout) findViewById(R.id.drwar);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        //
        home = (LinearLayout) findViewById(R.id.home);
        videos = (LinearLayout) findViewById(R.id.videos);
        profile = (LinearLayout) findViewById(R.id.profile);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);


        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.my_viewpager);
        viewPager.setAdapter(new MyTab(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(C_FRAGMENTS_TO_KEEP_IN_MEMORY);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHome = false;
                isOther = true;

                et.clear();
                et.commit();

                Fragment_Videos fragment_videos = new Fragment_Videos();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.sub_frame, fragment_videos).addToBackStack(null).commit();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHome = false;
                isOther = true;
                Fragment_Profile fragment_profile = new Fragment_Profile();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.sub_frame, fragment_profile).addToBackStack(null).commit();
            }
        });
        //theme color
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        color_code = settings.getString("color", "");
        setColor(color_code);
        String method = "submit";
        BackGroundTask bt = new BackGroundTask(MainActivity.this);
        bt.execute(method , recent_token, android_id);
    }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main_menu, menu);
     /* menu.getItem(0).getSubMenu().getItem(3).setVisible(false);
      menu.getItem(0).getSubMenu().getItem(4).setVisible(true);*/
      return super.onCreateOptionsMenu(menu);
  }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        else  if (item.getItemId() == R.id.notification)
        {
            startActivity(new Intent(MainActivity.this, Notifications_Activity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        else if (item.getItemId()==R.id.theme)
        {
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.theme_dailogue, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            CircleImageView theme_blue = (CircleImageView) popupView.findViewById(R.id.theme_blue);
            theme_blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settings = getSharedPreferences(PREFS_NAME, 0);
                    editor = settings.edit();
                    editor.remove("color");
                    editor.putString("color", "BLUE");
                    editor.commit();
                    setColor("BLUE");
                    popupWindow.dismiss();

                }
            });
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

            CircleImageView theme_green = (CircleImageView) popupView.findViewById(R.id.theme_green);
            theme_green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    settings = getSharedPreferences(PREFS_NAME, 0);
                    editor = settings.edit();
                    editor.remove("color");
                    editor.putString("color", "GREEN");
                    editor.commit();
                    setColor("GREEN");
                    popupWindow.dismiss();

                }
            });

            CircleImageView theme_sky_blue = (CircleImageView) popupView.findViewById(R.id.theme_sky_blue);
            theme_sky_blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settings = getSharedPreferences(PREFS_NAME, 0);
                    editor = settings.edit();
                    editor.remove("color");

                    editor.putString("color", "SKYBLUE");
                    editor.commit();
                    setColor("SKYBLUE");
                    popupWindow.dismiss();


                }
            });

            CircleImageView theme_dark_orange = (CircleImageView) popupView.findViewById(R.id.theme_dark_orange);
            theme_dark_orange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settings = getSharedPreferences(PREFS_NAME, 0);
                    editor = settings.edit();
                    editor.remove("color");

                    editor.putString("color", "ORANGE");
                    editor.commit();
                    setColor("ORANGE");
                    popupWindow.dismiss();


                }
            });
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.update();
        }
        else if (item.getItemId()==R.id.shareApp)
        {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody1 = "https://play.google.com/store/apps/details?id=onlineind.com&hl=en";
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody1);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else if (item.getItemId()==R.id.rateUs)
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=onlineind.com&hl=en"));
            startActivity(i);
        }
        else if (item.getItemId()==R.id.feedback)
        {
            String myVersion = android.os.Build.VERSION.RELEASE;
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            String deviceName = android.os.Build.MODEL;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            String shareBody = "Version = " + (myVersion) + "\n" +
                    "Sdk = " + (sdkVersion) + "\n" +
                    "Model = " + (deviceName);
            intent.putExtra(Intent.EXTRA_TEXT,  Html.fromHtml(shareBody.toString()));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"OnlineCGnews@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.setType("message/rfc822");
            //intent.setType("text/plain");
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
                // finish();
            } catch (android.content.ActivityNotFoundException ex) {
                // Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        color_code = settings.getString("color", "");
        if (id == R.id.rajya) {

            title = "राज्य";
            isHome = false;
            isOther = true;
            myFragment = new Fragment_Rajya();
            callfragment(myFragment);
           // settoolbar(color_code);
        } else if (id == R.id.desh_videsh) {
            isHome = false;
            isOther = true;
            title = "देश-विदेश";
            myFragment = new Fragment_DeshVidesh();
            callfragment(myFragment);

            //settoolbar(color_code);

        } else if (id == R.id.rajniti) {
            isHome = false;
            isOther = true;
            title = "राजनीति";
            myFragment = new Fragment_Rajniti();
            callfragment(myFragment);
           // settoolbar(color_code);

        } else if (id == R.id.khel) {
            isHome = false;
            isOther = true;
            title = "खेल";
            myFragment = new Fragment_Khel();
            callfragment(myFragment);
           // settoolbar(color_code);

        } else if (id == R.id.taknik) {
            isHome = false;
            isOther = true;
            title = "तकनीक";
            myFragment = new Fragment_Takniki();
            callfragment(myFragment);
          //  settoolbar(color_code);

        } else if (id == R.id.vyapar) {
            isHome = false;
            isOther = true;
            title = "व्यापार";
            myFragment = new Fragment_Vyapar();
            callfragment(myFragment);
          //  settoolbar(color_code);

        } else if (id == R.id.manoranjan) {
            isHome = false;
            isOther = true;
            title = "मनोरंजन";
            myFragment = new Fragment_Manoranjan();
            callfragment(myFragment);
          //  settoolbar(color_code);

        } else if (id == R.id.health) {
            isHome = false;
            isOther = true;
            title = "हेल्थ";
            myFragment = new Fragment_Health();
            callfragment(myFragment);
          //  settoolbar(color_code);

        } else if (id == R.id.lifestyle) {
            isHome = false;
            isOther = true;
            title = "लाइफ स्टाइल";
            myFragment = new Fragment_LifeStyle();
            callfragment(myFragment);
          //  settoolbar(color_code);

        } else if (id == R.id.astha) {
            isHome = false;
            isOther = true;
            title = "आस्था";
            myFragment = new Fragment_Astha();
            callfragment(myFragment);
          //  settoolbar(color_code);

        } else if (id == R.id.trending) {
            isHome = false;
            isOther = true;
            title = "ट्रेंडिंग";
            myFragment = new Fragment_Trending();
            callfragment(myFragment);
         //   settoolbar(color_code);


        }
        drawer = (DrawerLayout) findViewById(R.id.drwar);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onRefresh() {

    }

    class MyTab extends FragmentPagerAdapter {


        public MyTab(FragmentManager fm) {
            super(fm);
        }

        @Override
        public  android.support.v4.app.Fragment getItem(final int position) {
            android.support.v4.app.Fragment fragments = null;

            switch (position) {
                case 0:
                    return new Fragment_Home();
                case 1:
                    return new Fragment_Rajya();
                case 2:
                    return new Fragment_DeshVidesh();
                case 3:
                    return new Fragment_Rajniti();
                case 4:
                    return new Fragment_Khel();
                case 5:
                    return new Fragment_Takniki();
                case 6:
                    return new Fragment_Vyapar();
                case 7:
                    return new Fragment_Manoranjan();
                case 8:
                    return new Fragment_Health();
                case 9:
                    return new Fragment_LifeStyle();
                case 10:
                    return new Fragment_Astha();
                case 11:
                    return new Fragment_Trending();
            }

            return fragments;
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    discription_titel = "मुख्य ख़बरें ";
                    return discription_titel;
                case 1:
                    return "राज्य";
                case 2:
                    return "देश-विदेश";
                case 3:
                    return "राजनीति";
                case 4:
                    return "खेल";
                case 5:
                    return "तकनीक";
                case 6:
                    return "व्यापार";
                case 7:
                    return "मनोरंजन";
                case 8:
                    return "हेल्थ";
                case 9:
                    return "लाइफ स्टाइल";
                case 10:
                    return "आस्था";
                case 11:
                    return "ट्रेंडिंग";
            }
            return super.getPageTitle(position);
        }
    }


    public void callfragment(Fragment myFragment) {

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#042B7A\">" + title + "</font>")));
        drawerToggle.setDrawerIndicatorEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.Blue), PorterDuff.Mode.SRC_ATOP);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.more));
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

      /*  toolbar.setBackgroundColor(getResources().getColor(R.color.Blue));
        toolbar.setBackgroundColor(getResources().getColor(R.color.Green));
        toolbar.setBackgroundColor(getResources().getColor(R.color.DarkOrange));
        toolbar.setBackgroundColor(getResources().getColor(R.color.SkyBlue));*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
       // logo.setVisibility(GONE);
        logo1.setVisibility(GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);

        myFragment.setArguments(bundle);
        ft.replace(R.id.main_frame, myFragment);
        ft.addToBackStack(null);
        ft.commit();

    }


    private void setColor(String color_code) {
        if (color_code.equals("BLUE")) {
            home.setBackgroundColor(getResources().getColor(R.color.Blue));
            videos.setBackgroundColor(getResources().getColor(R.color.Blue));
            profile.setBackgroundColor(getResources().getColor(R.color.Blue));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.Light_Blue));
        } else if (color_code.equals("GREEN")) {
            home.setBackgroundColor(getResources().getColor(R.color.Green));
            videos.setBackgroundColor(getResources().getColor(R.color.Green));
            profile.setBackgroundColor(getResources().getColor(R.color.Green));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.LightGreen));
        } else if (color_code.equals("ORANGE")) {
            home.setBackgroundColor(getResources().getColor(R.color.DarkOrange));
            videos.setBackgroundColor(getResources().getColor(R.color.DarkOrange));
            profile.setBackgroundColor(getResources().getColor(R.color.DarkOrange));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.LightOrange));

        } else if (color_code.equals("SKYBLUE")) {
            home.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
            videos.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
            profile.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.LightSkyBlue));
        }

    }





    private boolean key = false;
    @Override
    public void onBackPressed() {
        if (isOther){
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            //  Toast.makeText(this, "isOther", Toast.LENGTH_SHORT).show();
           // logo.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }else if (isHome) {
            //  Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public class BackGroundTask extends AsyncTask<String, Void, String> {
        Context ctx;

        public BackGroundTask(MainActivity ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            // Toast.makeText(ctx, "Token Submit", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            String method = params[0];
            if (method.equals("submit")) {

                String recent_token = params[1];
                String android_id = params[2];
                String data;

                try {

                    URL url = new URL(TOKENREGISTER);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
                    data = URLEncoder.encode("tbl_tokens_token", "UTF-8") + "=" + URLEncoder.encode(recent_token, "UTF-8") + "&" +
                            URLEncoder.encode("tbl_android_id", "UTF-8") + "=" + URLEncoder.encode(android_id, "UTF-8") ;
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
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            // Toast.makeText(ctx, "" + result, Toast.LENGTH_SHORT).show();
        }
    }



}
