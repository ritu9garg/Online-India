package onlineind.com.fragment_package;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import onlineind.com.Activity_Package.Likes_Activity;
import onlineind.com.Activity_Package.LogIn_Activity;
import onlineind.com.Activity_Package.MyList_Activity;
import onlineind.com.R;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.app.Activity.RESULT_OK;


public class Fragment_Profile extends Fragment {
    View view;
    Context context;
    Button btn_login, btn_logout;
    TextView camera, gallery;
    AlertDialog ab;
    PhotoViewAttacher pAttacher;
    String google_singin_name, google_singin_email, google_singin_img, tbl_user_image, tbl_user_name, tbl_user_email, facebook_singin_name, facebook_singin_email, facebook_singin_img;
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String MyPREFERENCES = "MyPre";


    private static int CAMERA_REQUEST = 1;
    SharedPreferences settingsMyPre;
    SharedPreferences settings,settings_color;
    public static final String PREFS_NAME_COLOR = "ThemePrefs";
    String color_code;
    SharedPreferences.Editor editorMyPre;
    SharedPreferences.Editor editor, editor_color;

    LinearLayout pro_layout;
    private static final int CAMERA_PHOTO = 111;
    TextView  profile_name, profile_email;
    LinearLayout likes,my_list;
    CircleImageView profile_img;
    //camera
    LayoutInflater li;
    View promptsView;
    Bitmap bitmap;
    Animation myAnim;
    AlertDialog.Builder alertDialogBuilder;
    String prof;
    String str_user_id,android_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__profile, container, false);

        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_email = (TextView) view.findViewById(R.id.profile_email);
        profile_img = (CircleImageView) view.findViewById(R.id.profileeeeeeee);
        pro_layout = (LinearLayout) view.findViewById(R.id.pro_layout);

        settings_color = getActivity().getSharedPreferences(PREFS_NAME_COLOR, 0);
        editor_color = settings_color.edit();

        color_code = settings_color.getString("color", "");

        //profile
        settings = view.getContext().getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        str_user_id = settings.getString("tbl_user_id","");


        settingsMyPre = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editorMyPre= settingsMyPre.edit();


        setColor(color_code);

        if (color_code.equals("BLUE")) {
            setColor("BLUE");

        } else if (color_code.equals("GREEN")) {
            setColor("GREEN");

        } else if (color_code.equals("ORANGE")) {
            setColor("ORANGE");

        } else if (color_code.equals("SKYBLUE")) {
            setColor("SKYBLUE");
        }



        prof = settingsMyPre.getString("PROFILE", "");


        google_singin_name = settings.getString("google_singin_name", null);
      //  Toast.makeText(context, ""+google_singin_name, Toast.LENGTH_SHORT).show();
        google_singin_email = settings.getString("google_singin_email", null);
        google_singin_img = settings.getString("google_singin_img", null);

        facebook_singin_name = settings.getString("facebook_singin_name", null);
        facebook_singin_email = settings.getString("facebook_singin_email", null);
        facebook_singin_img = settings.getString("facebook_singin_img", null);


        tbl_user_name = settings.getString("tbl_user_name", null);
        tbl_user_email = settings.getString("tbl_user_email", null);
        tbl_user_image = settings.getString("tbl_user_image", null);

        profile_img.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (profile_name.getText().toString().trim().equals("User Name")) {}
                   else {
                       alertDialogBuilder = new AlertDialog.Builder(getActivity());
                      li = LayoutInflater.from(getActivity());
                       promptsView = li.inflate(R.layout.profile_img_alertdailog, null,false);

                       alertDialogBuilder.setTitle("Select option");
                       gallery = (TextView) promptsView.findViewById(R.id.gallery);
                       camera = (TextView) promptsView.findViewById(R.id.camera);
                       camera.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                               startActivityForResult(cameraIntent, 1);
                               ab.dismiss();
                           }
                       });
                       gallery.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                               startActivityForResult(i, CAMERA_REQUEST);
                               ab.dismiss();
                           }
                       });
                       alertDialogBuilder.setView(promptsView);
                       alertDialogBuilder.setCancelable(false);
                       ab = alertDialogBuilder.create();
                       ab.show();
                       ab.setCanceledOnTouchOutside(true);
                   }
               }});


        my_list = (LinearLayout) view.findViewById(R.id.my_list);
        my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyList_Activity.class);
                getActivity().startActivity(intent);
            }});

        likes = (LinearLayout) view.findViewById(R.id.likes);
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!str_user_id.isEmpty()) {

                    Intent intent = new Intent(getActivity(), Likes_Activity.class);
                    getActivity().startActivity(intent);

                }else {
                    Toast.makeText(v.getContext(), "Please Register Your Self..", Toast.LENGTH_SHORT).show();

                }
            }});

        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.milkshake);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setAnimation(myAnim);




        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim);
                if ("fblogin".equals("fblogin")) {
                    LoginManager.getInstance().logOut();
                    editor = settings.edit();
                        editor.clear();
                    editor.apply();


                    btn_logout.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    profile_name.setText("User Name");
                    profile_email.setText("Email");
                    profile_img.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                }
               else if ("adminlogin".equals("adminlogin")) {
                    editor = settings.edit();
                    editor.remove("tbl_user_name");
                    editor.remove("tbl_user_email");
                    editor.clear();
                    editor.apply();
                    editorMyPre = settingsMyPre.edit();
                    editorMyPre.clear();
                    btn_logout.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    profile_name.setText("User Name");
                    profile_email.setText("Email");
                    profile_img.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                }
            }
        });
        final Animation myAnim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.milkshake);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setAnimation(myAnim1);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim1);
                Intent intent = new Intent(getActivity(), LogIn_Activity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        //camera permision....

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        }
        //

        loadSharedPreferences();
        return view;
    }
      private void loadSharedPreferences() {
            if (google_singin_name != null && !google_singin_name.isEmpty()) {
                profile_name.setText(google_singin_name);
                profile_email.setText(google_singin_email);
                if (settingsMyPre.getString("img_pre","0").equals("1")){
                    bitmap = decodeBase64(prof);
                    profile_img.setImageBitmap(bitmap);
                }else {
                    Glide.with(getActivity()).load(google_singin_img).into(profile_img);
                }
                btn_login.setVisibility(View.GONE);
                btn_logout.setVisibility(View.VISIBLE);

            } else if (facebook_singin_name != null && !facebook_singin_name.isEmpty()) {
                profile_name.setText(facebook_singin_name);
                profile_email.setText(facebook_singin_email);
                if (settingsMyPre.getString("img_pre","0").equals("1")){
                    bitmap = decodeBase64(prof);
                    profile_img.setImageBitmap(bitmap);
                }else {
                    Glide.with(this).load(facebook_singin_img).into(profile_img);
                }
                btn_login.setVisibility(View.GONE);
                btn_logout.setVisibility(View.VISIBLE);
            } else if(tbl_user_name != null && !tbl_user_name.isEmpty()){
                profile_name.setText(tbl_user_name);
                profile_email.setText(tbl_user_email);
                if (!prof.isEmpty()) {
                    bitmap = decodeBase64(prof);
                    profile_img.setImageBitmap(bitmap);
                }

                try {
                    if(tbl_user_image.isEmpty()){

                        Glide.with(getActivity()).load(tbl_user_image).into(profile_img);
                    }
                }catch (Exception e){}

                btn_login.setVisibility(View.GONE);
                btn_logout.setVisibility(View.VISIBLE);
            } else {
                profile_name.setText("User Name");
                profile_email.setText("Email");
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Log.d("String_img", BitMapToString(bitmap));
                editorMyPre.putString("PROFILE", BitMapToString(bitmap));
                editorMyPre.putString("img_pre", "1");
                profile_img.setImageBitmap(bitmap);
                editorMyPre.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(resultCode != 1)
        {
            if (requestCode == CAMERA_REQUEST)
            {
               try {
                   Bitmap photo = (Bitmap) data.getExtras().get("data");

                   editorMyPre.putString("PROFILE", BitMapToString(photo));
                   editorMyPre.putString("img_pre", "1");
                   profile_img.setImageBitmap(photo);
                   editorMyPre.commit();
               }
               catch (Exception e)
               {

               }
            }
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void setColor(String color_code) {
        if (color_code.equals("BLUE")) {
            pro_layout.setBackgroundColor(getResources().getColor(R.color.Light_Light_Blue));
        } else if (color_code.equals("GREEN")) {
            pro_layout.setBackgroundColor(getResources().getColor(R.color.LightGreen));
        } else if (color_code.equals("ORANGE")) {
            pro_layout.setBackgroundColor(getResources().getColor(R.color.LightOrange));

        } else if (color_code.equals("SKYBLUE")) {
            pro_layout.setBackgroundColor(getResources().getColor(R.color.LightSkyBlue));
        }

    }
}