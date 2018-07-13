package project.internship.tcs.collegeattendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class StudentViewProfile extends Fragment {
    TextView emailid,mobileno,dob,address,city,state,pincode,rollno,name,gender,year,course,branch,section,fathername,attendance;

    SharedPreferences sp;
    ProgressBar progress;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.studentviewprofile,container,false);
        recognize(view);
        bind();
        sp=getActivity().getSharedPreferences("college_data",MODE_PRIVATE);

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
        emailid=view.findViewById(R.id.emailid);

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
        city.setText(sp.getString("city",null));
        state.setText(sp.getString("state",null));
        pincode.setText(sp.getString("pincode",null));
        attendance.setText(sp.getString("attendance",null));
        emailid.setText(sp.getString("email",null));

        return view;
    }

    private void bind() {
        //BindONClick
    }

    private void recognize(View view) {
        //Find Elements By View.findViewById();
        progress=view.findViewById(R.id.progress);

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
