package project.internship.tcs.collegeattendance;

import android.app.ActivityOptions;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class Permisssion extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisssion);
    }
    @Override
    public void onStart() {
        super.onStart();
        check();
    }
    private void check() {
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
            Intent intent=new Intent(Permisssion.this,Welcome.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Permisssion.this, R.anim.fade_in, R.anim.fade_out);
                startActivity(intent, options.toBundle());
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        check();
    }
    @Override
    protected void onResume() {
        super.onResume();
        check();
    }

    public void grantpermission(View view) {
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
            Intent intent=new Intent(Permisssion.this,Welcome.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Permisssion.this, R.anim.fade_in, R.anim.fade_out);
                startActivity(intent, options.toBundle());
        }
        else
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE,READ_PHONE_STATE,WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readPhoneStae = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean accessFineLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean accessCourseLocation = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean granted = false;
                    AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
                    int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                            android.os.Process.myUid(), this.getPackageName());
                    if (mode == AppOpsManager.MODE_DEFAULT) {
                        granted = (this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
                    } else {
                        granted = (mode == AppOpsManager.MODE_ALLOWED);
                    }
                    if (readExternalStorage&&readPhoneStae&&writeExternalStorage&&accessFineLocation&&accessCourseLocation&&Settings.System.canWrite(getApplication())&&granted)
                    {
                        Intent intent=new Intent(Permisssion.this,Welcome.class);
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(Permisssion.this, R.anim.fade_in, R.anim.fade_out);
                            startActivity(intent, options.toBundle());

                    }
                    else {
                        if (readExternalStorage&&readPhoneStae&&writeExternalStorage&&accessFineLocation&&accessCourseLocation) {
                                    if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                        showMessageOKCancel("You need to allow access to all the permissions",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                                                                    PERMISSION_REQUEST_CODE);
                                                        }
                                                    }
                                                });
                                    }
                        }
                            if(!Settings.System.canWrite(this)) {
                                new AlertDialog.Builder(Permisssion.this)
                                        .setMessage("Grant Special Permission\nFind and click on College Management ->\nMark Yes to Allow modify system settings.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                startActivityForResult(intent,121);
                                            }
                                        })
                                        .setIcon(R.mipmap.ic_launcher_round)
                                        .setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"))
                                        .create()
                                        .show();
                            }
                        if(!granted){
                            new AlertDialog.Builder(Permisssion.this)
                                    .setMessage("Grant Special Permission for Monitoring Apps\nFind and click on College Management ->\nMark Yes to Permit Usage access.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                            startActivityForResult(intent,121);
                                        }
                                    })
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"))
                                    .create()
                                    .show();
                        }
                    }
                }
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Permisssion.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"))
                .create()
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==resultCode){
            Intent intent1=new Intent(this,Welcome.class);
            setResult(121,intent1);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent1=new Intent(this,Welcome.class);
        setResult(121,intent1);
        finish();
            }
        }
