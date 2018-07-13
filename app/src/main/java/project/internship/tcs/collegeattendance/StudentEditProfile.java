package project.internship.tcs.collegeattendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class StudentEditProfile extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    EditText mobileno,dob,address,pincode,rollno,name,gender,year,course,branch,section,fathername,attendance,emailid;
    SharedPreferences sp;
    SharedPreferences.Editor se;
    Button editbtn,profilepic;
    ProgressBar progress;
    Spinner city,state;
    JSONObject jsonObject;
    String mobilenotext,addresstext,citytext,statetext,pincodetext;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.studenteditprofile,container,false);
        recognize(view);
        bind();

        sp=getActivity().getSharedPreferences("college_data",MODE_PRIVATE);
        se = sp.edit();

        rollno.setText(sp.getString("rollno",null));
        name.setText(sp.getString("firstname",null)+" "+sp.getString("lastname",null));
        gender.setText(sp.getString("gender",null));
        year.setText(sp.getString("year",null));
        course.setText(sp.getString("course",null));
        branch.setText(sp.getString("branch",null));
        section.setText(sp.getString("section",null));
        mobileno.setText(sp.getString("mobileno",null));
        fathername.setText(sp.getString("fathername",null));
        dob.setText(sp.getString("dob",null));
        address.setText(sp.getString("address",null));
        /*city.setText(sp.getString("city",null));
        state.setText(sp.getString("state",null));*/
        pincode.setText(sp.getString("pincode",null));
        attendance.setText(sp.getString("attendance",null));
        emailid.setText(sp.getString("email",null));
        return view;
    }


    private void recognize(View view) {
        //Find Elements By View.findViewById();
        progress=view.findViewById(R.id.progress);
        editbtn=view.findViewById(R.id.editbtn);
        rollno=view.findViewById(R.id.rollno);
        name=view.findViewById(R.id.name);
        gender=view.findViewById(R.id.gender);
        year=view.findViewById(R.id.year);
        course=view.findViewById(R.id.course);
        branch=view.findViewById(R.id.branch);
        section=view.findViewById(R.id.section);
        mobileno=view.findViewById(R.id.mobileno);
        fathername=view.findViewById(R.id.fathername);
        dob=view.findViewById(R.id.dob);
        address=view.findViewById(R.id.address);
        city=view.findViewById(R.id.city);
        state=view.findViewById(R.id.state);
        pincode=view.findViewById(R.id.pincode);
        attendance=view.findViewById(R.id.attendance);
        profilepic=view.findViewById(R.id.profilepic);
        emailid=view.findViewById(R.id.emailid);

    }
    private void bind() {
        //BindONClick
        city.setEnabled(false);
        state.setAdapter(LinkCities.addStates(getContext()));
        state.setOnItemSelectedListener(this);
        editbtn.setOnClickListener(this);
        profilepic.setOnClickListener(this);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private boolean validate(View view) {

        mobilenotext=mobileno.getText().toString().trim();

        addresstext=address.getText().toString().trim();
        citytext=city.getSelectedItem().toString().trim();
        statetext=state.getSelectedItem().toString().trim();
        pincodetext=pincode.getText().toString().trim();


        if(TextUtils.isEmpty(mobilenotext)||TextUtils.isEmpty(addresstext)||TextUtils.isEmpty(citytext)||TextUtils.isEmpty(statetext)||TextUtils.isEmpty(pincodetext)) {

            if (TextUtils.isEmpty(pincodetext)) {
                pincode.setError("Enter Pincode");
                pincode.requestFocus();
            }
            if (TextUtils.isEmpty(statetext)) {
                Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(citytext)) {
                Toast.makeText(getActivity(), "Select City", Toast.LENGTH_SHORT).show();
            }


            return false;
        }

        else if (!Validation.isValidPhone(mobilenotext)){
            mobileno.setError("Enter Valid 10 digit mobile number");
            mobileno.requestFocus();
            return false;
        }
        else  if(pincodetext.length()!=6){
            pincode.setError("Enter 6 Digit Pincode");
            pincode.requestFocus();
            return false;
        }
        else return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.editbtn)
        {
            updateprofile(v);
        }
        if (v.getId()==R.id.profilepic)
        {
            Intent intent = new Intent(getActivity(), StudentProfilePicUpload.class);
            intent.putExtra("rollno",sp.getString("rollno",null));
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fade_in, R.anim.fade_out);
                startActivity(intent, options.toBundle());
        }

    }

    public void updateprofile(View view)
    {
        if(validate(view)) {
        createJson();
        toServer();
    }

    }

    private void createJson() {
        jsonObject=new JSONObject();
        try {

            jsonObject.put("mobileno",mobilenotext);
            jsonObject.put("address",addresstext);
            jsonObject.put("city",citytext);
            jsonObject.put("state",statetext);
            jsonObject.put("pincode",pincodetext);
            jsonObject.put("rollno",sp.getString("rollno",null));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toServer() {
        showProgress(true);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.URL_update_profile, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                showProgress(false);
                if(response.contains("success"))
                    /*success();*/ {
                    AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
                    builder.setMessage("Changes Saved!");
                    builder.show();
                    se.putString("mobileno",mobilenotext);
                    se.putString("address",addresstext);
                    se.putString("pincode",pincodetext);
                    se.putString("city",citytext);
                    se.putString("state",statetext);
                    se.commit();
                     }
                else{
                    AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
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
                params.put("data",jsonObject.toString());
                Log.d("error1",jsonObject.toString());
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    @Override
    public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
        Spinner spinner=(Spinner)parent;
        if(spinner.getId()==R.id.state){
            city.setAdapter(LinkCities.addCities(view.getContext(),position));
            city.setEnabled(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView <?> parent) {

    }
}
