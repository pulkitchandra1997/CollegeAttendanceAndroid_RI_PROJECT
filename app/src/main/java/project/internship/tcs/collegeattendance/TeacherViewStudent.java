package project.internship.tcs.collegeattendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherViewStudent extends Fragment implements View.OnClickListener {

    TextView mobileno,dob,address,city,state,pincode,rollno,name,gender,year,course,branch,section,fathername,attendance,email,model;
    AutoCompleteTextView roll;
    Button searchbtn;
    String rollnum;
    JSONObject jsonObject;
    CardView confirm;
    ProgressBar progress;
    ImageView studentpic;
    LinearLayout mainLayout;

    public void hidekeyboard()

    {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacherviewstudent,container,false);

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
        roll=view.findViewById(R.id.roll);
        model=view.findViewById(R.id.model);
        email=view.findViewById(R.id.email);
        searchbtn=view.findViewById(R.id.searchbtn);
        confirm=view.findViewById(R.id.confirm);
        progress=view.findViewById(R.id.progress);
        mainLayout=view.findViewById(R.id.mainlayout);
        studentpic=view.findViewById(R.id.studentpic);

        searchbtn.setOnClickListener(this);

        return view;

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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.searchbtn)
        {
            hidekeyboard();
            rollnum=roll.getText().toString().trim();

            if (TextUtils.isEmpty(rollnum))
            {
                roll.setError("Enter Roll Number");
                roll.requestFocus();
            }
            else
            {
                toserver();
            }
        }

    }


    private void toserver() {
        showProgress(true);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.URL_search_student, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                showProgress(false);
                if(response.equalsIgnoreCase("error0")){
                    AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>RentZHub</font>"));
                    builder.setMessage("No Record found");
                    builder.show();
                }
                else {
                    confirm.setVisibility(View.VISIBLE);
                    try {
                        jsonObject=new JSONObject(response);

                        rollno.setText(jsonObject.getString("rollno"));
                        name.setText(jsonObject.getString("firstname")+" "+jsonObject.getString("lastname"));
                        gender.setText(jsonObject.getString("gender"));
                        year.setText(jsonObject.getString("year"));
                        course.setText(jsonObject.getString("course"));
                        branch.setText(jsonObject.getString("branch"));
                        section.setText(jsonObject.getString("section"));
                        mobileno.setText(jsonObject.getString("mobileno"));
                        fathername.setText(jsonObject.getString("fathername"));
                        dob.setText(jsonObject.getString("dob"));
                        address.setText(jsonObject.getString("address"));
                        city.setText(jsonObject.getString("city"));
                        state.setText(jsonObject.getString("state"));
                        pincode.setText(jsonObject.getString("pincode"));
                        attendance.setText(jsonObject.getString("attendance"));
                        email.setText(jsonObject.getString("email"));
                        model.setText(jsonObject.getString("model"));

                        if (jsonObject.getString("pic").equalsIgnoreCase("null"))
                        {
                            studentpic.setImageResource(R.mipmap.profilepic);
                        }
                        else {
                            Picasso.with(getActivity()).load(jsonObject.getString("pic")).fit().into(studentpic);
                        }

                    }catch (Exception e){
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
                params.put("rollno",rollnum);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


}
