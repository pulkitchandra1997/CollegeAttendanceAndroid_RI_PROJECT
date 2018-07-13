package project.internship.tcs.collegeattendance;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

/**
 * A login screen that offers login via email/password.
 */
public class TeacherRegisterActivity extends AppCompatActivity implements OnClickListener, DialogInterface.OnClickListener {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mConfirmView;
    private String empid, mobileno;
    SharedPreferences sp;
    SharedPreferences.Editor se;
    String model;
    com.beardedhen.androidbootstrap.BootstrapButton proceed;
    JSONObject jsonObject = null;
    ArrayList<String> num = new ArrayList<String>();
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        com.beardedhen.androidbootstrap.BootstrapButton mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mConfirmView = findViewById(R.id.confirm);
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent=new Intent(this,Permisssion.class);
            startActivity(intent);
        }
        model = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName() + "##" + telephonyManager.getDeviceId();
        sp=getSharedPreferences("college_data",MODE_PRIVATE);
        se=sp.edit();
        proceed=findViewById(R.id.proceed);
        proceed.setOnClickListener(this);
    }

    private void attemptLogin() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);
        empid = mEmailView.getText().toString();
        mobileno = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(mobileno) && !isPasswordValid(mobileno)) {
            mPasswordView.setError("Invalid Mobile Number");
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(empid)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel)
            focusView.requestFocus();
        else
            toserver();

    }


    private boolean isPasswordValid(String password) {
        return password.length() > 9;
    }

    private void showProgress(final boolean show) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
    }
    private void toserver() {
        showProgress(true);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.URL_teacher_register, new Response.Listener<String>()
        {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(String response) {
                showProgress(false);
                if (response.toLowerCase().contains("error")) {
                    if(response.toLowerCase().contains("error01")){
                        AlertDialog builder = new AlertDialog.Builder(TeacherRegisterActivity.this).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                        builder.setMessage("Incorrect Mobile Number");
                        builder.show();
                    }
                    if(response.toLowerCase().contains("error02")){
                        AlertDialog builder = new AlertDialog.Builder(TeacherRegisterActivity.this).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                        builder.setMessage("Incorrect Emp ID");
                        builder.show();
                    }
                    if(response.toLowerCase().contains("error03")){
                        AlertDialog builder = new AlertDialog.Builder(TeacherRegisterActivity.this).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                        builder.setMessage("Internet Error.Check Connection!");
                        builder.show();
                    }
                } else {
                    try {
                        jsonObject=new JSONObject(response);
                        mConfirmView.setVisibility(View.VISIBLE);
                        mLoginFormView.setVisibility(View.INVISIBLE);
                        tosharedpreference();
                        fillout();
                        imageProcess();
                        if(jsonObject.getString("model")!=null&&jsonObject.getString("model")!="") {
                            if(!jsonObject.getString("model").equals(model)) {
                                TextView modelchange = findViewById(R.id.modelchange);
                                modelchange.setText("You were already logged in another mobile. You will have no access through previous mobile.");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                showProgress(false);
                boolean haveConnectedWifi = false;
                boolean haveConnectedMobile = false;
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected())
                            haveConnectedWifi = true;
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected())
                            haveConnectedMobile = true;
                }
                if( !haveConnectedWifi && !haveConnectedMobile)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(TeacherRegisterActivity.this).create();
                    alertDialog.setMessage("No Internet Connection");
                    alertDialog.setIcon(R.mipmap.ic_launcher_round);
                    alertDialog.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                    alertDialog.show();
                }
                else {
                    AlertDialog builder = new AlertDialog.Builder(TeacherRegisterActivity.this).create();
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                    builder.setMessage("Connection error! Retry");
                    builder.show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("empid",empid);
                params.put("mobileno",mobileno);
                params.put("model",model);
                return params;
            }
        };
        MySingleton.getInstance(TeacherRegisterActivity.this).addToRequestQueue(stringRequest);
    }

    private void fillout() {
        TextView mobileno,dob,address,city,state,pincode,employeeid,name,gender,position;
        employeeid=findViewById(R.id.empid);
        name=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        position=findViewById(R.id.position);
        mobileno=findViewById(R.id.mobileno);
        dob=findViewById(R.id.dob);
        address=findViewById(R.id.address);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        pincode=findViewById(R.id.pincode);
            employeeid.setText(sp.getString("empid",null));
            name.setText(sp.getString("firstname",null)+" "+sp.getString("lastname",null));
            gender.setText(sp.getString("gender",null));
            position.setText(sp.getString("position",null));
            mobileno.setText(sp.getString("mobileno",null));
            dob.setText(sp.getString("dob",null));
            address.setText(sp.getString("address",null));
            city.setText(sp.getString("city",null));
            state.setText(sp.getString("state",null));
            pincode.setText(sp.getString("pincode",null));
    }
    private void tosharedpreference() {
        try {
            se.putString("empid", empid);
            se.putString("firstname", jsonObject.getString("firstname"));
            se.putString("lastname", jsonObject.getString("lastname"));
            se.putString("mobileno", mobileno);
            if(jsonObject.getString("gender")=="1")
                se.putString("gender", "Male");
            else
                se.putString("gender", "Female");
            se.putString("dob", jsonObject.getString("dob"));
            se.putString("city",jsonObject.getString("city"));
            se.putString("address", jsonObject.getString("address"));
            se.putString("pincode", jsonObject.getString("pincode"));
            se.putString("state",jsonObject.getString("state"));
            se.putString("email", jsonObject.getString("email"));
            se.putString("position", jsonObject.getString("position"));
            se.putString("email",jsonObject.getString("email"));
            if(jsonObject.getString("pic")!=null&&jsonObject.getString("pic")!="")
                se.putString("pic",jsonObject.getString("pic"));
            else
                se.putString("pic",null);
            se.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.proceed){
            Intent intent=new Intent(TeacherRegisterActivity.this,TeacherMainActivity.class);             ActivityOptions options = ActivityOptions.makeCustomAnimation(TeacherRegisterActivity.this, R.anim.fade_in, R.anim.fade_out);
                startActivity(intent, options.toBundle());
        }
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which!=-3) {
            if (which == -1)
                mobileno = num.get(0);
            if (which == -2)
                mobileno = num.get(1);
            mPasswordView.setText(mobileno);
            toserver();
        }
    }

    private void imageProcess() {
        if (sp.getString("pic", null) != null) {
            Picasso.with(this).load(sp.getString("pic", null)).into(picassoImageTarget(getApplicationContext(), "imageDir", "profilepic.jpeg"));
        }
    }
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 40, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }
}

