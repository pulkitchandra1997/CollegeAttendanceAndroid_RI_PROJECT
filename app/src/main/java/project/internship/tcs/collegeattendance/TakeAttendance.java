package project.internship.tcs.collegeattendance;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeAttendance extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    String[] name, rollno, mobileno, pic, attendance;
    WifiManager mWifiManager;
    ArrayList<String> absent=new ArrayList<String>();
    ArrayList<STUDENTATTENDANCE> STUDENTATTENDANCE =new ArrayList<STUDENTATTENDANCE>();
    ListView listView;
    boolean flag=false;
    ATTENDANCERESULT attendanceresult;
    Button finish,confirm;
    String finalrollno="",finalattendance="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        Intent intent = getIntent();
        finish=findViewById(R.id.finish);
        listView=findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        confirm=findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        finish.setOnClickListener(this);
        if (intent != null) {
            name = intent.getStringArrayExtra("name");
            rollno = intent.getStringArrayExtra("rollno");
            mobileno = intent.getStringArrayExtra("mobileno");
            pic = intent.getStringArrayExtra("pic");
            attendance = intent.getStringArrayExtra("attendance");
            for (String roll:rollno)
                absent.add(roll);
        }
        getWifi();
    }

    private void getWifi() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!mWifiManager.isWifiEnabled()) {
            flag=false;
            mWifiManager.setWifiEnabled(true);
        }
        else flag=true;
        mWifiManager.disconnect();
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = mWifiManager.getScanResults();
                EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
                for (ScanResult scanResult : mScanResults) {
                    try {
                        Log.d("SSID",scanResult.SSID);
                        if(scanResult.SSID.length()==24) {
                            String rollno = encryptDecrypt.decrypt(scanResult.SSID);
                            if (absent.contains(rollno))
                                absent.remove(rollno);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.finish){
            confirm.setVisibility(View.VISIBLE);
            if(!flag)
                mWifiManager.setWifiEnabled(false);
            else{
                mWifiManager.reconnect();
            }
            int i=0;
            for(;i<rollno.length;i++){
                boolean present;
                if(absent.contains(rollno[i]))
                    present=false;
                else
                    present =true;
                STUDENTATTENDANCE.add(new STUDENTATTENDANCE(rollno[i],name[i],attendance[i],pic[i],mobileno[i],present));
            }
            attendanceresult=new ATTENDANCERESULT(this, STUDENTATTENDANCE);
            listView.setAdapter(attendanceresult);
            listView.setVisibility(View.VISIBLE);
        }
        if(v.getId()==R.id.confirm){
            for(int i =0;i<attendance.length;i++){
                if(absent.contains(rollno[i]))
                    attendance[i]=String.valueOf((Double.parseDouble(attendance[i])/(101))*100);
                else
                    attendance[i]=String.valueOf((Double.parseDouble(attendance[i]+1)/(101))*100);
            }
            toServer();
        }
    }

    private void toServer() {
        SharedPreferences sp=getSharedPreferences("college_data",MODE_PRIVATE);
        String empid=sp.getString("empid",null);
        ProgressDialog dialog = ProgressDialog.show(TakeAttendance.this,"","Uploading Attendance...",true);


        for(int i=0;i<rollno.length;i++){
            finalrollno=finalrollno+rollno[i]+"#";
            finalattendance=finalattendance+attendance[i]+"#";
        }
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.URL_updateattendance, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                dialog.dismiss();
                if(response.contains("success"))
                    /*success();*/ {
                    AlertDialog builder = new AlertDialog.Builder(TakeAttendance.this).create();
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                    builder.setMessage("Attendance uploaded successfully");
                    builder.show();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(TakeAttendance.this,TeacherMainActivity.class);
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(TakeAttendance.this, R.anim.fade_in, R.anim.fade_out);
                            startActivity(i,options.toBundle());
                        }
                    },3000);
                }
                else{
                    AlertDialog builder = new AlertDialog.Builder(TakeAttendance.this).create();
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                    builder.setMessage(response);
                    builder.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog.dismiss();
                AlertDialog builder = new AlertDialog.Builder(TakeAttendance.this).create();
                builder.setIcon(R.mipmap.ic_launcher_round);
                builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                builder.setMessage("Error in connection");
                builder.show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("rollno",finalrollno);
                params.put("attendance",finalattendance);
                params.put("empid",empid);
                return params;
            }
        };
        MySingleton.getInstance(TakeAttendance.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        STUDENTATTENDANCE stu= STUDENTATTENDANCE.get(position);
        if(stu.isPresent()){
            String roll=stu.getRollno();
            if(!absent.contains(roll))
                absent.add(roll);

            view.setBackgroundColor(Color.parseColor("#B6B6B4"));
        }
        else{
            if(absent.contains(stu.getRollno()))
                absent.remove(stu.getRollno());
            view.setBackgroundColor(Color.parseColor("#3EA055"));
        }
        int i=0;
        STUDENTATTENDANCE.clear();
        attendanceresult=null;
        listView.setAdapter(null);
        for(;i<rollno.length;i++){
            boolean present;
            if(absent.contains(rollno[i]))
                present=false;
            else
                present =true;
            STUDENTATTENDANCE.add(new STUDENTATTENDANCE(rollno[i],name[i],attendance[i],pic[i],mobileno[i],present));
        }
        attendanceresult=new ATTENDANCERESULT(this, STUDENTATTENDANCE);
        listView.setAdapter(attendanceresult);
    }
}