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

public class TeacherViewProfile extends Fragment {
    TextView mobileno,dob,address,city,state,pincode,employeeid,name,gender,position,emailid;

    SharedPreferences sp;
    ProgressBar progress;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacherviewprofile,container,false);
        recognize(view);
        bind();
        sp=getActivity().getSharedPreferences("college_data",MODE_PRIVATE);

        employeeid=view.findViewById(R.id.empid);
        name=view.findViewById(R.id.name);
        gender=view.findViewById(R.id.gender);
        position=view.findViewById(R.id.position);
        mobileno=view.findViewById(R.id.mobileno);
        dob=view.findViewById(R.id.dob);
        address=view.findViewById(R.id.address);
        city=view.findViewById(R.id.city);
        state=view.findViewById(R.id.state);
        pincode=view.findViewById(R.id.pincode);
        emailid=view.findViewById(R.id.emailid);

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
