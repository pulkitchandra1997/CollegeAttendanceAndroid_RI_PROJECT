package project.internship.tcs.collegeattendance;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkAttendance extends AppCompatActivity implements View.OnClickListener {

private WifiConfiguration wifiCon;
private HotspotDetails hotspotDetails;
private WifiManager mWifiManager;
String id=null,rollno=null,model;
Boolean wifipre,flag;
Method setWifiApMethod;
LinearLayout base;
TelephonyManager telephonyManager;
ProgressBar mProgressBar;
Button finish;
public MarkAttendance() {
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mark_attendance);
    base=findViewById(R.id.base);
    Intent  intent=getIntent();
    rollno=intent.getStringExtra("rollno");
    mProgressBar=findViewById(R.id.login_progress);
    finish=findViewById(R.id.finish);
    finish.setOnClickListener(this);


    if(rollno!=null){
        EncryptDecrypt encryptDecrypt=new EncryptDecrypt();
        try {
            id=encryptDecrypt.encrypt(rollno);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    flag=false;
    checkMobile();
    if(flag) {
        usage();
        if (!Settings.System.canWrite(getApplicationContext())) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Permission Denied. App wont work. Grant all the permissions.");
            alertDialog.setIcon(R.mipmap.ic_launcher_round);
            alertDialog.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Grant Permission", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(i, 200);
                }
            });

            alertDialog.show();
        } else {
            try {
                createHotspot();
            } catch (Exception e) {
                Snackbar snackbar = Snackbar
                        .make(base, "Error: Open App again", Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
    }
}

private void checkMobile() {
    showProgress(true);
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        Intent intent=new Intent(this,Permisssion.class);
        startActivity(intent);
    }
    model = Build.MANUFACTURER
            + " " + Build.MODEL + " " + Build.VERSION.RELEASE
            + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName() + "##" + telephonyManager.getDeviceId();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_check_mobile_attendance, new Response.Listener<String>() {
        @SuppressLint("HardwareIds")
        @Override
        public void onResponse(String response) {
            showProgress(false);
            if (response.contains("errorincheck")) {
                    AlertDialog builder = new AlertDialog.Builder(MarkAttendance.this).create();
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                    builder.setMessage("Error in checking mobile.");
                    builder.show();

            } else {
                if(response.equals("1"))
                    flag=true;
                else
                    if(response.contains("newmobilefound")){
                    flag=false;
                    String[] newm=response.split("##");

                        AlertDialog builder = new AlertDialog.Builder(MarkAttendance.this).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                        builder.setMessage("Your Account has been logged in from new mobile ("+newm[1]+" DEVICEID: "+newm[2]+" ). Mark your attendance from new mobile. The Data will be cleared.");
                        builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp=getSharedPreferences("college_data",MODE_PRIVATE);
                                sp.edit().clear().commit();
                                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                File MyDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                String path = MyDirectory.getAbsolutePath();
                                File fileToBeDeleted = new File(path + "//Image"+ "profilepic.jpeg"); // current image
                                boolean WasDeleted = fileToBeDeleted.delete();
                                if(!WasDeleted)
                                    System.out.println("ERROR:IMAGE NOT DELETED");
                                Intent intent=new Intent(MarkAttendance.this,Welcome.class);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(MarkAttendance.this, R.anim.fade_in, R.anim.fade_out);
                                startActivity(intent, options.toBundle());
                            }
                        });
                        builder.show();
                    }
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            showProgress(false);
            flag=false;
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
                AlertDialog alertDialog = new AlertDialog.Builder(MarkAttendance.this).create();
                alertDialog.setMessage("No Internet Connection");
                alertDialog.setIcon(R.mipmap.ic_launcher_round);
                alertDialog.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                alertDialog.show();
            }
            else {
                AlertDialog builder = new AlertDialog.Builder(MarkAttendance.this).create();
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
            params.put("rollno",rollno);
            params.put("model",model);
            return params;
        }
    };
    MySingleton.getInstance(MarkAttendance.this).addToRequestQueue(stringRequest);
}

private void usage() {
    final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
    Calendar now = Calendar.getInstance();
    Calendar pre = Calendar.getInstance();
    pre.add(Calendar.HOUR,-1);
    final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, pre.getTimeInMillis(), now.getTimeInMillis());
    System.out.println("results for " + pre.getTime().toString() + " - " + now.getTime().toString());
    for (UsageStats app : queryUsageStats) {
        if(!(app.getPackageName().contains("com.android")||app.getPackageName().contains("google")))
        System.out.println(app.getPackageName() + " | " + (float) (app.getTotalTimeInForeground() / 1000));
    }
}

private void createHotspot() throws InterruptedException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
    mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    if (wifipre=mWifiManager.isWifiEnabled()) {
        mWifiManager.disconnect();
        mWifiManager.setWifiEnabled(false);
    }
    HotspotDetails hotspotDetails = new HotspotDetails();
    hotspotDetails.setSsid(id);
    hotspotDetails.setPassword(rollno.toString());
    setHotspot(hotspotDetails);
}
public boolean setHotspot(HotspotDetails hotspotDetails) {
    this.hotspotDetails = hotspotDetails;
    boolean apstatus;

    wifiCon = new WifiConfiguration();
    wifiCon.SSID = hotspotDetails.getSsid();
    wifiCon.preSharedKey = hotspotDetails.getPassword();
    wifiCon.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    //wifiCon.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
   // wifiCon.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    //wifiCon.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
    wifiCon.allowedKeyManagement.set(4);

    try {
        setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        setWifiApMethod.invoke(mWifiManager, wifiCon, false);
        Thread.sleep(2000);
        apstatus = (Boolean) setWifiApMethod.invoke(mWifiManager, wifiCon, true);
        if(!apstatus){
            AlertDialog builder = new AlertDialog.Builder(this).create();
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
            builder.setMessage("Error in Generating HotSpot. Mark your Attendance manually and report the issue to the teacher.");
            builder.show();
        }
    } catch (Exception e) {
        Log.d("errorhere",e.getMessage());
        return false;
    }
    return apstatus;
}
    boolean doubleBackToExitPressedOnce = false;
@Override
public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
        try {
            setWifiApMethod.invoke(mWifiManager, wifiCon, false);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifipre) {
            mWifiManager.setWifiEnabled(true);
            mWifiManager.reconnect();
        } else if (mWifiManager.isWifiEnabled())
            mWifiManager.setWifiEnabled(false);
        Intent intent = new Intent(this, StudentMainActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(MarkAttendance.this, R.anim.fade_in, R.anim.fade_out);
        startActivity(intent, options.toBundle());
    } else {
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Finish when told by teacher.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}

private void showProgress(final boolean show) {
    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    mProgressBar.animate().setDuration(shortAnimTime).alpha(
            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    });
}

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.finish){
            try {
                setWifiApMethod.invoke(mWifiManager, wifiCon, false);
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(wifipre){
                mWifiManager.setWifiEnabled(true);
                mWifiManager.reconnect();
            }
            else
            if(mWifiManager.isWifiEnabled())
                mWifiManager.setWifiEnabled(false);

            SharedPreferences sp=getSharedPreferences("college_data",MODE_PRIVATE);
            SharedPreferences.Editor se=sp.edit();
            String att=String.valueOf((Double.parseDouble(sp.getString("attendance",null))+1)/(101)*100);
            se.putString("attendance",att);
            se.commit();
            Intent intent=new Intent(this,StudentMainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(MarkAttendance.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(intent, options.toBundle());
        }
    }
}
