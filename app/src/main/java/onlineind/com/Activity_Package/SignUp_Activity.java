package onlineind.com.Activity_Package;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.SIGNUP;


/**
 * Created by admin on 9/7/2017.
 */

public class SignUp_Activity extends AppCompatActivity  {
    TextView txt_signin,txt_male,txt_female;
    Spinner edt_user_gender;
    Button btn_signup;
    String user_name, user_gender, user_city, user_email, user_password,str_edt_user_confirmpass;
    EditText edt_user_name, edt_user_city, edt_user_password,edt_user_confirmpass;
    AppCompatEditText edt_user_email;
    LinearLayout img_signup_back;
    boolean temp=true;
    String strGender;
    String gender[]={"Select Gender","Male","Female"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        txt_signin = (TextView) findViewById(R.id.txt_signin);
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp_Activity.this, LogIn_Activity.class);
                startActivity(intent);
            }
        });
        img_signup_back = (LinearLayout) findViewById(R.id.img_singup_back);
        img_signup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        edt_user_gender = (Spinner) findViewById(R.id.edt_user_gender);
       // edt_user_gender.setOnClickListener(SignUp_Activity.this);
        edt_user_city = (EditText) findViewById(R.id.edt_user_city);
        edt_user_email = (AppCompatEditText) findViewById(R.id.edt_user_email);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        edt_user_password = (EditText) findViewById(R.id.edt_user_password);
        edt_user_confirmpass = (EditText) findViewById(R.id.edt_user_confirmpass);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUp_Activity.this,android.R.layout.simple_list_item_1, gender);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        edt_user_gender.setAdapter(dataAdapter);
        edt_user_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                strGender=gender[i];

                if (strGender.equals("Select Gender"))
                {
                }
                else
                {
                    user_gender = strGender;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_name = edt_user_name.getText().toString().trim();
                user_gender = strGender;
                user_city = edt_user_city.getText().toString().trim();
                user_email = edt_user_email.getText().toString().trim();
                str_edt_user_confirmpass = edt_user_confirmpass.getText().toString().trim();
                user_password = edt_user_password.getText().toString().trim();

                if (edt_user_name.getText().toString().length() == 0) {
                    edt_user_name.setError("Please Enter Name");
                    edt_user_name.requestFocus();
                }
                else if (!checkEmailValidity(edt_user_email.getText().toString())) {
                    edt_user_email.setError("Please Enter Valid Email!");
                    edt_user_name.requestFocus();
                }
                else if (edt_user_city.getText().toString().length() == 0) {
                    edt_user_city.setError("Please Enter City Name");
                    edt_user_city.requestFocus();
                }
                else if (edt_user_password.getText().toString().length() == 0) {
                    edt_user_password.setError("Password not entered");
                    edt_user_password.requestFocus();
                }
                else if (edt_user_confirmpass.getText().toString().length() == 0) {
                    edt_user_confirmpass.setError("Please confirm password");
                    edt_user_confirmpass.requestFocus();
                }
                else if (!edt_user_password.getText().toString().equals(edt_user_confirmpass.getText().toString())) {
                    edt_user_confirmpass.setError("Password Not matched");
                    edt_user_confirmpass.requestFocus();
                }
                else if (edt_user_password.getText().toString().length() < 8) {
                    edt_user_password.setError("Password should be atleast of 8 characters");
                    edt_user_password.requestFocus();
                } else {
                   // Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    String method = "submit";
                    BackGroundTask bt = new BackGroundTask(SignUp_Activity.this);
                    bt.execute(method, user_name, user_gender, user_city, user_email, user_password);
                }
            }
        });
    }

    public boolean checkEmailValidity(String emailFormat){

        String getEmail = edt_user_email.getText().toString();
        boolean getEnd;

        //CHECK STARTING STRING IF THE USER
        //entered @gmail.com / @yahoo.com / @outlook.com only
        boolean getResult = !TextUtils.isEmpty(getEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail).matches();

        //CHECK THE EMAIL EXTENSION IF IT ENDS CORRECTLY
        if (getEmail.endsWith("@gmail.com") || getEmail.endsWith("@yahoo.com") || getEmail.endsWith("@outlook.com")){
            getEnd = true;
        }else {
            getEnd = false;
            String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = emailFormat;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);

            return matcher.matches();

        }
        //TEST THE START AND END
        return (getResult && getEnd);
    }

   /* public void onClick(View v)
    {
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.gender_dailog, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        txt_male = (TextView)popupView.findViewById(R.id.txt_male);
        txt_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_user_gender.setText(txt_male.getText().toString());
                popupWindow.dismiss();
            }
        });
        txt_female = (TextView)popupView.findViewById(R.id.txt_female);
        txt_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_user_gender.setText(txt_female.getText().toString());
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY,1,1);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.update();

    }*/

    public class BackGroundTask extends AsyncTask<String, Void, String> {
        Context ctx;
        private ProgressDialog pDialog;

        public BackGroundTask(Context ctx) {
            this.ctx = ctx;
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

            String method = params[0];
            if (method.equals("submit")) {

                String str_name = params[1];
                String str_gender = params[2];
                String str_city = params[3];
                String str_email = params[4];
                String str_password = params[5];
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
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hidePDialog();
           // Toast.makeText(ctx, "" + result, Toast.LENGTH_SHORT).show();
            Toast.makeText(ctx, "You are successfully registered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUp_Activity.this,LogIn_Activity.class));
            finish();
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }
    }
}