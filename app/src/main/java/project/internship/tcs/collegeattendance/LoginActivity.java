package project.internship.tcs.collegeattendance;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button teacher,student;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        teacher=findViewById(R.id.teacher);
        student=findViewById(R.id.student);
        teacher.setOnClickListener(this);
        student.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.teacher)
            intent=new Intent(LoginActivity.this,TeacherRegisterActivity.class);
        else
            intent=new Intent(LoginActivity.this,StudentRegisterActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(LoginActivity.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(intent, options.toBundle());

    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
    }
}
