package project.internship.tcs.collegeattendance;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class Welcome extends Activity {

    SharedPreferences sp;
    SharedPreferences.Editor se;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sp = getSharedPreferences("college_data", MODE_PRIVATE);
        se = sp.edit();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(checkPermission()) {
                    if (sp != null)
                        checkstorage();
                    launch();
                }
                else{
                    intent=new Intent(Welcome.this,Permisssion.class);
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(Welcome.this, R.anim.fade_in, R.anim.fade_out);
                        startActivityForResult(intent,121, options.toBundle());
                }
            }
        },3000);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==resultCode){
            if(checkPermission()) {
                if (sp != null)
                    checkstorage();
                launch();
            }
            else{
                intent=new Intent(Welcome.this,Permisssion.class);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(Welcome.this, R.anim.fade_in, R.anim.fade_out);
                    startActivityForResult(intent,121, options.toBundle());
            }
        }
    }

    //PERMISSION CHECK AND ASK FOR IT
        private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
            int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
            int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
            boolean result6 = Settings.System.canWrite(getApplication());
            boolean granted = false;
            AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), this.getPackageName());
            if (mode == AppOpsManager.MODE_DEFAULT) {
                granted = (this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            } else {
                granted = (mode == AppOpsManager.MODE_ALLOWED);
            }
            if (result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED&& result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED&&result6&&granted) {
            return true;
        } else
            return false;
    }



    private void launch() {
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(Welcome.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);
    }

    private void checkstorage() {
        String rollno=sp.getString("rollno",null);
        if(rollno!=null)
            intent=new Intent(Welcome.this,StudentMainActivity.class);
        else {
            String id = sp.getString("empid", null);
            if (id != null)
                intent = new Intent(Welcome.this, TeacherMainActivity.class);
            else
                intent=new Intent(Welcome.this,LoginActivity.class);

        }
    }

}
