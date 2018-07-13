package project.internship.tcs.collegeattendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Attendance extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner course,branch,year;
    EditText section;
    com.beardedhen.androidbootstrap.BootstrapButton attendance,cancel;
    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    LinearLayout fab;
    LinearLayout linearLayout;
    ArrayList<String> filepath;
    String sectiontext,coursetext,branchtext,yeartext;
    ProgressBar progressBar;
    JSONArray jsonArray;
    String[]name,rollno,mobileno,pic,att;
    ProgressDialog dialog;


    @SuppressLint("ValidFragment")
    public Attendance(FrameLayout frameLayout, DrawerLayout drawerLayout, LinearLayout floatingActionButton){
        this.frameLayout=frameLayout;
        this.drawerLayout=drawerLayout;
        this.fab=floatingActionButton;
    }
    public Attendance(){}

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.attendance,container,false);
        recognize(view);
        bind();
        return view;
    }
    private void bind() {
    attendance.setOnClickListener(this);
    course.setOnItemSelectedListener(this);
    cancel.setOnClickListener(this);
}

    private void recognize(View view) {
        course=view.findViewById(R.id.course);
        branch=view.findViewById(R.id.branch);
        section=view.findViewById(R.id.section);
        attendance=view.findViewById(R.id.attendance);
        cancel=view.findViewById(R.id.cancel);
        linearLayout=view.findViewById(R.id.attendancemain);
        filepath=new ArrayList<String>();
        progressBar=view.findViewById(R.id.progress);
        year=view.findViewById(R.id.year);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.attendance){
            sectiontext=section.getText().toString().trim();
            coursetext=course.getSelectedItem().toString().trim();
            branchtext=branch.getSelectedItem().toString().trim();
            yeartext=year.getSelectedItem().toString().trim();
            if(TextUtils.isEmpty(sectiontext)){
                section.setError("Enter Section");
                section.requestFocus();
            }
            else{
                checkclass();
            }
        }
        if(v.getId()==R.id.cancel){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            fab.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void checkclass() {
        showProgress(true);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.URL_check_section, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                showProgress(false);
                if(response.contains("errorincheckingsection")){
                    if(response.equalsIgnoreCase("errorincheckingsection01")) {
                        AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                        builder.setMessage("No such Class found.");
                        builder.show();
                    }
                    if(response.equalsIgnoreCase("errorincheckingsection02")) {
                        AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                        builder.setMessage("Error in server. Retry after sometime");
                        builder.show();
                    }
                    if(response.equalsIgnoreCase("errorincheckingsection03")) {
                        AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                        builder.setMessage("Could not connect to server. Retry.");
                        builder.show();
                    }
                }
                else{
                    try {
                         jsonArray=new JSONArray(response);
                        name=new String[jsonArray.length()];
                        rollno=new String[jsonArray.length()];
                        mobileno=new String[jsonArray.length()];
                        pic=new String[jsonArray.length()];
                        att=new String[jsonArray.length()];
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            name[i]=jsonObject.getString("name");
                            rollno[i]=jsonObject.getString("rollno");
                            mobileno[i]=jsonObject.getString("mobileno");
                            pic[i]=jsonObject.getString("pic");
                            att[i]=jsonObject.getString("attendance");
                        }
                            Intent intent = new Intent(getActivity(), TakeAttendance.class);
                            intent.putExtra("name", name);
                            intent.putExtra("rollno", rollno);
                            intent.putExtra("mobileno", mobileno);
                            intent.putExtra("pic", pic);
                            intent.putExtra("attendance", att);
                            intent.putExtra("course",coursetext);
                            intent.putExtra("branch",branchtext);
                            intent.putExtra("year",yeartext);
                            intent.putExtra("section",sectiontext);
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fade_in, R.anim.fade_out);
                            startActivity(intent, options.toBundle());
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
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("No Internet Connection");
                    alertDialog.setIcon(R.mipmap.ic_launcher_round);
                    alertDialog.setTitle(Html.fromHtml("<font color='#FF0000'>College Management App</font>"));
                    alertDialog.show();
                }
                else {
                    AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
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
                params.put("course",coursetext);
                params.put("branch",branchtext);
                params.put("section",sectiontext);
                params.put("year",yeartext);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
/**
 * Fires an intent to spin up the "file chooser" UI and select an image.
 */



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner=(Spinner)parent;
        if(spinner.getId()==R.id.course){
            String b = course.getSelectedItem().toString();
            int rid = getResources().getIdentifier(b, "array", getContext().getPackageName());
            String temp[] = getResources().getStringArray(rid);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, temp);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            branch.setAdapter(spinnerArrayAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }
    private void showProgress(final boolean show) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
    }

}
