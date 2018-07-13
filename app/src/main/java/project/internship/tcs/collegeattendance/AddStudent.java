package project.internship.tcs.collegeattendance;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddStudent extends Fragment implements Validation, AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText rollno,firstname,lastname,mobileno,email,section,fathername,address,pincode,dob;
    RadioGroup gender;
    int day,yeard,month;
    Spinner course,year,branch,city,state;
    String rollnotext,firstnametext,lastnametext,mobilenotext,emailtext,branchtext,sectiontext,fathernametext,addresstext,citytext,statetext,pincodetext,dobtext,gendertext,coursetext,yeartext;
    com.beardedhen.androidbootstrap.BootstrapButton addsinglestudent,openmultipleadd;
    JSONObject jsonObject;
    LinearLayout content;
    ProgressBar progress;
    RadioButton male,female;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_addstudent,container,false);
        recognize(view);
        bind();
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void bind() {
        course.setOnItemSelectedListener(this);
        addsinglestudent.setOnClickListener(this);
        openmultipleadd.setOnClickListener(this);
        dob.setOnClickListener(this);
        city.setEnabled(false);
        state.setAdapter(LinkCities.addStates(getContext()));
        state.setOnItemSelectedListener(this);
    }
    private void recognize(View view) {
        rollno=view.findViewById(R.id.rollno);
        firstname=view.findViewById(R.id.firstname);
        lastname=view.findViewById(R.id.lastname);
        mobileno=view.findViewById(R.id.mobileno);
        email=view.findViewById(R.id.email);
        branch=view.findViewById(R.id.branch);
        section=view.findViewById(R.id.section);;
        fathername=view.findViewById(R.id.fathername);
        address=view.findViewById(R.id.address);
        city=view.findViewById(R.id.city);
        state=view.findViewById(R.id.state);
        pincode=view.findViewById(R.id.pincode);
        dob=view.findViewById(R.id.dob);
        gender=view.findViewById(R.id.gender);
        course=view.findViewById(R.id.course);
        year=view.findViewById(R.id.year);
        addsinglestudent=view.findViewById(R.id.addsinglestudent);
        openmultipleadd=view.findViewById(R.id.openmultipleadd);
        content=view.findViewById(R.id.content);
        progress=view.findViewById(R.id.progress);
        male=view.findViewById(R.id.male);
        female=view.findViewById(R.id.female);
    }




    //OnClick Method
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addsinglestudent:
                addsinglestudent(v);
                break;
            case R.id.openmultipleadd:
                Intent intent=new Intent(getActivity(),AddMultipleStudents.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fade_in, R.anim.fade_out);
                startActivity(intent,options.toBundle());
                break;
            case R.id.dob:
                getDob(v);
                break;
        }
    }

    public void addsinglestudent(View view){
        if(validate(view)) {
            createJson();
            toServer();
        }
    }
    private void createJson() {
        jsonObject=new JSONObject();
        try {
            jsonObject.put("rollno",rollnotext);
            jsonObject.put("firstname",firstnametext);
            jsonObject.put("lastname",lastnametext);
            jsonObject.put("mobileno",mobilenotext);
            jsonObject.put("email",emailtext);
            jsonObject.put("gender",gendertext);
            jsonObject.put("year",yeartext);
            jsonObject.put("course",coursetext);
            jsonObject.put("branch",branchtext);
            jsonObject.put("section",sectiontext);
            jsonObject.put("fathername",fathernametext);
            jsonObject.put("address",addresstext);
            jsonObject.put("city",citytext);
            jsonObject.put("state",statetext);
            jsonObject.put("pincode",pincodetext);
            jsonObject.put("dob",dobtext);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean validate(View view) {
        rollnotext=rollno.getText().toString().trim();
        firstnametext=firstname.getText().toString().trim();
        lastnametext=lastname.getText().toString().trim();
        mobilenotext=mobileno.getText().toString().trim();
        emailtext=email.getText().toString().trim();
        coursetext=course.getSelectedItem().toString().trim();
        branchtext=branch.getSelectedItem().toString().trim();
        sectiontext=section.getText().toString().trim();
        fathernametext=fathername.getText().toString().trim();
        addresstext=address.getText().toString().trim();
        citytext=city.getSelectedItem().toString().trim();
        statetext=state.getSelectedItem().toString().trim();
        pincodetext=pincode.getText().toString().trim();
        dobtext=dob.getText().toString().trim();
        if(male.isChecked()||female.isChecked()){
            if(male.isChecked())
                gendertext="1";
            else
                gendertext="0";
        }
        else
            gendertext=null;
        coursetext=course.getSelectedItem().toString();
        yeartext=year.getSelectedItem().toString();
        if(TextUtils.isEmpty(rollnotext)||TextUtils.isEmpty(firstnametext)||TextUtils.isEmpty(lastnametext)||TextUtils.isEmpty(mobilenotext)||TextUtils.isEmpty(emailtext)||TextUtils.isEmpty(branchtext)||TextUtils.isEmpty(sectiontext)||TextUtils.isEmpty(fathernametext)||TextUtils.isEmpty(addresstext)||TextUtils.isEmpty(citytext)||TextUtils.isEmpty(statetext)||TextUtils.isEmpty(pincodetext)||TextUtils.isEmpty(dobtext)||TextUtils.isEmpty(gendertext)||TextUtils.isEmpty(coursetext)||TextUtils.isEmpty(yeartext)) {
            if (TextUtils.isEmpty(dobtext)) {
                dob.setError("Enter Date of Birth");
                dob.requestFocus();
            }
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
            if (TextUtils.isEmpty(fathernametext)) {
                fathername.setError("Enter Father's Name");
                fathername.requestFocus();
            }
            if (TextUtils.isEmpty(sectiontext)) {
                section.setError("Enter Section");
                section.requestFocus();
            }
            if (TextUtils.isEmpty(branchtext)) {
                Toast.makeText(getActivity(), "Select Branch", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(yeartext)) {
                Toast.makeText(getActivity(), "Select Year", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(coursetext)) {
                Toast.makeText(getActivity(), "Enter Course", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(gendertext)) {
                Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(emailtext)) {
                email.setError("Enter EmailID");
                email.requestFocus();
            }
            if (TextUtils.isEmpty(mobilenotext)) {
                mobileno.setError("Enter Contact Number");
                mobileno.requestFocus();
            }
            if (TextUtils.isEmpty(lastnametext)) {
                lastname.setError("Enter Last Name");
                lastname.requestFocus();
            }
            if (TextUtils.isEmpty(firstnametext)) {
                firstname.setError("Enter First Name");
                firstname.requestFocus();
            }
            if (TextUtils.isEmpty(rollnotext)) {
                rollno.setError("Enter Roll Number");
                rollno.requestFocus();
            }
            return false;
        }

        else if(rollnotext.length()!=10){
            rollno.setError("Roll Number Must be of 10 digits.");
            rollno.requestFocus();
            return false;
        }
        else if (!Validation.isValidName(firstnametext)){
            firstname.setError("Enter Valid Name");
            firstname.requestFocus();
            return false;
        }
        else if(!Validation.isValidName(lastnametext)){
            lastname.setError("Enter Valid Name");
            lastname.requestFocus();
            return false;
        }
        else if (!Validation.isValidPhone(mobilenotext)){
            mobileno.setError("Enter Valid 10 digit mobile number");
            mobileno.requestFocus();
            return false;
        }
        else if (!Validation.isValidEmail(emailtext)){
            email.setError("Enter Valid Email");
            email.requestFocus();
            return false;
        }
        else if (!Validation.isValidName(fathernametext)){
            fathername.setError("Enter Valid Name");
            fathername.requestFocus();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner=(Spinner)parent;
        if(spinner.getId()==R.id.course) {
            String b = course.getSelectedItem().toString();
            int rid = getResources().getIdentifier(b, "array", getContext().getPackageName());
            String temp[] = getResources().getStringArray(rid);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, temp);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            branch.setAdapter(spinnerArrayAdapter);
        }
        if(spinner.getId()==R.id.state){
            city.setAdapter(LinkCities.addCities(view.getContext(),position));
            city.setEnabled(true);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    //DateOfBirth Dialog
    private void getDob(View v) {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                day=selectedday;
                month=selectedmonth;
                yeard=selectedyear;
                dob.setText(getDate(day,month,yeard));
            }
        },mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMaxDate(new Date().getTime());
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }
    public String getDate(int day,int month,int year){
        StringBuilder builder=new StringBuilder();
        builder.append(day+"-");
        builder.append((month + 1)+"-");//month is 0 based
        builder.append(year);
        return builder.toString();
    }
    private void toServer() {
        showProgress(true);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.URL_add_singleStudent, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                showProgress(false);
                if(response.contains("success"))
                    success();
                else{
                    AlertDialog builder = new AlertDialog.Builder(getContext()).create();
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
    private void success() {
        AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
        builder.setMessage("Student Record Saved");
        builder.show();
        rollno.setText("");
        firstname.setText("");
        lastname.setText("");
        mobileno.setText("");
        email.setText("");
        gender.clearCheck();
        section.setText("");
        fathername.setText("");
        address.setText("");
        pincode.setText("");
        dob.setText("");
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
}
